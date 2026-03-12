package com.example.tb2025.blog;

import java.text.Normalizer;
import java.util.List;
import java.util.Locale;

import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * スタッフブログ機能のController（画面の入口）。
 *
 * 提供機能：
 *   ログイン（学習用：固定ID/固定PW）
 *   投稿
 *   一覧表示（ページング：5件/ページ）
 *   削除
 *   ログアウト
 *
 * 保存方式：
 *   DBは使用せず BlogFileStore を通して JSONファイルに保存する。
 *
 * 学習ポイント：
 *   今回は JSON 保存だが、保存先が DB でなくても
 *   「画面に表示する文字列」は安全性を考える必要がある。
 *
 *   特にブログ本文やタイトルは一覧・詳細などで何度も表示されるため、
 *   XSS 対策の意識が大切。
 *
 *   ただし本筋は「表示時エスケープ」。
 *   テンプレート側で th:text を使うことが最重要であり、
 *   ここでの危険文字チェックは補助的な対策と考える。
 */
@Controller
public class StaffBlogController {

    /** セッションに保存するログイン名のキー */
    private static final String SESSION_STAFF_NAME = "staffName";

    /** ページング：1ページあたりの件数 */
    private static final int PAGE_SIZE = 5;

    /** タイトルの最大文字数 */
    private static final int TITLE_MAX_LENGTH = 80;

    /** 本文の最大文字数 */
    private static final int BODY_MAX_LENGTH = 2000;

    /** 画像URLの最大文字数 */
    private static final int IMAGE_URL_MAX_LENGTH = 300;

    private final BlogFileStore store;

    public StaffBlogController(BlogFileStore store) {
        this.store = store;
    }

    // ==============================
    // 1) ログイン / ログアウト
    // ==============================

    /** ログイン画面 */
    @GetMapping("/staff/login")
    public String loginPage() {
        return "staff-login";
    }

    /**
     * ログイン処理（学習用：固定ID/固定PW）
     *
     * <p>※本番ではDB認証やSpring Security等を使うのが一般的。</p>
     */
    @PostMapping("/staff/login")
    public String doLogin(
            @RequestParam String id,
            @RequestParam String pw,
            HttpSession session,
            Model model
    ) {
        if ("staff".equals(id) && "pass1234".equals(pw)) {
            session.setAttribute(SESSION_STAFF_NAME, "スタッフ");
            return "redirect:/staff/blog?page=1";
        }

        model.addAttribute("error", "IDまたはパスワードが違います");
        return "staff-login";
    }

    /** ログアウト（セッション破棄） */
    @PostMapping("/staff/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/staff/login";
    }

    // ==============================
    // 2) 記事一覧（ページング）
    // ==============================

    /**
     * 記事一覧（ページング）表示。
     *
     * @param page ページ番号（1始まり）
     */
    @GetMapping("/staff/blog")
    public String blog(
            @RequestParam(defaultValue = "1") int page,
            Model model,
            HttpSession session
    ) {
        List<BlogPost> all = store.load(); // 新しい順（先頭が最新）
        int total = all.size();

        int totalPages = (int) Math.ceil(total / (double) PAGE_SIZE);
        if (totalPages == 0) {
            totalPages = 1;
        }

        // page補正（範囲外防止）
        page = normalizePage(page, totalPages);

        int fromIndex = (page - 1) * PAGE_SIZE;
        int toIndex = Math.min(fromIndex + PAGE_SIZE, total);

        List<BlogPost> posts = (fromIndex >= total)
                ? List.of()
                : all.subList(fromIndex, toIndex);

        model.addAttribute("posts", posts);
        model.addAttribute("page", page);
        model.addAttribute("totalPages", totalPages);

        // テンプレ側の表示制御用（ログイン名）
        model.addAttribute("staffName", session.getAttribute(SESSION_STAFF_NAME));

        return "staff-blog";
    }

    // ==============================
    // 3) 新規投稿
    // ==============================

    /** 新規投稿画面（ログイン必須） */
    @GetMapping("/staff/blog/new")
    public String newPost(HttpSession session) {
        if (!isLoggedIn(session)) {
            return "redirect:/staff/login";
        }
        return "staff-post";
    }

    /**
     * 投稿作成（ログイン必須）
     *
     * <p>学習ポイント：</p>
     * <ul>
     *   <li>フロントの maxlength だけでは不十分。サーバー側でも長さチェックする</li>
     *   <li>タイトル・本文は trim して入力ゆれを少し整える</li>
     *   <li>画像URLは http / https のみ許可して、想定外の値を弾く</li>
     *   <li>危険文字チェックは補助。本命はテンプレート側の th:text</li>
     * </ul>
     */
    @PostMapping("/staff/blog")
    public String create(
            @RequestParam String title,
            @RequestParam String body,
            @RequestParam(required = false) String imageUrl,
            HttpSession session,
            Model model
    ) {
        Object staffName = session.getAttribute(SESSION_STAFF_NAME);
        if (staffName == null) {
            return "redirect:/staff/login";
        }

        // ==============================
        // 入力値の正規化
        // ==============================
        // 学習ポイント：
        // まずは入力値を軽く整えてから判定すると、
        // バリデーションの精度が上がりやすい。
        title = normalizeText(title);
        body = normalizeText(body);
        imageUrl = normalizeText(imageUrl);

        // ==============================
        // 必須チェック
        // ==============================
        if (title == null || title.isBlank() || body == null || body.isBlank()) {
            return backToPostForm(model, title, body, imageUrl,
                    "タイトルと本文は必須です");
        }

        // ==============================
        // 文字数チェック
        // ==============================
        // 学習ポイント：
        // HTML側の maxlength はユーザー補助。
        // 開発者ツール等で外せるので、サーバー側でも必ず確認する。
        if (title.length() > TITLE_MAX_LENGTH) {
            return backToPostForm(model, title, body, imageUrl,
                    "タイトルは80文字以内で入力してください");
        }

        if (body.length() > BODY_MAX_LENGTH) {
            return backToPostForm(model, title, body, imageUrl,
                    "本文は2000文字以内で入力してください");
        }

        if (imageUrl != null && imageUrl.length() > IMAGE_URL_MAX_LENGTH) {
            return backToPostForm(model, title, body, imageUrl,
                    "画像URLは300文字以内で入力してください");
        }

        // ==============================
        // タイトルの簡易危険文字チェック
        // ==============================
        // 学習ポイント：
        // 完全なXSS対策ではないが、
        // 明らかに危険な script 系文字列は補助的に弾いておく。
        //
        // 本文は th:text で表示していればかなり安全寄りなので、
        // 今回はまずタイトルだけに入れても十分自然。
        if (containsDangerousScript(title)) {
            return backToPostForm(model, title, body, imageUrl,
                    "タイトルに使用できない文字列が含まれています");
        }

        // ==============================
        // 画像URL形式チェック
        // ==============================
        // 学習ポイント：
        // img の src にそのまま入る値なので、
        // http / https のみ許可しておくと運用が安定しやすい。
        if (!isValidImageUrl(imageUrl)) {
            return backToPostForm(model, title, body, imageUrl,
                    "画像URLは http:// または https:// で入力してください");
        }

        // 保存
        store.add(staffName.toString(), title, body, imageUrl);

        // 最新が見えるページへ
        return "redirect:/staff/blog?page=1";
    }

    // ==============================
    // 4) 削除
    // ==============================

    /**
     * 記事削除（ログイン必須）
     *
     * @param id   削除対象の記事ID
     * @param page 削除後に戻るページ番号
     */
    @PostMapping("/staff/blog/delete")
    public String delete(
            @RequestParam String id,
            @RequestParam(defaultValue = "1") int page,
            HttpSession session
    ) {
        if (!isLoggedIn(session)) {
            return "redirect:/staff/login";
        }

        store.deleteById(id);

        // 削除後は「今いたページ」に戻す
        // blog側で範囲補正されるのでそのままでOK
        return "redirect:/staff/blog?page=" + page;
    }

    // ==============================
    // 5) private
    // ==============================

    /** ログイン済みか判定 */
    private boolean isLoggedIn(HttpSession session) {
        return session.getAttribute(SESSION_STAFF_NAME) != null;
    }

    /** pageを 1〜totalPages の範囲に収める */
    private int normalizePage(int page, int totalPages) {
        if (page < 1) {
            return 1;
        }
        if (page > totalPages) {
            return totalPages;
        }
        return page;
    }

    /**
     * 入力文字列の軽い正規化
     *
     * <p>学習ポイント：</p>
     * <ul>
     *   <li>全角英数字の揺れを減らす</li>
     *   <li>前後の空白を除去する</li>
     *   <li>null はそのまま返す</li>
     * </ul>
     */
    private String normalizeText(String value) {
        if (value == null) {
            return null;
        }
        return Normalizer.normalize(value, Normalizer.Form.NFKC).trim();
    }

    /**
     * 危険そうな script 系文字列を簡易判定
     *
     * <p>※補助的な対策。本命は th:text による表示時エスケープ。</p>
     */
    private boolean containsDangerousScript(String value) {
        if (value == null || value.isBlank()) {
            return false;
        }

        String lower = value.toLowerCase(Locale.ROOT);

        return lower.contains("<script")
                || lower.contains("</script>")
                || lower.contains("javascript:");
    }

    /**
     * 画像URLの形式チェック
     *
     * <p>空なら未設定として許可。
     * 値がある場合は http / https のみ許可する。</p>
     */
    private boolean isValidImageUrl(String imageUrl) {
        if (imageUrl == null || imageUrl.isBlank()) {
            return true;
        }

        String lower = imageUrl.toLowerCase(Locale.ROOT);
        return lower.startsWith("http://") || lower.startsWith("https://");
    }

    /**
     * 投稿画面へ戻すときの共通処理
     *
     * <p>学習ポイント：
     * エラー時に入力値を戻しておくと、再入力の手間が減る。</p>
     */
    private String backToPostForm(
            Model model,
            String title,
            String body,
            String imageUrl,
            String errorMessage
    ) {
        model.addAttribute("error", errorMessage);
        model.addAttribute("title", title);
        model.addAttribute("body", body);
        model.addAttribute("imageUrl", imageUrl);
        return "staff-post";
    }
}
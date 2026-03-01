package com.example.tb2025.blog;

import java.util.List;

import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * スタッフブログ機能のController（画面の入口）。
 *
 * <p>提供機能：</p>
 * <ul>
 *   <li>ログイン（学習用：固定ID/固定PW）</li>
 *   <li>投稿</li>
 *   <li>一覧表示（ページング：5件/ページ）</li>
 *   <li>削除</li>
 *   <li>ログアウト</li>
 * </ul>
 *
 * <p>保存方式：</p>
 * DBは使用せず {@link BlogFileStore} を通して JSONファイルに保存する。</p>
 */
@Controller
public class StaffBlogController {

    /** セッションに保存するログイン名のキー */
    private static final String SESSION_STAFF_NAME = "staffName";

    /** ページング：1ページあたりの件数 */
    private static final int PAGE_SIZE = 5;

    private final BlogFileStore store;

    public StaffBlogController(BlogFileStore store) {
        this.store = store;
    }

    // ==============================
    // ログイン / ログアウト
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
    // 記事一覧（ページング）
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
        if (totalPages == 0) totalPages = 1;

        // page補正（範囲外防止）
        page = normalizePage(page, totalPages);

        int fromIndex = (page - 1) * PAGE_SIZE;
        int toIndex = Math.min(fromIndex + PAGE_SIZE, total);

        List<BlogPost> posts = (fromIndex >= total) ? List.of() : all.subList(fromIndex, toIndex);

        model.addAttribute("posts", posts);
        model.addAttribute("page", page);
        model.addAttribute("totalPages", totalPages);

        // テンプレ側の表示制御用（ログイン名）
        model.addAttribute("staffName", session.getAttribute(SESSION_STAFF_NAME));

        return "staff-blog";
    }

    // ==============================
    // 新規投稿
    // ==============================

    /** 新規投稿画面（ログイン必須） */
    @GetMapping("/staff/blog/new")
    public String newPost(HttpSession session) {
        if (!isLoggedIn(session)) return "redirect:/staff/login";
        return "staff-post";
    }

    /**
     * 投稿作成（ログイン必須）
     *
     * <p>入力が空の場合は同じ画面に戻し、エラーメッセージを表示する。</p>
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
        if (staffName == null) return "redirect:/staff/login";

        // 最低限のバリデーション（空投稿防止）
        if (title == null || title.isBlank() || body == null || body.isBlank()) {
            model.addAttribute("error", "タイトルと本文は必須です");
            model.addAttribute("title", title);
            model.addAttribute("body", body);
            model.addAttribute("imageUrl", imageUrl);
            return "staff-post";
        }

        store.add(staffName.toString(), title, body, imageUrl);

        // 最新が見えるページへ
        return "redirect:/staff/blog?page=1";
    }

    // ==============================
    // 削除
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
        if (!isLoggedIn(session)) return "redirect:/staff/login";

        store.deleteById(id);

        // 削除後は「今いたページ」に戻す（blog側で範囲補正されるのでOK）
        return "redirect:/staff/blog?page=" + page;
    }

    // ==============================
    // private
    // ==============================

    /** ログイン済みか判定 */
    private boolean isLoggedIn(HttpSession session) {
        return session.getAttribute(SESSION_STAFF_NAME) != null;
    }

    /** pageを 1〜totalPages の範囲に収める */
    private int normalizePage(int page, int totalPages) {
        if (page < 1) return 1;
        if (page > totalPages) return totalPages;
        return page;
    }
}
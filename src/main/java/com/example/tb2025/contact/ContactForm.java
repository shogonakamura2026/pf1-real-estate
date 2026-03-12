package com.example.tb2025.contact;

import java.text.Normalizer;
import java.util.Locale;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class ContactForm {

    // =========================================================
    // 1) 基本入力項目
    // =========================================================

    // お名前は必須
    @NotBlank(message = "お名前を入力してください")
    @Size(max = 50, message = "お名前は50文字以内で入力してください")
    private String name;

    // メールアドレスは必須
    // さらに @Email でメール形式をチェックする
    @NotBlank(message = "メールアドレスを入力してください")
    @Email(message = "メールアドレスの形式が正しくありません")
    private String mail;

    // 電話番号は任意
    // ただし、希望連絡方法で「電話」を選んだ場合は
    // 条件付きで必須＆形式チェックを行う
    private String tel;

    // 希望連絡方法（email / phone）は必須
    @NotBlank(message = "希望連絡方法を選択してください")
    private String contactMethod;

    // お問い合わせ種別は必須
    @NotBlank(message = "お問い合わせ種別を選択してください")
    private String inquiryType;

    // =========================================================
    // 2) 任意項目
    // =========================================================

    // 物件詳細から遷移してきた場合に保持する物件ID
    private String propertyId;

    // 学習ポイント：
    // propertyId は DB保存や連携用の識別子。
    // 一方 propertyName は画面表示用・入力補助用として使う。
    //
    // 今回は textarea に
    // 「物件『○○』について」
    // という初期文を入れたいので、表示用として持っておくと便利。
    //
    // ※ DB保存必須ではないため、Contact テーブルへの追加は不要。
    private String propertyName;

    // =========================================================
    // 3) 希望条件
    // =========================================================
    // 学習ポイント：
    // 任意入力欄でも、文字数制限をつけておくと
    // 異常に長い入力や想定外データの流入を防ぎやすい。
    //
    // また、自由入力に近い項目は
    // 危険文字列の簡易チェックも補助的に入れておくと安心。

    // 希望エリア
    @Size(max = 100, message = "希望エリアは100文字以内で入力してください")
    private String area;

    // ご予算
    @Size(max = 100, message = "ご予算は100文字以内で入力してください")
    private String budget;

    // 間取り
    @Size(max = 50, message = "間取りは50文字以内で入力してください")
    private String layout;

    // 入居希望時期
    @Size(max = 50, message = "入居希望時期は50文字以内で入力してください")
    private String moveIn;

    // 内見希望日時（今回は String で保持）
    private String date1;
    private String time1;
    private String date2;
    private String time2;
    private String date3;
    private String time3;

    // =========================================================
    // 4) 相談内容・自由入力欄
    // =========================================================
    // 学習ポイント：
    // 自由入力欄は何でも入れられるため、
    // 必須チェックだけでなく「長すぎる入力」や
    // 「危険そうな文字列」にも注意する必要がある
    //
    // 今回は最大1000文字までに制限
    @Size(max = 1000, message = "お問い合わせ内容は1000文字以内で入力してください")
    private String message;

    // =========================================================
    // 5) 同意チェック
    // =========================================================
    // checkbox は未チェック時に false / null になるため、
    // 「必須でチェックしてほしい」場合は @AssertTrue が向いている
    @AssertTrue(message = "個人情報の取り扱いに同意してください")
    private Boolean consent;

    // =========================================================
    // 6) 共通の正規化メソッド
    // =========================================================
    // 学習ポイント：
    // 入力値はそのまま使わず、
    // 先に「全角→半角」「前後空白除去」をしておくと、
    // バリデーションの精度や保存データの統一性が上がる
    private String normalizeToHalfWidth(String value) {
        if (value == null) {
            return null;
        }
        return Normalizer.normalize(value, Normalizer.Form.NFKC).trim();
    }

    // =========================================================
    // 7) 物件情報からメッセージ初期文を作る補助メソッド
    // =========================================================
    // 学習ポイント：
    // 物件詳細ページからお問い合わせ画面へ来たとき、
    // ユーザーに「何についての問い合わせか」を分かりやすくするため、
    // message の先頭に物件名を入れる。
    //
    // ただし、すでに入力済みの message を上書きしないよう、
    // 空の場合だけ初期文をセットする設計にしている。
    //
    // このメソッドは Controller から呼ぶ想定。
    public void applyPropertyContextMessage() {
        if (propertyName == null || propertyName.isBlank()) {
            return;
        }

        if (message == null || message.isBlank()) {
            this.message = "物件「" + propertyName + "」について\n";
        }
    }

    // =========================================================
    // 8) 条件付きバリデーション（名前の危険文字列）
    // =========================================================
    // 学習ポイント：
    // 名前は文字種を厳しく制限しすぎると、
    // 海外名・ハイフン付き・スペース入りの名前などを
    // 不必要に弾いてしまう。
    //
    // そのため今回は、
    // 「漢字だけ」「ひらがなだけ」のような制限はせず、
    // 文字数制限 + 明らかに危険な文字列の簡易チェック
    // という実務寄りのバランスにしている。
    //
    // ※ これは XSS 対策の補助であり、
    // 本筋は表示時に th:text でエスケープすること。
    @AssertTrue(message = "お名前に使用できない文字列が含まれています")
    public boolean isNameSafe() {
        if (name == null || name.isBlank()) {
            return true;
        }

        String lower = name.toLowerCase(Locale.ROOT);

        return !lower.contains("<script")
                && !lower.contains("</script>")
                && !lower.contains("javascript:");
    }

    // =========================================================
    // 9) 条件付きバリデーション（電話番号）
    // =========================================================
    // 学習ポイント：
    // 「電話」を選んだときだけ電話番号を必須にしたいので、
    // 単純な @NotBlank ではなく @AssertTrue で条件付き判定を行う
    //
    // ルール：
    // - 希望連絡方法が phone のときだけチェック
    // - 数字とハイフンのみ許可
    // - ハイフンを除いた数字が10〜11桁ならOK
    @AssertTrue(message = "希望連絡方法で「電話」を選択した場合、電話番号を正しく入力してください")
    public boolean isTelRequiredWhenPhone() {
        if (!"phone".equals(contactMethod)) {
            return true;
        }

        if (tel == null || tel.isBlank()) {
            return false;
        }

        // 数字とハイフン以外が入っていたらNG
        if (!tel.matches("[0-9-]+")) {
            return false;
        }

        // ハイフンを除去した純粋な数字で桁数確認
        String digits = tel.replace("-", "");
        return digits.matches("\\d{10,11}");
    }

    // =========================================================
    // 10) 条件付きバリデーション（希望エリアの危険文字列）
    // =========================================================
    // 学習ポイント：
    // 希望エリアは任意入力なので、
    // 「那覇市」「浦添市」「ゆいレール沿線」など
    // 柔軟に入力できるようにしつつ、
    // 明らかに危険な文字列だけ簡易的に弾く。
    @AssertTrue(message = "希望エリアに使用できない文字列が含まれています")
    public boolean isAreaSafe() {
        if (area == null || area.isBlank()) {
            return true;
        }

        String lower = area.toLowerCase(Locale.ROOT);

        return !lower.contains("<script")
                && !lower.contains("</script>")
                && !lower.contains("javascript:");
    }

    // =========================================================
    // 11) 条件付きバリデーション（ご予算の危険文字列）
    // =========================================================
    // 学習ポイント：
    // ご予算も自由入力に近いため、
    // 「3000万円以内」「月8万円台」などは許可しつつ、
    // script 系の危険文字列だけは弾く。
    @AssertTrue(message = "ご予算に使用できない文字列が含まれています")
    public boolean isBudgetSafe() {
        if (budget == null || budget.isBlank()) {
            return true;
        }

        String lower = budget.toLowerCase(Locale.ROOT);

        return !lower.contains("<script")
                && !lower.contains("</script>")
                && !lower.contains("javascript:");
    }

    // =========================================================
    // 12) 条件付きバリデーション（相談内容の危険文字列）
    // =========================================================
    // 学習ポイント：
    // これは XSS 対策の「補助」。
    //
    // 完全な防御は
    // 1. 表示時のHTMLエスケープ
    // 2. 必要に応じた危険タグ除去
    // が本筋。
    //
    // 今回はポートフォリオ向けとして、
    // 明らかに危険な script 系文字列を簡易的に拒否する。
    //
    // 例：
    // <script>alert(1)</script>
    // javascript:alert(1)
    @AssertTrue(message = "お問い合わせ内容に使用できない文字列が含まれています")
    public boolean isMessageSafe() {
        if (message == null || message.isBlank()) {
            return true;
        }

        // 小文字化して判定すると、大文字小文字の揺れに強くなる
        String lower = message.toLowerCase(Locale.ROOT);

        return !lower.contains("<script")
                && !lower.contains("</script>")
                && !lower.contains("javascript:");
    }

    // =========================================================
    // 13) getter / setter
    // =========================================================

    public String getName() {
        return name;
    }

    // お名前も軽く正規化しておく
    // ※ 文字種制限はせず、全角英数字の揺れや前後空白を整える
    public void setName(String name) {
        this.name = normalizeToHalfWidth(name);
    }

    public String getMail() {
        return mail;
    }

    // 学習ポイント：
    // メールアドレスは setter の時点で整えておくと、
    // @Email の判定時にも整った値が使われる
    //
    // やっていること
    // 1. 全角→半角
    // 2. 前後空白除去
    // 3. 小文字化
    public void setMail(String mail) {
        String normalized = normalizeToHalfWidth(mail);

        if (normalized == null || normalized.isBlank()) {
            this.mail = normalized;
            return;
        }

        this.mail = normalized.toLowerCase(Locale.ROOT);
    }

    public String getTel() {
        return tel;
    }

    // 学習ポイント：
    // 電話番号は入力ゆれが起きやすいので、
    // setter の時点である程度形をそろえておく
    //
    // 例：
    // ０９０ー１２３４ー５６７８
    // 090―1234―5678
    // 090-1234-5678
    //
    // これらを半角ハイフン中心の形に寄せる
    public void setTel(String tel) {
        String normalized = normalizeToHalfWidth(tel);

        if (normalized == null) {
            this.tel = null;
            return;
        }

        // ハイフンに見える文字を半角ハイフンへ統一
        normalized = normalized
                .replace('ー', '-')
                .replace('―', '-')
                .replace('‐', '-')
                .replace('−', '-');

        this.tel = normalized;
    }

    public String getContactMethod() {
        return contactMethod;
    }

    public void setContactMethod(String contactMethod) {
        this.contactMethod = contactMethod;
    }

    public String getInquiryType() {
        return inquiryType;
    }

    public void setInquiryType(String inquiryType) {
        this.inquiryType = inquiryType;
    }

    public String getPropertyId() {
        return propertyId;
    }

    public void setPropertyId(String propertyId) {
        this.propertyId = propertyId;
    }

    public String getPropertyName() {
        return propertyName;
    }

    // propertyName は表示補助用なので軽く正規化して保持
    public void setPropertyName(String propertyName) {
        this.propertyName = normalizeToHalfWidth(propertyName);
    }

    public String getArea() {
        return area;
    }

    // 希望エリアも全角英数字や前後空白の揺れを軽く整える
    public void setArea(String area) {
        this.area = normalizeToHalfWidth(area);
    }

    public String getBudget() {
        return budget;
    }

    // ご予算も同様に軽く正規化しておく
    public void setBudget(String budget) {
        this.budget = normalizeToHalfWidth(budget);
    }

    public String getLayout() {
        return layout;
    }

    public void setLayout(String layout) {
        this.layout = normalizeToHalfWidth(layout);
    }

    public String getMoveIn() {
        return moveIn;
    }

    public void setMoveIn(String moveIn) {
        this.moveIn = normalizeToHalfWidth(moveIn);
    }

    public String getDate1() {
        return date1;
    }

    public void setDate1(String date1) {
        this.date1 = date1;
    }

    public String getTime1() {
        return time1;
    }

    public void setTime1(String time1) {
        this.time1 = time1;
    }

    public String getDate2() {
        return date2;
    }

    public void setDate2(String date2) {
        this.date2 = date2;
    }

    public String getTime2() {
        return time2;
    }

    public void setTime2(String time2) {
        this.time2 = time2;
    }

    public String getDate3() {
        return date3;
    }

    public void setDate3(String date3) {
        this.date3 = date3;
    }

    public String getTime3() {
        return time3;
    }

    public void setTime3(String time3) {
        this.time3 = time3;
    }

    public String getMessage() {
        return message;
    }

    // message も軽く正規化しておく
    // ※ 中身そのものは消しすぎず、前後空白整理だけ寄せるイメージ
    public void setMessage(String message) {
        this.message = normalizeToHalfWidth(message);
    }

    public Boolean getConsent() {
        return consent;
    }

    public void setConsent(Boolean consent) {
        this.consent = consent;
    }

    // =========================================================
    // 14) Thymeleaf のエラー表示補助
    // =========================================================
    // @AssertTrue のメソッド名に対応したエラー表示用
    public boolean getNameSafe() {
        return isNameSafe();
    }

    public boolean getTelRequiredWhenPhone() {
        return isTelRequiredWhenPhone();
    }

    public boolean getAreaSafe() {
        return isAreaSafe();
    }

    public boolean getBudgetSafe() {
        return isBudgetSafe();
    }

    public boolean getMessageSafe() {
        return isMessageSafe();
    }
}
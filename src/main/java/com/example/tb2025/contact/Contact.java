package com.example.tb2025.contact;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/*
 * =========================================================
 * Contact エンティティ
 * =========================================================
 * このクラスは「お問い合わせデータ」を表す Entity。
 *
 * Spring Data JPA / Hibernate に対して
 * 「このクラスをDBのテーブルと対応づけて使います」
 * と伝える役割を持つ。
 *
 * 今回は contacts テーブルの1行分を、
 * Javaオブジェクト1件として扱うイメージ。
 */
@Entity

/*
 * テーブル名を明示的に "contacts" に指定している。
 *
 * これを書かない場合、クラス名 Contact から自動推測されることもあるが、
 * 実務では「DB側の実際のテーブル名」とズレないように
 * 明示しておくほうが分かりやすい。
 */
@Table(name = "contacts")
public class Contact {

    /*
     * =========================================================
     * 1) 主キー
     * =========================================================
     */

    /*
     * このフィールドが主キー（PRIMARY KEY）であることを表す。
     * テーブルの1レコードを一意に識別するための値。
     */
    @Id

    /*
     * 主キーの値を自動採番する設定。
     *
     * GenerationType.IDENTITY は、
     * DB側の auto_increment のような仕組みに任せる方式。
     *
     * つまり insert 時に id を自分で入れなくても、
     * DBが自動で 1,2,3... と振ってくれる。
     */
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /*
     * =========================================================
     * 2) 基本情報
     * =========================================================
     * ここはお問い合わせした人の基本入力情報。
     */

    // 名前
    private String name;

    // メールアドレス
    private String mail;

    // 電話番号
    // 入力時はハイフンありでも、保存時は数字だけにそろえる想定
    private String tel;

    /*
     * =========================================================
     * 3) お問い合わせ条件
     * =========================================================
     */

    /*
     * Java側のフィールド名は contactMethod
     * DB側のカラム名は contact_method
     *
     * Javaではキャメルケース、
     * DBではスネークケースにすることが多い。
     *
     * その対応関係を明示するのが @Column(name = "...")。
     */
    @Column(name = "contact_method")
    private String contactMethod;

    /*
     * お問い合わせ種別
     * 例：document / viewing / visit / consult / other
     */
    @Column(name = "inquiry_type")
    private String inquiryType;

    /*
     * 物件詳細ページから来た場合の物件ID
     * 任意入力なので null の可能性あり
     */
    @Column(name = "property_id")
    private String propertyId;

    /*
     * =========================================================
     * 4) 希望条件
     * =========================================================
     * 将来的に検索・提案に使える情報。
     */

    // 希望エリア
    private String area;

    // 予算
    private String budget;

    // 間取り
    private String layout;

    /*
     * 入居希望時期
     * move_in というDBカラム名に対応
     */
    @Column(name = "move_in")
    private String moveIn;

    /*
     * =========================================================
     * 5) 内見希望日時
     * =========================================================
     *
     * 今回は画面入力との相性を優先して String で保持している。
     *
     * 例：
     * date1 = "2026-03-10"
     * time1 = "11:30"
     *
     * これは「まず動かす」「フォームと連携しやすい」
     * という意味では分かりやすい構成。
     *
     * ただし、将来的には
     *   LocalDate / LocalTime
     * あるいは
     *   LocalDateTime
     * に変えると、日付計算や比較がしやすくなる。
     */

    // 第1希望日
    private String date1;

    // 第1希望時間
    private String time1;

    // 第2希望日
    private String date2;

    // 第2希望時間
    private String time2;

    // 第3希望日
    private String date3;

    // 第3希望時間
    private String time3;

    /*
     * =========================================================
     * 6) 自由記述欄
     * =========================================================
     */

    /*
     * message はお問い合わせ本文。
     *
     * 長文が入る可能性があるため、DBカラムを TEXT として扱う設定。
     *
     * これを付けないと、DBや設定によっては
     * VARCHAR(255) のような短い長さで作られてしまうことがある。
     *
     * お問い合わせ内容は長くなることがあるため、
     * TEXT にしておくのは実務でもよくある。
     */
    @Column(columnDefinition = "TEXT")
    private String message;

    /*
     * =========================================================
     * 7) 作成日時
     * =========================================================
     */

    /*
     * created_at というDBカラム名に対応。
     *
     * Contact オブジェクト生成時点で
     * LocalDateTime.now() を入れている。
     *
     * そのため、save() した時に「いつ作られた問い合わせか」を記録できる。
     *
     * 補足：
     * 実務では以下のような設計もよくある
     *
     * 1. Java側で now() を入れる
     * 2. DB側で DEFAULT CURRENT_TIMESTAMP を使う
     * 3. @PrePersist で保存直前に自動セットする
     *
     * 今回の書き方はシンプルで分かりやすい。
     */
    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    /*
     * =========================================================
     * 8) getter / setter
     * =========================================================
     * JPA や Thymeleaf、Controller などから値を扱うために用意する。
     *
     * JavaBeans の基本形として、
     * private フィールドに対して getter / setter を作るのが一般的。
     */

    // id は自動採番なので setter をあえて作らないことも多い
    public Long getId() {
        return id;
    }

    // -------------------------
    // name
    // -------------------------
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // -------------------------
    // mail
    // -------------------------
    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    // -------------------------
    // tel
    // -------------------------
    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    // -------------------------
    // contactMethod
    // -------------------------
    public String getContactMethod() {
        return contactMethod;
    }

    public void setContactMethod(String contactMethod) {
        this.contactMethod = contactMethod;
    }

    // -------------------------
    // inquiryType
    // -------------------------
    public String getInquiryType() {
        return inquiryType;
    }

    public void setInquiryType(String inquiryType) {
        this.inquiryType = inquiryType;
    }

    // -------------------------
    // propertyId
    // -------------------------
    public String getPropertyId() {
        return propertyId;
    }

    public void setPropertyId(String propertyId) {
        this.propertyId = propertyId;
    }

    // -------------------------
    // area
    // -------------------------
    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    // -------------------------
    // budget
    // -------------------------
    public String getBudget() {
        return budget;
    }

    public void setBudget(String budget) {
        this.budget = budget;
    }

    // -------------------------
    // layout
    // -------------------------
    public String getLayout() {
        return layout;
    }

    public void setLayout(String layout) {
        this.layout = layout;
    }

    // -------------------------
    // moveIn
    // -------------------------
    public String getMoveIn() {
        return moveIn;
    }

    public void setMoveIn(String moveIn) {
        this.moveIn = moveIn;
    }

    // -------------------------
    // date1 / time1
    // -------------------------
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

    // -------------------------
    // date2 / time2
    // -------------------------
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

    // -------------------------
    // date3 / time3
    // -------------------------
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

    // -------------------------
    // message
    // -------------------------
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    // createdAt は通常「作成時に決まったら基本変更しない」値なので、
    // getter のみでも十分なケースが多い
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
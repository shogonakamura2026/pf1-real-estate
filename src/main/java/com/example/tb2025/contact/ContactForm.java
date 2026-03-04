package com.example.tb2025.contact;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class ContactForm {

    // 必須
    @NotBlank(message = "お名前を入力してください")
    private String name;

    @NotBlank(message = "メールアドレスを入力してください")
    @Email(message = "メールアドレスの形式が正しくありません")
    private String mail;

    // 任意（ただし電話選択時は必須＆桁チェック）
    private String tel;

    @NotBlank(message = "希望連絡方法を選択してください")
    private String contactMethod; // email / phone

    @NotBlank(message = "お問い合わせ種別を選択してください")
    private String inquiryType;

    private String propertyId;

    private String area;
    private String budget;
    private String layout;
    private String moveIn;

    private String date1;
    private String time1;
    private String date2;
    private String time2;
    private String date3;
    private String time3;

    private String message;

    // 同意は必須（checkbox未チェックだと null/false）
    @AssertTrue(message = "個人情報の取り扱いに同意してください")
    private Boolean consent;

    // -----------------------------
    // ★修正③：電話を選んだら、電話番号は必須＆桁チェック
    // - 入力は「090-1234-5678」「09012345678」どちらでもOK
    // - ここでは数字だけにして、10〜11桁を許可（日本の一般的な想定）
    // -----------------------------
    @AssertTrue(message = "希望連絡方法で「電話」を選択した場合、電話番号を正しく入力してください")
    public boolean isTelRequiredWhenPhone() {
        if (!"phone".equals(contactMethod)) return true;

        if (tel == null) return false;

        String digits = tel.replaceAll("[^0-9]", "");
        return digits.length() >= 10 && digits.length() <= 11;
    }

    // getter / setter
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getMail() { return mail; }
    public void setMail(String mail) { this.mail = mail; }

    public String getTel() { return tel; }
    public void setTel(String tel) { this.tel = tel; }

    public String getContactMethod() { return contactMethod; }
    public void setContactMethod(String contactMethod) { this.contactMethod = contactMethod; }

    public String getInquiryType() { return inquiryType; }
    public void setInquiryType(String inquiryType) { this.inquiryType = inquiryType; }

    public String getPropertyId() { return propertyId; }
    public void setPropertyId(String propertyId) { this.propertyId = propertyId; }

    public String getArea() { return area; }
    public void setArea(String area) { this.area = area; }

    public String getBudget() { return budget; }
    public void setBudget(String budget) { this.budget = budget; }

    public String getLayout() { return layout; }
    public void setLayout(String layout) { this.layout = layout; }

    public String getMoveIn() { return moveIn; }
    public void setMoveIn(String moveIn) { this.moveIn = moveIn; }

    public String getDate1() { return date1; }
    public void setDate1(String date1) { this.date1 = date1; }

    public String getTime1() { return time1; }
    public void setTime1(String time1) { this.time1 = time1; }

    public String getDate2() { return date2; }
    public void setDate2(String date2) { this.date2 = date2; }

    public String getTime2() { return time2; }
    public void setTime2(String time2) { this.time2 = time2; }

    public String getDate3() { return date3; }
    public void setDate3(String date3) { this.date3 = date3; }

    public String getTime3() { return time3; }
    public void setTime3(String time3) { this.time3 = time3; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public Boolean getConsent() { return consent; }
    public void setConsent(Boolean consent) { this.consent = consent; }
}
package com.example.tb2025.contact;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "contacts")
public class Contact {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String mail;
    private String tel;

    @Column(name = "contact_method")
    private String contactMethod;

    @Column(name = "inquiry_type")
    private String inquiryType;

    @Column(name = "property_id")
    private String propertyId;

    private String area;
    private String budget;
    private String layout;

    @Column(name = "move_in")
    private String moveIn;

    // 日付はとりあえずStringでも動く（改善するなら LocalDate/LocalTime に）
    private String date1;
    private String time1;

    private String date2;
    private String time2;

    private String date3;
    private String time3;

    @Column(columnDefinition = "TEXT")
    private String message;

    // 生成日時（DB側でDEFAULT NOW() にしてもOK）
    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    // getter/setter
    public Long getId() { return id; }

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

    public LocalDateTime getCreatedAt() { return createdAt; }
}
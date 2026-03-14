package com.example.tb2025;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * 物件情報を表すEntityクラス。
 *
 * DBテーブル {@code properties} とマッピングし、
 * 物件一覧・物件詳細画面で使用する基本情報を保持する。
 * 
 * 画像は 相対パス保存
 * 例: chintai/house1.jpg
 * 例: chintai-floor-plan/floor1.jpg
 * 
 * 表示用の整形は Java側である程度行う
 * 
 * 関連画像は別テーブル property_images 想定なので、このEntityには持たせない
 * 
 */



@Entity
@Table(name = "properties")
public class Property {

    /**
     * 物件ID（主キー）
     * DBテーブル properties の id 列に対応。
     * AUTO_INCREMENT によりDB側で自動採番される。
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 物件名 / 建物名 / キャッチコピー
     * DB列: title
     * 例: "ゆい サンライト II"
     */
    @Column(name = "title", nullable = false)
    private String title;

    /**
     * 賃料（円）
     * DB列: price
     * 例: 78000
     */
    @Column(name = "price", nullable = false)
    private int price;

    /**
     * 所在地
     * DB列: address
     * 例: "那覇市安里2-15-12"
     */
    @Column(name = "address", nullable = false)
    private String address;

    /**
     * 間取り
     * DB列: layout
     * 例: "1LDK", "2DK", "3LDK"
     */
    @Column(name = "layout", nullable = false)
    private String layout;

    /**
     * 地域ID
     * DB列: area_id
     * 例: 1=北部, 2=中部, 3=南部
     */
    @Column(name = "area_id", nullable = false)
    private int areaId;

    /**
     * 専有面積（㎡）
     * DB列: floor_area
     * 例: 42.5
     */
    @Column(name = "floor_area", nullable = false)
    private double floorArea;

    /**
     * 物件説明
     * DB列: description
     */
    @Column(name = "description", nullable = false)
    private String description;

    /**
     * 表示順
     * DB列: sort_order
     * 数値が小さいほど上位表示する想定。
     */
    @Column(name = "sort_order", nullable = false)
    private int sortOrder;

    /**
     * ペット可否
     * DB列: pet_allowed
     * true = 可 / false = 不可
     */
    @Column(name = "pet_allowed", nullable = false)
    private Boolean petAllowed;

    /**
     * 共益費（円）
     * DB列: common_fee
     * 0 の場合は「なし」として扱う想定。
     */
    @Column(name = "common_fee", nullable = false)
    private Integer commonFee;

    /**
     * 駐車場の状態
     * DB列: parking_type
     * 例:
     * AVAILABLE = あり
     * NONE = なし
     * NEGOTIABLE = 要相談
     */
    @Column(name = "parking_type", nullable = false)
    private String parkingType;

    /**
     * 駐車可能台数
     * DB列: parking_spaces
     * 例: 1, 2
     */
    @Column(name = "parking_spaces")
    private Integer parkingSpaces;

    /**
     * 駐車場料金（1台あたり・円）
     * DB列: parking_fee
     * 例: 5000, 8000
     */
    @Column(name = "parking_fee")
    private Integer parkingFee;

    /**
     * 築年数
     * DB列: building_age_year
     * 例: 5, 12, 20
     */
    @Column(name = "building_age_year")
    private Integer buildingAgeYear;

    /**
     * 敷金（円）
     * DB列: security_deposit
     * 0 の場合は「なし」として扱う想定。
     */
    @Column(name = "security_deposit", nullable = false)
    private Integer securityDeposit;

    /**
     * 礼金（円）
     * DB列: key_money
     * 0 の場合は「なし」として扱う想定。
     */
    @Column(name = "key_money", nullable = false)
    private Integer keyMoney;

    /**
     * 管理会社名
     * DB列: management_company_name
     */
    @Column(name = "management_company_name")
    private String managementCompanyName;

    /**
     * 管理会社電話番号
     * DB列: management_company_tel
     */
    @Column(name = "management_company_tel")
    private String managementCompanyTel;

    /**
     * 物件メイン画像の相対パス
     * DB列: image_url
     * 例: "chintai/house1.jpg"
     */
    @Column(name = "image_url", nullable = false)
    private String imageUrl;

    /**
     * 間取り図画像の相対パス
     * DB列: floor_plan_image_url
     * 例: "chintai-floor-plan/floor1.jpg"
     */
    @Column(name = "floor_plan_image_url")
    private String floorPlanImageUrl;

    // ------------------------------
    // Getter / Setter
    // 基本項目
    // ------------------------------

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLayout() {
        return layout;
    }

    public void setLayout(String layout) {
        this.layout = layout;
    }

    public int getAreaId() {
        return areaId;
    }

    public void setAreaId(int areaId) {
        this.areaId = areaId;
    }

    public double getFloorArea() {
        return floorArea;
    }

    public void setFloorArea(double floorArea) {
        this.floorArea = floorArea;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(int sortOrder) {
        this.sortOrder = sortOrder;
    }

    // ------------------------------
    // Getter / Setter
    // （設備・契約条件）
    // ------------------------------

    public Boolean getPetAllowed() {
        return petAllowed;
    }

    public void setPetAllowed(Boolean petAllowed) {
        this.petAllowed = petAllowed;
    }

    public Integer getCommonFee() {
        return commonFee;
    }

    public void setCommonFee(Integer commonFee) {
        this.commonFee = commonFee;
    }

    public String getParkingType() {
        return parkingType;
    }

    public void setParkingType(String parkingType) {
        this.parkingType = parkingType;
    }

    public Integer getParkingSpaces() {
        return parkingSpaces;
    }

    public void setParkingSpaces(Integer parkingSpaces) {
        this.parkingSpaces = parkingSpaces;
    }

    public Integer getParkingFee() {
        return parkingFee;
    }

    public void setParkingFee(Integer parkingFee) {
        this.parkingFee = parkingFee;
    }

    public Integer getBuildingAgeYear() {
        return buildingAgeYear;
    }

    public void setBuildingAgeYear(Integer buildingAgeYear) {
        this.buildingAgeYear = buildingAgeYear;
    }

    public Integer getSecurityDeposit() {
        return securityDeposit;
    }

    public void setSecurityDeposit(Integer securityDeposit) {
        this.securityDeposit = securityDeposit;
    }

    public Integer getKeyMoney() {
        return keyMoney;
    }

    public void setKeyMoney(Integer keyMoney) {
        this.keyMoney = keyMoney;
    }

    // ------------------------------
    // Getter / Setter
    // 管理会社・画像
    // ------------------------------

    public String getManagementCompanyName() {
        return managementCompanyName;
    }

    public void setManagementCompanyName(String managementCompanyName) {
        this.managementCompanyName = managementCompanyName;
    }

    public String getManagementCompanyTel() {
        return managementCompanyTel;
    }

    public void setManagementCompanyTel(String managementCompanyTel) {
        this.managementCompanyTel = managementCompanyTel;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getFloorPlanImageUrl() {
        return floorPlanImageUrl;
    }

    public void setFloorPlanImageUrl(String floorPlanImageUrl) {
        this.floorPlanImageUrl = floorPlanImageUrl;
    }

    // ------------------------------
    // 表示用メソッド
    // HTML側の分岐を減らすための補助
    // ------------------------------

    /**
     * メイン画像の表示用パス
     * DBには相対パスを保存し、画面表示時に /images/ を付与する。
     */
    public String getImageFullPath() {
        if (imageUrl == null || imageUrl.isBlank()) {
            return "/images/no-image.jpg";
        }
        return "/images/" + imageUrl;
    }

    /**
     * 間取り図の表示用パス
     * 未設定時は代替画像を返す。
     */
    public String getFloorPlanFullPath() {
        if (floorPlanImageUrl == null || floorPlanImageUrl.isBlank()) {
            return "/images/no-image-floor-plan.jpg";
        }
        return "/images/" + floorPlanImageUrl;
    }

    /**
     * 賃料の表示用文字列
     * 例: 78000 -> 7.8万円
     */
    public String getPriceDisplay() {
        return formatYenToMan(price);
    }

    /**
     * 共益費の表示用文字列
     * 例: 3000 -> 0.3万円
     * 0 または null は「なし」
     */
    public String getCommonFeeDisplay() {
        return formatYenToMan(commonFee);
    }

    /**
     * 敷金の表示用文字列
     */
    public String getSecurityDepositDisplay() {
        return formatYenToMan(securityDeposit);
    }

    /**
     * 礼金の表示用文字列
     */
    public String getKeyMoneyDisplay() {
        return formatYenToMan(keyMoney);
    }

    /**
     * ペット可否の表示用文字列
     */
    public String getPetAllowedDisplay() {
        return Boolean.TRUE.equals(petAllowed) ? "可" : "不可";
    }

    /**
     * 築年数の表示用文字列
     * 例: 12 -> 築12年
     */
    public String getBuildingAgeDisplay() {
        if (buildingAgeYear == null) {
            return "要確認";
        }
        return "築" + buildingAgeYear + "年";
    }

    /**
     * 駐車場の表示用文字列
     * parkingType に応じて見やすい文言へ変換する。
     */
    public String getParkingDisplay() {
        if (parkingType == null || parkingType.isBlank()) {
            return "要確認";
        }

        switch (parkingType) {
            case "NONE":
                return "なし";

            case "NEGOTIABLE":
                return "要相談";

            case "AVAILABLE":
                String spaces = parkingSpaces != null ? parkingSpaces + "台可" : "空きあり";
                String fee = (parkingFee != null && parkingFee > 0)
                        ? "（1台 " + formatYenToMan(parkingFee) + "）"
                        : "";
                return spaces + fee;

            default:
                return "要確認";
        }
    }

    /**
     * 円の金額を万円表記へ変換する共通メソッド
     * 例:
     * 78000 -> 7.8万円
     * 100000 -> 10万円
     * 0 or null -> なし
     */
    private String formatYenToMan(Integer value) {
        if (value == null || value == 0) {
            return "なし";
        }

        double man = value / 10000.0;
        if (man == Math.floor(man)) {
            return String.format("%.0f万円", man);
        }
        return String.format("%.1f万円", man);
    }
}

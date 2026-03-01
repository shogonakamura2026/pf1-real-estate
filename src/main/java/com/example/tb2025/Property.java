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
 * DBテーブル {@code properties} とマッピングし、物件一覧・詳細画面で使用する
 * 基本情報（タイトル、価格、住所など）を保持する。
 */
@Entity
@Table(name = "properties")
public class Property {

    /**
     * 物件ID（主キー）
     * DB側の AUTO_INCREMENT（自動採番）と連携して採番される。
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** タイトル（キャッチコピー / 建物名） */
    @Column(name = "title", nullable = false)
    private String title;

    /** 価格（家賃：円） */
    @Column(name = "price", nullable = false)
    private int price;

    /** 住所 */
    @Column(name = "address", nullable = false)
    private String address;

    /** 間取り（例：1DK / 2LDK など） */
    @Column(name = "layout", nullable = false)
    private String layout;

    /** 地域ID（例：1=北部, 2=中部, 3=南部） */
    @Column(name = "area_id", nullable = false)
    private int areaId;

    /** 面積（㎡） */
    @Column(name = "floor_area", nullable = false)
    private double floorArea;

    /** 物件説明（長文） */
    @Column(name = "description", nullable = false)
    private String description;

    /**
     * 画像ファイル名（例：house1.jpg）
     * HTML側で /images/ を付与して参照する想定。
     */
    @Column(name = "image_url", nullable = false)
    private String imageUrl;

    /** 表示順（小さいほど上位に表示） */
    @Column(name = "sort_order", nullable = false)
    private int sortOrder;

    // ------------------------------
    // Getter / Setter
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

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(int sortOrder) {
        this.sortOrder = sortOrder;
    }
}
package com.example.tb2025.blog;

import java.time.OffsetDateTime;

/**
 * スタッフブログの記事データ（1件分）。
 *
 * <p>このプロジェクトではDBを使わず、JSONファイルに保存する方式のため、
 * このクラスは「保存/読込されるデータ構造」を表す。</p>
 */
public class BlogPost {

    /** 一意ID（例：timestamp + ランダム文字列） */
    public String id;

    /** 投稿者名（ログインセッションから設定） */
    public String author;

    /** 記事タイトル */
    public String title;

    /** 記事本文 */
    public String body;

    /** 画像URL（任意：未指定ならnull） */
    public String imageUrl;

    /** 投稿日時 */
    public OffsetDateTime createdAt;

    /** JSON読込（Jackson）用：デフォルトコンストラクタ */
    public BlogPost() {
    }
}
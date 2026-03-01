# pf1-real-estate

Spring Boot を使用して開発した不動産ポートフォリオサイトです。  
実際に VPS 上で本番公開しています。

---

## 🌐 デモサイト（本番環境）

👉 [https://](https://kunnren202511.com/)

※ Ubuntu VPS + Nginx + Spring Boot で公開中

---

## 🛠 使用技術

- Java 17
- Spring Boot
- Maven
- Thymeleaf
- MariaDB
- JSON（ブログデータ保存）
- Nginx
- Ubuntu (VPS)

---

## 📌 主な機能

- 物件一覧表示
- 物件詳細ページ
- お気に入り機能（localStorage）
- 閲覧履歴表示機能
- スタッフブログ機能（JSONファイル保存）
- レスポンシブ対応

---

## 🏗 システム構成

Browser  
↓  
Nginx  
↓  
Spring Boot  
↓  
MariaDB  

---

## 🚀 起動方法（ローカル環境）

```bash
mvn spring-boot:run

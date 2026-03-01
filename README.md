# pf1-real-estate

Spring Boot を使用して開発した不動産ポートフォリオサイトです。

職業訓練校 Webプログラミング科（アプロス学院）でのグループ課題を基に、
個人で機能追加・改修を行い、本番環境へデプロイしております。

訓練校で扱っていない技術についても自主的に調査・実装を行い、
学習の深化を目的として継続的にバージョンアップを行っています。

---

## デモサイト（本番環境）

https://kunnren202511.com/

※ Ubuntu VPS + Nginx + Spring Boot で公開中

---

## 使用技術

### 開発環境
- Windows 10

### 本番環境
- Ubuntu (VPS)
- Nginx

### バックエンド
- Java 17
- Spring Boot 3.2.3
- Maven
- Thymeleaf

### データベース
- MariaDB
- 文字コード: UTF-8 (utf8mb4)

### その他
- JSON（ブログデータ保存）
- localStorage（お気に入り・閲覧履歴）

---

## データベース設計

| カラム名 | 型 | 用途 |
|----------|------|------|
| id | BIGINT | AUTO_INCREMENT による物件ID |
| title | VARCHAR(255) | 物件名・キャッチコピー |
| price | INT | 家賃 |
| address | VARCHAR(255) | 住所 |
| layout | VARCHAR(50) | 間取り |
| floor_area | DOUBLE | 面積 |
| description | TEXT | 詳細説明 |
| image_url | VARCHAR(255) | 画像パス |
| sort_order | INT | 表示順 |
| area_id | INT | 地域区分 |

---

## 主な機能

- 物件一覧表示（家賃範囲・地域・間取りによる複合検索）
- 物件詳細ページ
- 地域IDを利用したおすすめ物件表示（3件）
- お気に入り機能（localStorage）
- 閲覧履歴表示機能
- 各ページ右上にフローティングUI実装
- スタッフブログ機能（JSONファイル保存）
- レスポンシブ対応

---

## システム構成

Browser  
↓  
Nginx  
↓  
Spring Boot  
↓  
MariaDB  

---

## 🚀 ローカル起動方法

```bash
mvn spring-boot:run

---
## 工夫した点・苦労した点

- グループ課題をベースに、設計の見直し・機能追加・VPS公開まで一貫して個人で構築しました。
- 訓練校で扱っていない VPS構築・Nginx設定・SSL対応まで自主的に学習し実装しました。
- AI（ChatGPT）を補助ツールとして活用しながらも、生成コードの理解・検証・修正は行い、動作確認を徹底しました。
- 可読性・保守性を意識し、グループ学習において見返せる記述を心掛けました。

---

## 今後の改善予定
- バリデーション強化および例外処理の最適化
- テストコードの追加（JUnit）
- REST API化への拡張検討
- Dockerによる環境構築の自動化
- AIを活用した開発効率向上と品質管理の強化




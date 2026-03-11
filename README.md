# PF1 Real Estate  
Spring Boot を使用して開発した **不動産ポートフォリオサイト**です。

職業訓練校 Webプログラミング科（アプロス学院）のグループ課題を基に、  
個人で機能追加・改修を行い、本番環境へデプロイしています。

訓練校で扱っていない技術についても自主的に調査・実装を行い、  
学習の深化を目的として継続的にバージョンアップを行っています。

---

# デモサイト（本番環境）

https://kunnren202511.com/

※ Ubuntu VPS + Nginx + Spring Boot で公開中

---

# 技術スタック

## 開発環境
- Windows 10

## 本番環境
- Ubuntu (VPS)
- Nginx

## バックエンド
- Java 17
- Spring Boot 3.4.2
- Maven
- Thymeleaf

## データベース
- MariaDB
- 文字コード：utf8mb4

## その他
- JSON（ブログデータ保存）
- localStorage（お気に入り・閲覧履歴）

---

# システム構成

```
Browser
  ↓
Nginx
  ↓
Spring Boot
  ↓
MariaDB
```

---

# データベース設計

DB名：`real_estate`

## properties（物件テーブル）

```sql
CREATE TABLE properties (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  title VARCHAR(255) NOT NULL,
  price INT NOT NULL,
  address VARCHAR(255) NOT NULL,
  layout VARCHAR(50) NOT NULL,
  floor_area DOUBLE NOT NULL,
  description TEXT NOT NULL,
  image_url VARCHAR(255) NOT NULL,
  sort_order INT NOT NULL DEFAULT 9999,
  area_id INT NOT NULL
);
```

---

## contacts（問い合わせテーブル）

```sql
CREATE TABLE contacts (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(100) NOT NULL,
  mail VARCHAR(255) NOT NULL,
  tel VARCHAR(30),
  contact_method VARCHAR(20) NOT NULL,
  inquiry_type VARCHAR(30) NOT NULL,
  property_id VARCHAR(255),
  area VARCHAR(100),
  budget VARCHAR(100),
  layout VARCHAR(20),
  move_in VARCHAR(20),
  date1 VARCHAR(20),
  time1 VARCHAR(10),
  date2 VARCHAR(20),
  time2 VARCHAR(10),
  date3 VARCHAR(20),
  time3 VARCHAR(10),
  message TEXT NOT NULL,
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
);
```

---

# 主な機能

- 物件一覧表示  
  （家賃・地域・間取りによる複合検索）

- 物件詳細ページ

- 地域IDを利用したおすすめ物件表示  
  （同地域の物件を3件表示）

- お気に入り機能  
  localStorageを利用

- 閲覧履歴表示機能

- フローティングUI  
  （お気に入り・履歴などへ即アクセス）

- スタッフブログ機能  
  JSONファイル保存方式

- レスポンシブ対応

3/8 更新情報
☆「メールアドレスの正規化処理」
メール入力の揺れを吸収するため、setterで入力値を正規化。
処理内容
・全角 → 半角変換
・前後スペース削除
・大文字 → 小文字変換

例：
ＴＥＳＴ＠ＥＸＡＭＰＬＥ．ＣＯＭ
→ test@example.com

目的：
入力ミスを減らす
DBデータの統一

☆電話番号の入力
電話番号入力の柔軟性を改善。

対応内容：
全角数字 → 半角

対応例：
０９０ー１２３４ー５６７８
090―1234―5678
090-1234-5678

すべて同じ形式として処理。
数字 + ハイフンのみ許可

ハイフン除去後 10〜11桁チェック


☆お問い合わせ内容の安全対策
相談欄（message）の入力制御を追加。

長文対策：
@Size(max = 1000)
1000文字以内に制限。

script対策（簡易）
危険な文字列を拒否

検知対象：
<script
</script>
javascript:

XSS対策の補助として実装。

☆XSS対策の確認
お問い合わせ内容表示は

th:textを使用し、HTMLエスケープを確保。

危険な書き方（回避）
th:utext

☆サイト全体のCSS階層整理、競合CSSを削除
保守性の向上

---

# ローカル起動方法

```bash
mvn spring-boot:run
```

---

# 工夫した点・苦労した点

- グループ課題をベースに、設計の見直し・機能追加・VPS公開まで一貫して個人で構築しました。

- 訓練校では扱っていない  
  **VPS構築・Nginx設定・SSL対応まで自主的に学習し実装しました。**

- AI（ChatGPT）を補助ツールとして活用しながらも  
  **生成コードの理解・検証・修正を行い動作確認を徹底しました。**

- 可読性・保守性を意識し  
  **他の学習者が見ても理解しやすい構成を心掛けました。**

- GitHubに機密情報を含む application.properties を誤ってコミットしてしまったため、git-filter-repo を使用して履歴から完全削除しました。
  **その後 .gitignore の設定を見直し、機密情報を含む設定ファイルがリポジトリに含まれないよう対策を実施しています。**

- フォームのバリデーションの強化


---

# 今後の改善予定

## 機能改善・品質向上
3/8時点
- さらにバリデーション強化
- 例外処理の最適化
- DBデータ追加、削除用ページ作成
---

## 更新履歴

※ 2026/3/4 フォームアップデート

- 問い合わせ種別追加  
  （資料請求 / 内見予約 / 来店予約 / 入居・購入相談 / その他）

- 希望連絡方法  
  （メール / 電話）

- 電話選択時の必須バリデーション

- 物件ID・物件名自動反映

- 希望エリア入力

- 予算（賃料または価格）

- 間取り

- 入居希望時期

- 内見希望日時（第1〜第3希望）

- 個人情報同意チェック

- 送信前確認画面

- 送信完了画面

---
※ 2026/3/6 
- フッターをフッターを共通パーツ化（フラグメントその1）
サイト全体で共通のフッターをThymeleafのフラグメント機能を利用。 サイト名や著作権年などの共通データはSpringの@ControllerAdviceを利用。（保守性の向上）
サイト名や年代など@ModelAttribute経由にて表示。

- cookie同意項目追加（フラグメントその2）
localStorageを利用して同意後は再表示無し、キャッシュクリアで再度表示可。

- プライバシーポリシーなど、各サイト内コンテンツ作成（footer部分参照）

- 学習目的のためお気に入り、履歴ページlocalStorage実装部分の解説を詳細に追記（AIにて清書）

- Contactフォームの内見希望日カレンダー機能アップデート：現在の日時以前の日付、定休日等選択時に警告表示。

---
※2026/3/8
- フォームのバリデーション強化
- CSSレイアウト見直し、階層整理にて保守性向上
- 右下折りたたみ式の共通パーツ作成
- 共通パーツをhtmlから分離（header、footer、その他パーツ）し保守性向上
---
※2026/3/11
- フォームのバリデーション（名前入力欄Script対策）
- 日付以前の時間帯選択ができるコードが抜けてしまっていたため追加し直し。
- 物件一覧、物件詳細から問い合わせた際にhiddenでidを保持、問い合わせテキストエリア内に物件名を自動で入力、DBへidを渡す流れを実装。



## 今後の予定
- 管理者アカウント管理
- 物件追加 / 編集 / 削除機能（簡易CMS化）
- PDF等ダウンロード可能コンテンツをどう管理するのが効率がいいか調べる
- 動画コンテンツ作成
- 素材等の追加作成、デザイン学習
---





package com.example.tb2025.blog;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

/**
 * DBの代わりに JSONファイルへブログ記事を保存・読込するストアクラス。
 *
 * 保存先：実行ディレクトリ基準 {@code ./data/staff-blog.json}
 * 形式：{@link BlogPost} の {@code List} を丸ごと保存する
 *
 * 学習用途のため、簡易的に synchronized で排他制御を行う（同時投稿対策）。
 */
@Service
public class BlogFileStore {

    /** 保存先パス（実行ディレクトリ基準） */
    private final Path filePath = Path.of("data", "staff-blog.json");

    /** JSON変換（Jackson） */
    private final ObjectMapper mapper;

    public BlogFileStore() {
        this.mapper = new ObjectMapper();
        // OffsetDateTime を JSON に保存/復元できるようにする
        this.mapper.registerModule(new JavaTimeModule());
    }

    /**
     * 全記事を読み込む（新しい順：先頭が最新の想定）。
     *
     * @return 記事リスト（ファイルが無い/空なら空リスト）
     */
    public synchronized List<BlogPost> load() {
        try {
            if (!Files.exists(filePath)) {
                return new ArrayList<>();
            }

            String json = Files.readString(filePath, StandardCharsets.UTF_8);
            if (json == null || json.isBlank()) {
                return new ArrayList<>();
            }

            List<BlogPost> posts = mapper.readValue(json, new TypeReference<List<BlogPost>>() {});
            return (posts != null) ? posts : new ArrayList<>();

        } catch (Exception e) {
            // 学習用途：JSONが壊れていてもアプリ全体を落とさず空で返す
            return new ArrayList<>();
        }
    }

    /**
     * 新規投稿を追加する（先頭に追加＝新しい順）。
     * <p>※ページングしたいので、古い記事は削除しない（全件保持）。</p>
     */
    public synchronized void add(String author, String title, String body, String imageUrl) {
        List<BlogPost> posts = load();

        BlogPost p = new BlogPost();
        p.id = System.currentTimeMillis() + "-" + UUID.randomUUID().toString().substring(0, 8);
        p.author = safe(author);
        p.title = safe(title);
        p.body = safe(body);
        p.imageUrl = (imageUrl == null || imageUrl.isBlank()) ? null : imageUrl.trim();
        p.createdAt = OffsetDateTime.now();

        // 新しい投稿を先頭へ
        posts.add(0, p);

        save(posts);
    }

    /**
     * 指定IDの記事を削除する。
     *
     * @param id 削除対象の記事ID
     */
    public synchronized void deleteById(String id) {
        List<BlogPost> posts = load();
        posts.removeIf(p -> p.id != null && p.id.equals(id));
        save(posts);
    }

    // ==============================
    // private
    // ==============================

    /** null対策＋トリム（空文字は許可） */
    private String safe(String s) {
        return (s == null) ? "" : s.trim();
    }

    /**
     * 記事一覧をJSONへ上書き保存する。
     */
    private synchronized void save(List<BlogPost> posts) {
        try {
            Files.createDirectories(filePath.getParent());

            String out = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(posts);

            Files.writeString(
                    filePath,
                    out,
                    StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING
            );

        } catch (Exception e) {
            throw new RuntimeException("ブログ保存に失敗しました: " + filePath.toAbsolutePath(), e);
        }
    }
}

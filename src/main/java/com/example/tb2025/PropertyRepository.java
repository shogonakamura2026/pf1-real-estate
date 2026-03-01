package com.example.tb2025;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * 物件（Property）テーブルへのDBアクセスを担当するRepository。
 *
 * 役割：
 * 
 *   物件一覧の絞り込み検索（search）
 *   詳細ページの「こちらもチェック」用データ取得
 * 
 */
@Repository
public interface PropertyRepository extends JpaRepository<Property, Long> {

    /**
     * 物件一覧（絞り込み）用の検索。
     *
     * ポイント：
     * 
     *   null/空の条件は「未指定」として無視する
     *   JPQL（エンティティ基準のクエリ）で検索する
     *
     *
     * @param areaId   エリアID（未指定ならnull）
     * @param layout   間取り（未指定ならnull/空文字）
     * @param minPrice 最低家賃（未指定ならnull）
     * @param maxPrice 最高家賃（未指定ならnull）
     * @return 条件に一致する物件リスト
     */
    @Query("""
            SELECT p FROM Property p
            WHERE (:areaId IS NULL OR p.areaId = :areaId)
              AND (:layout IS NULL OR :layout = '' OR p.layout = :layout)
              AND (:minPrice IS NULL OR p.price >= :minPrice)
              AND (:maxPrice IS NULL OR p.price <= :maxPrice)
            ORDER BY p.price ASC, p.sortOrder ASC, p.id DESC
            """)
    List<Property> search(
            @Param("areaId") Integer areaId,
            @Param("layout") String layout,
            @Param("minPrice") Integer minPrice,
            @Param("maxPrice") Integer maxPrice
    );

    /**
     * 詳細ページの「こちらもチェック」用。
     *
     * 条件：
     * 
     *   同じエリア（areaId）
     *   表示中の物件（id）は除外
     *   上位3件だけ取得（Top3）
     * 
     *
     * ※このメソッドは、名前のルールに従って Spring Data JPA がクエリを自動生成する。
     */
    List<Property> findTop3ByAreaIdAndIdNotOrderBySortOrderAscIdDesc(int areaId, Long id);
}
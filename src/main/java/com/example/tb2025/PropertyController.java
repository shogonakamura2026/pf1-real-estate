package com.example.tb2025;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

/**
 * 物件関連ページのController（画面の入口）。
 *
 * 主な役割：
 * 
 *   物件一覧（絞り込み検索）
 *   物件詳細（おすすめ表示、地図用座標の設定）
 *   お気に入り / 履歴ページ（localStorage表示：DBは使わない）
 *   固定ページ（会社概要 / お問い合わせ / スタッフ）
 * 
 */
@Controller
public class PropertyController {

    private final PropertyRepository repo;

    public PropertyController(PropertyRepository repo) {
        this.repo = repo;
    }

    // ==============================
    // 固定ページ（ダミー含む）
    // ==============================

    /** スタッフページ（今回はダミー） */
    @GetMapping("/staff")
    public String staff() {
        return "staff";
    }

    /** お問い合わせページ（Contact用Controllerと競合するためコメントアウト） */
	/* @GetMapping("/contact")
	public String contact() {
	    return "contact";
	}*/

    /** 会社概要ページ */
    @GetMapping("/about")
    public String about() {
        return "about";
    }

    // ==============================
    // 物件一覧（絞り込み）
    // ==============================

    /**
     * 物件一覧ページ（絞り込み検索対応）。
     *
     * @param areaId   エリアID（未指定ならnull）
     * @param layout   間取り（未指定ならnull/空文字）
     * @param minPrice 最低家賃（未指定ならnull）
     * @param maxPrice 最高家賃（未指定ならnull）
     */
    @GetMapping("/properties")
    public String properties(
            @RequestParam(required = false) Integer areaId,
            @RequestParam(required = false) String layout,
            @RequestParam(required = false) Integer minPrice,
            @RequestParam(required = false) Integer maxPrice,
            Model model
    ) {
        model.addAttribute("properties", repo.search(areaId, layout, minPrice, maxPrice));

        // フォームに入力値を保持して再表示する（検索後も選択状態を維持）
        model.addAttribute("areaId", areaId);
        model.addAttribute("layout", layout);
        model.addAttribute("minPrice", minPrice);
        model.addAttribute("maxPrice", maxPrice);

        // 検索結果件数表示などの装飾用（テンプレ側で利用）
        model.addAttribute("resultCountColor", "red");

        return "properties";
    }

    // ==============================
    // 物件詳細
    // ==============================

    /**
     * 物件詳細ページ。
     * 
     *   物件情報の表示
     *   地図表示用の座標設定（areaIdベース）
     *   同エリアのおすすめ物件（3件）
     * 
     */
    @GetMapping("/properties/{id}")
    public String detail(@PathVariable Long id, Model model) {
        Property property = repo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "物件が見つかりません"));

        double[] latLng = resolveMapLatLng(property.getAreaId());

        model.addAttribute("property", property);
        model.addAttribute("mapLat", latLng[0]);
        model.addAttribute("mapLng", latLng[1]);

        model.addAttribute(
                "recommendations",
                repo.findTop3ByAreaIdAndIdNotOrderBySortOrderAscIdDesc(property.getAreaId(), property.getId())
        );

        return "property-detail";
    }

    /**
     * エリアIDから地図表示用の代表座標を返す。
     * ※今回の仕様：エリアごとに固定の代表地点を使用する
     */
    private double[] resolveMapLatLng(int areaId) {
        return switch (areaId) {
            case 1 -> new double[]{26.5910, 127.9770};
            case 2 -> new double[]{26.3340, 127.8050};
            case 3 -> new double[]{26.2124, 127.6792};
            default -> new double[]{26.2124, 127.6792};
        };
    }

    // ==============================
    // localStorage表示ページ（DB未使用）
    // ==============================

    /** お気に入り一覧（localStorageの内容を表示） */
    @GetMapping("/favorites")
    public String favorites() {
        return "favorites";
    }

    /** 履歴一覧（localStorageの内容を表示） */
    @GetMapping("/history")
    public String history() {
        return "history";
    }
}
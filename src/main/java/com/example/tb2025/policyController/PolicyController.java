package com.example.tb2025.policyController;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PolicyController {

    /* プライバシーポリシー */
    @GetMapping("/privacy")
    public String privacy() {
        return "privacy";
    }

    /* 利用規約 */
    @GetMapping("/terms")
    public String terms() {
        return "terms";
    }

    /* 外部送信ポリシー */
    @GetMapping("/external-transmission")
    public String externalTransmission() {
        return "external-transmission";
    }

    /* サイトマップ */
    @GetMapping("/sitemap")
    public String sitemap() {
        return "sitemap";
    }

    /* 掲載申し込み */
    @GetMapping("/listing-apply")
    public String listingApply() {
        return "listing-apply";
    }

    /* 会社概要 */
    @GetMapping("/about")
    public String about() {
        return "about";
    }

    /* 特定商取引法に基づく表記 */
    @GetMapping("/legal")
    public String legal() {
        return "legal";
    }

}
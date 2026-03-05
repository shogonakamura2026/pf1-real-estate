package com.example.tb2025.policyController;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PolicyController {

    @GetMapping("/privacy")
    public String privacy(Model model) {
        return "privacy";
    }

    @GetMapping("/terms")
    public String terms(Model model) {
        return "terms";
    }

    @GetMapping("/external-transmission")
    public String externalTransmission(Model model) {
        return "external-transmission";
    }

    @GetMapping("/sitemap")
    public String sitemap(Model model) {
        return "sitemap";
    }

    @GetMapping("/listing-apply")
    public String listingApply(Model model) {
        return "listing-apply";
    }
}
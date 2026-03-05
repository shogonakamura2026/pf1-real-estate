package com.example.tb2025.config;

import java.time.Year;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;


//footer用、全ページ共通で出せるように（練習）

@ControllerAdvice
public class GlobalModelAttributes {

  @ModelAttribute
  public void addCommonAttributes(Model model) {
    model.addAttribute("thisYear", Year.now().getValue());
    model.addAttribute("siteName", "チームBe不動産");
  }
}
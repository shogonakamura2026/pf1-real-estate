package com.example.tb2025.contact;

import jakarta.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ContactController {

    private final ContactRepository contactRepository;

    public ContactController(ContactRepository contactRepository) {
        this.contactRepository = contactRepository;
    }

    // =========================================================
    // 1) 入力画面表示
    // =========================================================
    // 初回アクセス時は空の ContactForm を作る
    // ただし、リダイレクト後などで既に contactForm がある場合は上書きしない
    @GetMapping("/contact")
    public String contact(Model model) {
        if (!model.containsAttribute("contactForm")) {
            model.addAttribute("contactForm", new ContactForm());
        }
        return "contact";
    }

    // =========================================================
    // 2) 確認画面から「修正する」で戻る用
    // =========================================================
    // confirm 画面から hidden で値を持ち帰って、そのまま入力画面へ戻す
    @PostMapping("/contact")
    public String backToContact(@ModelAttribute("contactForm") ContactForm form) {
        return "contact";
    }

    // =========================================================
    // 3) 入力内容チェック → 確認画面へ
    // =========================================================
    // @Valid により ContactForm のアノテーションバリデーションが動く
    @PostMapping("/contact/confirm")
    public String confirm(
            @Valid @ModelAttribute("contactForm") ContactForm form,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            return "contact";
        }

        return "contact-confirm";
    }

    // =========================================================
    // 4) 送信確定 → DB保存 → 完了画面
    // =========================================================
    @PostMapping("/contact/complete")
    public String complete(
            @Valid @ModelAttribute("contactForm") ContactForm form,
            BindingResult bindingResult,
            RedirectAttributes ra
    ) {
        // 学習ポイント：
        // complete でも再度 @Valid を付けておくことで、
        // 確認画面を飛ばして直接送信された場合にも守れる
        if (bindingResult.hasErrors()) {
            ra.addFlashAttribute("org.springframework.validation.BindingResult.contactForm", bindingResult);
            ra.addFlashAttribute("contactForm", form);
            return "redirect:/contact";
        }

        // =====================================================
        // 5) DB保存用に値を整える
        // =====================================================
        // メールアドレスは ContactForm の setter ですでに
        // 半角化・trim・小文字化が済んでいるのでそのまま使ってよい
        String normalizedMail = form.getMail();

        // 電話番号は入力時はハイフン付きOKだが、
        // DBには数字だけで保存する
        String normalizedTel = form.getTel();
        if (normalizedTel != null && !normalizedTel.isBlank()) {
            normalizedTel = normalizedTel.replace("-", "");
        }

        // =====================================================
        // 6) Entity に詰め替えて保存
        // =====================================================
        Contact c = new Contact();

        c.setName(form.getName());
        c.setMail(normalizedMail);
        c.setTel(normalizedTel);

        c.setContactMethod(form.getContactMethod());
        c.setInquiryType(form.getInquiryType());
        c.setPropertyId(form.getPropertyId());

        c.setArea(form.getArea());
        c.setBudget(form.getBudget());
        c.setLayout(form.getLayout());
        c.setMoveIn(form.getMoveIn());

        c.setDate1(form.getDate1());
        c.setTime1(form.getTime1());
        c.setDate2(form.getDate2());
        c.setTime2(form.getTime2());
        c.setDate3(form.getDate3());
        c.setTime3(form.getTime3());

        c.setMessage(form.getMessage());

        contactRepository.save(c);

        return "contact-complete";
    }
}
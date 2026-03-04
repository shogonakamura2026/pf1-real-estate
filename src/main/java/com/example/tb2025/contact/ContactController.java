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

    // ① 入力画面（GET）
    @GetMapping("/contact")
    public String contact(Model model) {
        if (!model.containsAttribute("contactForm")) {
            model.addAttribute("contactForm", new ContactForm());
        }
        return "contact";
    }

    // ★戻って修正（POST）
    // confirm画面から hidden で値を持ったまま戻るための受け口
    @PostMapping("/contact")
    public String backToContact(@ModelAttribute("contactForm") ContactForm form) {
        return "contact";
    }

    // ② 確認画面（POST）
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

    // ③ 送信確定（POST）→ DB保存 → 完了画面
    @PostMapping("/contact/complete")
    public String complete(
            @Valid @ModelAttribute("contactForm") ContactForm form,
            BindingResult bindingResult,
            RedirectAttributes ra
    ) {
        if (bindingResult.hasErrors()) {
            ra.addFlashAttribute("org.springframework.validation.BindingResult.contactForm", bindingResult);
            ra.addFlashAttribute("contactForm", form);
            return "redirect:/contact";
        }

        // -----------------------------
        // ★修正②：電話番号を「数字だけ」に正規化して保存（ハイフン/スペース等を除去）
        // 例）090-1234-5678 → 09012345678
        // -----------------------------
        if (form.getTel() != null) {
            form.setTel(form.getTel().replaceAll("[^0-9]", ""));
        }

        Contact c = new Contact();

        c.setName(form.getName());
        c.setMail(form.getMail());
        c.setTel(form.getTel()); // 正規化後の値が入る
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
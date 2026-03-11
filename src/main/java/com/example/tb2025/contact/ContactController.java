package com.example.tb2025.contact;

import java.util.Optional;

import jakarta.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.tb2025.Property;
import com.example.tb2025.PropertyRepository;

@Controller
public class ContactController {

    private final ContactRepository contactRepository;
    private final PropertyRepository propertyRepository;

    public ContactController(
            ContactRepository contactRepository,
            PropertyRepository propertyRepository
    ) {
        this.contactRepository = contactRepository;
        this.propertyRepository = propertyRepository;
    }

    // =========================================================
    // 1) 入力画面表示
    // =========================================================
    // 学習ポイント：
    // 物件詳細ページから
    // /contact?propertyId=xx
    // の形で来た場合、その propertyId をフォームに引き継ぐ。
    //
    // さらに PropertyRepository を使って物件名を取得し、
    // message の先頭へ
    // 「物件『○○』について」
    // という初期文を入れる。
    //
    // ただし、バリデーションエラー後の再表示などで
    // すでに contactForm がある場合は上書きしない。
    @GetMapping("/contact")
    public String contact(
            @RequestParam(required = false) String propertyId,
            Model model
    ) {
        if (!model.containsAttribute("contactForm")) {
            ContactForm form = new ContactForm();

            if (propertyId != null && !propertyId.isBlank()) {
                form.setPropertyId(propertyId);

                try {
                    Long id = Long.valueOf(propertyId);
                    Optional<Property> propertyOpt = propertyRepository.findById(id);

                    if (propertyOpt.isPresent()) {
                        Property property = propertyOpt.get();

                        // ここは Property クラスの物件名フィールド名に合わせて変更
                        // 例：getTitle(), getName(), getPropertyName() など
                        form.setPropertyName(property.getTitle());

                        // message が空のときだけ初期文を入れる
                        form.applyPropertyContextMessage();

                        // 画面側でも必要なら使えるように渡す
                        model.addAttribute("propertyName", property.getTitle());
                    }
                } catch (NumberFormatException e) {
                    // propertyId が数値でない場合は通常のお問い合わせフォームとして表示
                }
            }

            model.addAttribute("contactForm", form);
        }

        return "contact";
    }

    // =========================================================
    // 2) 確認画面から「修正する」で戻る用
    // =========================================================
    @PostMapping("/contact")
    public String backToContact(@ModelAttribute("contactForm") ContactForm form) {
        return "contact";
    }

    // =========================================================
    // 3) 入力内容チェック → 確認画面へ
    // =========================================================
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
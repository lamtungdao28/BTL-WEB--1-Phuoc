package com.ptit.thuvien.controller;

import com.ptit.thuvien.service.SuKienService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 * Controller sự kiện
 */
@Controller
@RequestMapping("/su-kien")
@RequiredArgsConstructor
public class SuKienController {

    private final SuKienService suKienService;

    @GetMapping
    public String danhSach(Model model) {
        model.addAttribute("dsSapDienRa", suKienService.laySapDienRa());
        model.addAttribute("dsDaDienRa", suKienService.layDaDienRa());
        return "su-kien/su-kien";
    }

    @GetMapping("/{id}")
    public String chiTiet(@PathVariable Long id, Model model) {
        suKienService.timTheoId(id).ifPresent(sk -> model.addAttribute("suKien", sk));
        return "su-kien/chi-tiet-su-kien";
    }
}

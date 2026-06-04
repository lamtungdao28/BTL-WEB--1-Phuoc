package com.ptit.thuvien.controller;

import com.ptit.thuvien.service.TinTucService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 * Controller tin tức
 */
@Controller
@RequestMapping("/tin-tuc")
@RequiredArgsConstructor
public class TinTucController {

    private final TinTucService tinTucService;

    /**
     * Danh sách tin tức (phân trang)
     */
    @GetMapping
    public String danhSach(@RequestParam(defaultValue = "0") int page, Model model) {
        model.addAttribute("trang", tinTucService.layTatCa(PageRequest.of(page, 5)));
        model.addAttribute("trangHienTai", page);
        return "tin-tuc/tin-tuc";
    }

    /**
     * Chi tiết tin tức
     */
    @GetMapping("/{id}")
    public String chiTiet(@PathVariable Long id, Model model) {
        tinTucService.tangLuotXem(id);
        tinTucService.timTheoId(id).ifPresent(tin -> model.addAttribute("tinTuc", tin));
        return "tin-tuc/chi-tiet-tin";
    }
}

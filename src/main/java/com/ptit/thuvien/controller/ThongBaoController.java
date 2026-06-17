package com.ptit.thuvien.controller;

import com.ptit.thuvien.service.ThongBaoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 * Controller thông báo thư viện
 */
@Controller
@RequestMapping("/thong-bao")
@RequiredArgsConstructor
public class ThongBaoController {

    private final ThongBaoService thongBaoService;

    @GetMapping
    public String danhSach(Model model) {
        model.addAttribute("dsThongBao", thongBaoService.layTatCa());
        return "trang/thong-bao";
    }

    @GetMapping("/{id}")
    public String chiTiet(@PathVariable Long id, Model model) {
        thongBaoService.tangLuotXem(id);
        thongBaoService.timTheoId(id).ifPresent(tb -> model.addAttribute("thongBao", tb));
        return "trang/chi-tiet-thong-bao";
    }
}

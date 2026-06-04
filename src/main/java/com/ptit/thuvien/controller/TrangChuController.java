package com.ptit.thuvien.controller;

import com.ptit.thuvien.service.TinTucService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controller trang chủ
 */
@Controller
@RequiredArgsConstructor
public class TrangChuController {

    private final TinTucService tinTucService;

    @GetMapping({"/", "/trang-chu"})
    public String trangChu(Model model) {
        // Lấy 4 tin tức mới nhất cho trang chủ
        model.addAttribute("dsTinTuc", tinTucService.layTatCa(PageRequest.of(0, 4)).getContent());
        return "trang/trang-chu";
    }

    @GetMapping("/gioi-thieu")
    public String gioiThieu() {
        return "trang/gioi-thieu";
    }

    @GetMapping("/noi-quy")
    public String noiQuy() {
        return "trang/noi-quy-thu-vien";
    }
}

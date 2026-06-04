package com.ptit.thuvien.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controller đăng nhập / đăng xuất
 * Spring Security xử lý POST /xu-ly-dang-nhap tự động
 * Controller chỉ cần render trang form đăng nhập
 */
@Controller
public class DangNhapController {

    @GetMapping("/dang-nhap")
    public String formDangNhap() {
        return "trang/dang-nhap";
    }
}

package com.ptit.thuvien.controller;

import com.ptit.thuvien.model.NguoiDung;
import com.ptit.thuvien.service.NguoiDungService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controller đăng ký tài khoản mới
 */
@Controller
@RequiredArgsConstructor
public class DangKyController {

    private final NguoiDungService nguoiDungService;

    @GetMapping("/dang-ky")
    public String formDangKy() {
        return "trang/dang-ky";
    }

    @PostMapping("/dang-ky")
    public String xuLyDangKy(@RequestParam String taiKhoan,
                              @RequestParam String matKhau,
                              @RequestParam String hoTen,
                              @RequestParam String email,
                              @RequestParam(required = false) String soDienThoai,
                              @RequestParam(required = false) String lop,
                              RedirectAttributes redirect) {

        // Kiểm tra tài khoản đã tồn tại
        if (nguoiDungService.tonTaiTaiKhoan(taiKhoan)) {
            redirect.addFlashAttribute("error", "Tài khoản đã tồn tại!");
            redirect.addFlashAttribute("taiKhoan", taiKhoan);
            redirect.addFlashAttribute("hoTen", hoTen);
            redirect.addFlashAttribute("email", email);
            return "redirect:/dang-ky";
        }

        // Kiểm tra email đã tồn tại
        if (nguoiDungService.tonTaiEmail(email)) {
            redirect.addFlashAttribute("error", "Email đã được sử dụng!");
            redirect.addFlashAttribute("taiKhoan", taiKhoan);
            redirect.addFlashAttribute("hoTen", hoTen);
            redirect.addFlashAttribute("email", email);
            return "redirect:/dang-ky";
        }

        // Tạo người dùng mới
        NguoiDung nguoiDung = NguoiDung.builder()
                .taiKhoan(taiKhoan)
                .matKhau(matKhau)
                .hoTen(hoTen)
                .email(email)
                .soDienThoai(soDienThoai)
                .lop(lop)
                .vaiTro(NguoiDung.VaiTro.SINH_VIEN)
                .trangThai(NguoiDung.TrangThaiNguoiDung.HOAT_DONG)
                .build();

        nguoiDungService.luu(nguoiDung);

        redirect.addFlashAttribute("success", "Đăng ký thành công! Vui lòng đăng nhập.");
        return "redirect:/dang-nhap";
    }
}

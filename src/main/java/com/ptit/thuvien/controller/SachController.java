package com.ptit.thuvien.controller;

import com.ptit.thuvien.model.NguoiDung;
import com.ptit.thuvien.service.DanhMucService;
import com.ptit.thuvien.service.PhieuMuonService;
import com.ptit.thuvien.service.TaiLieuService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 * Controller sách (dành cho Sinh viên) - Xem sách, Sách của tôi
 */
@Controller
@RequestMapping("/sach")
@RequiredArgsConstructor
public class SachController {

    private final TaiLieuService taiLieuService;
    private final DanhMucService danhMucService;
    private final PhieuMuonService phieuMuonService;

    /**
     * Trang danh sách sách để mượn
     */
    @GetMapping("/muon-sach")
    public String danhSachSach(@RequestParam(required = false) Long danhMuc,
                                @RequestParam(required = false) String tuKhoa,
                                Model model) {
        model.addAttribute("dsDanhMuc", danhMucService.layTatCa());

        if (tuKhoa != null && !tuKhoa.isEmpty()) {
            model.addAttribute("dsTaiLieu", taiLieuService.timKiem(tuKhoa));
            model.addAttribute("tuKhoa", tuKhoa);
        } else if (danhMuc != null) {
            model.addAttribute("dsTaiLieu", taiLieuService.timTheoDanhMuc(danhMuc));
        } else {
            model.addAttribute("dsTaiLieu", taiLieuService.layTatCa());
        }

        return "sach/muon-sach";
    }

    /**
     * Trang chi tiết sách
     */
    @GetMapping("/chi-tiet/{id}")
    public String chiTietSach(@PathVariable Long id, Model model) {
        taiLieuService.timTheoId(id).ifPresent(tl -> model.addAttribute("taiLieu", tl));
        return "sach/chi-tiet-sach";
    }

    /**
     * Trang form đăng ký mượn sách
     */
    @GetMapping("/dang-ky-muon/{id}")
    public String formDangKyMuon(@PathVariable Long id, Model model, HttpSession session) {
        taiLieuService.timTheoId(id).ifPresent(tl -> model.addAttribute("taiLieu", tl));

        // Truyền thông tin người dùng từ session
        NguoiDung nguoiDung = (NguoiDung) session.getAttribute("nguoiDung");
        model.addAttribute("nguoiDung", nguoiDung);

        return "sach/dang-ky-muon";
    }

    /**
     * Trang sách của tôi - xem phiếu mượn
     */
    @GetMapping("/sach-cua-toi")
    public String sachCuaToi(HttpSession session, Model model) {
        NguoiDung nguoiDung = (NguoiDung) session.getAttribute("nguoiDung");
        if (nguoiDung != null) {
            var dsPhieu = phieuMuonService.layTheoNguoiDung(nguoiDung.getMaNguoiDung());
            model.addAttribute("dsPhieuMuon", dsPhieu);

            // Thống kê
            long dangMuon = dsPhieu.stream().filter(p ->
                    p.getTrangThai().name().equals("DANG_MUON") ||
                    p.getTrangThai().name().equals("CHO_GIA_HAN")).count();
            long choDuyet = dsPhieu.stream().filter(p ->
                    p.getTrangThai().name().equals("CHO_DUYET")).count();
            long daTra = dsPhieu.stream().filter(p ->
                    p.getTrangThai().name().equals("DA_TRA")).count();
            long quaHan = dsPhieu.stream().filter(p ->
                    p.getTrangThai().name().equals("QUA_HAN")).count();

            model.addAttribute("dangMuon", dangMuon);
            model.addAttribute("choDuyet", choDuyet);
            model.addAttribute("daTra", daTra);
            model.addAttribute("quaHan", quaHan);
        }
        return "sach/sach-cua-toi";
    }
}

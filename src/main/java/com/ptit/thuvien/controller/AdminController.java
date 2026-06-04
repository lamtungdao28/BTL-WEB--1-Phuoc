package com.ptit.thuvien.controller;

import com.ptit.thuvien.model.NguoiDung;
import com.ptit.thuvien.model.PhieuMuon;
import com.ptit.thuvien.model.TaiLieu;
import com.ptit.thuvien.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controller quản lý admin - Dashboard, Quản lý sách, Sinh viên, Lịch sử mượn
 */
@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final TaiLieuService taiLieuService;
    private final DanhMucService danhMucService;
    private final PhieuMuonService phieuMuonService;
    private final NguoiDungService nguoiDungService;

    // ==========================================
    //              DASHBOARD
    // ==========================================

    @GetMapping({"/", "/dashboard", "/quan-ly-sach"})
    public String dashboard(Model model) {
        model.addAttribute("dsTaiLieu", taiLieuService.layTatCa());
        model.addAttribute("dsDanhMuc", danhMucService.layTatCa());
        model.addAttribute("dsPhieuMuon", phieuMuonService.layTatCa());
        model.addAttribute("dsNguoiDung", nguoiDungService.layTatCa());

        // Thống kê
        var allPhieu = phieuMuonService.layTatCa();
        long tongSach = taiLieuService.layTatCa().size();
        long tongSV = nguoiDungService.layTatCa().stream()
                .filter(u -> u.getVaiTro() == NguoiDung.VaiTro.SINH_VIEN).count();
        long tongMuon = allPhieu.size();
        long quaHan = allPhieu.stream()
                .filter(p -> p.getTrangThai() == PhieuMuon.TrangThaiMuon.QUA_HAN).count();

        model.addAttribute("tongSach", tongSach);
        model.addAttribute("tongSinhVien", tongSV);
        model.addAttribute("tongMuon", tongMuon);
        model.addAttribute("quaHan", quaHan);

        return "admin/quan-ly-sach";
    }

    // ==========================================
    //            QUẢN LÝ SÁCH (CRUD)
    // ==========================================

    @PostMapping("/them-sach")
    public String themSach(@ModelAttribute TaiLieu taiLieu, RedirectAttributes redirect) {
        taiLieuService.luu(taiLieu);
        redirect.addFlashAttribute("thongBao", "Thêm sách thành công!");
        return "redirect:/admin/quan-ly-sach";
    }

    @PostMapping("/sua-sach/{id}")
    public String suaSach(@PathVariable Long id,
                           @ModelAttribute TaiLieu taiLieuMoi,
                           RedirectAttributes redirect) {
        taiLieuService.timTheoId(id).ifPresent(tl -> {
            tl.setTenTaiLieu(taiLieuMoi.getTenTaiLieu());
            tl.setTacGia(taiLieuMoi.getTacGia());
            tl.setNxb(taiLieuMoi.getNxb());
            tl.setSoLuong(taiLieuMoi.getSoLuong());
            tl.setSoLuongCon(taiLieuMoi.getSoLuongCon());
            taiLieuService.luu(tl);
        });
        redirect.addFlashAttribute("thongBao", "Cập nhật sách thành công!");
        return "redirect:/admin/quan-ly-sach";
    }

    @PostMapping("/xoa-sach/{id}")
    public String xoaSach(@PathVariable Long id, RedirectAttributes redirect) {
        taiLieuService.xoa(id);
        redirect.addFlashAttribute("thongBao", "Đã xóa sách!");
        return "redirect:/admin/quan-ly-sach";
    }

    // ==========================================
    //          QUẢN LÝ SINH VIÊN
    // ==========================================

    @PostMapping("/them-sinh-vien")
    public String themSinhVien(@RequestParam String taiKhoan,
                                @RequestParam String matKhau,
                                @RequestParam String hoTen,
                                @RequestParam(required = false) String email,
                                @RequestParam(required = false) String soDienThoai,
                                @RequestParam(defaultValue = "HOAT_DONG") String trangThai,
                                RedirectAttributes redirect) {

        NguoiDung sv = NguoiDung.builder()
                .taiKhoan(taiKhoan)
                .matKhau(matKhau)
                .hoTen(hoTen)
                .email(email)
                .soDienThoai(soDienThoai)
                .vaiTro(NguoiDung.VaiTro.SINH_VIEN)
                .trangThai(NguoiDung.TrangThaiNguoiDung.valueOf(trangThai))
                .build();

        nguoiDungService.luu(sv);
        redirect.addFlashAttribute("thongBao", "Thêm sinh viên thành công!");
        return "redirect:/admin/quan-ly-sach";
    }

    @PostMapping("/khoa-tai-khoan/{id}")
    public String khoaTaiKhoan(@PathVariable Long id, RedirectAttributes redirect) {
        nguoiDungService.timTheoId(id).ifPresent(nd -> {
            nd.setTrangThai(NguoiDung.TrangThaiNguoiDung.BI_KHOA);
            nguoiDungService.luu(nd);
        });
        redirect.addFlashAttribute("thongBao", "Đã khoá tài khoản!");
        return "redirect:/admin/quan-ly-sach";
    }

    @PostMapping("/mo-khoa/{id}")
    public String moKhoaTaiKhoan(@PathVariable Long id, RedirectAttributes redirect) {
        nguoiDungService.timTheoId(id).ifPresent(nd -> {
            nd.setTrangThai(NguoiDung.TrangThaiNguoiDung.HOAT_DONG);
            nguoiDungService.luu(nd);
        });
        redirect.addFlashAttribute("thongBao", "Đã mở khoá tài khoản!");
        return "redirect:/admin/quan-ly-sach";
    }

    // ==========================================
    //         QUẢN LÝ MƯỢN SÁCH
    // ==========================================

    @PostMapping("/duyet-muon/{id}")
    public String duyetMuon(@PathVariable Long id, RedirectAttributes redirect) {
        phieuMuonService.duyetPhieuMuon(id);
        redirect.addFlashAttribute("thongBao", "Đã duyệt phiếu mượn!");
        return "redirect:/admin/quan-ly-sach";
    }

    @PostMapping("/tu-choi-muon/{id}")
    public String tuChoiMuon(@PathVariable Long id, RedirectAttributes redirect) {
        phieuMuonService.tuChoiPhieuMuon(id);
        redirect.addFlashAttribute("thongBao", "Đã từ chối phiếu mượn!");
        return "redirect:/admin/quan-ly-sach";
    }

    @PostMapping("/xac-nhan-tra/{id}")
    public String xacNhanTra(@PathVariable Long id, RedirectAttributes redirect) {
        phieuMuonService.traSach(id);
        redirect.addFlashAttribute("thongBao", "Đã xác nhận trả sách!");
        return "redirect:/admin/quan-ly-sach";
    }
}

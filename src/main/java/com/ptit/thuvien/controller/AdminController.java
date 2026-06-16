package com.ptit.thuvien.controller;

import com.ptit.thuvien.model.NguoiDung;
import com.ptit.thuvien.model.PhieuMuon;
import com.ptit.thuvien.model.TaiLieu;
import com.ptit.thuvien.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

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

    @Value("${app.upload.dir:${user.dir}/uploads}")
    private String uploadDir;

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

    /**
     * Lưu file PDF upload và trả về đường dẫn tương đối
     */
    private String luuFilePdf(MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) {
            return null;
        }
        Path uploadPath = Paths.get(uploadDir, "pdf");
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }
        // Tên file unique: timestamp_originalname
        String originalName = file.getOriginalFilename();
        String fileName = System.currentTimeMillis() + "_" + originalName;
        Path filePath = uploadPath.resolve(fileName);
        file.transferTo(filePath.toFile());
        return "/uploads/pdf/" + fileName;
    }

    @PostMapping("/them-sach")
    public String themSach(@ModelAttribute TaiLieu taiLieu,
                           @RequestParam(value = "pdfFile", required = false) MultipartFile pdfFile,
                           RedirectAttributes redirect) {
        try {
            String pdfPath = luuFilePdf(pdfFile);
            if (pdfPath != null) {
                taiLieu.setFilePdf(pdfPath);
            }
            taiLieuService.luu(taiLieu);
            redirect.addFlashAttribute("thongBao", "Thêm sách thành công!");
        } catch (IOException e) {
            redirect.addFlashAttribute("thongBao", "Lỗi khi upload file PDF: " + e.getMessage());
        }
        return "redirect:/admin/quan-ly-sach";
    }

    @PostMapping("/sua-sach/{id}")
    public String suaSach(@PathVariable Long id,
                           @ModelAttribute TaiLieu taiLieuMoi,
                           @RequestParam(value = "pdfFile", required = false) MultipartFile pdfFile,
                           RedirectAttributes redirect) {
        taiLieuService.timTheoId(id).ifPresent(tl -> {
            tl.setTenTaiLieu(taiLieuMoi.getTenTaiLieu());
            tl.setTacGia(taiLieuMoi.getTacGia());
            tl.setNxb(taiLieuMoi.getNxb());
            tl.setSoLuong(taiLieuMoi.getSoLuong());
            tl.setSoLuongCon(taiLieuMoi.getSoLuongCon());
            tl.setNoiDungChiTiet(taiLieuMoi.getNoiDungChiTiet());
            try {
                String pdfPath = luuFilePdf(pdfFile);
                if (pdfPath != null) {
                    tl.setFilePdf(pdfPath);
                }
            } catch (IOException e) {
                throw new RuntimeException("Lỗi upload PDF: " + e.getMessage());
            }
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

    @PostMapping("/duyet-gia-han/{id}")
    public String duyetGiaHan(@PathVariable Long id, RedirectAttributes redirect) {
        phieuMuonService.duyetGiaHan(id);
        redirect.addFlashAttribute("thongBao", "Đã duyệt gia hạn! Hạn trả được gia hạn thêm 14 ngày.");
        return "redirect:/admin/quan-ly-sach";
    }

    @PostMapping("/tu-choi-gia-han/{id}")
    public String tuChoiGiaHan(@PathVariable Long id, RedirectAttributes redirect) {
        phieuMuonService.tuChoiGiaHan(id);
        redirect.addFlashAttribute("thongBao", "Đã từ chối yêu cầu gia hạn!");
        return "redirect:/admin/quan-ly-sach";
    }
}

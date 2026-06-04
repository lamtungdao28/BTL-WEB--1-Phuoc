package com.ptit.thuvien.controller;

import com.ptit.thuvien.model.NguoiDung;
import com.ptit.thuvien.model.PhieuMuon;
import com.ptit.thuvien.service.PhieuMuonService;
import com.ptit.thuvien.service.TaiLieuService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Controller xử lý mượn sách (POST đăng ký mượn)
 */
@Controller
@RequestMapping("/sach")
@RequiredArgsConstructor
public class MuonSachController {

    private final PhieuMuonService phieuMuonService;
    private final TaiLieuService taiLieuService;

    /**
     * Xử lý đăng ký mượn sách
     * POST /sach/dang-ky-muon
     */
    @PostMapping("/dang-ky-muon")
    public String dangKyMuon(@RequestParam Long maTaiLieu,
                              @RequestParam(required = false) String ngayMuon,
                              @RequestParam(required = false) String ngayHenTra,
                              @RequestParam(required = false) String ghiChu,
                              HttpSession session,
                              RedirectAttributes redirect) {

        NguoiDung nguoiDung = (NguoiDung) session.getAttribute("nguoiDung");
        if (nguoiDung == null) {
            return "redirect:/dang-nhap";
        }

        // Kiểm tra tài liệu còn sách
        var taiLieu = taiLieuService.timTheoId(maTaiLieu);
        if (taiLieu.isEmpty() || taiLieu.get().getSoLuongCon() <= 0) {
            redirect.addFlashAttribute("error", "Sách đã hết, không thể đăng ký mượn!");
            return "redirect:/sach/muon-sach";
        }

        // Parse ngày mượn và ngày hẹn trả từ form (datetime-local: yyyy-MM-ddTHH:mm)
        LocalDateTime ngayMuonDT = (ngayMuon != null && !ngayMuon.isEmpty())
                ? LocalDateTime.parse(ngayMuon)
                : LocalDateTime.now();
        LocalDateTime ngayHenTraDT = (ngayHenTra != null && !ngayHenTra.isEmpty())
                ? LocalDateTime.parse(ngayHenTra)
                : ngayMuonDT.plusDays(14);

        // Tạo phiếu mượn mới (trạng thái: CHO_DUYET)
        PhieuMuon phieuMuon = PhieuMuon.builder()
                .nguoiDung(nguoiDung)
                .taiLieu(taiLieu.get())
                .ngayDangKy(LocalDate.now())
                .ngayMuon(ngayMuonDT)
                .ngayHenTra(ngayHenTraDT)
                .trangThai(PhieuMuon.TrangThaiMuon.CHO_DUYET)
                .ghiChu(ghiChu)
                .tienPhat(0L)
                .build();

        phieuMuonService.taoPhieuMuon(phieuMuon);

        redirect.addFlashAttribute("success", "Đăng ký mượn sách thành công! Vui lòng đến quầy thư viện để nhận sách.");
        return "redirect:/sach/sach-cua-toi";
    }

    /**
     * Gia hạn phiếu mượn (+14 ngày)
     * POST /sach/gia-han/{maMuon}
     */
    @PostMapping("/gia-han/{maMuon}")
    public String giaHan(@PathVariable Long maMuon, RedirectAttributes redirect) {
        try {
            PhieuMuon pm = phieuMuonService.timTheoId(maMuon)
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy phiếu mượn"));

            // Chỉ gia hạn khi đang mượn và chưa quá hạn
            if (pm.getTrangThai() == PhieuMuon.TrangThaiMuon.DANG_MUON
                    && pm.getNgayHenTra().isAfter(LocalDateTime.now())) {
                pm.setNgayHenTra(pm.getNgayHenTra().plusDays(14));
                phieuMuonService.taoPhieuMuon(pm); // lưu lại
                redirect.addFlashAttribute("success", "Gia hạn thành công! Hạn trả mới: " + pm.getNgayHenTra());
            } else {
                redirect.addFlashAttribute("error", "Không thể gia hạn phiếu mượn này!");
            }
        } catch (Exception e) {
            redirect.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/sach/sach-cua-toi";
    }

    /**
     * Hủy đăng ký mượn (chỉ khi CHO_DUYET)
     * POST /sach/huy-dang-ky/{maMuon}
     */
    @PostMapping("/huy-dang-ky/{maMuon}")
    public String huyDangKy(@PathVariable Long maMuon, RedirectAttributes redirect) {
        try {
            PhieuMuon pm = phieuMuonService.timTheoId(maMuon)
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy phiếu mượn"));

            if (pm.getTrangThai() == PhieuMuon.TrangThaiMuon.CHO_DUYET) {
                phieuMuonService.xoa(maMuon);
                redirect.addFlashAttribute("success", "Đã hủy đăng ký mượn sách!");
            } else {
                redirect.addFlashAttribute("error", "Không thể hủy phiếu mượn này!");
            }
        } catch (Exception e) {
            redirect.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/sach/sach-cua-toi";
    }
}

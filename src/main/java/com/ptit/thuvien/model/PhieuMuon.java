package com.ptit.thuvien.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Entity Phiếu mượn sách
 */
@Entity
@Table(name = "phieu_muon")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PhieuMuon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ma_muon")
    private Long maMuon;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ma_nguoi_dung", nullable = false)
    private NguoiDung nguoiDung;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ma_tai_lieu", nullable = false)
    private TaiLieu taiLieu;

    @Column(name = "ngay_dang_ky")
    private LocalDate ngayDangKy;

    @Column(name = "ngay_muon")
    private LocalDateTime ngayMuon;

    @Column(name = "ngay_hen_tra")
    private LocalDateTime ngayHenTra;

    @Column(name = "ngay_tra_thuc_te")
    private LocalDate ngayTraThucTe;

    @Enumerated(EnumType.STRING)
    @Column(name = "trang_thai")
    @Builder.Default
    private TrangThaiMuon trangThai = TrangThaiMuon.DANG_MUON;

    @Column(name = "ghi_chu", columnDefinition = "TEXT")
    private String ghiChu;

    @Column(name = "tien_phat")
    @Builder.Default
    private Long tienPhat = 0L;

    @Column(name = "ngay_tao")
    private LocalDateTime ngayTao;

    @PrePersist
    protected void onCreate() {
        ngayTao = LocalDateTime.now();
        if (ngayDangKy == null) {
            ngayDangKy = LocalDate.now();
        }
    }

    /**
     * Enum trạng thái mượn sách
     */
    public enum TrangThaiMuon {
        CHO_DUYET,      // Chờ admin duyệt
        DANG_MUON,      // Đang mượn
        DA_TRA,         // Đã trả
        QUA_HAN,        // Quá hạn
        TU_CHOI,        // Bị từ chối
        MAT_SACH        // Mất sách
    }
}

package com.ptit.thuvien.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

/**
 * Entity Người dùng (Admin, Sinh viên, Giảng viên)
 */
@Entity
@Table(name = "nguoi_dung")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NguoiDung {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ma_nguoi_dung")
    private Long maNguoiDung;

    @Column(name = "tai_khoan", unique = true, nullable = false, length = 50)
    private String taiKhoan;

    @Column(name = "mat_khau", nullable = false)
    private String matKhau;

    @Column(name = "ho_ten", nullable = false, length = 100)
    private String hoTen;

    @Column(name = "email", length = 100)
    private String email;

    @Column(name = "so_dien_thoai", length = 15)
    private String soDienThoai;

    @Column(name = "lop", length = 100)
    private String lop;

    @Enumerated(EnumType.STRING)
    @Column(name = "vai_tro", nullable = false)
    private VaiTro vaiTro;

    @Enumerated(EnumType.STRING)
    @Column(name = "trang_thai")
    @Builder.Default
    private TrangThaiNguoiDung trangThai = TrangThaiNguoiDung.HOAT_DONG;

    @Column(name = "ngay_tao")
    private LocalDateTime ngayTao;

    @PrePersist
    protected void onCreate() {
        ngayTao = LocalDateTime.now();
    }

    /**
     * Enum vai trò người dùng
     */
    public enum VaiTro {
        ADMIN, SINH_VIEN, GIANG_VIEN
    }

    /**
     * Enum trạng thái người dùng
     */
    public enum TrangThaiNguoiDung {
        HOAT_DONG, BI_KHOA
    }
}

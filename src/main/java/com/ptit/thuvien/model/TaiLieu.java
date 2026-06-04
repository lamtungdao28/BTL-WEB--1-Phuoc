package com.ptit.thuvien.model;

import jakarta.persistence.*;
import lombok.*;

/**
 * Entity Tài liệu / Sách
 */
@Entity
@Table(name = "tai_lieu")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaiLieu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ma_tai_lieu")
    private Long maTaiLieu;

    @Column(name = "ten_tai_lieu", nullable = false)
    private String tenTaiLieu;

    @Column(name = "tac_gia", length = 200)
    private String tacGia;

    @Column(name = "nha_xuat_ban", length = 200)
    private String nxb;

    @Column(name = "nam_xuat_ban")
    private Integer namXuatBan;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ma_danh_muc")
    private DanhMuc danhMuc;

    @Column(name = "so_luong")
    private Integer soLuong;

    @Column(name = "so_luong_con")
    private Integer soLuongCon;

    @Column(name = "hinh_anh")
    private String hinhAnh;

    @Column(name = "mo_ta", columnDefinition = "TEXT")
    private String moTa;
}

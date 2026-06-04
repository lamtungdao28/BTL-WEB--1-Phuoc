package com.ptit.thuvien.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

/**
 * Entity Tin tức
 */
@Entity
@Table(name = "tin_tuc")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TinTuc {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ma_tin")
    private Long maTin;

    @Column(name = "tieu_de", nullable = false)
    private String tieuDe;

    @Column(name = "noi_dung", columnDefinition = "LONGTEXT")
    private String noiDung;

    @Column(name = "tom_tat", columnDefinition = "TEXT")
    private String tomTat;

    @Column(name = "hinh_anh")
    private String hinhAnh;

    @Column(name = "danh_muc_tin", length = 50)
    private String danhMucTin;

    @Column(name = "luot_xem")
    @Builder.Default
    private Integer luotXem = 0;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tac_gia_id")
    private NguoiDung tacGia;

    @Column(name = "ngay_dang")
    private LocalDateTime ngayDang;

    @PrePersist
    protected void onCreate() {
        ngayDang = LocalDateTime.now();
    }
}

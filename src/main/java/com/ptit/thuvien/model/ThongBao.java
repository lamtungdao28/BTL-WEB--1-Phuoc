package com.ptit.thuvien.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

/**
 * Entity Thông báo thư viện
 */
@Entity
@Table(name = "thong_bao")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ThongBao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ma_thong_bao")
    private Long maThongBao;

    @Column(name = "tieu_de", nullable = false)
    private String tieuDe;

    @Column(name = "noi_dung", columnDefinition = "LONGTEXT")
    private String noiDung;

    @Column(name = "loai_thong_bao", length = 50)
    private String loaiThongBao; // important, muon, event, digital

    @Column(name = "luot_xem")
    @Builder.Default
    private Integer luotXem = 0;

    @Column(name = "ngay_dang")
    private LocalDateTime ngayDang;

    @PrePersist
    protected void onCreate() {
        ngayDang = LocalDateTime.now();
    }
}

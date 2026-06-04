package com.ptit.thuvien.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO cho form đăng ký mượn sách
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MuonSachDTO {

    private Long maTaiLieu;
    private Long maNguoiDung;
    private String ghiChu;
    private int soNgayMuon; // Mặc định 14 ngày
}

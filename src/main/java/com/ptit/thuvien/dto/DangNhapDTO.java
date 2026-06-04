package com.ptit.thuvien.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO cho form đăng nhập
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DangNhapDTO {

    private String taiKhoan;
    private String matKhau;
    private boolean ghiNho;
}

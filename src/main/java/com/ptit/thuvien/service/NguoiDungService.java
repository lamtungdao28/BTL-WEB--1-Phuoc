package com.ptit.thuvien.service;

import com.ptit.thuvien.model.NguoiDung;
import java.util.List;
import java.util.Optional;

/**
 * Service interface cho Người dùng
 */
public interface NguoiDungService {

    List<NguoiDung> layTatCa();

    Optional<NguoiDung> timTheoId(Long id);

    Optional<NguoiDung> timTheoTaiKhoan(String taiKhoan);

    NguoiDung luu(NguoiDung nguoiDung);

    void xoa(Long id);

    boolean dangNhap(String taiKhoan, String matKhau);

    boolean tonTaiTaiKhoan(String taiKhoan);

    Optional<NguoiDung> timTheoEmail(String email);

    boolean tonTaiEmail(String email);
}

package com.ptit.thuvien.service;

import com.ptit.thuvien.model.TaiLieu;
import java.util.List;
import java.util.Optional;

/**
 * Service interface cho Tài liệu / Sách
 */
public interface TaiLieuService {

    List<TaiLieu> layTatCa();

    Optional<TaiLieu> timTheoId(Long id);

    List<TaiLieu> timTheoDanhMuc(Long maDanhMuc);

    List<TaiLieu> timKiem(String keyword);

    List<TaiLieu> layTaiLieuConSach();

    TaiLieu luu(TaiLieu taiLieu);

    void xoa(Long id);

    void capNhatSoLuongCon(Long maTaiLieu, int soLuongThayDoi);
}

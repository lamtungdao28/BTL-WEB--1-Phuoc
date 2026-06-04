package com.ptit.thuvien.service;

import com.ptit.thuvien.model.PhieuMuon;
import java.util.List;
import java.util.Optional;

/**
 * Service interface cho Phiếu mượn sách
 */
public interface PhieuMuonService {

    List<PhieuMuon> layTatCa();

    Optional<PhieuMuon> timTheoId(Long id);

    List<PhieuMuon> layTheoNguoiDung(Long maNguoiDung);

    List<PhieuMuon> layTheoTrangThai(PhieuMuon.TrangThaiMuon trangThai);

    List<PhieuMuon> layTheoNguoiDungVaTrangThai(Long maNguoiDung, PhieuMuon.TrangThaiMuon trangThai);

    PhieuMuon taoPhieuMuon(PhieuMuon phieuMuon);

    PhieuMuon duyetPhieuMuon(Long maMuon);

    PhieuMuon tuChoiPhieuMuon(Long maMuon);

    PhieuMuon traSach(Long maMuon);

    void xoa(Long id);

    void capNhatTrangThaiQuaHan();
}

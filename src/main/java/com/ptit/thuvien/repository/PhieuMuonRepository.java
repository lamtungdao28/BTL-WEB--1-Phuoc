package com.ptit.thuvien.repository;

import com.ptit.thuvien.model.PhieuMuon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PhieuMuonRepository extends JpaRepository<PhieuMuon, Long> {

    List<PhieuMuon> findByNguoiDung_MaNguoiDung(Long maNguoiDung);

    List<PhieuMuon> findByTrangThai(PhieuMuon.TrangThaiMuon trangThai);

    List<PhieuMuon> findByNguoiDung_MaNguoiDungAndTrangThai(Long maNguoiDung, PhieuMuon.TrangThaiMuon trangThai);

    void deleteByNguoiDung_MaNguoiDung(Long maNguoiDung);
}

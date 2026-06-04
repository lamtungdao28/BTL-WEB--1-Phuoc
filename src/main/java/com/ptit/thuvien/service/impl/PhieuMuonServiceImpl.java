package com.ptit.thuvien.service.impl;

import com.ptit.thuvien.model.PhieuMuon;
import com.ptit.thuvien.repository.PhieuMuonRepository;
import com.ptit.thuvien.service.PhieuMuonService;
import com.ptit.thuvien.service.TaiLieuService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class PhieuMuonServiceImpl implements PhieuMuonService {

    private final PhieuMuonRepository phieuMuonRepository;
    private final TaiLieuService taiLieuService;

    @Override
    @Transactional(readOnly = true)
    public List<PhieuMuon> layTatCa() {
        return phieuMuonRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PhieuMuon> timTheoId(Long id) {
        return phieuMuonRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PhieuMuon> layTheoNguoiDung(Long maNguoiDung) {
        return phieuMuonRepository.findByNguoiDung_MaNguoiDung(maNguoiDung);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PhieuMuon> layTheoTrangThai(PhieuMuon.TrangThaiMuon trangThai) {
        return phieuMuonRepository.findByTrangThai(trangThai);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PhieuMuon> layTheoNguoiDungVaTrangThai(Long maNguoiDung, PhieuMuon.TrangThaiMuon trangThai) {
        return phieuMuonRepository.findByNguoiDung_MaNguoiDungAndTrangThai(maNguoiDung, trangThai);
    }

    @Override
    public PhieuMuon taoPhieuMuon(PhieuMuon phieuMuon) {
        phieuMuon.setTrangThai(PhieuMuon.TrangThaiMuon.CHO_DUYET);
        phieuMuon.setNgayMuon(LocalDate.now());
        phieuMuon.setNgayHenTra(LocalDate.now().plusDays(14)); // Mặc định 14 ngày
        return phieuMuonRepository.save(phieuMuon);
    }

    @Override
    public PhieuMuon duyetPhieuMuon(Long maMuon) {
        PhieuMuon phieuMuon = phieuMuonRepository.findById(maMuon)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy phiếu mượn: " + maMuon));

        phieuMuon.setTrangThai(PhieuMuon.TrangThaiMuon.DANG_MUON);
        // Giảm số lượng còn
        taiLieuService.capNhatSoLuongCon(phieuMuon.getTaiLieu().getMaTaiLieu(), -1);

        return phieuMuonRepository.save(phieuMuon);
    }

    @Override
    public PhieuMuon tuChoiPhieuMuon(Long maMuon) {
        PhieuMuon phieuMuon = phieuMuonRepository.findById(maMuon)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy phiếu mượn: " + maMuon));

        phieuMuon.setTrangThai(PhieuMuon.TrangThaiMuon.TU_CHOI);
        return phieuMuonRepository.save(phieuMuon);
    }

    @Override
    public PhieuMuon traSach(Long maMuon) {
        PhieuMuon phieuMuon = phieuMuonRepository.findById(maMuon)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy phiếu mượn: " + maMuon));

        phieuMuon.setTrangThai(PhieuMuon.TrangThaiMuon.DA_TRA);
        phieuMuon.setNgayTraThucTe(LocalDate.now());
        // Tăng lại số lượng còn
        taiLieuService.capNhatSoLuongCon(phieuMuon.getTaiLieu().getMaTaiLieu(), 1);

        return phieuMuonRepository.save(phieuMuon);
    }

    @Override
    public void xoa(Long id) {
        phieuMuonRepository.deleteById(id);
    }
}

package com.ptit.thuvien.service.impl;

import com.ptit.thuvien.model.PhieuMuon;
import com.ptit.thuvien.repository.PhieuMuonRepository;
import com.ptit.thuvien.service.PhieuMuonService;
import com.ptit.thuvien.service.TaiLieuService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class PhieuMuonServiceImpl implements PhieuMuonService {

    private final PhieuMuonRepository phieuMuonRepository;
    private final TaiLieuService taiLieuService;

    @Override
    public List<PhieuMuon> layTatCa() {
        capNhatTrangThaiQuaHan();
        return phieuMuonRepository.findAll();
    }

    @Override
    public Optional<PhieuMuon> timTheoId(Long id) {
        capNhatTrangThaiQuaHan();
        return phieuMuonRepository.findById(id);
    }

    @Override
    public List<PhieuMuon> layTheoNguoiDung(Long maNguoiDung) {
        capNhatTrangThaiQuaHan();
        return phieuMuonRepository.findByNguoiDung_MaNguoiDung(maNguoiDung);
    }

    @Override
    public List<PhieuMuon> layTheoTrangThai(PhieuMuon.TrangThaiMuon trangThai) {
        capNhatTrangThaiQuaHan();
        return phieuMuonRepository.findByTrangThai(trangThai);
    }

    @Override
    public List<PhieuMuon> layTheoNguoiDungVaTrangThai(Long maNguoiDung, PhieuMuon.TrangThaiMuon trangThai) {
        capNhatTrangThaiQuaHan();
        return phieuMuonRepository.findByNguoiDung_MaNguoiDungAndTrangThai(maNguoiDung, trangThai);
    }

    @Override
    public PhieuMuon taoPhieuMuon(PhieuMuon phieuMuon) {
        if (phieuMuon.getMaMuon() == null) {
            // Chỉ set trạng thái CHO_DUYET cho phiếu mới
            phieuMuon.setTrangThai(PhieuMuon.TrangThaiMuon.CHO_DUYET);
        }
        if (phieuMuon.getNgayMuon() == null) {
            phieuMuon.setNgayMuon(LocalDateTime.now());
        }
        if (phieuMuon.getNgayHenTra() == null) {
            phieuMuon.setNgayHenTra(LocalDateTime.now().plusDays(14));
        }
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
    public PhieuMuon duyetGiaHan(Long maMuon) {
        PhieuMuon phieuMuon = phieuMuonRepository.findById(maMuon)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy phiếu mượn: " + maMuon));

        // Gia hạn thêm 14 ngày và chuyển về trạng thái DANG_MUON
        phieuMuon.setNgayHenTra(phieuMuon.getNgayHenTra().plusDays(14));
        phieuMuon.setTrangThai(PhieuMuon.TrangThaiMuon.DANG_MUON);
        return phieuMuonRepository.save(phieuMuon);
    }

    @Override
    public PhieuMuon tuChoiGiaHan(Long maMuon) {
        PhieuMuon phieuMuon = phieuMuonRepository.findById(maMuon)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy phiếu mượn: " + maMuon));

        // Từ chối gia hạn → chuyển về DANG_MUON (giữ nguyên hạn trả cũ)
        phieuMuon.setTrangThai(PhieuMuon.TrangThaiMuon.DANG_MUON);
        return phieuMuonRepository.save(phieuMuon);
    }

    @Override
    public void xoa(Long id) {
        phieuMuonRepository.deleteById(id);
    }

    @Override
    public void capNhatTrangThaiQuaHan() {
        LocalDateTime now = LocalDateTime.now();
        
        // Quét các phiếu DANG_MUON đã quá hạn để chuyển sang QUA_HAN
        List<PhieuMuon> dsDangMuon = phieuMuonRepository.findByTrangThai(PhieuMuon.TrangThaiMuon.DANG_MUON);
        for (PhieuMuon pm : dsDangMuon) {
            if (pm.getNgayHenTra() != null && now.isAfter(pm.getNgayHenTra())) {
                pm.setTrangThai(PhieuMuon.TrangThaiMuon.QUA_HAN);
                phieuMuonRepository.save(pm);
                System.out.println("[Scheduler/Check] Phiếu mượn #" + pm.getMaMuon() + " của " + pm.getNguoiDung().getHoTen() + " đã quá hạn.");
            }
        }
    }
}

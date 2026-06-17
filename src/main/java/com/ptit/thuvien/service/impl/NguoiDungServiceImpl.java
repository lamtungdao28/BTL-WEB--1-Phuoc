package com.ptit.thuvien.service.impl;

import com.ptit.thuvien.model.NguoiDung;
import com.ptit.thuvien.model.PhieuMuon;
import com.ptit.thuvien.repository.NguoiDungRepository;
import com.ptit.thuvien.repository.PhieuMuonRepository;
import com.ptit.thuvien.service.NguoiDungService;
import com.ptit.thuvien.service.TaiLieuService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class NguoiDungServiceImpl implements NguoiDungService {

    private final NguoiDungRepository nguoiDungRepository;
    private final PhieuMuonRepository phieuMuonRepository;
    private final TaiLieuService taiLieuService;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional(readOnly = true)
    public List<NguoiDung> layTatCa() {
        return nguoiDungRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<NguoiDung> timTheoId(Long id) {
        return nguoiDungRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<NguoiDung> timTheoTaiKhoan(String taiKhoan) {
        return nguoiDungRepository.findByTaiKhoan(taiKhoan);
    }

    @Override
    public NguoiDung luu(NguoiDung nguoiDung) {
        // Mã hóa mật khẩu trước khi lưu
        if (nguoiDung.getMaNguoiDung() == null) {
            nguoiDung.setMatKhau(passwordEncoder.encode(nguoiDung.getMatKhau()));
        }
        return nguoiDungRepository.save(nguoiDung);
    }

    @Override
    public void xoa(Long id) {
        // Trước khi xóa người dùng, cần hoàn trả số lượng sách cho các phiếu đang mượn
        List<PhieuMuon> dsPhieuMuon = phieuMuonRepository.findByNguoiDung_MaNguoiDung(id);
        for (PhieuMuon pm : dsPhieuMuon) {
            // Nếu phiếu đang mượn hoặc quá hạn, hoàn trả số lượng sách
            if (pm.getTrangThai() == PhieuMuon.TrangThaiMuon.DANG_MUON
                    || pm.getTrangThai() == PhieuMuon.TrangThaiMuon.QUA_HAN) {
                taiLieuService.capNhatSoLuongCon(pm.getTaiLieu().getMaTaiLieu(), 1);
            }
        }
        // Xóa tất cả phiếu mượn của người dùng này
        phieuMuonRepository.deleteByNguoiDung_MaNguoiDung(id);
        // Rồi mới xóa người dùng
        nguoiDungRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean dangNhap(String email, String matKhau) {
        Optional<NguoiDung> nguoiDung = nguoiDungRepository.findByEmail(email);
        return nguoiDung.isPresent() && passwordEncoder.matches(matKhau, nguoiDung.get().getMatKhau());
    }

    @Override
    @Transactional(readOnly = true)
    public boolean tonTaiTaiKhoan(String taiKhoan) {
        return nguoiDungRepository.existsByTaiKhoan(taiKhoan);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<NguoiDung> timTheoEmail(String email) {
        return nguoiDungRepository.findByEmail(email);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean tonTaiEmail(String email) {
        return nguoiDungRepository.findByEmail(email).isPresent();
    }
}

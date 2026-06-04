package com.ptit.thuvien.service.impl;

import com.ptit.thuvien.model.NguoiDung;
import com.ptit.thuvien.repository.NguoiDungRepository;
import com.ptit.thuvien.service.NguoiDungService;
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
        nguoiDungRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean dangNhap(String taiKhoan, String matKhau) {
        Optional<NguoiDung> nguoiDung = nguoiDungRepository.findByTaiKhoan(taiKhoan);
        return nguoiDung.isPresent() && passwordEncoder.matches(matKhau, nguoiDung.get().getMatKhau());
    }

    @Override
    @Transactional(readOnly = true)
    public boolean tonTaiTaiKhoan(String taiKhoan) {
        return nguoiDungRepository.existsByTaiKhoan(taiKhoan);
    }
}

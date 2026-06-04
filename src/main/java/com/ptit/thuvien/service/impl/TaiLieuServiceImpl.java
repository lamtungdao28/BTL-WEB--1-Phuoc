package com.ptit.thuvien.service.impl;

import com.ptit.thuvien.model.TaiLieu;
import com.ptit.thuvien.repository.TaiLieuRepository;
import com.ptit.thuvien.service.TaiLieuService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class TaiLieuServiceImpl implements TaiLieuService {

    private final TaiLieuRepository taiLieuRepository;

    @Override
    @Transactional(readOnly = true)
    public List<TaiLieu> layTatCa() {
        return taiLieuRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<TaiLieu> timTheoId(Long id) {
        return taiLieuRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TaiLieu> timTheoDanhMuc(Long maDanhMuc) {
        return taiLieuRepository.findByDanhMuc_MaDanhMuc(maDanhMuc);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TaiLieu> timKiem(String keyword) {
        return taiLieuRepository.timKiem(keyword);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TaiLieu> layTaiLieuConSach() {
        return taiLieuRepository.findBySoLuongConGreaterThan(0);
    }

    @Override
    public TaiLieu luu(TaiLieu taiLieu) {
        return taiLieuRepository.save(taiLieu);
    }

    @Override
    public void xoa(Long id) {
        taiLieuRepository.deleteById(id);
    }

    @Override
    public void capNhatSoLuongCon(Long maTaiLieu, int soLuongThayDoi) {
        TaiLieu taiLieu = taiLieuRepository.findById(maTaiLieu)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy tài liệu: " + maTaiLieu));
        taiLieu.setSoLuongCon(taiLieu.getSoLuongCon() + soLuongThayDoi);
        taiLieuRepository.save(taiLieu);
    }
}

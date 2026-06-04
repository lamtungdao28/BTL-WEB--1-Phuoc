package com.ptit.thuvien.service.impl;

import com.ptit.thuvien.model.SuKien;
import com.ptit.thuvien.repository.SuKienRepository;
import com.ptit.thuvien.service.SuKienService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class SuKienServiceImpl implements SuKienService {

    private final SuKienRepository suKienRepository;

    @Override
    @Transactional(readOnly = true)
    public List<SuKien> layTatCa() {
        return suKienRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<SuKien> timTheoId(Long id) {
        return suKienRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SuKien> laySapDienRa() {
        return suKienRepository.findByTrangThaiOrderByNgayBatDauAsc(SuKien.TrangThaiSuKien.SAP_DIEN_RA);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SuKien> layDaDienRa() {
        return suKienRepository.findByTrangThaiOrderByNgayBatDauAsc(SuKien.TrangThaiSuKien.DA_KET_THUC);
    }

    @Override
    public SuKien luu(SuKien suKien) {
        return suKienRepository.save(suKien);
    }

    @Override
    public void xoa(Long id) {
        suKienRepository.deleteById(id);
    }
}

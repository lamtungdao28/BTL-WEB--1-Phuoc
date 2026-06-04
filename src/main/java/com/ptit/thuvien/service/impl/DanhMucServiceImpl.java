package com.ptit.thuvien.service.impl;

import com.ptit.thuvien.model.DanhMuc;
import com.ptit.thuvien.repository.DanhMucRepository;
import com.ptit.thuvien.service.DanhMucService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class DanhMucServiceImpl implements DanhMucService {

    private final DanhMucRepository danhMucRepository;

    @Override
    @Transactional(readOnly = true)
    public List<DanhMuc> layTatCa() {
        return danhMucRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<DanhMuc> timTheoId(Long id) {
        return danhMucRepository.findById(id);
    }

    @Override
    public DanhMuc luu(DanhMuc danhMuc) {
        return danhMucRepository.save(danhMuc);
    }

    @Override
    public void xoa(Long id) {
        danhMucRepository.deleteById(id);
    }
}

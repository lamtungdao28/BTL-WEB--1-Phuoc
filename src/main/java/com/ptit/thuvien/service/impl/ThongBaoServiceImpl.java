package com.ptit.thuvien.service.impl;

import com.ptit.thuvien.model.ThongBao;
import com.ptit.thuvien.repository.ThongBaoRepository;
import com.ptit.thuvien.service.ThongBaoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class ThongBaoServiceImpl implements ThongBaoService {

    private final ThongBaoRepository thongBaoRepository;

    @Override
    @Transactional(readOnly = true)
    public List<ThongBao> layTatCa() {
        return thongBaoRepository.findAllByOrderByNgayDangDesc();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ThongBao> timTheoId(Long id) {
        return thongBaoRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ThongBao> layTheoLoai(String loai) {
        return thongBaoRepository.findByLoaiThongBaoOrderByNgayDangDesc(loai);
    }

    @Override
    public void tangLuotXem(Long id) {
        thongBaoRepository.findById(id).ifPresent(tb -> {
            tb.setLuotXem(tb.getLuotXem() + 1);
            thongBaoRepository.save(tb);
        });
    }
}

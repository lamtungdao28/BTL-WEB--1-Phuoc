package com.ptit.thuvien.service.impl;

import com.ptit.thuvien.model.TinTuc;
import com.ptit.thuvien.repository.TinTucRepository;
import com.ptit.thuvien.service.TinTucService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class TinTucServiceImpl implements TinTucService {

    private final TinTucRepository tinTucRepository;

    @Override
    @Transactional(readOnly = true)
    public Page<TinTuc> layTatCa(Pageable pageable) {
        return tinTucRepository.findAllByOrderByNgayDangDesc(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TinTuc> layTheoDanhMuc(String danhMuc, Pageable pageable) {
        return tinTucRepository.findByDanhMucTinOrderByNgayDangDesc(danhMuc, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<TinTuc> timTheoId(Long id) {
        return tinTucRepository.findById(id);
    }

    @Override
    public TinTuc luu(TinTuc tinTuc) {
        return tinTucRepository.save(tinTuc);
    }

    @Override
    public void xoa(Long id) {
        tinTucRepository.deleteById(id);
    }

    @Override
    public void tangLuotXem(Long maTin) {
        tinTucRepository.findById(maTin).ifPresent(tin -> {
            tin.setLuotXem(tin.getLuotXem() + 1);
            tinTucRepository.save(tin);
        });
    }
}

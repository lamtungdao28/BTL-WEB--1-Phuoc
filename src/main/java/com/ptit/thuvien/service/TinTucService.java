package com.ptit.thuvien.service;

import com.ptit.thuvien.model.TinTuc;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.Optional;

/**
 * Service interface cho Tin tức
 */
public interface TinTucService {

    Page<TinTuc> layTatCa(Pageable pageable);

    Page<TinTuc> layTheoDanhMuc(String danhMuc, Pageable pageable);

    Optional<TinTuc> timTheoId(Long id);

    TinTuc luu(TinTuc tinTuc);

    void xoa(Long id);

    void tangLuotXem(Long maTin);
}

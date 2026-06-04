package com.ptit.thuvien.service;

import com.ptit.thuvien.model.DanhMuc;
import java.util.List;
import java.util.Optional;

/**
 * Service interface cho Danh mục
 */
public interface DanhMucService {

    List<DanhMuc> layTatCa();

    Optional<DanhMuc> timTheoId(Long id);

    DanhMuc luu(DanhMuc danhMuc);

    void xoa(Long id);
}

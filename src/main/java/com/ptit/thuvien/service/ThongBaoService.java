package com.ptit.thuvien.service;

import com.ptit.thuvien.model.ThongBao;
import java.util.List;
import java.util.Optional;

/**
 * Service interface cho Thông báo
 */
public interface ThongBaoService {

    List<ThongBao> layTatCa();

    Optional<ThongBao> timTheoId(Long id);

    List<ThongBao> layTheoLoai(String loai);

    void tangLuotXem(Long id);
}

package com.ptit.thuvien.service;

import com.ptit.thuvien.model.SuKien;
import java.util.List;
import java.util.Optional;

/**
 * Service interface cho Sự kiện
 */
public interface SuKienService {

    List<SuKien> layTatCa();

    Optional<SuKien> timTheoId(Long id);

    List<SuKien> laySapDienRa();

    List<SuKien> layDaDienRa();

    SuKien luu(SuKien suKien);

    void xoa(Long id);
}

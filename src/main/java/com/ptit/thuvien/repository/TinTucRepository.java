package com.ptit.thuvien.repository;

import com.ptit.thuvien.model.TinTuc;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TinTucRepository extends JpaRepository<TinTuc, Long> {

    Page<TinTuc> findAllByOrderByNgayDangDesc(Pageable pageable);

    Page<TinTuc> findByDanhMucTinOrderByNgayDangDesc(String danhMucTin, Pageable pageable);
}

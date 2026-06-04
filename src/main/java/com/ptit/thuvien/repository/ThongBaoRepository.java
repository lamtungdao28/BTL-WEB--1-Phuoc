package com.ptit.thuvien.repository;

import com.ptit.thuvien.model.ThongBao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ThongBaoRepository extends JpaRepository<ThongBao, Long> {

    List<ThongBao> findAllByOrderByNgayDangDesc();

    List<ThongBao> findByLoaiThongBaoOrderByNgayDangDesc(String loai);
}

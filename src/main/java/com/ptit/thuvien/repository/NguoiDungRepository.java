package com.ptit.thuvien.repository;

import com.ptit.thuvien.model.NguoiDung;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

/**
 * Repository truy vấn bảng nguoi_dung
 */
@Repository
public interface NguoiDungRepository extends JpaRepository<NguoiDung, Long> {

    Optional<NguoiDung> findByTaiKhoan(String taiKhoan);

    Optional<NguoiDung> findByEmail(String email);

    boolean existsByTaiKhoan(String taiKhoan);
}

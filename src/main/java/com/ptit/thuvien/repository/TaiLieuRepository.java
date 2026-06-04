package com.ptit.thuvien.repository;

import com.ptit.thuvien.model.TaiLieu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * Repository truy vấn bảng tai_lieu
 */
@Repository
public interface TaiLieuRepository extends JpaRepository<TaiLieu, Long> {

    List<TaiLieu> findByDanhMuc_MaDanhMuc(Long maDanhMuc);

    @Query("SELECT t FROM TaiLieu t WHERE LOWER(t.tenTaiLieu) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
           "OR LOWER(t.tacGia) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<TaiLieu> timKiem(@Param("keyword") String keyword);

    List<TaiLieu> findBySoLuongConGreaterThan(int soLuong);
}

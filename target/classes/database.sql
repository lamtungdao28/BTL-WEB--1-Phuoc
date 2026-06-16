-- ===================================================================
-- SCRIPT TẠO DATABASE THƯ VIỆN PTIT
-- Dựa theo dữ liệu từ các file HTML frontend
-- Stack: MySQL 8.x + Spring Boot + JPA
-- ===================================================================

-- Tạo database
CREATE DATABASE IF NOT EXISTS thuvien_ptit
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE thuvien_ptit;

-- ===================================================================
-- BẢNG 1: NGUOI_DUNG (Đăng nhập, Đăng ký)
-- Vai trò: ADMIN, SINH_VIEN, GIANG_VIEN
-- Trạng thái: HOAT_DONG, BI_KHOA
-- ===================================================================
CREATE TABLE IF NOT EXISTS nguoi_dung (
    ma_nguoi_dung   BIGINT AUTO_INCREMENT PRIMARY KEY,
    tai_khoan       VARCHAR(50)  NOT NULL UNIQUE,
    mat_khau        VARCHAR(255) NOT NULL,
    ho_ten          VARCHAR(100) NOT NULL,
    email           VARCHAR(100),
    so_dien_thoai   VARCHAR(15),
    lop             VARCHAR(20),
    vai_tro         ENUM('ADMIN', 'SINH_VIEN', 'GIANG_VIEN') NOT NULL DEFAULT 'SINH_VIEN',
    trang_thai      ENUM('HOAT_DONG', 'BI_KHOA') NOT NULL DEFAULT 'HOAT_DONG',
    ngay_tao        DATETIME DEFAULT CURRENT_TIMESTAMP,

    INDEX idx_tai_khoan (tai_khoan),
    INDEX idx_vai_tro (vai_tro),
    INDEX idx_trang_thai (trang_thai)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ===================================================================
-- BẢNG 2: DANH_MUC (Danh mục tài liệu)
-- ===================================================================
CREATE TABLE IF NOT EXISTS danh_muc (
    ma_danh_muc     BIGINT AUTO_INCREMENT PRIMARY KEY,
    ten_danh_muc    VARCHAR(100) NOT NULL,
    mo_ta           TEXT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ===================================================================
-- BẢNG 3: TAI_LIEU (Sách / Tài liệu)
-- ===================================================================
CREATE TABLE IF NOT EXISTS tai_lieu (
    ma_tai_lieu     BIGINT AUTO_INCREMENT PRIMARY KEY,
    ten_tai_lieu    VARCHAR(255) NOT NULL,
    tac_gia         VARCHAR(200),
    nha_xuat_ban    VARCHAR(200),
    nam_xuat_ban    INT,
    ma_danh_muc     BIGINT,
    so_luong        INT DEFAULT 0,
    so_luong_con    INT DEFAULT 0,
    hinh_anh        VARCHAR(500),
    mo_ta           TEXT,

    FOREIGN KEY (ma_danh_muc) REFERENCES danh_muc(ma_danh_muc)
        ON DELETE SET NULL ON UPDATE CASCADE,

    INDEX idx_danh_muc (ma_danh_muc),
    INDEX idx_ten (ten_tai_lieu),
    FULLTEXT INDEX ft_search (ten_tai_lieu, tac_gia)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ===================================================================
-- BẢNG 4: PHIEU_MUON (Mượn sách / Thuê sách)
-- Trạng thái: CHO_DUYET, DANG_MUON, DA_TRA, QUA_HAN, TU_CHOI, MAT_SACH, CHO_GIA_HAN
-- ===================================================================
CREATE TABLE IF NOT EXISTS phieu_muon (
    ma_muon         BIGINT AUTO_INCREMENT PRIMARY KEY,
    ma_nguoi_dung   BIGINT NOT NULL,
    ma_tai_lieu     BIGINT NOT NULL,
    ngay_dang_ky    DATE,
    ngay_muon       DATE,
    ngay_hen_tra    DATE,
    ngay_tra_thuc_te DATE,
    trang_thai      ENUM('CHO_DUYET','DANG_MUON','DA_TRA','QUA_HAN','TU_CHOI','MAT_SACH','CHO_GIA_HAN')
                    NOT NULL DEFAULT 'CHO_DUYET',
    ghi_chu         TEXT,
    tien_phat       DECIMAL(12,0) DEFAULT 0,
    ngay_tao        DATETIME DEFAULT CURRENT_TIMESTAMP,

    FOREIGN KEY (ma_nguoi_dung) REFERENCES nguoi_dung(ma_nguoi_dung)
        ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (ma_tai_lieu) REFERENCES tai_lieu(ma_tai_lieu)
        ON DELETE CASCADE ON UPDATE CASCADE,

    INDEX idx_nguoi_dung (ma_nguoi_dung),
    INDEX idx_tai_lieu (ma_tai_lieu),
    INDEX idx_trang_thai (trang_thai),
    INDEX idx_ngay_muon (ngay_muon)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ===================================================================
-- BẢNG 5: TIN_TUC (Tin tức thư viện)
-- ===================================================================
CREATE TABLE IF NOT EXISTS tin_tuc (
    ma_tin          BIGINT AUTO_INCREMENT PRIMARY KEY,
    tieu_de         VARCHAR(500) NOT NULL,
    noi_dung        LONGTEXT,
    tom_tat         TEXT,
    hinh_anh        VARCHAR(500),
    danh_muc_tin    VARCHAR(50),
    luot_xem        INT DEFAULT 0,
    tac_gia_id      BIGINT,
    ngay_dang       DATETIME DEFAULT CURRENT_TIMESTAMP,

    FOREIGN KEY (tac_gia_id) REFERENCES nguoi_dung(ma_nguoi_dung)
        ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ===================================================================
-- BẢNG 6: SU_KIEN (Sự kiện thư viện)
-- ===================================================================
CREATE TABLE IF NOT EXISTS su_kien (
    ma_su_kien      BIGINT AUTO_INCREMENT PRIMARY KEY,
    tieu_de         VARCHAR(500) NOT NULL,
    mo_ta           LONGTEXT,
    hinh_anh        VARCHAR(500),
    ngay_bat_dau    DATE,
    ngay_ket_thuc   DATE,
    gio_bat_dau     TIME,
    gio_ket_thuc    TIME,
    dia_diem        VARCHAR(255),
    trang_thai      ENUM('SAP_DIEN_RA','DANG_DIEN_RA','DA_KET_THUC') DEFAULT 'SAP_DIEN_RA',
    ngay_tao        DATETIME DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ===================================================================
-- BẢNG 7: THONG_BAO (Thông báo thư viện)
-- ===================================================================
CREATE TABLE IF NOT EXISTS thong_bao (
    ma_thong_bao    BIGINT AUTO_INCREMENT PRIMARY KEY,
    tieu_de         VARCHAR(500) NOT NULL,
    noi_dung        LONGTEXT,
    loai_thong_bao  VARCHAR(50),
    luot_xem        INT DEFAULT 0,
    ngay_dang       DATETIME DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


-- ===================================================================
-- ===================================================================
--                    DỮ LIỆU MẪU (SAMPLE DATA)
-- ===================================================================
-- ===================================================================

-- ===================================================================
-- NGƯỜI DÙNG
-- Mật khẩu BCrypt cho "123456": $2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy
-- ===================================================================
INSERT INTO nguoi_dung (tai_khoan, mat_khau, ho_ten, email, so_dien_thoai, lop, vai_tro, trang_thai) VALUES
-- Admin
('admin', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy',
 'Quản trị viên', 'admin@ptit.edu.vn', '0900000000', NULL, 'ADMIN', 'HOAT_DONG'),

-- Sinh viên (dữ liệu từ dang-nhap.html + quan-ly-sach.html)
('namdt.b21.tc603_1', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy',
 'Dương Thị Nam', 'namdt.b21.tc603_1@stu.ptit.edu.vn', '0912600458', 'D21CQTC01', 'SINH_VIEN', 'HOAT_DONG'),

('phongvq.b21.tc873_2', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy',
 'Vũ Quang Phong', 'phongvq.b21.tc873_2@stu.ptit.edu.vn', '0937834065', 'D21CQTC02', 'SINH_VIEN', 'HOAT_DONG'),

('phucdt.b20.tc317_3', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy',
 'Đặng Thị Phúc', 'phucdt.b20.tc317_3@stu.ptit.edu.vn', '0936984616', 'D20CQTC01', 'SINH_VIEN', 'HOAT_DONG'),

('trangbd.b20.tc293_4', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy',
 'Bùi Đức Trang', 'trangbd.b20.tc293_4@stu.ptit.edu.vn', '0925471903', 'D20CQTC02', 'SINH_VIEN', 'HOAT_DONG'),

('lanpn.b22.tc248_5', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy',
 'Phạm Ngọc Lan', 'lanpn.b22.tc248_5@stu.ptit.edu.vn', '0993647580', 'D22CQCN01', 'SINH_VIEN', 'HOAT_DONG'),

('duchm.b24.tc422_7', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy',
 'Hồ Minh Đức', 'duchm.b24.tc422_7@stu.ptit.edu.vn', '0900476409', 'D24CQCN01', 'SINH_VIEN', 'HOAT_DONG'),

('cuongnt.b22.tc166_8', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy',
 'Nguyễn Tuấn Cường', 'cuongnt.b22.tc166_8@stu.ptit.edu.vn', '0912485620', 'D22CQCN02', 'SINH_VIEN', 'HOAT_DONG'),

('giangph.b24.tc526_10', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy',
 'Phạm Hữu Giang', 'giangph.b24.tc526_10@stu.ptit.edu.vn', '0978632014', 'D24CQCN02', 'SINH_VIEN', 'HOAT_DONG'),

('anhtt.b18.tc859_45', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy',
 'Trần Tuấn Anh', 'anhtt.b18.tc859_45@stu.ptit.edu.vn', '0975802277', 'D18CQCN01', 'SINH_VIEN', 'HOAT_DONG'),

('linhlm.b23.tc725_50', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy',
 'Lê Minh Linh', 'linhlm.b23.tc725_50@stu.ptit.edu.vn', '0942727389', 'D23CQCN01', 'SINH_VIEN', 'HOAT_DONG'),

-- Sinh viên bị khoá (từ admin page)
('khanhvm.b21.tc874_56', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy',
 'Vũ Minh Khánh', 'khanhvm.b21.tc874_56@stu.ptit.edu.vn', '0934100309', 'D21CQCN01', 'SINH_VIEN', 'BI_KHOA'),

('dungvx.b18.tc962_173', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy',
 'Vũ Xuân Dũng', 'dungvx.b18.tc962_173@stu.ptit.edu.vn', '0935950219', 'D18CQCN02', 'SINH_VIEN', 'HOAT_DONG'),

('baond.b18.tc048_106', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy',
 'Nguyễn Đức Bảo', 'baond.b18.tc048_106@stu.ptit.edu.vn', '0996338763', 'D18CQCN03', 'SINH_VIEN', 'BI_KHOA');

-- ===================================================================
-- DANH MỤC (từ muon-sach.html)
-- ===================================================================
INSERT INTO danh_muc (ten_danh_muc, mo_ta) VALUES
(1, 'Sách về lập trình, CNTT, khoa học máy tính'),
(2, 'Sách về hệ thống mạng, bảo mật mạng'),
(3, 'Sách về Big Data, Machine Learning, AI'),
(4, 'Sách về quy trình phát triển phần mềm');

-- Sửa lại INSERT đúng cú pháp:
DELETE FROM danh_muc;
INSERT INTO danh_muc (ma_danh_muc, ten_danh_muc, mo_ta) VALUES
(1, 'Công nghệ thông tin', 'Sách về lập trình, CNTT, khoa học máy tính'),
(2, 'Mạng máy tính', 'Sách về hệ thống mạng, bảo mật mạng'),
(3, 'Khoa học dữ liệu', 'Sách về Big Data, Machine Learning, AI'),
(4, 'Kỹ thuật phần mềm', 'Sách về quy trình phát triển phần mềm');

-- ===================================================================
-- TÀI LIỆU / SÁCH (từ muon-sach.html allBooks)
-- ===================================================================
INSERT INTO tai_lieu (ma_tai_lieu, ten_tai_lieu, tac_gia, nha_xuat_ban, nam_xuat_ban, ma_danh_muc, so_luong, so_luong_con, hinh_anh, mo_ta) VALUES
(1, 'Lập trình Python cơ bản', 'Nguyễn Văn A', 'NXB CNTT', 2024, 1,
    83, 71, 'https://images.unsplash.com/photo-1515879218367-8466d910aaa4?w=400&q=80',
    'Giáo trình lập trình Python từ cơ bản đến nâng cao dành cho sinh viên CNTT.'),

(2, 'Mạng máy tính và truyền dữ liệu', 'Trần Thị B', 'NXB Giáo Dục', 2022, 2,
    59, 10, 'https://images.unsplash.com/photo-1518770660439-4636190af475?w=400&q=80',
    'Kiến thức nền tảng về mô hình OSI, TCP/IP và các giao thức mạng.'),

(3, 'Phân tích dữ liệu lớn với Spark', 'Lê Văn C', 'NXB Khoa Học', 2023, 3,
    41, 0, 'https://images.unsplash.com/photo-1551288049-bebda4e38f71?w=400&q=80',
    'Xử lý và phân tích tập dữ liệu hàng triệu bản ghi với Apache Spark.'),

(4, 'Kỹ nghệ phần mềm hiện đại', 'Nguyễn Thị D', 'NXB Bách Khoa', 2021, 4,
    69, 33, 'https://images.unsplash.com/photo-1555066931-4365d14bab8c?w=400&q=80',
    'Quy trình phát triển Agile, thiết kế hướng đối tượng và kiểm thử phần mềm.'),

(5, 'Cơ sở dữ liệu SQL Server', 'Trần Văn D', 'NXB ĐHQG', 2023, 1,
    86, 55, 'https://images.unsplash.com/photo-1544716278-ca5e3f4abd8c?w=400&q=80',
    'Thiết kế, quản trị và tối ưu hóa cơ sở dữ liệu trên SQL Server.'),

(6, 'An toàn và bảo mật mạng', 'Lê Thị A', 'NXB CNTT', 2022, 2,
    54, 8, 'https://images.unsplash.com/photo-1550751827-4bd374c3f58b?w=400&q=80',
    'Các kỹ thuật tấn công mạng phổ biến và phương pháp phòng chống hiệu quả.');

-- ===================================================================
-- PHIẾU MƯỢN (từ sach-cua-toi.html + quan-ly-sach.html)
-- ===================================================================
INSERT INTO phieu_muon (ma_muon, ma_nguoi_dung, ma_tai_lieu, ngay_dang_ky, ngay_muon, ngay_hen_tra, ngay_tra_thuc_te, trang_thai, ghi_chu, tien_phat) VALUES
-- Đang mượn: Dương Thị Nam mượn CNTT Tập 1
(1, 2, 5, '2026-05-15', '2026-05-15', '2026-05-29', NULL, 'DANG_MUON', NULL, 0),

-- Chờ duyệt: user mượn Cấu trúc dữ liệu
(2, 2, 2, '2026-05-14', NULL, NULL, NULL, 'CHO_DUYET', NULL, 0),

-- Quá hạn: Phạm Ngọc Lan mượn CSDL Tập 3
(3, 6, 1, '2026-04-20', '2026-04-20', '2026-04-30', NULL, 'QUA_HAN', NULL, 0),

-- Đã trả: Vũ Quang Phong mượn Lập trình C++
(4, 3, 4, '2026-03-10', '2026-03-10', '2026-03-24', '2026-03-22', 'DA_TRA', NULL, 0),

-- Đang mượn: Trần Tuấn Anh mượn Giải tích
(5, 10, 6, '2026-05-05', '2026-05-05', '2026-05-19', NULL, 'DANG_MUON', NULL, 0),

-- Đã trả: Lê Minh Linh mượn Kỹ thuật phần mềm
(6, 11, 4, '2026-04-15', '2026-04-15', '2026-04-29', '2026-04-28', 'DA_TRA', NULL, 0),

-- Mất sách: Trần Tuấn Anh mất Giải tích A1
(7, 10, 3, '2024-11-18', '2024-11-18', '2024-12-02', NULL, 'MAT_SACH', 'Sinh viên báo mất sách', 150000),

-- Quá hạn: Phạm Ngọc Lan
(8, 6, 5, '2026-05-01', '2026-05-01', '2026-05-15', NULL, 'QUA_HAN', NULL, 0);


-- ===================================================================
-- VIEW: Thống kê mượn sách theo người dùng
-- ===================================================================
CREATE OR REPLACE VIEW v_thong_ke_muon AS
SELECT
    nd.ma_nguoi_dung,
    nd.tai_khoan,
    nd.ho_ten,
    SUM(CASE WHEN pm.trang_thai = 'DANG_MUON' THEN 1 ELSE 0 END) AS dang_muon,
    SUM(CASE WHEN pm.trang_thai = 'CHO_DUYET' THEN 1 ELSE 0 END) AS cho_duyet,
    SUM(CASE WHEN pm.trang_thai = 'DA_TRA' THEN 1 ELSE 0 END) AS da_tra,
    SUM(CASE WHEN pm.trang_thai = 'QUA_HAN' THEN 1 ELSE 0 END) AS qua_han,
    SUM(CASE WHEN pm.trang_thai = 'MAT_SACH' THEN 1 ELSE 0 END) AS mat_sach,
    COUNT(pm.ma_muon) AS tong
FROM nguoi_dung nd
LEFT JOIN phieu_muon pm ON nd.ma_nguoi_dung = pm.ma_nguoi_dung
WHERE nd.vai_tro = 'SINH_VIEN'
GROUP BY nd.ma_nguoi_dung, nd.tai_khoan, nd.ho_ten;


-- ===================================================================
-- VIEW: Dashboard admin
-- ===================================================================
CREATE OR REPLACE VIEW v_dashboard AS
SELECT
    (SELECT COUNT(*) FROM tai_lieu) AS tong_sach,
    (SELECT COUNT(*) FROM nguoi_dung WHERE vai_tro = 'SINH_VIEN') AS tong_sinh_vien,
    (SELECT COUNT(*) FROM phieu_muon) AS tong_luot_muon,
    (SELECT COUNT(*) FROM phieu_muon WHERE trang_thai = 'QUA_HAN') AS qua_han_chua_tra,
    (SELECT COUNT(*) FROM phieu_muon WHERE trang_thai = 'MAT_SACH') AS mat_sach,
    (SELECT COALESCE(SUM(tien_phat), 0) FROM phieu_muon WHERE tien_phat > 0) AS tong_tien_phat;


-- ===================================================================
-- KIỂM TRA DỮ LIỆU
-- ===================================================================
SELECT '=== NGƯỜI DÙNG ===' AS '';
SELECT ma_nguoi_dung, tai_khoan, ho_ten, vai_tro, trang_thai FROM nguoi_dung;

SELECT '=== DANH MỤC ===' AS '';
SELECT * FROM danh_muc;

SELECT '=== TÀI LIỆU ===' AS '';
SELECT ma_tai_lieu, ten_tai_lieu, tac_gia, so_luong, so_luong_con FROM tai_lieu;

SELECT '=== PHIẾU MƯỢN ===' AS '';
SELECT pm.ma_muon, nd.ho_ten, tl.ten_tai_lieu, pm.trang_thai, pm.ngay_muon, pm.ngay_hen_tra
FROM phieu_muon pm
JOIN nguoi_dung nd ON pm.ma_nguoi_dung = nd.ma_nguoi_dung
JOIN tai_lieu tl ON pm.ma_tai_lieu = tl.ma_tai_lieu;

SELECT '=== DASHBOARD ===' AS '';
SELECT * FROM v_dashboard;

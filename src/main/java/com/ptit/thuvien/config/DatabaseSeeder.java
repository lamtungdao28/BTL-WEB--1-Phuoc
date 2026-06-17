package com.ptit.thuvien.config;

import com.ptit.thuvien.model.*;
import com.ptit.thuvien.repository.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Tự động tạo dữ liệu giả lập (Mock Data) cho các Domain khi ứng dụng khởi chạy nếu chưa có dữ liệu.
 */
@Component
@RequiredArgsConstructor
public class DatabaseSeeder implements CommandLineRunner {

    private final NguoiDungRepository nguoiDungRepository;
    private final DanhMucRepository danhMucRepository;
    private final TaiLieuRepository taiLieuRepository;
    private final PhieuMuonRepository phieuMuonRepository;
    private final TinTucRepository tinTucRepository;
    private final SuKienRepository suKienRepository;
    private final ThongBaoRepository thongBaoRepository;
    private final PasswordEncoder passwordEncoder;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        // Sửa cột trang_thai nếu quá ngắn (fix lỗi Data truncated cho CHO_GIA_HAN)
        try {
            entityManager.createNativeQuery(
                    "ALTER TABLE phieu_muon MODIFY COLUMN trang_thai VARCHAR(20)"
            ).executeUpdate();
            System.out.println("[Seeder] Đã mở rộng cột trang_thai thành VARCHAR(20).");
        } catch (Exception e) {
            System.out.println("[Seeder] Cột trang_thai đã OK hoặc bảng chưa tồn tại: " + e.getMessage());
        }

        System.out.println("====== BẮT ĐẦU KIỂM TRA VÀ KHỞI TẠO MOCK DATA ======");

        // 1. Gieo dữ liệu Người dùng (NguoiDung)
        NguoiDung admin = seedUsers();

        // 2. Gieo dữ liệu Danh mục (DanhMuc)
        List<DanhMuc> dsDanhMuc = seedCategories();

        // 3. Gieo dữ liệu Tài liệu / Sách (TaiLieu)
        List<TaiLieu> dsTaiLieu = seedBooks(dsDanhMuc);

        // 4. Gieo dữ liệu Phiếu mượn (PhieuMuon)
        seedBorrowSlips(dsTaiLieu);

        // 5. Gieo dữ liệu Tin tức (TinTuc)
        seedNews(admin);

        // 6. Gieo dữ liệu Sự kiện (SuKien)
        seedEvents();

        // 7. Gieo dữ liệu Thông báo (ThongBao)
        seedNotifications();

        System.out.println("====== HOÀN TẤT KHỞI TẠO MOCK DATA ======");
    }

    private void createStudentIfNotExist(String taiKhoan, String hoTen, String email, String soDienThoai, String lop) {
        NguoiDung u = nguoiDungRepository.findByTaiKhoan(taiKhoan).orElse(null);
        if (u == null) {
            u = NguoiDung.builder()
                    .taiKhoan(taiKhoan)
                    .matKhau(passwordEncoder.encode("123456"))
                    .hoTen(hoTen)
                    .email(email)
                    .soDienThoai(soDienThoai)
                    .lop(lop)
                    .vaiTro(NguoiDung.VaiTro.SINH_VIEN)
                    .trangThai(NguoiDung.TrangThaiNguoiDung.HOAT_DONG)
                    .build();
            nguoiDungRepository.save(u);
            System.out.println("[Seeder] Đã khởi tạo tài khoản sinh viên: " + taiKhoan + " / 123456");
        }
    }

    private void createTeacherIfNotExist(String taiKhoan, String hoTen, String email, String soDienThoai, String khoa) {
        NguoiDung u = nguoiDungRepository.findByTaiKhoan(taiKhoan).orElse(null);
        if (u == null) {
            u = NguoiDung.builder()
                    .taiKhoan(taiKhoan)
                    .matKhau(passwordEncoder.encode("123456"))
                    .hoTen(hoTen)
                    .email(email)
                    .soDienThoai(soDienThoai)
                    .lop(khoa)
                    .vaiTro(NguoiDung.VaiTro.GIANG_VIEN)
                    .trangThai(NguoiDung.TrangThaiNguoiDung.HOAT_DONG)
                    .build();
            nguoiDungRepository.save(u);
            System.out.println("[Seeder] Đã khởi tạo tài khoản giảng viên: " + taiKhoan + " / 123456");
        }
    }

    private NguoiDung seedUsers() {
        // Tài khoản Admin
        NguoiDung admin = nguoiDungRepository.findByTaiKhoan("admin").orElse(null);
        if (admin == null) {
            admin = NguoiDung.builder()
                    .taiKhoan("admin")
                    .matKhau(passwordEncoder.encode("admin"))
                    .hoTen("Quản trị viên")
                    .email("admin@ptit.edu.vn")
                    .vaiTro(NguoiDung.VaiTro.ADMIN)
                    .trangThai(NguoiDung.TrangThaiNguoiDung.HOAT_DONG)
                    .build();
            admin = nguoiDungRepository.save(admin);
            System.out.println("[Seeder] Đã khởi tạo tài khoản admin: admin / admin (Email: admin@ptit.edu.vn)");
        } else if (admin.getEmail() == null) {
            admin.setEmail("admin@ptit.edu.vn");
            admin = nguoiDungRepository.save(admin);
            System.out.println("[Seeder] Đã cập nhật email cho tài khoản admin: admin@ptit.edu.vn");
        }

        // Tài khoản Sinh viên mẫu lamtungdao28
        NguoiDung sv = nguoiDungRepository.findByTaiKhoan("lamtungdao28").orElse(null);
        if (sv == null) {
            sv = NguoiDung.builder()
                    .taiKhoan("lamtungdao28")
                    .matKhau(passwordEncoder.encode("123456"))
                    .hoTen("Đào Tùng Lâm")
                    .email("lamtungdao28@gmail.com")
                    .soDienThoai("0987654321")
                    .lop("D21CQCN01-B")
                    .vaiTro(NguoiDung.VaiTro.SINH_VIEN)
                    .trangThai(NguoiDung.TrangThaiNguoiDung.HOAT_DONG)
                    .build();
            nguoiDungRepository.save(sv);
            System.out.println("[Seeder] Đã khởi tạo tài khoản sinh viên: lamtungdao28 / 123456");
        } else {
            sv.setMatKhau(passwordEncoder.encode("123456"));
            nguoiDungRepository.save(sv);
            System.out.println("[Seeder] Đã cập nhật mật khẩu tài khoản sinh viên: lamtungdao28 / 123456");
        }

        // Tài khoản Giảng viên mẫu lam
        NguoiDung gv = nguoiDungRepository.findByTaiKhoan("lam").orElse(null);
        if (gv == null) {
            gv = NguoiDung.builder()
                    .taiKhoan("lam")
                    .matKhau(passwordEncoder.encode("123456"))
                    .hoTen("Nguyễn Tùng Lâm")
                    .email("lamnt@ptit.edu.vn")
                    .soDienThoai("0912345678")
                    .lop("Khoa CNTT")
                    .vaiTro(NguoiDung.VaiTro.GIANG_VIEN)
                    .trangThai(NguoiDung.TrangThaiNguoiDung.HOAT_DONG)
                    .build();
            nguoiDungRepository.save(gv);
            System.out.println("[Seeder] Đã khởi tạo tài khoản giảng viên: lam / 123456");
        } else {
            gv.setMatKhau(passwordEncoder.encode("123456"));
            nguoiDungRepository.save(gv);
            System.out.println("[Seeder] Đã cập nhật mật khẩu tài khoản giảng viên: lam / 123456");
        }

        // Thêm 5 Giảng viên mẫu cũ
        createTeacherIfNotExist("giangvien01", "PGS.TS. Nguyễn Mạnh Hùng", "hungnm@ptit.edu.vn", "0911111222", "Khoa CNTT");
        createTeacherIfNotExist("giangvien02", "TS. Lê Thị Thảo", "thaolt@ptit.edu.vn", "0912222333", "Khoa CNTT");
        createTeacherIfNotExist("giangvien03", "ThS. Nguyễn Quang Huy", "huynq@ptit.edu.vn", "0913333444", "Khoa ĐTVT");
        createTeacherIfNotExist("giangvien04", "TS. Phạm Thanh Sơn", "sonpt@ptit.edu.vn", "0914444555", "Khoa Cơ bản");
        createTeacherIfNotExist("giangvien05", "ThS. Đỗ Thị Quỳnh", "quynhdt@ptit.edu.vn", "0915555666", "Khoa QTKD");

        // Thêm 200 Sinh viên mẫu tự sinh
        String[] hos = {"Nguyễn", "Trần", "Lê", "Phạm", "Hoàng", "Phan", "Vũ", "Võ", "Đặng", "Bùi", "Đỗ", "Hồ", "Ngô", "Dương", "Lý"};
        String[] dems = {"Văn", "Minh", "Quang", "Đức", "Anh", "Ngọc", "Thanh", "Hữu", "Quốc", "Thành", "Thế", "Trọng", "Đình", "Hải"};
        String[] tens = {"Nam", "Đức", "Linh", "Tuấn", "Phương", "Dũng", "Hà", "Anh", "Huy", "Hùng", "Lan", "Mai", "Sơn", "Quỳnh", "Trang", "Thảo", "Hải", "Phong", "Tiến", "Cường", "Bình", "Minh", "Trung", "Khánh", "Duy"};
        String[] lops = {"D21CQCN01-B", "D21CQCN02-B", "D21CQCN03-B", "D20CQVT01-A", "D20CQVT02-B", "D22CQCN04-A", "D22CQCN02-B", "D21CQKT01-A", "D21CQKT02-B", "D20CQCN05-B", "D22CQVT01-A", "D22CQVT02-B", "D21CQAT01-B", "D21CQAT02-B"};

        for (int i = 1; i <= 200; i++) {
            String taiKhoan = String.format("sv%03d", i);
            String ho = hos[i % hos.length];
            String dem = dems[(i * 3) % dems.length];
            String ten = tens[(i * 7) % tens.length];
            String hoTen = ho + " " + dem + " " + ten;
            String email = taiKhoan + "@student.ptit.edu.vn";
            String soDienThoai = String.format("098%07d", i);
            String lop = lops[(i * 11) % lops.length];
            createStudentIfNotExist(taiKhoan, hoTen, email, soDienThoai, lop);
        }

        return admin;
    }

    private List<DanhMuc> seedCategories() {
        List<DanhMuc> danhMucs = new ArrayList<>();
        if (danhMucRepository.count() == 0) {
            danhMucs.add(DanhMuc.builder().tenDanhMuc("Công nghệ thông tin").moTa("Sách chuyên ngành CNTT, Lập trình, Khoa học dữ liệu, AI").build());
            danhMucs.add(DanhMuc.builder().tenDanhMuc("Điện tử viễn thông").moTa("Sách về Điện tử, Viễn thông, Thiết kế vi mạch, IoT").build());
            danhMucs.add(DanhMuc.builder().tenDanhMuc("Kinh tế & Quản trị").moTa("Sách về Quản trị kinh doanh, Marketing, Tài chính kế toán").build());
            danhMucs.add(DanhMuc.builder().tenDanhMuc("Kỹ năng mềm").moTa("Sách phát triển bản thân, kỹ năng giao tiếp, làm việc nhóm").build());
            danhMucs.add(DanhMuc.builder().tenDanhMuc("Khoa học cơ bản").moTa("Toán cao cấp, Vật lý đại cương, Triết học").build());
            
            danhMucs = danhMucRepository.saveAll(danhMucs);
            System.out.println("[Seeder] Đã tạo mới " + danhMucs.size() + " danh mục sách.");
        } else {
            danhMucs = danhMucRepository.findAll();
        }
        return danhMucs;
    }

    private TaiLieu createBook(String ten, String tacGia, String nxb, int nam, DanhMuc dm, int sl, String hinhAnh, String moTa, String filePdf) {
        return TaiLieu.builder()
                .tenTaiLieu(ten)
                .tacGia(tacGia)
                .nxb(nxb)
                .namXuatBan(nam)
                .danhMuc(dm)
                .soLuong(sl)
                .soLuongCon(sl)
                .hinhAnh(hinhAnh)
                .moTa(moTa)
                .filePdf(filePdf)
                .build();
    }

    private List<TaiLieu> seedBooks(List<DanhMuc> dsDanhMuc) {
        List<TaiLieu> taiLieus = new ArrayList<>();
        if (!dsDanhMuc.isEmpty()) {
            DanhMuc cntt = dsDanhMuc.get(0);
            DanhMuc dtvt = dsDanhMuc.size() > 1 ? dsDanhMuc.get(1) : cntt;
            DanhMuc kinhTe = dsDanhMuc.size() > 2 ? dsDanhMuc.get(2) : cntt;
            DanhMuc kyNang = dsDanhMuc.size() > 3 ? dsDanhMuc.get(3) : cntt;
            DanhMuc khoaHoc = dsDanhMuc.size() > 4 ? dsDanhMuc.get(4) : cntt;

            // Load tất cả sách hiện tại để tránh trùng lặp
            List<TaiLieu> existingBooks = taiLieuRepository.findAll();
            java.util.Set<String> existingTitles = new java.util.HashSet<>();
            for (TaiLieu t : existingBooks) {
                existingTitles.add(t.getTenTaiLieu().trim().toLowerCase());
            }

            // Danh sách 50 cuốn sách mẫu (10 cuốn mỗi danh mục)
            List<TaiLieu> booksToSeed = new ArrayList<>();

            // 1. Công nghệ thông tin (10 cuốn)
            booksToSeed.add(createBook("Lập trình Java căn bản & nâng cao", "Nguyễn Văn A", "NXB Bách Khoa", 2023, cntt, 15, "/tai-nguyen/anhsach/book-cover-1.jpg", "Sách giáo trình giảng dạy lập trình hướng đối tượng Java tại PTIT.", "/tai-nguyen/noidung/Lập trình Java căn bản.pdf"));
            booksToSeed.add(createBook("Cấu trúc dữ liệu và Giải thuật", "Phạm Thế Long", "NXB Giáo Dục", 2022, cntt, 20, "/tai-nguyen/anhsach/book-cover-2.jpg", "Các thuật toán cơ bản, cấu trúc dữ liệu mảng, danh sách liên kết, cây, đồ thị.", null));
            booksToSeed.add(createBook("Lập trình Web với Spring Boot và Thymeleaf", "Trần Văn Bình", "NXB Thông tin & Truyền thông", 2024, cntt, 15, "/tai-nguyen/anhsach/book-cover-3.jpg", "Hướng dẫn xây dựng các ứng dụng web doanh nghiệp hiện đại với Spring Boot.", null));
            booksToSeed.add(createBook("Nhập môn Trí tuệ Nhân tạo và Học máy", "TS. Lê Hoài Bắc", "NXB Khoa học và Kỹ thuật", 2023, cntt, 10, "/tai-nguyen/anhsach/book-cover-4.jpg", "Kiến thức nền tảng về AI, học máy có giám sát và không giám sát, mạng neural.", "/tai-nguyen/noidung/Nhập môn Trí tuệ Nhân tạo và Học máy.pdf"));
            booksToSeed.add(createBook("Thiết kế và Quản trị Cơ sở dữ liệu SQL", "Nguyễn Thị Minh", "NXB Bách Khoa", 2022, cntt, 18, "/tai-nguyen/anhsach/book-cover-5.jpg", "Ngôn ngữ SQL, thiết kế lược đồ quan hệ, chuẩn hóa dữ liệu và tối ưu hóa câu lệnh.", "/tai-nguyen/noidung/Thiết kế và Quản trị Cơ sở dữ liệu SQL.pdf"));
            booksToSeed.add(createBook("Kiến trúc phần mềm Clean Architecture", "Robert C. Martin", "NXB Lao Động", 2020, cntt, 12, "/tai-nguyen/anhsach/book-cover-6.jpg", "Các nguyên lý thiết kế hệ thống phần mềm sạch, dễ mở rộng và kiểm thử.", "/tai-nguyen/noidung/Kiến trúc phần mềm Clean Architecture.pdf"));
            booksToSeed.add(createBook("Lập trình di động Android nâng cao", "Bill Phillips", "NXB Trẻ", 2023, cntt, 10, "/tai-nguyen/anhsach/book-cover-7.jpg", "Lập trình Android nâng cao với Kotlin và Jetpack Compose.", "/tai-nguyen/noidung/Lập trình di động Android nâng cao.pdf"));
            booksToSeed.add(createBook("Điện toán đám mây và AWS cơ bản", "Thomas Erl", "NXB Thông tin & Truyền thông", 2022, cntt, 8, "/tai-nguyen/anhsach/book-cover-8.jpg", "Nguyên lý điện toán đám mây, kiến trúc dịch vụ AWS, EC2, S3, RDS.", "/tai-nguyen/noidung/Điện toán đám mây và AWS cơ bản.pdf"));
            booksToSeed.add(createBook("Nhập môn Mật mã học và An toàn thông tin", "William Stallings", "NXB Khoa học tự nhiên", 2023, cntt, 15, "/tai-nguyen/anhsach/book-cover-9.jpg", "Mã hóa đối xứng, bất đối xứng, chữ ký số và các cơ chế bảo mật mạng cơ bản.", "/tai-nguyen/noidung/Nhập môn Mật mã học và An toàn thông tin.pdf"));
            booksToSeed.add(createBook("Docker và Kubernetes cho lập trình viên", "Nigel Poulton", "NXB Trẻ", 2024, cntt, 14, "/tai-nguyen/anhsach/book-cover-10.jpg", "Đóng gói ứng dụng dạng Container, triển khai và quản trị Kubernetes cluster.", "/tai-nguyen/noidung/Docker và Kubernetes cho lập trình viên.pdf"));

            // 2. Điện tử viễn thông (10 cuốn)
            booksToSeed.add(createBook("Mạng máy tính và Truyền số liệu", "Nguyễn Trung Hiếu", "NXB Thông tin & Truyền thông", 2024, dtvt, 10, "/tai-nguyen/anhsach/book-cover-11.jpg", "Kiến thức tổng quan về các tầng trong mô hình OSI/TCP-IP và giao thức mạng.", null));
            booksToSeed.add(createBook("Kỹ thuật Vi mạch và Hệ thống Nhúng", "Vũ Đình Thành", "NXB Khoa học tự nhiên", 2023, dtvt, 12, "/tai-nguyen/anhsach/book-cover-12.jpg", "Nguyên lý hoạt động của hệ thống nhúng, lập trình vi điều khiển ARM.", null));
            booksToSeed.add(createBook("Thiết kế mạch IoT với ESP32", "Phạm Ngọc Nam", "NXB Bách Khoa", 2024, dtvt, 15, "/tai-nguyen/anhsach/book-cover-13.jpg", "Thực hành lập trình kết nối Wi-Fi, Bluetooth trên ESP32, đọc cảm biến.", null));
            booksToSeed.add(createBook("Truyền thông vô tuyến và Anten", "Đỗ Quốc Huy", "NXB Giáo dục", 2021, dtvt, 8, "/tai-nguyen/anhsach/book-cover-14.jpg", "Lý thuyết điện từ trường, thiết kế anten và các mô hình truyền sóng vô tuyến.", null));
            booksToSeed.add(createBook("Xử lý tín hiệu số cơ bản", "Alan V. Oppenheim", "NXB Bách Khoa", 2020, dtvt, 11, "/tai-nguyen/anhsach/book-cover-15.jpg", "Biến đổi Fourier, thiết kế bộ lọc số IIR, FIR và các ứng dụng xử lý tín hiệu thực tế.", null));
            booksToSeed.add(createBook("Cơ sở Kỹ thuật Điện tử thông tin", "Trần Xuân Việt", "NXB Giáo dục", 2022, dtvt, 10, "/tai-nguyen/anhsach/book-cover-16.jpg", "Các linh kiện bán dẫn, mạch khuếch đại, mạch tạo dao động và biến đổi tần số.", "/tai-nguyen/noidung/Cơ sở Kỹ thuật Điện tử thông tin.pdf"));
            booksToSeed.add(createBook("Mạng di động thế hệ mới 5G và IoT", "Harish Kumar", "NXB Khoa học tự nhiên", 2023, dtvt, 9, "/tai-nguyen/anhsach/book-cover-17.jpg", "Kiến trúc mạng lõi 5G, truyền thông thiết bị đến thiết bị và ứng dụng trong IoT.", "/tai-nguyen/noidung/Mạng di động thế hệ mới 5G và IoT.pdf"));
            booksToSeed.add(createBook("Kỹ thuật Truyền thanh truyền hình số", "Nguyễn Kim Sách", "NXB Thông tin & Truyền thông", 2021, dtvt, 7, "/tai-nguyen/anhsach/book-cover-18.jpg", "Nguyên lý số hóa tín hiệu âm thanh, hình ảnh và các chuẩn phát sóng DVB-T2.", "/tai-nguyen/noidung/Kỹ thuật Truyền thanh truyền hình số.pdf"));
            booksToSeed.add(createBook("Thông tin sợi quang và Quang điện tử", "Gerd Keiser", "NXB Giáo dục", 2022, dtvt, 10, "/tai-nguyen/anhsach/book-cover-19.jpg", "Truyền dẫn ánh sáng trong sợi quang, nguồn phát quang và bộ thu quang điện tử.", null));
            booksToSeed.add(createBook("Lập trình vi điều khiển STM32", "Carmine Noviello", "NXB Bách Khoa", 2023, dtvt, 12, "/tai-nguyen/anhsach/book-cover-20.jpg", "Kiến trúc ARM Cortex-M, lập trình ngoại vi GPIO, USART, ADC, DMA trên STM32.", null));

            // 3. Kinh tế & Quản trị (10 cuốn)
            booksToSeed.add(createBook("Giáo trình Kinh tế vĩ mô", "Trần Thị C", "NXB Đại học Kinh tế Quốc dân", 2021, kinhTe, 12, "/tai-nguyen/anhsach/book-cover-21.jpg", "Tìm hiểu về lạm phát, thất nghiệp, GDP, chính sách tài khóa và tiền tệ.", null));
            booksToSeed.add(createBook("Quản trị Học đại cương", "TS. Nguyễn Thanh Liêm", "NXB Thống kê", 2022, kinhTe, 20, "/tai-nguyen/anhsach/book-cover-22.jpg", "Các chức năng quản trị cơ bản: hoạch định, tổ chức, lãnh đạo và kiểm tra.", null));
            booksToSeed.add(createBook("Marketing kỹ thuật số trong kỷ nguyên 4.0", "Philip Kotler", "NXB Trẻ", 2023, kinhTe, 25, "/tai-nguyen/anhsach/book-cover-23.jpg", "Chiến lược tiếp thị số, SEO, SEM, truyền thông xã hội và phân tích dữ liệu hành vi.", null));
            booksToSeed.add(createBook("Phân tích báo cáo tài chính doanh nghiệp", "Nguyễn Minh Kiều", "NXB Tài chính", 2024, kinhTe, 15, "/tai-nguyen/anhsach/book-cover-24.jpg", "Phương pháp đọc hiểu và phân tích bảng cân đối kế toán, báo cáo kết quả kinh doanh.", null));
            booksToSeed.add(createBook("Khởi nghiệp đổi mới sáng tạo", "Eric Ries", "NXB Tổng hợp TP.HCM", 2021, kinhTe, 18, "/tai-nguyen/anhsach/book-cover-25.jpg", "Phương pháp khởi nghiệp tinh gọn giúp tối ưu nguồn lực và kiểm chứng giả thuyết.", null));
            booksToSeed.add(createBook("Quản trị chuỗi cung ứng hiện đại", "Sunil Chopra", "NXB Thống kê", 2022, kinhTe, 10, "/tai-nguyen/anhsach/book-cover-26.jpg", "Logistics, quản trị tồn kho và điều phối trong chuỗi cung ứng toàn cầu.", null));
            booksToSeed.add(createBook("Hành vi tổ chức trong doanh nghiệp", "Stephen P. Robbins", "NXB Trẻ", 2023, kinhTe, 15, "/tai-nguyen/anhsach/book-cover-27.jpg", "Động lực làm việc, giao tiếp nội bộ, xung đột và văn hóa tổ chức doanh nghiệp.", null));
            booksToSeed.add(createBook("Kinh tế học vi mô căn bản", "N. Gregory Mankiw", "NXB Hồng Đức", 2022, kinhTe, 20, "/tai-nguyen/anhsach/book-cover-28.jpg", "Cung cầu, thị trường cạnh tranh, độc quyền và hành vi người tiêu dùng, doanh nghiệp.", null));
            booksToSeed.add(createBook("Quản trị nguồn nhân lực", "Gary Dessler", "NXB Tài chính", 2021, kinhTe, 12, "/tai-nguyen/anhsach/book-cover-29.jpg", "Tuyển dụng, đào tạo, đánh giá hiệu quả công việc và các chính sách đãi ngộ nhân sự.", null));
            booksToSeed.add(createBook("Tài chính doanh nghiệp chuyên sâu", "Stephen A. Ross", "NXB Thống kê", 2023, kinhTe, 10, "/tai-nguyen/anhsach/book-cover-30.jpg", "Hoạch định ngân sách vốn đầu tư, chi phí sử dụng vốn và cấu trúc vốn doanh nghiệp.", null));

            // 4. Kỹ năng mềm (10 cuốn)
            booksToSeed.add(createBook("Đắc Nhân Tâm (How to Win Friends & Influence People)", "Dale Carnegie", "NXB Trẻ", 2020, kyNang, 30, "/tai-nguyen/anhsach/book-cover-31.jpg", "Cuốn sách kinh điển về nghệ thuật ứng xử, giao tiếp và thu phục lòng người.", null));
            booksToSeed.add(createBook("Tư duy ngược - Thay đổi cách nghĩ để thành công", "Nguyễn Anh Dũng", "NXB Lao động", 2022, kyNang, 30, "/tai-nguyen/anhsach/book-cover-32.jpg", "Khai phá tư duy đột phá, giải quyết vấn đề từ các góc nhìn khác biệt.", null));
            booksToSeed.add(createBook("Kỹ năng thuyết trình và làm việc nhóm hiệu quả", "Lê Huy Khoa", "NXB Tổng hợp TP.HCM", 2023, kyNang, 22, "/tai-nguyen/anhsach/book-cover-33.jpg", "Phương pháp chuẩn bị slide thuyết trình, kiểm soát giọng nói, tương tác khán giả.", null));
            booksToSeed.add(createBook("Quản lý thời gian và giải quyết vấn đề", "Brian Tracy", "NXB Hồng Đức", 2021, kyNang, 25, "/tai-nguyen/anhsach/book-cover-34.jpg", "Quy tắc 80/20, thiết lập mục tiêu hàng ngày, tập trung vào nhiệm vụ trọng tâm.", null));
            booksToSeed.add(createBook("Đọc vị bất kỳ ai để không bị lợi dụng", "David J. Lieberman", "NXB Trẻ", 2020, kyNang, 20, "/tai-nguyen/anhsach/book-cover-35.jpg", "Các kỹ thuật tâm lý giúp nhận diện cảm xúc, suy nghĩ và ý đồ thực sự.", null));
            booksToSeed.add(createBook("Người tối giản - Hành trình tìm lại bản thân", "Sasaki Fumio", "NXB Lao động", 2022, kyNang, 15, "/tai-nguyen/anhsach/book-cover-36.jpg", "Lối sống tối giản, giảm bớt đồ đạc dư thừa để tập trung vào giá trị tinh thần.", null));
            booksToSeed.add(createBook("Tư duy nhanh và chậm", "Daniel Kahneman", "NXB Thế giới", 2021, kyNang, 18, "/tai-nguyen/anhsach/book-cover-37.jpg", "Hai hệ thống tư duy chi phối hành vi: hệ thống 1 (nhanh) và hệ thống 2 (chậm).", null));
            booksToSeed.add(createBook("7 Thói quen của bạn trẻ thành đạt", "Sean Covey", "NXB Trẻ", 2022, kyNang, 28, "/tai-nguyen/anhsach/book-cover-38.jpg", "Xây dựng thói quen tích cực, quản lý bản thân, hợp tác và làm mới tâm hồn.", null));
            booksToSeed.add(createBook("Nói thế nào để trẻ nghe lời và lắng nghe thế nào", "Adele Faber", "NXB Lao động", 2020, kyNang, 12, "/tai-nguyen/anhsach/book-cover-39.jpg", "Nghệ thuật giao tiếp trong gia đình, xây dựng sự tin cậy và hiểu biết lẫn nhau.", null));
            booksToSeed.add(createBook("Lối tư duy của người thành công", "Carol S. Dweck", "NXB Trẻ", 2021, kyNang, 20, "/tai-nguyen/anhsach/book-cover-40.jpg", "Sức mạnh của tư duy phát triển (growth mindset) so với tư duy cố định.", null));

            // 5. Khoa học cơ bản (10 cuốn)
            booksToSeed.add(createBook("Toán cao cấp cho kỹ sư và cử nhân", "Nguyễn Đình Trí", "NXB Giáo dục", 2020, khoaHoc, 40, "/tai-nguyen/anhsach/book-cover-41.jpg", "Giải tích toán học một biến, nhiều biến, phép tính vi phân và tích phân.", null));
            booksToSeed.add(createBook("Vật lý đại cương - Điện và Từ học", "Lương Duyên Bình", "NXB Giáo dục", 2021, khoaHoc, 35, "/tai-nguyen/anhsach/book-cover-42.jpg", "Trường tĩnh điện, vật dẫn, điện môi, từ trường tĩnh, cảm ứng điện từ.", null));
            booksToSeed.add(createBook("Nhập môn Logic học và Phương pháp luận", "Nguyễn Đức Dân", "NXB Khoa học Xã hội", 2022, khoaHoc, 20, "/tai-nguyen/anhsach/book-cover-43.jpg", "Cơ sở tư duy logic hình thức, các quy luật logic cơ bản, phương pháp luận.", null));
            booksToSeed.add(createBook("Đại số tuyến tính và Hình học giải tích", "Nguyễn Hữu Việt Hưng", "NXB Giáo dục", 2021, khoaHoc, 30, "/tai-nguyen/anhsach/book-cover-44.jpg", "Không gian vector, ma trận, định thức, hệ phương trình tuyến tính và dạng toàn phương.", null));
            booksToSeed.add(createBook("Hóa học đại cương và ứng dụng", "Nguyễn Đức Chung", "NXB Bách Khoa", 2020, khoaHoc, 25, "/tai-nguyen/anhsach/book-cover-45.jpg", "Cấu tạo chất, nhiệt động hóa học, động hóa học, cân bằng hóa học và điện hóa học.", null));
            booksToSeed.add(createBook("Giáo trình Xác suất và Thống kê toán", "Tống Đình Quỳ", "NXB Bách Khoa", 2022, khoaHoc, 28, "/tai-nguyen/anhsach/book-cover-46.jpg", "Biến ngẫu nhiên, quy luật phân phối xác suất, ước lượng và kiểm định giả thuyết.", null));
            booksToSeed.add(createBook("Vật lý đại cương - Cơ và Nhiệt học", "Lương Duyên Bình", "NXB Giáo dục", 2021, khoaHoc, 32, "/tai-nguyen/anhsach/book-cover-0.jpg", "Cơ học chất điểm, hệ chất điểm, vật rắn, thuyết động học phân tử và nhiệt động.", null));
            booksToSeed.add(createBook("Lịch sử các học thuyết kinh tế học", "Nguyễn Minh Tuấn", "NXB Tài chính", 2020, khoaHoc, 15, "/tai-nguyen/anhsach/book-cover-extra.png", "Quá trình hình thành và phát triển các hệ tư tưởng kinh tế học từ cổ đại đến hiện đại.", null));
            booksToSeed.add(createBook("Nhập môn Triết học đại cương", "Bộ Giáo dục và Đào tạo", "NXB Chính trị Quốc gia", 2021, khoaHoc, 30, "/tai-nguyen/anhsach/book-cover-dl.png", "Các khái niệm triết học cơ bản, chủ nghĩa duy vật biện chứng và duy vật lịch sử.", null));
            booksToSeed.add(createBook("Xác suất thống kê và quy hoạch thực nghiệm", "Phạm Hải Tùng", "NXB Bách Khoa", 2023, khoaHoc, 18, "/tai-nguyen/anhsach/book-cover-0.jpg", "Xử lý số liệu thực nghiệm, thiết kế thí nghiệm và phân tích hồi quy tương quan.", null));

            // Lưu các cuốn sách chưa tồn tại
            List<TaiLieu> booksToSave = new ArrayList<>();
            for (TaiLieu book : booksToSeed) {
                if (!existingTitles.contains(book.getTenTaiLieu().trim().toLowerCase())) {
                    booksToSave.add(book);
                }
            }

            if (!booksToSave.isEmpty()) {
                List<TaiLieu> saved = taiLieuRepository.saveAll(booksToSave);
                taiLieus.addAll(saved);
                System.out.println("[Seeder] Đã tạo mới " + saved.size() + " sách/tài liệu mẫu.");
            } else {
                System.out.println("[Seeder] Không có thêm sách mới nào cần tạo.");
            }

            // Cập nhật ảnh bìa và file PDF cho sách đã tồn tại
            java.util.Map<String, TaiLieu> seedMap = new java.util.HashMap<>();
            for (TaiLieu s : booksToSeed) {
                seedMap.put(s.getTenTaiLieu().trim().toLowerCase(), s);
            }
            int updated = 0;
            for (TaiLieu existing : existingBooks) {
                TaiLieu seedData = seedMap.get(existing.getTenTaiLieu().trim().toLowerCase());
                if (seedData != null) {
                    boolean changed = false;
                    // Cập nhật ảnh bìa nếu chưa có path đúng
                    if (seedData.getHinhAnh() != null && (existing.getHinhAnh() == null || !existing.getHinhAnh().startsWith("/tai-nguyen/anhsach/"))) {
                        existing.setHinhAnh(seedData.getHinhAnh());
                        changed = true;
                    }
                    // Cập nhật file PDF nếu chưa có
                    if (seedData.getFilePdf() != null && existing.getFilePdf() == null) {
                        existing.setFilePdf(seedData.getFilePdf());
                        changed = true;
                    }
                    if (changed) {
                        taiLieuRepository.save(existing);
                        updated++;
                    }
                }
            }
            if (updated > 0) {
                System.out.println("[Seeder] Đã cập nhật ảnh bìa/PDF cho " + updated + " sách đã tồn tại.");
            }
        }
        return taiLieuRepository.findAll();
    }

    private void seedBorrowSlips(List<TaiLieu> dsTaiLieu) {
        if (phieuMuonRepository.count() == 0 && !dsTaiLieu.isEmpty()) {
            NguoiDung sinhVien = nguoiDungRepository.findByTaiKhoan("lamtungdao28").orElse(null);
            if (sinhVien != null) {
                // Phiếu mượn 1: Đang mượn (chưa quá hạn)
                phieuMuonRepository.save(PhieuMuon.builder()
                        .nguoiDung(sinhVien)
                        .taiLieu(dsTaiLieu.get(0))
                        .ngayDangKy(LocalDate.now().minusDays(5))
                        .ngayMuon(LocalDateTime.now().minusDays(5))
                        .ngayHenTra(LocalDateTime.now().plusDays(9))
                        .trangThai(PhieuMuon.TrangThaiMuon.DANG_MUON)
                        .ghiChu("Mượn học tập môn Lập trình Java.")
                        .build());

                // Phiếu mượn 2: Chờ duyệt đăng ký
                if (dsTaiLieu.size() > 2) {
                    phieuMuonRepository.save(PhieuMuon.builder()
                            .nguoiDung(sinhVien)
                            .taiLieu(dsTaiLieu.get(2))
                            .ngayDangKy(LocalDate.now().minusDays(1))
                            .trangThai(PhieuMuon.TrangThaiMuon.CHO_DUYET)
                            .ghiChu("Đăng ký mượn tài liệu học môn Mạng máy tính.")
                            .build());
                }

                // Phiếu mượn 3: Quá hạn (Overdue)
                if (dsTaiLieu.size() > 4) {
                    phieuMuonRepository.save(PhieuMuon.builder()
                            .nguoiDung(sinhVien)
                            .taiLieu(dsTaiLieu.get(4))
                            .ngayDangKy(LocalDate.now().minusDays(20))
                            .ngayMuon(LocalDateTime.now().minusDays(20))
                            .ngayHenTra(LocalDateTime.now().minusDays(6))
                            .trangThai(PhieuMuon.TrangThaiMuon.QUA_HAN)
                            .tienPhat(0L)
                            .ghiChu("Mượn đọc kỹ năng giao tiếp.")
                            .build());
                }

                // Phiếu mượn 4: Đã trả thành công
                if (dsTaiLieu.size() > 1) {
                    phieuMuonRepository.save(PhieuMuon.builder()
                            .nguoiDung(sinhVien)
                            .taiLieu(dsTaiLieu.get(1))
                            .ngayDangKy(LocalDate.now().minusDays(30))
                            .ngayMuon(LocalDateTime.now().minusDays(30))
                            .ngayHenTra(LocalDateTime.now().minusDays(16))
                            .ngayTraThucTe(LocalDate.now().minusDays(18))
                            .trangThai(PhieuMuon.TrangThaiMuon.DA_TRA)
                            .ghiChu("Đã trả đủ sách, sách còn nguyên vẹn.")
                            .build());
                }

                System.out.println("[Seeder] Đã gieo dữ liệu Phiếu mượn mẫu.");
            }
        }
    }

    private void seedNews(NguoiDung admin) {
        tinTucRepository.deleteAll();
        
        tinTucRepository.save(TinTuc.builder()
                .tieuDe("Khai trương không gian học tập số PTIT hiện đại bậc nhất")
                .tomTat("Nhằm đáp ứng nhu cầu nghiên cứu học tập, Học viện Công nghệ Bưu chính Viễn thông vừa khánh thành không gian học tập số hiện đại.")
                .noiDung("Học viện Công nghệ Bưu chính Viễn thông đã chính thức đưa vào hoạt động khu phức hợp Thư viện số và Không gian tự học mới tại tầng 2 nhà A2. Với thiết kế mở, đầy đủ ánh sáng, trang bị hệ thống máy tính cấu hình cao, điều hòa 24/7 và hệ thống tra cứu sách tự động QR, đây hứa hẹn sẽ là địa điểm lý tưởng thu hút hàng nghìn sinh viên PTIT mỗi ngày.")
                .hinhAnh("/tai-nguyen/news-default.png")
                .danhMucTin("Thông báo")
                .luotXem(124)
                .tacGia(admin)
                .build());

        tinTucRepository.save(TinTuc.builder()
                .tieuDe("Hội sách Sinh viên PTIT 2026 chính thức khởi động")
                .tomTat("Ngày hội sách thường niên quy tụ hàng nghìn đầu sách hấp dẫn cùng hoạt động quyên góp sách cũ giúp đỡ trẻ em nghèo.")
                .noiDung("Nhằm tôn vinh văn hóa đọc trong thời đại công nghệ số, CLB Sách & Hành động phối hợp cùng Thư viện PTIT tổ chức Ngày hội sách 2026. Sự kiện diễn ra từ ngày 10/06 đến 12/06 tại sân nhà A2, với sự góp mặt của hơn 20 nhà xuất bản lớn, ưu đãi giảm giá lên tới 50% cho sinh viên và hoạt động đổi sách cũ lấy cây xanh.")
                .hinhAnh("/tai-nguyen/news-default.png")
                .danhMucTin("Sự kiện")
                .luotXem(412)
                .tacGia(admin)
                .build());

        tinTucRepository.save(TinTuc.builder()
                .tieuDe("Kế hoạch nâng cấp và bảo trì hệ thống Thư viện điện tử")
                .tomTat("Thư viện sẽ tiến hành bảo trì cổng tra cứu điện tử từ 22h thứ Bảy đến 4h sáng Chủ Nhật tuần này.")
                .noiDung("Để nâng cao chất lượng dịch vụ tra cứu trực tuyến và bảo mật thông tin, Ban quản lý Thư viện sẽ tiến hành nâng cấp định kỳ hệ thống máy chủ cơ sở dữ liệu. Trong thời gian bảo trì, dịch vụ đăng ký mượn sách trực tuyến và đọc ebook qua ứng dụng sẽ tạm thời gián đoạn. Rất mong các bạn sinh viên sắp xếp kế hoạch học tập phù hợp.")
                .hinhAnh("/tai-nguyen/news-default.png")
                .danhMucTin("Thông báo")
                .luotXem(89)
                .tacGia(admin)
                .build());

        tinTucRepository.save(TinTuc.builder()
                .tieuDe("Vinh danh các sinh viên PTIT đạt giải cao trong Kỳ thi Olympic Tin học toàn quốc")
                .tomTat("Sinh viên khoa CNTT của Học viện Công nghệ Bưu chính Viễn thông vừa đạt thành tích xuất sắc tại kỳ thi Olympic Tin học sinh viên toàn quốc năm nay.")
                .noiDung("Đội tuyển Olympic Tin học của Học viện đã xuất sắc giành được 2 giải Nhất cá nhân, 3 giải Nhì và vị trí Top 3 toàn đoàn. Đây là minh chứng cho tinh thần học tập và năng lực nghiên cứu không ngừng nghỉ của thầy và trò PTIT. Thư viện Học viện cũng đã cập nhật thêm nhiều tài liệu chuyên sâu phục vụ các kỳ thi học thuật.")
                .hinhAnh("/tai-nguyen/news-default.png")
                .danhMucTin("Học thuật")
                .luotXem(245)
                .tacGia(admin)
                .build());

        tinTucRepository.save(TinTuc.builder()
                .tieuDe("Ra mắt câu lạc bộ Sách và Hành động PTIT thế hệ mới")
                .tomTat("Câu lạc bộ chính thức khởi động chiến dịch tuyển thành viên lớn nhất trong năm nhằm lan tỏa văn hóa đọc tại giảng đường.")
                .noiDung("CLB Sách và Hành động PTIT vừa chính thức công bố đợt tuyển quân quy mô lớn với nhiều hoạt động hấp dẫn như: Ngày hội chia sẻ sách, Talkshow với các tác giả nổi tiếng, dự án quyên góp tủ sách vùng cao. Đăng ký tham gia ngay để trở thành một phần của cộng đồng yêu sách PTIT.")
                .hinhAnh("/tai-nguyen/news-default.png")
                .danhMucTin("Đời sống")
                .luotXem(198)
                .tacGia(admin)
                .build());

        tinTucRepository.save(TinTuc.builder()
                .tieuDe("Thư viện PTIT bổ sung hơn 1000 đầu sách ngoại văn mới nhập khẩu")
                .tomTat("Nhằm nâng cao năng lực tự học và hội nhập quốc tế, thư viện vừa nhập khẩu loạt giáo trình chuyên ngành bằng tiếng Anh từ các NXB lớn.")
                .noiDung("Bộ sưu tập sách mới bao gồm các lĩnh vực mũi nhọn như Trí tuệ nhân tạo, Khoa học dữ liệu, IoT, An toàn thông tin và Quản trị kinh doanh từ các nhà xuất bản hàng đầu như Pearson, Wiley, Springer. Sinh viên có thể tra cứu và mượn trực tiếp tại quầy thủ thư từ ngày mai.")
                .hinhAnh("/tai-nguyen/news-default.png")
                .danhMucTin("Thông báo")
                .luotXem(156)
                .tacGia(admin)
                .build());

        tinTucRepository.save(TinTuc.builder()
                .tieuDe("Bí quyết ôn thi hiệu quả tại thư viện trong mùa thi học kỳ")
                .tomTat("Cùng bỏ túi những mẹo nhỏ giúp các bạn sinh viên tập trung tối đa và đạt kết quả cao khi tự học tại thư viện Học viện.")
                .noiDung("Mùa thi đang đến gần, không gian thư viện luôn là lựa chọn hàng đầu của sinh viên. Bài viết chia sẻ các phương pháp quản lý thời gian Pomodoro, cách chọn vị trí ngồi đủ ánh sáng, cách tận dụng hệ thống tài liệu điện tử và lưu ý giữ gìn trật tự chung để có một kỳ ôn thi thành công rực rỡ.")
                .hinhAnh("/tai-nguyen/news-default.png")
                .danhMucTin("Học thuật")
                .luotXem(320)
                .tacGia(admin)
                .build());

        tinTucRepository.save(TinTuc.builder()
                .tieuDe("Talkshow 'Đọc sách thời đại số - Cơ hội hay thách thức?'")
                .tomTat("Buổi thảo luận sôi nổi về xu hướng đọc sách điện tử (ebook) và sách giấy trong thời đại bùng nổ công nghệ thông tin hiện nay.")
                .noiDung("Diễn ra vào sáng thứ 6 tuần tới tại hội trường A2, buổi talkshow quy tụ các diễn giả là chuyên gia công nghệ, nhà văn hóa và đại diện ban quản lý thư viện. Các bạn sinh viên sẽ được trao đổi trực tiếp về thói quen đọc sách, cách chọn lọc thông tin hữu ích trên internet và cách sử dụng hiệu quả các nền tảng đọc trực tuyến.")
                .hinhAnh("/tai-nguyen/news-default.png")
                .danhMucTin("Sự kiện")
                .luotXem(187)
                .tacGia(admin)
                .build());

        tinTucRepository.save(TinTuc.builder()
                .tieuDe("Khai mạc triển lãm bản đồ và tư liệu lịch sử biển đảo Việt Nam")
                .tomTat("Triển lãm mang lại cái nhìn chân thực, khoa học và khơi dậy lòng tự hào dân tộc, tình yêu quê hương đất nước trong mỗi sinh viên.")
                .noiDung("Phối hợp cùng Bảo tàng Lịch sử Quốc gia, Thư viện PTIT tổ chức trưng bày hơn 100 hiện vật, bản đồ cổ khẳng định chủ quyền của Việt Nam đối với hai quần đảo Hoàng Sa và Trường Sa. Triển lãm mở cửa tự do phục vụ cán bộ, giảng viên và sinh viên đến hết tuần này.")
                .hinhAnh("/tai-nguyen/news-default.png")
                .danhMucTin("Sự kiện")
                .luotXem(142)
                .tacGia(admin)
                .build());

        tinTucRepository.save(TinTuc.builder()
                .tieuDe("Chia sẻ trải nghiệm học tập của thủ khoa đầu ra PTIT khóa D20")
                .tomTat("Gặp gỡ cựu sinh viên xuất sắc chia sẻ về hành trình chinh phục điểm GPA tuyệt đối và cách khai thác kho tài liệu thư viện.")
                .noiDung("Bạn Trần Minh Hoàng, thủ khoa tốt nghiệp xuất sắc ngành Khoa học máy tính khóa D20 chia sẻ: 'Thư viện PTIT chính là ngôi nhà thứ hai của mình. Nơi đây không chỉ có không gian yên tĩnh tuyệt đối để tập trung mà còn sở hữu kho sách chuyên ngành cực kỳ phong phú và cập nhật'. Hoàng cũng đưa ra lời khuyên quý báu về cách nghiên cứu các bài báo khoa học trên IEEE.")
                .hinhAnh("/tai-nguyen/news-default.png")
                .danhMucTin("Đời sống")
                .luotXem(512)
                .tacGia(admin)
                .build());

        System.out.println("[Seeder] Đã tạo mới các bản tin mẫu.");
    }

    private void seedEvents() {
        suKienRepository.deleteAll();

        suKienRepository.save(SuKien.builder()
                .tieuDe("Triển lãm Sách khoa học công nghệ và Chuyển đổi số")
                .moTa("Trưng bày và giới thiệu các tựa sách mới nhất về Trí tuệ nhân tạo (AI), IoT, Blockchain và Big Data từ các nhà xuất bản uy tín thế giới.")
                .hinhAnh("hoi_sach.webp")
                .ngayBatDau(LocalDate.now().plusDays(10))
                .ngayKetThuc(LocalDate.now().plusDays(12))
                .gioBatDau(LocalTime.of(8, 30))
                .gioKetThuc(LocalTime.of(17, 30))
                .diaDiem("Sảnh tầng 1 nhà A2 - Học viện Công nghệ Bưu chính Viễn thông")
                .trangThai(SuKien.TrangThaiSuKien.SAP_DIEN_RA)
                .build());

        suKienRepository.save(SuKien.builder()
                .tieuDe("Workshop: Kỹ năng nghiên cứu khoa học và khai thác tài nguyên số hiệu quả")
                .moTa("Hướng dẫn các bạn sinh viên khóa mới cách tìm kiếm, trích dẫn tài liệu tham khảo và sử dụng cơ sở dữ liệu IEEE, Springer Link phục vụ làm đồ án, bài báo khoa học.")
                .hinhAnh("News4.jpg")
                .ngayBatDau(LocalDate.now().minusDays(1))
                .ngayKetThuc(LocalDate.now().plusDays(1))
                .gioBatDau(LocalTime.of(14, 0))
                .gioKetThuc(LocalTime.of(16, 30))
                .diaDiem("Phòng Hội thảo 2 - Nhà C")
                .trangThai(SuKien.TrangThaiSuKien.DANG_DIEN_RA)
                .build());

        suKienRepository.save(SuKien.builder()
                .tieuDe("Cuộc thi 'Đại sứ Văn hóa đọc PTIT 2026'")
                .moTa("Cuộc thi nhằm tìm kiếm những gương mặt sinh viên tiêu biểu có đam mê đọc sách và có những sáng kiến, hoạt động thiết thực nhằm lan tỏa văn hóa đọc trong cộng đồng Học viện. Thí sinh tham gia sẽ viết bài chia sẻ hoặc quay video giới thiệu cuốn sách yêu thích. Giải thưởng vô cùng hấp dẫn đang chờ đón!")
                .hinhAnh("News4.jpg")
                .ngayBatDau(LocalDate.now().plusDays(5))
                .ngayKetThuc(LocalDate.now().plusDays(25))
                .gioBatDau(LocalTime.of(8, 0))
                .gioKetThuc(LocalTime.of(17, 0))
                .diaDiem("Hội trường A2 - Học viện Công nghệ Bưu chính Viễn thông")
                .trangThai(SuKien.TrangThaiSuKien.SAP_DIEN_RA)
                .build());

        suKienRepository.save(SuKien.builder()
                .tieuDe("Ngày hội Đổi sách lấy cây xanh - Bảo vệ môi trường")
                .moTa("Hoạt động ý nghĩa do CLB Sách và Hành động phối hợp cùng Đoàn Thanh niên tổ chức. Sinh viên mang sách giáo trình cũ, truyện tranh, tiểu thuyết không dùng đến để đổi lấy những chậu cây sen đá, xương rồng nhỏ xinh trang trí góc học tập. Toàn bộ sách thu gom sẽ được phân loại quyên góp cho trẻ em nghèo vùng cao.")
                .hinhAnh("hoi_sach.webp")
                .ngayBatDau(LocalDate.now().plusDays(15))
                .ngayKetThuc(LocalDate.now().plusDays(16))
                .gioBatDau(LocalTime.of(9, 0))
                .gioKetThuc(LocalTime.of(16, 30))
                .diaDiem("Khuôn viên sân nhà A2")
                .trangThai(SuKien.TrangThaiSuKien.SAP_DIEN_RA)
                .build());

        suKienRepository.save(SuKien.builder()
                .tieuDe("Buổi giới thiệu sách: 'Làm chủ Trí tuệ nhân tạo thế hệ mới'")
                .moTa("Tác giả cuốn sách bán chạy cùng các chuyên gia hàng đầu về AI sẽ có buổi giao lưu, chia sẻ về cách áp dụng ChatGPT, Midjourney và các công cụ AI vào học tập và công việc hàng ngày. Cơ hội nhận sách có chữ ký trực tiếp từ tác giả.")
                .hinhAnh("News4.jpg")
                .ngayBatDau(LocalDate.now())
                .ngayKetThuc(LocalDate.now().plusDays(1))
                .gioBatDau(LocalTime.of(14, 0))
                .gioKetThuc(LocalTime.of(17, 0))
                .diaDiem("Phòng hội thảo đa năng tầng 2 - Thư viện PTIT")
                .trangThai(SuKien.TrangThaiSuKien.DANG_DIEN_RA)
                .build());

        suKienRepository.save(SuKien.builder()
                .tieuDe("Đại hội CLB Sách và Hành động nhiệm kỳ 2026-2027")
                .moTa("Tổng kết các hoạt động ý nghĩa trong nhiệm kỳ vừa qua, tuyên dương các thành viên có đóng góp xuất sắc và bầu ra Ban điều hành mới đầy nhiệt huyết để tiếp tục dẫn dắt CLB phát triển mạnh mẽ hơn.")
                .hinhAnh("hoi_sach.webp")
                .ngayBatDau(LocalDate.now().minusDays(10))
                .ngayKetThuc(LocalDate.now().minusDays(10))
                .gioBatDau(LocalTime.of(18, 30))
                .gioKetThuc(LocalTime.of(21, 30))
                .diaDiem("Phòng sinh hoạt chung nhà B2")
                .trangThai(SuKien.TrangThaiSuKien.DA_KET_THUC)
                .build());

        suKienRepository.save(SuKien.builder()
                .tieuDe("Lớp học trải nghiệm: Thiết kế và in 3D cơ bản")
                .moTa("Cơ hội trải nghiệm công nghệ in 3D hiện đại ngay tại Maker Space của thư viện. Sinh viên được hướng dẫn tự vẽ mô hình 3D đơn giản và vận hành máy in để tạo ra sản phẩm thực tế của riêng mình.")
                .hinhAnh("News4.jpg")
                .ngayBatDau(LocalDate.now().minusDays(5))
                .ngayKetThuc(LocalDate.now().minusDays(4))
                .gioBatDau(LocalTime.of(8, 30))
                .gioKetThuc(LocalTime.of(11, 30))
                .diaDiem("Không gian sáng chế Maker Space - Tầng 3 Thư viện")
                .trangThai(SuKien.TrangThaiSuKien.DA_KET_THUC)
                .build());

        suKienRepository.save(SuKien.builder()
                .tieuDe("Tuần lễ Tri ân và Trao giải Bạn đọc tích cực 2025")
                .moTa("Chương trình thường niên nhằm vinh danh các bạn sinh viên và thầy cô giảng viên có tần suất mượn sách và thời gian tự học tại thư viện nhiều nhất trong năm học vừa qua. Nhiều phần quà giá trị như học bổng, máy đọc sách Kindle đã được trao tặng.")
                .hinhAnh("hoi_sach.webp")
                .ngayBatDau(LocalDate.now().minusDays(30))
                .ngayKetThuc(LocalDate.now().minusDays(28))
                .gioBatDau(LocalTime.of(9, 0))
                .gioKetThuc(LocalTime.of(11, 0))
                .diaDiem("Hội trường A2")
                .trangThai(SuKien.TrangThaiSuKien.DA_KET_THUC)
                .build());

        System.out.println("[Seeder] Đã tạo mới các sự kiện mẫu.");
    }

    private void seedNotifications() {
        thongBaoRepository.deleteAll();

        thongBaoRepository.save(ThongBao.builder()
                .tieuDe("Thông báo: Thay đổi khung giờ mở cửa phục vụ hè 2026")
                .noiDung("Kể từ ngày 05/06/2026, Thư viện PTIT sẽ chuyển sang khung giờ làm việc mùa hè: Sáng từ 7h30 đến 11h30, Chiều từ 13h30 đến 17h00 (từ thứ Hai đến thứ Sáu). Thứ Bảy và Chủ Nhật thư viện đóng cửa. Yêu cầu sinh viên lưu ý để sắp xếp thời gian mượn trả tài liệu.")
                .loaiThongBao("important")
                .luotXem(312)
                .build());

        thongBaoRepository.save(ThongBao.builder()
                .tieuDe("Yêu cầu hoàn trả sách mượn quá hạn trước kỳ thi học kỳ")
                .noiDung("Để đảm bảo quyền lợi mượn tài liệu của toàn thể sinh viên, đề nghị các bạn sinh viên đang có phiếu mượn trạng thái QUA_HAN nhanh chóng mang sách đến quầy thủ thư để trả hoặc làm thủ tục gia hạn. Sinh viên không trả sách đúng hạn sẽ bị tạm khóa tài khoản mượn và không được duyệt phiếu mượn mới.")
                .loaiThongBao("muon")
                .luotXem(450)
                .build());

        thongBaoRepository.save(ThongBao.builder()
                .tieuDe("Ra mắt cổng cơ sở dữ liệu số IEEE Xplore miễn phí cho sinh viên")
                .noiDung("Nhà trường đã hoàn tất gia hạn bản quyền truy cập hệ thống IEEE Xplore. Toàn bộ giảng viên và sinh viên PTIT có thể truy cập đọc và tải tài liệu nghiên cứu miễn phí khi kết nối mạng wifi Học viện (PTIT-Student hoặc PTIT-Staff) hoặc sử dụng tài khoản VPN do Tổ Thông tin Thư viện cung cấp.")
                .loaiThongBao("digital")
                .luotXem(615)
                .build());

        thongBaoRepository.save(ThongBao.builder()
                .tieuDe("Thông báo tuyển cộng tác viên hỗ trợ Thư viện năm học mới")
                .noiDung("Thư viện Học viện cần tuyển 10 bạn sinh viên nhiệt tình làm cộng tác viên hỗ trợ công tác sắp xếp sách, trực quầy mượn trả và hỗ trợ kỹ thuật tại phòng máy tính. Ưu tiên các bạn sinh viên có hoàn cảnh khó khăn, muốn tích lũy kinh nghiệm làm việc thực tế. Quyền lợi: hỗ trợ phụ cấp theo giờ và cộng điểm rèn luyện.")
                .loaiThongBao("event")
                .luotXem(189)
                .build());

        thongBaoRepository.save(ThongBao.builder()
                .tieuDe("Thông báo: Khai trương góc đọc sách Ngoại văn và Giao lưu quốc tế")
                .noiDung("Được sự tài trợ của Đại sứ quán Anh, thư viện chính thức khai trương Góc văn hóa và đọc sách ngoại văn tại phòng đọc tầng 2. Tại đây trưng bày hàng trăm đầu sách văn học, lịch sử, khoa học bằng tiếng Anh chuẩn. Đồng thời, đây cũng là không gian sinh hoạt của CLB tiếng Anh định kỳ vào chiều thứ 5 hàng tuần.")
                .loaiThongBao("digital")
                .luotXem(241)
                .build());

        thongBaoRepository.save(ThongBao.builder()
                .tieuDe("Cảnh báo: Tình trạng mượn thẻ thư viện và tài khoản tra cứu cá nhân")
                .noiDung("Thời gian gần đây, ban quản lý phát hiện một số trường hợp sinh viên cho người ngoài mượn thẻ sinh viên để vào thư viện hoặc chia sẻ tài khoản tra cứu tài liệu số IEEE/Springer cho các tổ chức bên ngoài. Thư viện yêu cầu nghiêm túc tuân thủ nội quy bảo mật. Mọi trường hợp vi phạm sẽ bị đình chỉ quyền sử dụng thư viện và kỷ luật trước toàn trường.")
                .loaiThongBao("important")
                .luotXem(378)
                .build());

        thongBaoRepository.save(ThongBao.builder()
                .tieuDe("Hướng dẫn quy trình gia hạn sách trực tuyến cực kỳ đơn giản")
                .noiDung("Để tránh việc bị phạt tiền do trả sách quá hạn, sinh viên có thể tự gia hạn sách trực tuyến thông qua cổng thông tin cá nhân. Mỗi cuốn sách được gia hạn tối đa 2 lần, mỗi lần thêm 7 ngày. Việc gia hạn chỉ thực hiện được khi sách chưa bị quá hạn và không có người khác đang đăng ký chờ mượn. Xem chi tiết các bước trong tài liệu hướng dẫn.")
                .loaiThongBao("muon")
                .luotXem(504)
                .build());

        thongBaoRepository.save(ThongBao.builder()
                .tieuDe("Thông báo tổ chức buổi tập huấn sử dụng thư viện điện tử cho tân sinh viên")
                .noiDung("Nhằm giúp các tân sinh viên khóa mới nhanh chóng làm quen với môi trường học tập đại học, thư viện tổ chức chuỗi tập huấn hướng dẫn sử dụng thư viện số, tìm kiếm tài liệu trên giá sách vật lý, quy định mượn trả và đăng ký mượn sách trực tuyến qua app. Buổi tập huấn là bắt buộc đối với tất cả các lớp sinh viên năm nhất.")
                .loaiThongBao("event")
                .luotXem(128)
                .build());

        System.out.println("[Seeder] Đã tạo mới các thông báo mẫu.");
    }
}


package com.ptit.thuvien.config;

import com.ptit.thuvien.model.*;
import com.ptit.thuvien.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

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

    @Override
    public void run(String... args) throws Exception {
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

    private TaiLieu createBook(String ten, String tacGia, String nxb, int nam, DanhMuc dm, int sl, String hinhAnh, String moTa) {
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
            booksToSeed.add(createBook("Lập trình Java căn bản & nâng cao", "Nguyễn Văn A", "NXB Bách Khoa", 2023, cntt, 15, "book.jpg", "Sách giáo trình giảng dạy lập trình hướng đối tượng Java tại PTIT."));
            booksToSeed.add(createBook("Cấu trúc dữ liệu và Giải thuật", "Phạm Thế Long", "NXB Giáo Dục", 2022, cntt, 20, "tai-lieu-in1.jpg", "Các thuật toán cơ bản, cấu trúc dữ liệu mảng, danh sách liên kết, cây, đồ thị."));
            booksToSeed.add(createBook("Lập trình Web với Spring Boot và Thymeleaf", "Trần Văn Bình", "NXB Thông tin & Truyền thông", 2024, cntt, 15, "tai-lieu-so1.jpg", "Hướng dẫn xây dựng các ứng dụng web doanh nghiệp hiện đại với Spring Boot."));
            booksToSeed.add(createBook("Nhập môn Trí tuệ Nhân tạo và Học máy", "TS. Lê Hoài Bắc", "NXB Khoa học và Kỹ thuật", 2023, cntt, 10, "book.jpg", "Kiến thức nền tảng về AI, học máy có giám sát và không giám sát, mạng neural."));
            booksToSeed.add(createBook("Thiết kế và Quản trị Cơ sở dữ liệu SQL", "Nguyễn Thị Minh", "NXB Bách Khoa", 2022, cntt, 18, "tai-lieu-in1.jpg", "Ngôn ngữ SQL, thiết kế lược đồ quan hệ, chuẩn hóa dữ liệu và tối ưu hóa câu lệnh."));
            booksToSeed.add(createBook("Kiến trúc phần mềm Clean Architecture", "Robert C. Martin", "NXB Lao Động", 2020, cntt, 12, "tai-lieu-so1.jpg", "Các nguyên lý thiết kế hệ thống phần mềm sạch, dễ mở rộng và kiểm thử."));
            booksToSeed.add(createBook("Lập trình di động Android nâng cao", "Bill Phillips", "NXB Trẻ", 2023, cntt, 10, "book.jpg", "Lập trình Android nâng cao với Kotlin và Jetpack Compose."));
            booksToSeed.add(createBook("Điện toán đám mây và AWS cơ bản", "Thomas Erl", "NXB Thông tin & Truyền thông", 2022, cntt, 8, "tai-lieu-in1.jpg", "Nguyên lý điện toán đám mây, kiến trúc dịch vụ AWS, EC2, S3, RDS."));
            booksToSeed.add(createBook("Nhập môn Mật mã học và An toàn thông tin", "William Stallings", "NXB Khoa học tự nhiên", 2023, cntt, 15, "tai-lieu-so1.jpg", "Mã hóa đối xứng, bất đối xứng, chữ ký số và các cơ chế bảo mật mạng cơ bản."));
            booksToSeed.add(createBook("Docker và Kubernetes cho lập trình viên", "Nigel Poulton", "NXB Trẻ", 2024, cntt, 14, "book.jpg", "Đóng gói ứng dụng dạng Container, triển khai và quản trị Kubernetes cluster."));

            // 2. Điện tử viễn thông (10 cuốn)
            booksToSeed.add(createBook("Mạng máy tính và Truyền số liệu", "Nguyễn Trung Hiếu", "NXB Thông tin & Truyền thông", 2024, dtvt, 10, "tai-lieu-so1.jpg", "Kiến thức tổng quan về các tầng trong mô hình OSI/TCP-IP và giao thức mạng."));
            booksToSeed.add(createBook("Kỹ thuật Vi mạch và Hệ thống Nhúng", "Vũ Đình Thành", "NXB Khoa học tự nhiên", 2023, dtvt, 12, "tai-lieu-so1.jpg", "Nguyên lý hoạt động của hệ thống nhúng, lập trình vi điều khiển ARM."));
            booksToSeed.add(createBook("Thiết kế mạch IoT với ESP32", "Phạm Ngọc Nam", "NXB Bách Khoa", 2024, dtvt, 15, "book.jpg", "Thực hành lập trình kết nối Wi-Fi, Bluetooth trên ESP32, đọc cảm biến."));
            booksToSeed.add(createBook("Truyền thông vô tuyến và Anten", "Đỗ Quốc Huy", "NXB Giáo dục", 2021, dtvt, 8, "tai-lieu-in1.jpg", "Lý thuyết điện từ trường, thiết kế anten và các mô hình truyền sóng vô tuyến."));
            booksToSeed.add(createBook("Xử lý tín hiệu số cơ bản", "Alan V. Oppenheim", "NXB Bách Khoa", 2020, dtvt, 11, "tai-lieu-so1.jpg", "Biến đổi Fourier, thiết kế bộ lọc số IIR, FIR và các ứng dụng xử lý tín hiệu thực tế."));
            booksToSeed.add(createBook("Cơ sở Kỹ thuật Điện tử thông tin", "Trần Xuân Việt", "NXB Giáo dục", 2022, dtvt, 10, "book.jpg", "Các linh kiện bán dẫn, mạch khuếch đại, mạch tạo dao động và biến đổi tần số."));
            booksToSeed.add(createBook("Mạng di động thế hệ mới 5G và IoT", "Harish Kumar", "NXB Khoa học tự nhiên", 2023, dtvt, 9, "tai-lieu-in1.jpg", "Kiến trúc mạng lõi 5G, truyền thông thiết bị đến thiết bị và ứng dụng trong IoT."));
            booksToSeed.add(createBook("Kỹ thuật Truyền thanh truyền hình số", "Nguyễn Kim Sách", "NXB Thông tin & Truyền thông", 2021, dtvt, 7, "tai-lieu-so1.jpg", "Nguyên lý số hóa tín hiệu âm thanh, hình ảnh và các chuẩn phát sóng DVB-T2."));
            booksToSeed.add(createBook("Thông tin sợi quang và Quang điện tử", "Gerd Keiser", "NXB Giáo dục", 2022, dtvt, 10, "book.jpg", "Truyền dẫn ánh sáng trong sợi quang, nguồn phát quang và bộ thu quang điện tử."));
            booksToSeed.add(createBook("Lập trình vi điều khiển STM32", "Carmine Noviello", "NXB Bách Khoa", 2023, dtvt, 12, "tai-lieu-in1.jpg", "Kiến trúc ARM Cortex-M, lập trình ngoại vi GPIO, USART, ADC, DMA trên STM32."));

            // 3. Kinh tế & Quản trị (10 cuốn)
            booksToSeed.add(createBook("Giáo trình Kinh tế vĩ mô", "Trần Thị C", "NXB Đại học Kinh tế Quốc dân", 2021, kinhTe, 12, "ebook.jpg", "Tìm hiểu về lạm phát, thất nghiệp, GDP, chính sách tài khóa và tiền tệ."));
            booksToSeed.add(createBook("Quản trị Học đại cương", "TS. Nguyễn Thanh Liêm", "NXB Thống kê", 2022, kinhTe, 20, "ebook.jpg", "Các chức năng quản trị cơ bản: hoạch định, tổ chức, lãnh đạo và kiểm tra."));
            booksToSeed.add(createBook("Marketing kỹ thuật số trong kỷ nguyên 4.0", "Philip Kotler", "NXB Trẻ", 2023, kinhTe, 25, "book.jpg", "Chiến lược tiếp thị số, SEO, SEM, truyền thông xã hội và phân tích dữ liệu hành vi."));
            booksToSeed.add(createBook("Phân tích báo cáo tài chính doanh nghiệp", "Nguyễn Minh Kiều", "NXB Tài chính", 2024, kinhTe, 15, "ebook.jpg", "Phương pháp đọc hiểu và phân tích bảng cân đối kế toán, báo cáo kết quả kinh doanh."));
            booksToSeed.add(createBook("Khởi nghiệp đổi mới sáng tạo", "Eric Ries", "NXB Tổng hợp TP.HCM", 2021, kinhTe, 18, "book.jpg", "Phương pháp khởi nghiệp tinh gọn giúp tối ưu nguồn lực và kiểm chứng giả thuyết."));
            booksToSeed.add(createBook("Quản trị chuỗi cung ứng hiện đại", "Sunil Chopra", "NXB Thống kê", 2022, kinhTe, 10, "ebook.jpg", "Logistics, quản trị tồn kho và điều phối trong chuỗi cung ứng toàn cầu."));
            booksToSeed.add(createBook("Hành vi tổ chức trong doanh nghiệp", "Stephen P. Robbins", "NXB Trẻ", 2023, kinhTe, 15, "book.jpg", "Động lực làm việc, giao tiếp nội bộ, xung đột và văn hóa tổ chức doanh nghiệp."));
            booksToSeed.add(createBook("Kinh tế học vi mô căn bản", "N. Gregory Mankiw", "NXB Hồng Đức", 2022, kinhTe, 20, "ebook.jpg", "Cung cầu, thị trường cạnh tranh, độc quyền và hành vi người tiêu dùng, doanh nghiệp."));
            booksToSeed.add(createBook("Quản trị nguồn nhân lực", "Gary Dessler", "NXB Tài chính", 2021, kinhTe, 12, "book.jpg", "Tuyển dụng, đào tạo, đánh giá hiệu quả công việc và các chính sách đãi ngộ nhân sự."));
            booksToSeed.add(createBook("Tài chính doanh nghiệp chuyên sâu", "Stephen A. Ross", "NXB Thống kê", 2023, kinhTe, 10, "ebook.jpg", "Hoạch định ngân sách vốn đầu tư, chi phí sử dụng vốn và cấu trúc vốn doanh nghiệp."));

            // 4. Kỹ năng mềm (10 cuốn)
            booksToSeed.add(createBook("Đắc Nhân Tâm (How to Win Friends & Influence People)", "Dale Carnegie", "NXB Trẻ", 2020, kyNang, 30, "book.jpg", "Cuốn sách kinh điển về nghệ thuật ứng xử, giao tiếp và thu phục lòng người."));
            booksToSeed.add(createBook("Tư duy ngược - Thay đổi cách nghĩ để thành công", "Nguyễn Anh Dũng", "NXB Lao động", 2022, kyNang, 30, "book.jpg", "Khai phá tư duy đột phá, giải quyết vấn đề từ các góc nhìn khác biệt."));
            booksToSeed.add(createBook("Kỹ năng thuyết trình và làm việc nhóm hiệu quả", "Lê Huy Khoa", "NXB Tổng hợp TP.HCM", 2023, kyNang, 22, "ebook.jpg", "Phương pháp chuẩn bị slide thuyết trình, kiểm soát giọng nói, tương tác khán giả."));
            booksToSeed.add(createBook("Quản lý thời gian và giải quyết vấn đề", "Brian Tracy", "NXB Hồng Đức", 2021, kyNang, 25, "book.jpg", "Quy tắc 80/20, thiết lập mục tiêu hàng ngày, tập trung vào nhiệm vụ trọng tâm."));
            booksToSeed.add(createBook("Đọc vị bất kỳ ai để không bị lợi dụng", "David J. Lieberman", "NXB Trẻ", 2020, kyNang, 20, "ebook.jpg", "Các kỹ thuật tâm lý giúp nhận diện cảm xúc, suy nghĩ và ý đồ thực sự."));
            booksToSeed.add(createBook("Người tối giản - Hành trình tìm lại bản thân", "Sasaki Fumio", "NXB Lao động", 2022, kyNang, 15, "book.jpg", "Lối sống tối giản, giảm bớt đồ đạc dư thừa để tập trung vào giá trị tinh thần."));
            booksToSeed.add(createBook("Tư duy nhanh và chậm", "Daniel Kahneman", "NXB Thế giới", 2021, kyNang, 18, "ebook.jpg", "Hai hệ thống tư duy chi phối hành vi: hệ thống 1 (nhanh) và hệ thống 2 (chậm)."));
            booksToSeed.add(createBook("7 Thói quen của bạn trẻ thành đạt", "Sean Covey", "NXB Trẻ", 2022, kyNang, 28, "book.jpg", "Xây dựng thói quen tích cực, quản lý bản thân, hợp tác và làm mới tâm hồn."));
            booksToSeed.add(createBook("Nói thế nào để trẻ nghe lời và lắng nghe thế nào", "Adele Faber", "NXB Lao động", 2020, kyNang, 12, "ebook.jpg", "Nghệ thuật giao tiếp trong gia đình, xây dựng sự tin cậy và hiểu biết lẫn nhau."));
            booksToSeed.add(createBook("Lối tư duy của người thành công", "Carol S. Dweck", "NXB Trẻ", 2021, kyNang, 20, "book.jpg", "Sức mạnh của tư duy phát triển (growth mindset) so với tư duy cố định."));

            // 5. Khoa học cơ bản (10 cuốn)
            booksToSeed.add(createBook("Toán cao cấp cho kỹ sư và cử nhân", "Nguyễn Đình Trí", "NXB Giáo dục", 2020, khoaHoc, 40, "tai-lieu-in1.jpg", "Giải tích toán học một biến, nhiều biến, phép tính vi phân và tích phân."));
            booksToSeed.add(createBook("Vật lý đại cương - Điện và Từ học", "Lương Duyên Bình", "NXB Giáo dục", 2021, khoaHoc, 35, "book.jpg", "Trường tĩnh điện, vật dẫn, điện môi, từ trường tĩnh, cảm ứng điện từ."));
            booksToSeed.add(createBook("Nhập môn Logic học và Phương pháp luận", "Nguyễn Đức Dân", "NXB Khoa học Xã hội", 2022, khoaHoc, 20, "ebook.jpg", "Cơ sở tư duy logic hình thức, các quy luật logic cơ bản, phương pháp luận."));
            booksToSeed.add(createBook("Đại số tuyến tính và Hình học giải tích", "Nguyễn Hữu Việt Hưng", "NXB Giáo dục", 2021, khoaHoc, 30, "tai-lieu-in1.jpg", "Không gian vector, ma trận, định thức, hệ phương trình tuyến tính và dạng toàn phương."));
            booksToSeed.add(createBook("Hóa học đại cương và ứng dụng", "Nguyễn Đức Chung", "NXB Bách Khoa", 2020, khoaHoc, 25, "book.jpg", "Cấu tạo chất, nhiệt động hóa học, động hóa học, cân bằng hóa học và điện hóa học."));
            booksToSeed.add(createBook("Giáo trình Xác suất và Thống kê toán", "Tống Đình Quỳ", "NXB Bách Khoa", 2022, khoaHoc, 28, "tai-lieu-in1.jpg", "Biến ngẫu nhiên, quy luật phân phối xác suất, ước lượng và kiểm định giả thuyết."));
            booksToSeed.add(createBook("Vật lý đại cương - Cơ và Nhiệt học", "Lương Duyên Bình", "NXB Giáo dục", 2021, khoaHoc, 32, "book.jpg", "Cơ học chất điểm, hệ chất điểm, vật rắn, thuyết động học phân tử và nhiệt động."));
            booksToSeed.add(createBook("Lịch sử các học thuyết kinh tế học", "Nguyễn Minh Tuấn", "NXB Tài chính", 2020, khoaHoc, 15, "ebook.jpg", "Quá trình hình thành và phát triển các hệ tư tưởng kinh tế học từ cổ đại đến hiện đại."));
            booksToSeed.add(createBook("Nhập môn Triết học đại cương", "Bộ Giáo dục và Đào tạo", "NXB Chính trị Quốc gia", 2021, khoaHoc, 30, "book.jpg", "Các khái niệm triết học cơ bản, chủ nghĩa duy vật biện chứng và duy vật lịch sử."));
            booksToSeed.add(createBook("Xác suất thống kê và quy hoạch thực nghiệm", "Phạm Hải Tùng", "NXB Bách Khoa", 2023, khoaHoc, 18, "tai-lieu-in1.jpg", "Xử lý số liệu thực nghiệm, thiết kế thí nghiệm và phân tích hồi quy tương quan."));

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
                            .tienPhat(6000L) // Phạt 6 ngày trễ (1000đ/ngày)
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
        if (tinTucRepository.count() == 0) {
            tinTucRepository.save(TinTuc.builder()
                    .tieuDe("Khai trương không gian học tập số PTIT hiện đại bậc nhất")
                    .tomTat("Nhằm đáp ứng nhu cầu nghiên cứu học tập, Học viện Công nghệ Bưu chính Viễn thông vừa khánh thành không gian học tập số hiện đại.")
                    .noiDung("Học viện Công nghệ Bưu chính Viễn thông đã chính thức đưa vào hoạt động khu phức hợp Thư viện số và Không gian tự học mới tại tầng 2 nhà A2. Với thiết kế mở, đầy đủ ánh sáng, trang bị hệ thống máy tính cấu hình cao, điều hòa 24/7 và hệ thống tra cứu sách tự động QR, đây hứa hẹn sẽ là địa điểm lý tưởng thu hút hàng nghìn sinh viên PTIT mỗi ngày.")
                    .hinhAnh("News1.jpg")
                    .danhMucTin("Thông báo")
                    .luotXem(124)
                    .tacGia(admin)
                    .build());

            tinTucRepository.save(TinTuc.builder()
                    .tieuDe("Hội sách Sinh viên PTIT 2026 chính thức khởi động")
                    .tomTat("Ngày hội sách thường niên quy tụ hàng nghìn đầu sách hấp dẫn cùng hoạt động quyên góp sách cũ giúp đỡ trẻ em nghèo.")
                    .noiDung("Nhằm tôn vinh văn hóa đọc trong thời đại công nghệ số, CLB Sách & Hành động phối hợp cùng Thư viện PTIT tổ chức Ngày hội sách 2026. Sự kiện diễn ra từ ngày 10/06 đến 12/06 tại sân nhà A2, với sự góp mặt của hơn 20 nhà xuất bản lớn, ưu đãi giảm giá lên tới 50% cho sinh viên và hoạt động đổi sách cũ lấy cây xanh.")
                    .hinhAnh("News2.jpg")
                    .danhMucTin("Sự kiện")
                    .luotXem(412)
                    .tacGia(admin)
                    .build());

            tinTucRepository.save(TinTuc.builder()
                    .tieuDe("Kế hoạch nâng cấp và bảo trì hệ thống Thư viện điện tử")
                    .tomTat("Thư viện sẽ tiến hành bảo trì cổng tra cứu điện tử từ 22h thứ Bảy đến 4h sáng Chủ Nhật tuần này.")
                    .noiDung("Để nâng cao chất lượng dịch vụ tra cứu trực tuyến và bảo mật thông tin, Ban quản lý Thư viện sẽ tiến hành nâng cấp định kỳ hệ thống máy chủ cơ sở dữ liệu. Trong thời gian bảo trì, dịch vụ đăng ký mượn sách trực tuyến và đọc ebook qua ứng dụng sẽ tạm thời gián đoạn. Rất mong các bạn sinh viên sắp xếp kế hoạch học tập phù hợp.")
                    .hinhAnh("News3.jpg")
                    .danhMucTin("Thông báo")
                    .luotXem(89)
                    .tacGia(admin)
                    .build());

            System.out.println("[Seeder] Đã tạo mới các bản tin mẫu.");
        }
    }

    private void seedEvents() {
        if (suKienRepository.count() == 0) {
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

            System.out.println("[Seeder] Đã tạo mới các sự kiện mẫu.");
        }
    }

    private void seedNotifications() {
        if (thongBaoRepository.count() == 0) {
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

            System.out.println("[Seeder] Đã tạo mới các thông báo mẫu.");
        }
    }
}


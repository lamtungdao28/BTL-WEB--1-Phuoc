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

        // Thêm 10 Sinh viên mới
        createStudentIfNotExist("nguoidung01", "Nguyễn Văn Nam", "namnv.d21@student.ptit.edu.vn", "0981111111", "D21CQCN02-B");
        createStudentIfNotExist("nguoidung02", "Trần Thị Hoa", "hoatt.d21@student.ptit.edu.vn", "0982222222", "D21CQCN01-B");
        createStudentIfNotExist("nguoidung03", "Lê Hoàng Long", "longlh.d21@student.ptit.edu.vn", "0983333333", "D21CQCN03-B");
        createStudentIfNotExist("nguoidung04", "Phạm Minh Đức", "ducpm.d20@student.ptit.edu.vn", "0984444444", "D20CQVT01-A");
        createStudentIfNotExist("nguoidung05", "Đỗ Thùy Linh", "linhdt.d20@student.ptit.edu.vn", "0985555555", "D20CQVT02-B");
        createStudentIfNotExist("nguoidung06", "Hoàng Anh Tuấn", "tuanha.d22@student.ptit.edu.vn", "0986666666", "D22CQCN04-A");
        createStudentIfNotExist("nguoidung07", "Vũ Mai Phương", "phuongvm.d22@student.ptit.edu.vn", "0987777777", "D22CQCN02-B");
        createStudentIfNotExist("nguoidung08", "Bùi Tiến Dũng", "dungbt.d21@student.ptit.edu.vn", "0988888888", "D21CQKT01-A");
        createStudentIfNotExist("nguoidung09", "Nguyễn Thanh Hà", "hant.d21@student.ptit.edu.vn", "0989999999", "D21CQKT02-B");
        createStudentIfNotExist("nguoidung10", "Phan Quốc Anh", "anhpq.d20@student.ptit.edu.vn", "0980000000", "D20CQCN05-B");

        // Thêm 5 Giảng viên mới
        createTeacherIfNotExist("giangvien01", "PGS.TS. Nguyễn Mạnh Hùng", "hungnm@ptit.edu.vn", "0911111222", "Khoa CNTT");
        createTeacherIfNotExist("giangvien02", "TS. Lê Thị Thảo", "thaolt@ptit.edu.vn", "0912222333", "Khoa CNTT");
        createTeacherIfNotExist("giangvien03", "ThS. Nguyễn Quang Huy", "huynq@ptit.edu.vn", "0913333444", "Khoa ĐTVT");
        createTeacherIfNotExist("giangvien04", "TS. Phạm Thanh Sơn", "sonpt@ptit.edu.vn", "0914444555", "Khoa Cơ bản");
        createTeacherIfNotExist("giangvien05", "ThS. Đỗ Thị Quỳnh", "quynhdt@ptit.edu.vn", "0915555666", "Khoa QTKD");

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

            // Danh sách sách mẫu cần seed (5 cuốn cũ và 15 cuốn mới)
            List<TaiLieu> booksToSeed = new ArrayList<>();

            // 5 cuốn sách cũ
            booksToSeed.add(TaiLieu.builder()
                    .tenTaiLieu("Lập trình Java căn bản & nâng cao")
                    .tacGia("Nguyễn Văn A")
                    .nxb("NXB Bách Khoa")
                    .namXuatBan(2023)
                    .danhMuc(cntt)
                    .soLuong(15)
                    .soLuongCon(12)
                    .hinhAnh("book.jpg")
                    .moTa("Sách giáo trình giảng dạy lập trình hướng đối tượng Java tại PTIT.")
                    .build());

            booksToSeed.add(TaiLieu.builder()
                    .tenTaiLieu("Cấu trúc dữ liệu và Giải thuật")
                    .tacGia("Phạm Thế Long")
                    .nxb("NXB Giáo Dục")
                    .namXuatBan(2022)
                    .danhMuc(cntt)
                    .soLuong(20)
                    .soLuongCon(20)
                    .hinhAnh("tai-lieu-in1.jpg")
                    .moTa("Các thuật toán cơ bản, cấu trúc dữ liệu mảng, danh sách liên kết, cây, đồ thị.")
                    .build());

            booksToSeed.add(TaiLieu.builder()
                    .tenTaiLieu("Mạng máy tính và Truyền số liệu")
                    .tacGia("Nguyễn Trung Hiếu")
                    .nxb("NXB Thông tin & Truyền thông")
                    .namXuatBan(2024)
                    .danhMuc(dtvt)
                    .soLuong(10)
                    .soLuongCon(8)
                    .hinhAnh("tai-lieu-so1.jpg")
                    .moTa("Kiến thức tổng quan về các tầng trong mô hình OSI/TCP-IP và giao thức mạng.")
                    .build());

            booksToSeed.add(TaiLieu.builder()
                    .tenTaiLieu("Giáo trình Kinh tế vĩ mô")
                    .tacGia("Trần Thị C")
                    .nxb("NXB Đại học Kinh tế Quốc dân")
                    .namXuatBan(2021)
                    .danhMuc(kinhTe)
                    .soLuong(12)
                    .soLuongCon(12)
                    .hinhAnh("ebook.jpg")
                    .moTa("Tìm hiểu về lạm phát, thất nghiệp, GDP, chính sách tài khóa và tiền tệ.")
                    .build());

            booksToSeed.add(TaiLieu.builder()
                    .tenTaiLieu("Đắc Nhân Tâm (How to Win Friends & Influence People)")
                    .tacGia("Dale Carnegie")
                    .nxb("NXB Trẻ")
                    .namXuatBan(2020)
                    .danhMuc(kyNang)
                    .soLuong(30)
                    .soLuongCon(29)
                    .hinhAnh("book.jpg")
                    .moTa("Cuốn sách kinh điển về nghệ thuật ứng xử, giao tiếp và thu phục lòng người.")
                    .build());

            // 15 cuốn sách mới thêm
            // CNTT (3 cuốn mới)
            booksToSeed.add(TaiLieu.builder()
                    .tenTaiLieu("Lập trình Web với Spring Boot và Thymeleaf")
                    .tacGia("Trần Văn Bình")
                    .nxb("NXB Thông tin & Truyền thông")
                    .namXuatBan(2024)
                    .danhMuc(cntt)
                    .soLuong(15)
                    .soLuongCon(15)
                    .hinhAnh("tai-lieu-so1.jpg")
                    .moTa("Hướng dẫn xây dựng các ứng dụng web doanh nghiệp hiện đại với Spring Boot và Thymeleaf template engine.")
                    .build());

            booksToSeed.add(TaiLieu.builder()
                    .tenTaiLieu("Nhập môn Trí tuệ Nhân tạo và Học máy")
                    .tacGia("TS. Lê Hoài Bắc")
                    .nxb("NXB Khoa học và Kỹ thuật")
                    .namXuatBan(2023)
                    .danhMuc(cntt)
                    .soLuong(10)
                    .soLuongCon(10)
                    .hinhAnh("book.jpg")
                    .moTa("Kiến thức nền tảng về AI, học máy có giám sát và không giám sát, mạng neural nhân tạo.")
                    .build());

            booksToSeed.add(TaiLieu.builder()
                    .tenTaiLieu("Thiết kế và Quản trị Cơ sở dữ liệu SQL")
                    .tacGia("Nguyễn Thị Minh")
                    .nxb("NXB Bách Khoa")
                    .namXuatBan(2022)
                    .danhMuc(cntt)
                    .soLuong(18)
                    .soLuongCon(18)
                    .hinhAnh("tai-lieu-in1.jpg")
                    .moTa("Ngôn ngữ SQL, thiết kế lược đồ quan hệ, chuẩn hóa dữ liệu và tối ưu hóa câu lệnh truy vấn.")
                    .build());

            // ĐTVT (3 cuốn mới)
            booksToSeed.add(TaiLieu.builder()
                    .tenTaiLieu("Kỹ thuật Vi mạch và Hệ thống Nhúng")
                    .tacGia("Vũ Đình Thành")
                    .nxb("NXB Khoa học tự nhiên")
                    .namXuatBan(2023)
                    .danhMuc(dtvt)
                    .soLuong(12)
                    .soLuongCon(12)
                    .hinhAnh("tai-lieu-so1.jpg")
                    .moTa("Nguyên lý hoạt động của hệ thống nhúng, lập trình vi điều khiển ARM và thiết kế vi mạch chuyên dụng.")
                    .build());

            booksToSeed.add(TaiLieu.builder()
                    .tenTaiLieu("Thiết kế mạch IoT với ESP32")
                    .tacGia("Phạm Ngọc Nam")
                    .nxb("NXB Bách Khoa")
                    .namXuatBan(2024)
                    .danhMuc(dtvt)
                    .soLuong(15)
                    .soLuongCon(15)
                    .hinhAnh("book.jpg")
                    .moTa("Thực hành lập trình kết nối Wi-Fi, Bluetooth trên ESP32, đọc cảm biến và gửi dữ liệu lên Cloud server.")
                    .build());

            booksToSeed.add(TaiLieu.builder()
                    .tenTaiLieu("Truyền thông vô tuyến và Anten")
                    .tacGia("Đỗ Quốc Huy")
                    .nxb("NXB Giáo dục")
                    .namXuatBan(2021)
                    .danhMuc(dtvt)
                    .soLuong(8)
                    .soLuongCon(8)
                    .hinhAnh("tai-lieu-in1.jpg")
                    .moTa("Lý thuyết điện từ trường, thiết kế anten và các mô hình truyền sóng trong hệ thống thông tin di động.")
                    .build());

            // Kinh tế (3 cuốn mới)
            booksToSeed.add(TaiLieu.builder()
                    .tenTaiLieu("Quản trị Học đại cương")
                    .tacGia("TS. Nguyễn Thanh Liêm")
                    .nxb("NXB Thống kê")
                    .namXuatBan(2022)
                    .danhMuc(kinhTe)
                    .soLuong(20)
                    .soLuongCon(20)
                    .hinhAnh("ebook.jpg")
                    .moTa("Các chức năng quản trị cơ bản: hoạch định, tổ chức, lãnh đạo và kiểm tra trong tổ chức doanh nghiệp.")
                    .build());

            booksToSeed.add(TaiLieu.builder()
                    .tenTaiLieu("Marketing kỹ thuật số trong kỷ nguyên 4.0")
                    .tacGia("Philip Kotler")
                    .nxb("NXB Trẻ")
                    .namXuatBan(2023)
                    .danhMuc(kinhTe)
                    .soLuong(25)
                    .soLuongCon(25)
                    .hinhAnh("book.jpg")
                    .moTa("Chiến lược tiếp thị số, SEO, SEM, truyền thông xã hội và phân tích dữ liệu hành vi khách hàng trực tuyến.")
                    .build());

            booksToSeed.add(TaiLieu.builder()
                    .tenTaiLieu("Phân tích báo cáo tài chính doanh nghiệp")
                    .tacGia("Nguyễn Minh Kiều")
                    .nxb("NXB Tài chính")
                    .namXuatBan(2024)
                    .danhMuc(kinhTe)
                    .soLuong(15)
                    .soLuongCon(15)
                    .hinhAnh("ebook.jpg")
                    .moTa("Phương pháp đọc hiểu và phân tích bảng cân đối kế toán, báo cáo kết quả kinh doanh và lưu chuyển tiền tệ.")
                    .build());

            // Kỹ năng mềm (3 cuốn mới)
            booksToSeed.add(TaiLieu.builder()
                    .tenTaiLieu("Tư duy ngược - Thay đổi cách nghĩ để thành công")
                    .tacGia("Nguyễn Anh Dũng")
                    .nxb("NXB Lao động")
                    .namXuatBan(2022)
                    .danhMuc(kyNang)
                    .soLuong(30)
                    .soLuongCon(30)
                    .hinhAnh("book.jpg")
                    .moTa("Khai phá tư duy đột phá, giải quyết vấn đề từ các góc nhìn khác biệt và vượt qua rào cản tâm lý thông thường.")
                    .build());

            booksToSeed.add(TaiLieu.builder()
                    .tenTaiLieu("Kỹ năng thuyết trình và làm việc nhóm hiệu quả")
                    .tacGia("Lê Huy Khoa")
                    .nxb("NXB Tổng hợp TP.HCM")
                    .namXuatBan(2023)
                    .danhMuc(kyNang)
                    .soLuong(22)
                    .soLuongCon(22)
                    .hinhAnh("ebook.jpg")
                    .moTa("Phương pháp chuẩn bị slide thuyết trình, kiểm soát giọng nói, tương tác khán giả và giải quyết xung đột nhóm.")
                    .build());

            booksToSeed.add(TaiLieu.builder()
                    .tenTaiLieu("Quản lý thời gian và giải quyết vấn đề")
                    .tacGia("Brian Tracy")
                    .nxb("NXB Hồng Đức")
                    .namXuatBan(2021)
                    .danhMuc(kyNang)
                    .soLuong(25)
                    .soLuongCon(25)
                    .hinhAnh("book.jpg")
                    .moTa("Quy tắc 80/20, thiết lập mục tiêu hàng ngày, tập trung vào nhiệm vụ trọng tâm và loại bỏ thói quen trì hoãn.")
                    .build());

            // Khoa học cơ bản (3 cuốn mới)
            booksToSeed.add(TaiLieu.builder()
                    .tenTaiLieu("Toán cao cấp cho kỹ sư và cử nhân")
                    .tacGia("Nguyễn Đình Trí")
                    .nxb("NXB Giáo dục")
                    .namXuatBan(2020)
                    .danhMuc(khoaHoc)
                    .soLuong(40)
                    .soLuongCon(40)
                    .hinhAnh("tai-lieu-in1.jpg")
                    .moTa("Giải tích toán học một biến, nhiều biến, phép tính vi phân và tích phân, ứng dụng trong kỹ thuật.")
                    .build());

            booksToSeed.add(TaiLieu.builder()
                    .tenTaiLieu("Vật lý đại cương - Điện và Từ học")
                    .tacGia("Lương Duyên Bình")
                    .nxb("NXB Giáo dục")
                    .namXuatBan(2021)
                    .danhMuc(khoaHoc)
                    .soLuong(35)
                    .soLuongCon(35)
                    .hinhAnh("book.jpg")
                    .moTa("Trường tĩnh điện, vật dẫn, điện môi, từ trường tĩnh, hiện tượng cảm ứng điện từ và sóng điện từ.")
                    .build());

            booksToSeed.add(TaiLieu.builder()
                    .tenTaiLieu("Nhập môn Logic học và Phương pháp luận")
                    .tacGia("Nguyễn Đức Dân")
                    .nxb("NXB Khoa học Xã hội")
                    .namXuatBan(2022)
                    .danhMuc(khoaHoc)
                    .soLuong(20)
                    .soLuongCon(20)
                    .hinhAnh("ebook.jpg")
                    .moTa("Cơ sở tư duy logic hình thức, các quy luật logic cơ bản, phương pháp luận nghiên cứu và lập luận khoa học.")
                    .build());

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


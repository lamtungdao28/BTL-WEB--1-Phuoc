package com.ptit.thuvien.config;

import com.ptit.thuvien.model.*;
import com.ptit.thuvien.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
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

    private NguoiDung seedUsers() {
        // Tài khoản Admin
        NguoiDung admin = nguoiDungRepository.findByTaiKhoan("admin").orElse(null);
        if (admin == null) {
            admin = NguoiDung.builder()
                    .taiKhoan("admin")
                    .matKhau(passwordEncoder.encode("admin"))
                    .hoTen("Quản trị viên")
                    .vaiTro(NguoiDung.VaiTro.ADMIN)
                    .trangThai(NguoiDung.TrangThaiNguoiDung.HOAT_DONG)
                    .build();
            admin = nguoiDungRepository.save(admin);
            System.out.println("[Seeder] Đã khởi tạo tài khoản admin: admin / admin");
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
        if (taiLieuRepository.count() == 0 && !dsDanhMuc.isEmpty()) {
            DanhMuc cntt = dsDanhMuc.get(0);
            DanhMuc dtvt = dsDanhMuc.size() > 1 ? dsDanhMuc.get(1) : cntt;
            DanhMuc kinhTe = dsDanhMuc.size() > 2 ? dsDanhMuc.get(2) : cntt;
            DanhMuc kyNang = dsDanhMuc.size() > 3 ? dsDanhMuc.get(3) : cntt;

            taiLieus.add(TaiLieu.builder()
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

            taiLieus.add(TaiLieu.builder()
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

            taiLieus.add(TaiLieu.builder()
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

            taiLieus.add(TaiLieu.builder()
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

            taiLieus.add(TaiLieu.builder()
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

            taiLieus = taiLieuRepository.saveAll(taiLieus);
            System.out.println("[Seeder] Đã tạo mới " + taiLieus.size() + " sách/tài liệu mẫu.");
        } else {
            taiLieus = taiLieuRepository.findAll();
        }
        return taiLieus;
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
                        .ngayMuon(LocalDate.now().minusDays(5))
                        .ngayHenTra(LocalDate.now().plusDays(9))
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
                            .ngayMuon(LocalDate.now().minusDays(20))
                            .ngayHenTra(LocalDate.now().minusDays(6))
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
                            .ngayMuon(LocalDate.now().minusDays(30))
                            .ngayHenTra(LocalDate.now().minusDays(16))
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


<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE html>
<html lang="vi">
<head>
<meta charset="UTF-8">
<title>Quản lý hệ thống - PTIT Admin</title>
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
<style>
/* ===== RESET & BASE ===== */
* { box-sizing: border-box; margin: 0; padding: 0; }
:root {
    --red: #c8102e;
    --red-dark: #a00d25;
    --red-light: #fff0f2;
    --sidebar-w: 240px;
    --gray-50: #f8fafc;
    --gray-100: #f1f5f9;
    --gray-200: #e2e8f0;
    --gray-600: #475569;
    --gray-800: #1e293b;
    --shadow: 0 1px 3px rgba(0,0,0,0.1), 0 1px 2px rgba(0,0,0,0.06);
    --shadow-md: 0 4px 6px rgba(0,0,0,0.07), 0 2px 4px rgba(0,0,0,0.05);
}
body { font-family: 'Segoe UI', Arial, sans-serif; background: var(--gray-100); color: var(--gray-800); }

/* ===== TOPBAR ===== */
.topbar {
    position: fixed; top: 0; left: 0; right: 0; height: 48px;
    background: var(--red); color: white;
    display: flex; align-items: center; justify-content: space-between;
    padding: 0 20px; z-index: 100;
    font-size: 13px;
}
.topbar-left { display: flex; align-items: center; gap: 16px; }
.topbar-right { display: flex; align-items: center; gap: 16px; }
.topbar a { color: rgba(255,255,255,0.85); text-decoration: none; }
.topbar a:hover { color: white; }

/* ===== SIDEBAR ===== */
.sidebar {
    position: fixed; top: 48px; left: 0; bottom: 0;
    width: var(--sidebar-w); background: white;
    border-right: 1px solid var(--gray-200);
    overflow-y: auto; z-index: 90;
}
.sidebar-brand {
    display: flex; align-items: center; gap: 10px;
    padding: 18px 16px; border-bottom: 1px solid var(--gray-200);
}
.sidebar-brand img { height: 36px; }
.sidebar-brand-text { font-size: 13px; line-height: 1.3; }
.sidebar-brand-text strong { color: var(--red); display: block; }

.sidebar-section { padding: 8px 0; }
.sidebar-section-title {
    font-size: 10px; font-weight: 700; text-transform: uppercase;
    letter-spacing: 0.08em; color: #94a3b8; padding: 8px 16px 4px;
}
.sidebar-item {
    display: flex; align-items: center; gap: 10px;
    padding: 9px 16px; font-size: 14px; color: var(--gray-600);
    cursor: pointer; border-left: 3px solid transparent;
    transition: all 0.15s; text-decoration: none;
}
.sidebar-item:hover { background: var(--gray-50); color: var(--red); }
.sidebar-item.active {
    background: var(--red-light); color: var(--red);
    border-left-color: var(--red); font-weight: 600;
}
.sidebar-item i { width: 16px; text-align: center; }
.sidebar-badge {
    margin-left: auto; background: var(--red); color: white;
    font-size: 10px; font-weight: 700;
    padding: 1px 6px; border-radius: 10px;
}

/* ===== MAIN ===== */
.main {
    margin-left: var(--sidebar-w);
    margin-top: 48px;
    padding: 24px;
    min-height: calc(100vh - 48px);
}

/* ===== PAGE HEADER ===== */
.page-header {
    display: flex; align-items: center; justify-content: space-between;
    margin-bottom: 20px;
}
.page-title { font-size: 22px; font-weight: 700; }
.page-subtitle { font-size: 13px; color: var(--gray-600); margin-top: 2px; }
.btn {
    display: inline-flex; align-items: center; gap: 6px;
    padding: 8px 16px; border-radius: 6px; font-size: 14px;
    font-weight: 600; cursor: pointer; border: none; transition: 0.2s;
    text-decoration: none;
}
.btn-primary { background: var(--red); color: white; }
.btn-primary:hover { background: var(--red-dark); }
.btn-outline { background: white; color: var(--gray-600); border: 1px solid var(--gray-200); }
.btn-outline:hover { border-color: var(--red); color: var(--red); }
.btn-sm { padding: 5px 10px; font-size: 12px; }
.btn-danger { background: #fee2e2; color: #dc2626; }
.btn-danger:hover { background: #fecaca; }
.btn-success { background: #dcfce7; color: #16a34a; }
.btn-success:hover { background: #bbf7d0; }

/* ===== STATS CARDS ===== */
.stats-grid {
    display: grid; grid-template-columns: repeat(4, 1fr);
    gap: 16px; margin-bottom: 24px;
}
.stat-card {
    background: white; border-radius: 10px; padding: 18px;
    box-shadow: var(--shadow); display: flex; align-items: center; gap: 14px;
}
.stat-icon {
    width: 46px; height: 46px; border-radius: 10px;
    display: flex; align-items: center; justify-content: center;
    font-size: 20px;
}
.stat-icon.red { background: var(--red-light); color: var(--red); }
.stat-icon.blue { background: #eff6ff; color: #3b82f6; }
.stat-icon.green { background: #f0fdf4; color: #22c55e; }
.stat-icon.orange { background: #fff7ed; color: #f97316; }
.stat-val { font-size: 22px; font-weight: 700; }
.stat-lbl { font-size: 12px; color: var(--gray-600); margin-top: 2px; }

/* ===== TABLE CARD ===== */
.card {
    background: white; border-radius: 10px;
    box-shadow: var(--shadow); overflow: hidden;
}
.card-header {
    display: flex; align-items: center; justify-content: space-between;
    padding: 16px 20px; border-bottom: 1px solid var(--gray-200);
}
.card-title { font-size: 15px; font-weight: 700; }

/* ===== TOOLBAR ===== */
.toolbar {
    display: flex; align-items: center; gap: 10px;
    padding: 12px 20px; border-bottom: 1px solid var(--gray-200);
    background: var(--gray-50);
}
.search-box {
    display: flex; align-items: center; gap: 8px;
    background: white; border: 1px solid var(--gray-200);
    border-radius: 6px; padding: 7px 12px; flex: 1; max-width: 320px;
}
.search-box input {
    border: none; outline: none; font-size: 14px; width: 100%;
    background: transparent;
}
.search-box i { color: #94a3b8; font-size: 13px; }
.filter-select {
    padding: 7px 12px; border: 1px solid var(--gray-200); border-radius: 6px;
    font-size: 14px; background: white; color: var(--gray-600); cursor: pointer;
    outline: none;
}
.filter-select:focus { border-color: var(--red); }

/* ===== DATA TABLE ===== */
table { width: 100%; border-collapse: collapse; font-size: 14px; }
thead th {
    background: var(--gray-50); padding: 10px 16px;
    text-align: left; font-size: 12px; font-weight: 700;
    text-transform: uppercase; letter-spacing: 0.05em; color: var(--gray-600);
    border-bottom: 1px solid var(--gray-200);
    white-space: nowrap;
}
tbody tr { border-bottom: 1px solid var(--gray-100); transition: background 0.1s; }
tbody tr:hover { background: var(--gray-50); }
tbody td { padding: 12px 16px; vertical-align: middle; }
.badge {
    display: inline-flex; align-items: center;
    padding: 2px 8px; border-radius: 20px; font-size: 11px; font-weight: 600;
}
.badge-green { background: #dcfce7; color: #15803d; }
.badge-red { background: #fee2e2; color: #dc2626; }
.badge-blue { background: #dbeafe; color: #1d4ed8; }
.badge-orange { background: #fff7ed; color: #c2410c; }
.badge-gray { background: var(--gray-100); color: var(--gray-600); }
.book-img {
    width: 36px; height: 48px; object-fit: cover;
    border-radius: 4px; background: var(--gray-100);
    display: flex; align-items: center; justify-content: center;
    font-size: 18px; overflow: hidden;
}
.book-thumb-cell { display: flex; align-items: center; gap: 10px; }
.book-title { font-weight: 600; color: var(--gray-800); }
.book-author { font-size: 12px; color: var(--gray-600); }

/* ===== PAGINATION ===== */
.pagination {
    display: flex; align-items: center; justify-content: space-between;
    padding: 12px 20px; border-top: 1px solid var(--gray-200);
    font-size: 13px; color: var(--gray-600);
}
.page-btns { display: flex; gap: 4px; }
.page-btn {
    width: 30px; height: 30px; border-radius: 5px;
    display: flex; align-items: center; justify-content: center;
    cursor: pointer; border: 1px solid var(--gray-200); font-size: 13px;
    background: white; transition: 0.15s;
}
.page-btn:hover { border-color: var(--red); color: var(--red); }
.page-btn.active { background: var(--red); color: white; border-color: var(--red); }

/* ===== TABS ===== */
.tabs { display: flex; border-bottom: 2px solid var(--gray-200); margin-bottom: 20px; overflow-x: auto;}
.tab {
    padding: 10px 20px; font-size: 14px; font-weight: 600;
    cursor: pointer; color: var(--gray-600); position: relative;
    border-bottom: 2px solid transparent; margin-bottom: -2px;
    transition: 0.15s; white-space: nowrap;
}
.tab:hover { color: var(--red); }
.tab.active { color: var(--red); border-bottom-color: var(--red); }

/* ===== MODAL ===== */
.modal-overlay {
    display: none; position: fixed; inset: 0; z-index: 200;
    background: rgba(0,0,0,0.45); align-items: center; justify-content: center;
}
.modal-overlay.open { display: flex; }
.modal {
    background: white; border-radius: 12px; width: 520px; max-height: 85vh;
    overflow-y: auto; box-shadow: 0 20px 40px rgba(0,0,0,0.15);
    animation: slideUp 0.2s ease;
}
@keyframes slideUp { from { transform: translateY(20px); opacity: 0; } }
.modal-header {
    display: flex; align-items: center; justify-content: space-between;
    padding: 18px 20px; border-bottom: 1px solid var(--gray-200);
}
.modal-title { font-size: 16px; font-weight: 700; }
.modal-close {
    width: 30px; height: 30px; border-radius: 50%; border: none;
    background: var(--gray-100); cursor: pointer; font-size: 16px;
    display: flex; align-items: center; justify-content: center;
    color: var(--gray-600); transition: 0.15s;
}
.modal-close:hover { background: #fee2e2; color: var(--red); }
.modal-body { padding: 20px; }
.modal-footer {
    padding: 14px 20px; border-top: 1px solid var(--gray-200);
    display: flex; justify-content: flex-end; gap: 10px;
}
.form-group { margin-bottom: 16px; }
.form-label { font-size: 13px; font-weight: 600; margin-bottom: 6px; display: block; }
.form-input {
    width: 100%; padding: 9px 12px; border: 1px solid var(--gray-200);
    border-radius: 6px; font-size: 14px; outline: none; transition: 0.15s;
}
.form-input:focus { border-color: var(--red); box-shadow: 0 0 0 2px rgba(200,16,46,0.1); }
.form-row { display: grid; grid-template-columns: 1fr 1fr; gap: 12px; }
.form-select {
    width: 100%; padding: 9px 12px; border: 1px solid var(--gray-200);
    border-radius: 6px; font-size: 14px; outline: none; background: white;
}
.form-select:focus { border-color: var(--red); }

/* ===== ADMIN INFO ===== */
.admin-info {
    display: flex; align-items: center; gap: 8px; cursor: pointer;
}
.admin-avatar {
    width: 28px; height: 28px; border-radius: 50%;
    background: rgba(255,255,255,0.25);
    display: flex; align-items: center; justify-content: center;
    font-weight: 700; font-size: 13px;
}
.content-section { display: none; }
.content-section.active { display: block; }

/* ===== ALERTS ===== */
.alert { padding: 15px; margin-bottom: 20px; border: 1px solid transparent; border-radius: 4px; }
.alert-success { color: #155724; background-color: #d4edda; border-color: #c3e6cb; }

@media (max-width: 900px) {
    .stats-grid { grid-template-columns: 1fr 1fr; }
    .sidebar { display: none; }
    .main { margin-left: 0; }
}
</style>
</head>
<body>

<!-- TOPBAR -->
<div class="topbar">
    <div class="topbar-left">
        <i class="fa-solid fa-globe"></i>
        <span>Cổng thông tin điện tử – Học viện Công nghệ Bưu chính Viễn thông</span>
    </div>
    <div class="topbar-right">
        <a href="#"><i class="fa-regular fa-clock"></i> 7h30 – 21h30</a>
        <div class="admin-info">
            <div class="admin-avatar">A</div>
            <span id="admin-name"><sec:authentication property="principal.username" /></span>
        </div>
        <form action="${pageContext.request.contextPath}/dang-xuat" method="POST" style="margin:0;">
            <button type="submit" style="background:none;border:none;color:rgba(255,255,255,0.7); font-size:12px; cursor:pointer;">
                <i class="fa-solid fa-right-from-bracket"></i> Đăng xuất
            </button>
        </form>
    </div>
</div>

<!-- SIDEBAR -->
<div class="sidebar">
    <div class="sidebar-brand">
        <img src="${pageContext.request.contextPath}/tai-nguyen/logo.png" alt="Logo" onerror="this.style.display='none'">
        <div class="sidebar-brand-text">
            <strong>PTIT Library</strong>
            <span style="color:#64748b; font-size:11px;">Quản trị hệ thống</span>
        </div>
    </div>

    <div class="sidebar-section">
        <div class="sidebar-section-title">Tổng quan</div>
        <a class="sidebar-item active" onclick="showSection('dashboard')">
            <i class="fa-solid fa-gauge-high"></i> Dashboard
        </a>
    </div>

    <div class="sidebar-section">
        <div class="sidebar-section-title">Quản lý</div>
        <a class="sidebar-item" onclick="showSection('sach')">
            <i class="fa-solid fa-book"></i> Quản lý sách
            <span class="sidebar-badge">${tongSach}</span>
        </a>
        <a class="sidebar-item" onclick="showSection('sinhvien')">
            <i class="fa-solid fa-users"></i> Quản lý sinh viên
            <span class="sidebar-badge">${tongSinhVien}</span>
        </a>
        <a class="sidebar-item" onclick="showSection('muon')">
            <i class="fa-solid fa-book-open"></i> Lịch sử mượn
        </a>
    </div>

    <div class="sidebar-section">
        <div class="sidebar-section-title">Hệ thống</div>
        <a class="sidebar-item" href="${pageContext.request.contextPath}/trang-chu">
            <i class="fa-solid fa-house"></i> Về trang chủ
        </a>
    </div>
</div>

<!-- MAIN CONTENT -->
<div class="main">

    <c:if test="${not empty thongBao}">
        <div class="alert alert-success">${thongBao}</div>
    </c:if>

    <!-- ===== DASHBOARD ===== -->
    <div id="section-dashboard" class="content-section active">
        <div class="page-header">
            <div>
                <div class="page-title">Dashboard</div>
                <div class="page-subtitle">Tổng quan hệ thống thư viện PTIT</div>
            </div>
            <div style="font-size:13px; color:var(--gray-600)">
                <i class="fa-regular fa-calendar"></i>
                <span id="current-date"></span>
            </div>
        </div>

        <div class="stats-grid">
            <div class="stat-card">
                <div class="stat-icon red"><i class="fa-solid fa-book"></i></div>
                <div>
                    <div class="stat-val">${tongSach}</div>
                    <div class="stat-lbl">Tổng số sách</div>
                </div>
            </div>
            <div class="stat-card">
                <div class="stat-icon blue"><i class="fa-solid fa-users"></i></div>
                <div>
                    <div class="stat-val">${tongSinhVien}</div>
                    <div class="stat-lbl">Sinh viên đăng ký</div>
                </div>
            </div>
            <div class="stat-card">
                <div class="stat-icon green"><i class="fa-solid fa-book-open"></i></div>
                <div>
                    <div class="stat-val">${tongMuon}</div>
                    <div class="stat-lbl">Lượt mượn sách</div>
                </div>
            </div>
            <div class="stat-card">
                <div class="stat-icon orange"><i class="fa-solid fa-triangle-exclamation"></i></div>
                <div>
                    <div class="stat-val">${quaHan}</div>
                    <div class="stat-lbl">Quá hạn chưa trả</div>
                </div>
            </div>
        </div>
    </div>

    <!-- ===== QUẢN LÝ SÁCH ===== -->
    <div id="section-sach" class="content-section">
        <div class="page-header">
            <div>
                <div class="page-title"><i class="fa-solid fa-book" style="color:var(--red)"></i> Quản lý sách</div>
                <div class="page-subtitle">Danh sách tài liệu trong thư viện PTIT</div>
            </div>
            <div style="display:flex; gap:8px;">
                <button class="btn btn-primary" onclick="openModal('them-sach')">
                    <i class="fa-solid fa-plus"></i> Thêm sách
                </button>
            </div>
        </div>

        <div class="tabs">
            <div class="tab active" onclick="filterSach('ALL', this)">Tất cả</div>
            <c:forEach items="${dsDanhMuc}" var="dm">
                <div class="tab" onclick="filterSach('${dm.maDanhMuc}', this)">${dm.tenDanhMuc}</div>
            </c:forEach>
        </div>

        <div class="card">
            <div class="toolbar">
                <div class="search-box">
                    <i class="fa-solid fa-magnifying-glass"></i>
                    <input type="text" id="search-sach" placeholder="Tìm theo tên sách, tác giả..." oninput="timKiemSach()">
                </div>
            </div>
            <table>
                <thead>
                    <tr>
                        <th>Tên sách</th>
                        <th>Tác giả</th>
                        <th>NXB</th>
                        <th>Danh mục</th>
                        <th>Tổng SL</th>
                        <th>Còn lại</th>
                        <th>Thao tác</th>
                    </tr>
                </thead>
                <tbody id="sach-tbody">
                    <c:forEach items="${dsTaiLieu}" var="tl">
                        <tr class="sach-row" data-danh-muc="${tl.danhMuc.maDanhMuc}" data-ten="${tl.tenTaiLieu.toLowerCase()}" data-tac-gia="${tl.tacGia.toLowerCase()}">
                            <td>
                                <div class="book-thumb-cell">
                                    <img src="${tl.hinhAnh}" onerror="this.style.display='none'" class="book-img" />
                                    <div class="book-title">${tl.tenTaiLieu}</div>
                                </div>
                            </td>
                            <td style="color:var(--gray-600)">${tl.tacGia}</td>
                            <td style="color:var(--gray-600); font-size:13px">${tl.nxb}</td>
                            <td><span class="badge badge-blue">${tl.danhMuc.tenDanhMuc}</span></td>
                            <td style="text-align:center; font-weight:600">${tl.soLuong}</td>
                            <td style="text-align:center; font-weight:600">${tl.soLuongCon}</td>
                            <td>
                                <div style="display:flex; gap:4px;">
                                    <button class="btn btn-outline btn-sm" 
                                            data-id="${tl.maTaiLieu}" 
                                            data-ten="${tl.tenTaiLieu}" 
                                            data-tacgia="${tl.tacGia}" 
                                            data-nxb="${tl.nxb}" 
                                            data-sl="${tl.soLuong}" 
                                            data-slcon="${tl.soLuongCon}" 
                                            onclick="suaSach(this)" 
                                            title="Sửa"><i class="fa-solid fa-pen"></i></button>
                                    <form action="${pageContext.request.contextPath}/admin/xoa-sach/${tl.maTaiLieu}" method="POST" style="margin:0;" onsubmit="return confirm('Bạn có chắc muốn xoá sách này không?');">
                                        <button class="btn btn-danger btn-sm" title="Xoá"><i class="fa-solid fa-trash"></i></button>
                                    </form>
                                </div>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>
    </div>

    <!-- ===== QUẢN LÝ SINH VIÊN ===== -->
    <div id="section-sinhvien" class="content-section">
        <div class="page-header">
            <div>
                <div class="page-title"><i class="fa-solid fa-users" style="color:var(--red)"></i> Quản lý sinh viên</div>
                <div class="page-subtitle">Danh sách người dùng đăng ký hệ thống</div>
            </div>
            <div style="display:flex; gap:8px;">
                <button class="btn btn-primary" onclick="openModal('them-sv')">
                    <i class="fa-solid fa-user-plus"></i> Thêm sinh viên
                </button>
            </div>
        </div>

        <div class="card">
            <div class="toolbar">
                <div class="search-box">
                    <i class="fa-solid fa-magnifying-glass"></i>
                    <input type="text" id="search-sv" placeholder="Tìm theo tên, mã tài khoản..." oninput="timKiemSV()">
                </div>
            </div>
            <table>
                <thead>
                    <tr>
                        <th>Tài khoản</th>
                        <th>Họ tên</th>
                        <th>Email</th>
                        <th>Số điện thoại</th>
                        <th>Trạng thái</th>
                        <th>Thao tác</th>
                    </tr>
                </thead>
                <tbody id="sv-tbody">
                    <c:forEach items="${dsNguoiDung}" var="nd">
                        <c:if test="${nd.vaiTro == 'SINH_VIEN'}">
                            <tr class="sv-row" data-ten="${nd.hoTen.toLowerCase()}" data-tk="${nd.taiKhoan.toLowerCase()}">
                                <td style="font-family:monospace; font-size:12px; color:var(--gray-600)">${nd.taiKhoan}</td>
                                <td><strong>${nd.hoTen}</strong></td>
                                <td style="font-size:13px; color:var(--gray-600)">${nd.email}</td>
                                <td style="color:var(--gray-600)">${nd.soDienThoai}</td>
                                <td>
                                    <c:choose>
                                        <c:when test="${nd.trangThai == 'HOAT_DONG'}">
                                            <span class="badge badge-green">Hoạt động</span>
                                        </c:when>
                                        <c:otherwise>
                                            <span class="badge badge-red">Bị khoá</span>
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                                <td>
                                    <div style="display:flex; gap:4px;">
                                        <c:if test="${nd.trangThai == 'HOAT_DONG'}">
                                            <form action="${pageContext.request.contextPath}/admin/khoa-tai-khoan/${nd.maNguoiDung}" method="POST" style="margin:0;">
                                                <button class="btn btn-danger btn-sm" title="Khoá"><i class="fa-solid fa-lock"></i></button>
                                            </form>
                                        </c:if>
                                        <c:if test="${nd.trangThai == 'BI_KHOA'}">
                                            <form action="${pageContext.request.contextPath}/admin/mo-khoa/${nd.maNguoiDung}" method="POST" style="margin:0;">
                                                <button class="btn btn-success btn-sm" title="Mở khoá"><i class="fa-solid fa-lock-open"></i></button>
                                            </form>
                                        </c:if>
                                    </div>
                                </td>
                            </tr>
                        </c:if>
                    </c:forEach>
                </tbody>
            </table>
        </div>
    </div>

    <!-- ===== LỊCH SỬ MƯỢN ===== -->
    <div id="section-muon" class="content-section">
        <div class="page-header">
            <div>
                <div class="page-title"><i class="fa-solid fa-book-open" style="color:var(--red)"></i> Lịch sử mượn sách</div>
                <div class="page-subtitle">Toàn bộ giao dịch mượn – trả trong hệ thống</div>
            </div>
        </div>
        <div class="card">
            <table>
                <thead>
                    <tr>
                        <th>Mã PM</th>
                        <th>Sinh viên</th>
                        <th>Sách</th>
                        <th>Ngày mượn</th>
                        <th>Hạn trả</th>
                        <th>Trạng thái</th>
                        <th>Thao tác</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach items="${dsPhieuMuon}" var="pm">
                        <tr>
                            <td>#PM${pm.maMuon}</td>
                            <td>${pm.nguoiDung.hoTen}</td>
                            <td>${pm.taiLieu.tenTaiLieu}</td>
                            <td>${pm.ngayMuon}</td>
                            <td>${pm.ngayHenTra}</td>
                            <td>
                                <c:choose>
                                    <c:when test="${pm.trangThai == 'CHO_DUYET'}">
                                        <span class="badge badge-orange">Chờ duyệt</span>
                                    </c:when>
                                    <c:when test="${pm.trangThai == 'DANG_MUON'}">
                                        <span class="badge badge-blue">Đang mượn</span>
                                    </c:when>
                                    <c:when test="${pm.trangThai == 'DA_TRA'}">
                                        <span class="badge badge-green">Đã trả</span>
                                    </c:when>
                                    <c:when test="${pm.trangThai == 'QUA_HAN'}">
                                        <span class="badge badge-red">Quá hạn</span>
                                    </c:when>
                                </c:choose>
                            </td>
                            <td>
                                <div style="display:flex; gap:4px;">
                                    <c:if test="${pm.trangThai == 'CHO_DUYET'}">
                                        <form action="${pageContext.request.contextPath}/admin/duyet-muon/${pm.maMuon}" method="POST" style="margin:0;">
                                            <button class="btn btn-success btn-sm"><i class="fa-solid fa-check"></i> Duyệt</button>
                                        </form>
                                        <form action="${pageContext.request.contextPath}/admin/tu-choi-muon/${pm.maMuon}" method="POST" style="margin:0;">
                                            <button class="btn btn-danger btn-sm"><i class="fa-solid fa-xmark"></i> Từ chối</button>
                                        </form>
                                    </c:if>
                                    <c:if test="${pm.trangThai == 'DANG_MUON' || pm.trangThai == 'QUA_HAN'}">
                                        <form action="${pageContext.request.contextPath}/admin/xac-nhan-tra/${pm.maMuon}" method="POST" style="margin:0;">
                                            <button class="btn btn-outline btn-sm"><i class="fa-solid fa-rotate-left"></i> Xác nhận trả</button>
                                        </form>
                                    </c:if>
                                </div>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>
    </div>

</div><!-- /main -->

<!-- ===== MODAL THÊM SÁCH ===== -->
<div class="modal-overlay" id="modal-them-sach">
    <div class="modal">
        <form action="${pageContext.request.contextPath}/admin/them-sach" method="POST">
            <div class="modal-header">
                <span class="modal-title"><i class="fa-solid fa-plus" style="color:var(--red)"></i> Thêm sách mới</span>
                <button type="button" class="modal-close" onclick="closeModal('them-sach')">×</button>
            </div>
            <div class="modal-body">
                <div class="form-group">
                    <label class="form-label">Tên tài liệu *</label>
                    <input class="form-input" type="text" name="tenTaiLieu" required>
                </div>
                <div class="form-row">
                    <div class="form-group">
                        <label class="form-label">Tác giả *</label>
                        <input class="form-input" type="text" name="tacGia" required>
                    </div>
                    <div class="form-group">
                        <label class="form-label">Nhà xuất bản</label>
                        <input class="form-input" type="text" name="nxb">
                    </div>
                </div>
                <div class="form-row">
                    <div class="form-group">
                        <label class="form-label">Năm xuất bản</label>
                        <input class="form-input" type="number" name="namXuatBan">
                    </div>
                    <div class="form-group">
                        <label class="form-label">Danh mục</label>
                        <select class="form-select" name="danhMuc.maDanhMuc">
                            <c:forEach items="${dsDanhMuc}" var="dm">
                                <option value="${dm.maDanhMuc}">${dm.tenDanhMuc}</option>
                            </c:forEach>
                        </select>
                    </div>
                </div>
                <div class="form-row">
                    <div class="form-group">
                        <label class="form-label">Số lượng tổng</label>
                        <input class="form-input" type="number" name="soLuong" value="10">
                    </div>
                    <div class="form-group">
                        <label class="form-label">Số lượng còn</label>
                        <input class="form-input" type="number" name="soLuongCon" value="10">
                    </div>
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-outline" onclick="closeModal('them-sach')">Hủy</button>
                <button type="submit" class="btn btn-primary"><i class="fa-solid fa-save"></i> Lưu sách</button>
            </div>
        </form>
    </div>
</div>

<!-- ===== MODAL THÊM SINH VIÊN ===== -->
<div class="modal-overlay" id="modal-them-sv">
    <div class="modal">
        <form action="${pageContext.request.contextPath}/admin/them-sinh-vien" method="POST">
            <div class="modal-header">
                <span class="modal-title"><i class="fa-solid fa-user-plus" style="color:var(--red)"></i> Thêm sinh viên</span>
                <button type="button" class="modal-close" onclick="closeModal('them-sv')">×</button>
            </div>
            <div class="modal-body">
                <div class="form-group">
                    <label class="form-label">Tài khoản *</label>
                    <input class="form-input" type="text" name="taiKhoan" required>
                </div>
                <div class="form-row">
                    <div class="form-group">
                        <label class="form-label">Họ tên *</label>
                        <input class="form-input" type="text" name="hoTen" required>
                    </div>
                    <div class="form-group">
                        <label class="form-label">Mật khẩu *</label>
                        <input class="form-input" type="password" name="matKhau" required>
                    </div>
                </div>
                <div class="form-group">
                    <label class="form-label">Email</label>
                    <input class="form-input" type="email" name="email">
                </div>
                <div class="form-row">
                    <div class="form-group">
                        <label class="form-label">Số điện thoại</label>
                        <input class="form-input" type="text" name="soDienThoai">
                    </div>
                    <div class="form-group">
                        <label class="form-label">Trạng thái</label>
                        <select class="form-select" name="trangThai">
                            <option value="HOAT_DONG">Hoạt động</option>
                            <option value="BI_KHOA">Khoá tài khoản</option>
                        </select>
                    </div>
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-outline" onclick="closeModal('them-sv')">Hủy</button>
                <button type="submit" class="btn btn-primary"><i class="fa-solid fa-save"></i> Lưu</button>
            </div>
        </form>
    </div>
</div>

<!-- ===== MODAL SỬA SÁCH ===== -->
<div class="modal-overlay" id="modal-sua-sach">
    <div class="modal">
        <form id="form-sua-sach" method="POST">
            <div class="modal-header">
                <span class="modal-title"><i class="fa-solid fa-pen" style="color:var(--red)"></i> Sửa thông tin sách</span>
                <button type="button" class="modal-close" onclick="closeModal('sua-sach')">×</button>
            </div>
            <div class="modal-body">
                <div class="form-group">
                    <label class="form-label">Tên tài liệu *</label>
                    <input class="form-input" type="text" id="edit-ten" name="tenTaiLieu" required>
                </div>
                <div class="form-row">
                    <div class="form-group">
                        <label class="form-label">Tác giả</label>
                        <input class="form-input" type="text" id="edit-tacgia" name="tacGia">
                    </div>
                    <div class="form-group">
                        <label class="form-label">Nhà xuất bản</label>
                        <input class="form-input" type="text" id="edit-nxb" name="nxb">
                    </div>
                </div>
                <div class="form-row">
                    <div class="form-group">
                        <label class="form-label">Số lượng tổng</label>
                        <input class="form-input" type="number" id="edit-sl" name="soLuong">
                    </div>
                    <div class="form-group">
                        <label class="form-label">Số lượng còn</label>
                        <input class="form-input" type="number" id="edit-slcon" name="soLuongCon">
                    </div>
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-outline" onclick="closeModal('sua-sach')">Hủy</button>
                <button type="submit" class="btn btn-primary"><i class="fa-solid fa-save"></i> Cập nhật</button>
            </div>
        </form>
    </div>
</div>

<script>
const d = new Date();
document.getElementById('current-date').textContent = 
    d.toLocaleDateString('vi-VN', {weekday:'long', day:'2-digit', month:'2-digit', year:'numeric'});

function showSection(id) {
    document.querySelectorAll('.content-section').forEach(s => s.classList.remove('active'));
    document.getElementById('section-' + id).classList.add('active');
    document.querySelectorAll('.sidebar-item').forEach(item => item.classList.remove('active'));
    event.currentTarget.classList.add('active');
}

function filterSach(cat, el) {
    document.querySelectorAll('.tabs .tab').forEach(t => t.classList.remove('active'));
    el.classList.add('active');
    const rows = document.querySelectorAll('.sach-row');
    rows.forEach(r => {
        if (cat === 'ALL' || r.getAttribute('data-danh-muc') === cat) {
            r.style.display = 'table-row';
        } else {
            r.style.display = 'none';
        }
    });
}

function timKiemSach() {
    const q = document.getElementById('search-sach').value.toLowerCase();
    document.querySelectorAll('.sach-row').forEach(r => {
        if (r.getAttribute('data-ten').includes(q) || r.getAttribute('data-tac-gia').includes(q)) {
            r.style.display = 'table-row';
        } else {
            r.style.display = 'none';
        }
    });
}

function timKiemSV() {
    const q = document.getElementById('search-sv').value.toLowerCase();
    document.querySelectorAll('.sv-row').forEach(r => {
        if (r.getAttribute('data-ten').includes(q) || r.getAttribute('data-tk').includes(q)) {
            r.style.display = 'table-row';
        } else {
            r.style.display = 'none';
        }
    });
}

function openModal(id) {
    document.getElementById('modal-' + id).classList.add('open');
}
function closeModal(id) {
    document.getElementById('modal-' + id).classList.remove('open');
}

document.querySelectorAll('.modal-overlay').forEach(el => {
    el.addEventListener('click', function(e) {
        if (e.target === el) el.classList.remove('open');
    });
});

function suaSach(btn) {
    const id = btn.getAttribute('data-id');
    const ten = btn.getAttribute('data-ten');
    const tacGia = btn.getAttribute('data-tacgia');
    const nxb = btn.getAttribute('data-nxb');
    const sl = btn.getAttribute('data-sl');
    const slCon = btn.getAttribute('data-slcon');

    document.getElementById('edit-ten').value = ten;
    document.getElementById('edit-tacgia').value = tacGia;
    document.getElementById('edit-nxb').value = nxb;
    document.getElementById('edit-sl').value = sl;
    document.getElementById('edit-slcon').value = slCon;
    document.getElementById('form-sua-sach').action = '${pageContext.request.contextPath}/admin/sua-sach/' + id;
    openModal('sua-sach');
}
</script>
</body>
</html>

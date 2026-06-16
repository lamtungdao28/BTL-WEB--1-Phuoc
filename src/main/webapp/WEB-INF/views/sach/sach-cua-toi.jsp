<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>Sách Của Tôi - PTIT Library</title>
    <link href="https://fonts.googleapis.com/css2?family=Be+Vietnam+Pro:wght@300;400;500;600;700;800&display=swap" rel="stylesheet" />
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css" />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/giao-dien.css">

    <style>
        :root {
            --ptit-red:       #c8102e;
            --ptit-red-dark:  #a00d24;
            --ptit-red-light: #fde8ec;
            --green:          #16a34a;
            --green-light:    #dcfce7;
            --orange:         #ea580c;
            --orange-light:   #fff7ed;
            --blue:           #2563eb;
            --blue-light:     #eff6ff;
            --gray-50:        #f9fafb;
            --gray-100:       #f3f4f6;
            --gray-200:       #e5e7eb;
            --gray-300:       #d1d5db;
            --gray-400:       #9ca3af;
            --gray-500:       #6b7280;
            --gray-700:       #374151;
            --gray-900:       #111827;
            --white:          #ffffff;
            --shadow-sm:      0 1px 2px rgba(0,0,0,.06);
            --shadow:         0 1px 3px rgba(0,0,0,.10), 0 1px 2px rgba(0,0,0,.06);
            --shadow-md:      0 4px 6px rgba(0,0,0,.07), 0 2px 4px rgba(0,0,0,.06);
            --radius:         12px;
            --radius-sm:      8px;
        }

        body { font-family: 'Be Vietnam Pro', sans-serif; background: var(--gray-50); color: var(--gray-900); min-height: 100vh; margin: 0; padding-top: 120px; }

        /* PAGE HEADER */
        .page-wrapper { max-width: 1200px; margin: 0 auto; padding: 32px 24px 48px; }
        .page-header { display: flex; justify-content: space-between; align-items: flex-start; margin-bottom: 28px; }
        .page-title { font-size: 26px; font-weight: 800; color: var(--gray-900); }
        .page-subtitle { font-size: 13.5px; color: var(--gray-500); margin-top: 4px; }
        .breadcrumb { display: flex; align-items: center; gap: 6px; font-size: 13px; color: var(--gray-500); margin-top: 4px; }
        .breadcrumb a { color: var(--gray-500); text-decoration: none; }
        .breadcrumb a:hover { color: var(--ptit-red); }

        /* STATS CARDS */
        .stats-grid { display: grid; grid-template-columns: repeat(4, 1fr); gap: 16px; margin-bottom: 28px; }
        .stat-card { background: var(--white); border-radius: var(--radius); padding: 20px 22px; box-shadow: var(--shadow); display: flex; align-items: center; gap: 16px; border: 1px solid var(--gray-100); }
        .stat-icon { width: 52px; height: 52px; border-radius: 12px; display: flex; align-items: center; justify-content: center; font-size: 22px; flex-shrink: 0; }
        .stat-icon.green  { background: var(--green-light);  color: var(--green); }
        .stat-icon.orange { background: var(--orange-light); color: var(--orange); }
        .stat-icon.blue   { background: var(--blue-light);   color: var(--blue); }
        .stat-icon.red    { background: var(--ptit-red-light); color: var(--ptit-red); }
        .stat-number { font-size: 28px; font-weight: 800; line-height: 1; }
        .stat-number.green  { color: var(--green); }
        .stat-number.orange { color: var(--orange); }
        .stat-number.blue   { color: var(--blue); }
        .stat-number.red    { color: var(--ptit-red); }
        .stat-label { font-size: 13.5px; font-weight: 600; margin-top: 3px; color: var(--gray-700); }
        .stat-desc  { font-size: 12px; color: var(--gray-400); margin-top: 1px; }

        /* FILTER BAR */
        .filter-bar { background: var(--white); border-radius: var(--radius); border: 1px solid var(--gray-200); padding: 14px 20px; display: flex; align-items: center; gap: 10px; flex-wrap: wrap; margin-bottom: 20px; box-shadow: var(--shadow-sm); }
        .filter-tabs { display: flex; gap: 4px; flex: 1; flex-wrap: wrap; }
        .tab-btn { display: flex; align-items: center; gap: 7px; padding: 8px 16px; border-radius: 8px; border: none; background: transparent; color: var(--gray-600); font-size: 13.5px; font-weight: 500; cursor: pointer; transition: all .2s; }
        .tab-btn:hover { background: var(--gray-100); }
        .tab-btn.active { background: var(--ptit-red); color: var(--white); font-weight: 600; }
        .filter-right { display: flex; gap: 10px; align-items: center; }
        .search-box { display: flex; align-items: center; gap: 8px; background: var(--gray-50); border: 1.5px solid var(--gray-200); border-radius: 8px; padding: 8px 14px; width: 220px; }
        .search-box input { border: none; background: transparent; font-size: 13.5px; width: 100%; outline: none; }

        /* BOOK LIST */
        .book-list { display: flex; flex-direction: column; gap: 14px; }
        .book-card { background: var(--white); border-radius: var(--radius); border: 1px solid var(--gray-200); box-shadow: var(--shadow-sm); display: flex; overflow: hidden; transition: box-shadow .2s; }
        .book-card:hover { box-shadow: var(--shadow-md); border-color: var(--gray-300); }
        .book-thumb { width: 108px; flex-shrink: 0; background: var(--gray-100); }
        .book-thumb img { width: 100%; height: 100%; object-fit: cover; display: block; }
        .book-body { flex: 1; padding: 16px 20px; display: flex; flex-direction: column; justify-content: center; gap: 10px; }
        .book-title { font-size: 16px; font-weight: 700; line-height: 1.3; }
        .book-meta { display: flex; flex-wrap: wrap; gap: 10px 20px; font-size: 13px; color: var(--gray-500); }
        .book-dates { display: flex; gap: 30px; flex-wrap: wrap; }
        .date-item { display: flex; flex-direction: column; gap: 3px; }
        .date-label { font-size: 12px; color: var(--gray-400); font-weight: 500; }
        .date-value { display: flex; align-items: center; gap: 5px; font-size: 13px; font-weight: 500; }
        .status-badge { display: inline-flex; align-items: center; gap: 5px; padding: 4px 12px; border-radius: 20px; font-size: 12.5px; font-weight: 600; }
        .status-badge.dang-muon { background: var(--green-light); color: var(--green); }
        .status-badge.cho-duyet { background: #fff8e1; color: #b45309; }
        .status-badge.qua-han { background: var(--ptit-red-light); color: var(--ptit-red); }
        .status-badge.da-tra { background: var(--gray-100); color: var(--gray-500); }
        .status-badge.cho-gia-han { background: #f3e8ff; color: #7c3aed; }
        .book-actions { padding: 16px 20px; display: flex; flex-direction: column; justify-content: center; gap: 10px; min-width: 160px; border-left: 1px solid var(--gray-100); }
        
        .btn { display: inline-flex; align-items: center; justify-content: center; gap: 7px; padding: 9px 18px; border-radius: 8px; font-size: 13.5px; font-weight: 600; cursor: pointer; border: none; text-decoration: none; }
        .btn-outline { background: var(--white); border: 1.5px solid var(--gray-300); color: var(--gray-700); }
        .btn-danger-outline { background: var(--white); border: 1.5px solid var(--ptit-red); color: var(--ptit-red); }
        .btn-primary { background: var(--ptit-red); color: var(--white); }
        .empty-state { text-align: center; padding: 64px 24px; color: var(--gray-400); display: none; }

        /* ALERTS */
        .alert { padding: 15px; margin-bottom: 20px; border: 1px solid transparent; border-radius: 4px; }
        .alert-success { color: #155724; background-color: #d4edda; border-color: #c3e6cb; }
        .alert-danger { color: #721c24; background-color: #f8d7da; border-color: #f5c6cb; }
    </style>
</head>
<body>

    <jsp:include page="/WEB-INF/views/common/header.jsp"/>

    <div class="page-wrapper">
        <div class="page-header">
            <div>
                <h1 class="page-title">SÁCH CỦA TÔI</h1>
                <p class="page-subtitle">Quản lý các sách bạn đã đăng ký mượn tại thư viện</p>
            </div>
            <div class="breadcrumb">
                <i class="fa-solid fa-house" style="font-size:12px"></i>
                <a href="${pageContext.request.contextPath}/trang-chu">Trang chủ</a>
                <span class="sep">›</span>
                <span class="current">Sách của tôi</span>
            </div>
        </div>

        <c:if test="${not empty success}">
            <div class="alert alert-success">${success}</div>
        </c:if>
        <c:if test="${not empty error}">
            <div class="alert alert-danger">${error}</div>
        </c:if>

        <div class="stats-grid">
            <div class="stat-card">
                <div class="stat-icon green"><i class="fa-solid fa-book-open"></i></div>
                <div>
                    <div class="stat-number green">${dangMuon}</div>
                    <div class="stat-label">Đang mượn</div>
                </div>
            </div>
            <div class="stat-card">
                <div class="stat-icon orange"><i class="fa-solid fa-hourglass-half"></i></div>
                <div>
                    <div class="stat-number orange">${choDuyet}</div>
                    <div class="stat-label">Chờ duyệt</div>
                </div>
            </div>
            <div class="stat-card">
                <div class="stat-icon blue"><i class="fa-solid fa-circle-check"></i></div>
                <div>
                    <div class="stat-number blue">${daTra}</div>
                    <div class="stat-label">Đã trả</div>
                </div>
            </div>
            <div class="stat-card">
                <div class="stat-icon red"><i class="fa-solid fa-calendar-xmark"></i></div>
                <div>
                    <div class="stat-number red">${quaHan}</div>
                    <div class="stat-label">Quá hạn</div>
                </div>
            </div>
        </div>

        <div class="filter-bar">
            <div class="filter-tabs">
                <button class="tab-btn active" onclick="filterBooks('ALL', this)"><i class="fa-solid fa-grid-2"></i> Tất cả</button>
                <button class="tab-btn" onclick="filterBooks('DANG_MUON', this)"><i class="fa-regular fa-book"></i> Đang mượn</button>
                <button class="tab-btn" onclick="filterBooks('CHO_DUYET', this)"><i class="fa-regular fa-hourglass"></i> Chờ duyệt</button>
                <button class="tab-btn" onclick="filterBooks('CHO_GIA_HAN', this)"><i class="fa-solid fa-rotate-right"></i> Chờ gia hạn</button>
                <button class="tab-btn" onclick="filterBooks('DA_TRA', this)"><i class="fa-regular fa-circle-check"></i> Đã trả</button>
                <button class="tab-btn" onclick="filterBooks('QUA_HAN', this)"><i class="fa-regular fa-calendar-xmark"></i> Quá hạn</button>
            </div>
            <div class="filter-right">
                <div class="search-box">
                    <i class="fa-solid fa-magnifying-glass"></i>
                    <input type="text" placeholder="Tìm kiếm sách..." oninput="searchBooks(this.value)" />
                </div>
            </div>
        </div>

        <div class="book-list" id="bookList">
            <c:forEach items="${dsPhieuMuon}" var="pm">
                <div class="book-card" data-trang-thai="${pm.trangThai}" data-ten-sach="${pm.taiLieu.tenTaiLieu.toLowerCase()}">
                    <div class="book-thumb">
                        <c:set var="bookImg" value="${pm.taiLieu.hinhAnh}" />
                        <c:if test="${not empty bookImg && !bookImg.startsWith('http')}">
                            <c:set var="bookImg" value="${pageContext.request.contextPath}${bookImg}" />
                        </c:if>
                        <img src="${bookImg}" onerror="this.src='${pageContext.request.contextPath}/tai-nguyen/book.jpg'" />
                    </div>
                    <div class="book-body">
                        <div class="book-title">${pm.taiLieu.tenTaiLieu}</div>
                        <div class="book-meta">
                            <span><i class="fa-regular fa-comment"></i> Thể loại: <strong>${pm.taiLieu.danhMuc.tenDanhMuc}</strong></span>
                            <span><i class="fa-regular fa-user"></i> Tác giả: <strong>${pm.taiLieu.tacGia}</strong></span>
                        </div>
                        <div class="book-dates">
                            <div class="date-item">
                                <div class="date-label">Ngày giờ mượn</div>
                                <div class="date-value"><i class="fa-regular fa-calendar"></i> <span class="fmt-datetime">${pm.ngayMuon}</span></div>
                            </div>
                            <div class="date-item">
                                <div class="date-label">Hạn trả</div>
                                <div class="date-value"><i class="fa-regular fa-calendar"></i> <span class="fmt-datetime">${pm.ngayHenTra}</span></div>
                            </div>
                            <div class="date-item" style="align-self:flex-end">
                                <div class="date-label">Trạng thái</div>
                                <c:choose>
                                    <c:when test="${pm.trangThai == 'DANG_MUON'}">
                                        <span class="status-badge dang-muon"><i class="fa-solid fa-circle" style="font-size:7px"></i> Đang mượn</span>
                                    </c:when>
                                    <c:when test="${pm.trangThai == 'CHO_DUYET'}">
                                        <span class="status-badge cho-duyet"><i class="fa-solid fa-circle" style="font-size:7px"></i> Chờ duyệt</span>
                                    </c:when>
                                    <c:when test="${pm.trangThai == 'DA_TRA'}">
                                        <span class="status-badge da-tra"><i class="fa-solid fa-circle" style="font-size:7px"></i> Đã trả</span>
                                    </c:when>
                                    <c:when test="${pm.trangThai == 'QUA_HAN'}">
                                        <span class="status-badge qua-han"><i class="fa-solid fa-circle" style="font-size:7px"></i> Quá hạn</span>
                                    </c:when>
                                    <c:when test="${pm.trangThai == 'CHO_GIA_HAN'}">
                                        <span class="status-badge cho-gia-han"><i class="fa-solid fa-circle" style="font-size:7px"></i> Chờ gia hạn</span>
                                    </c:when>
                                </c:choose>
                            </div>
                        </div>
                    </div>
                    <div class="book-actions">
                        <a href="${pageContext.request.contextPath}/sach/chi-tiet/${pm.taiLieu.maTaiLieu}" class="btn btn-outline">
                            <i class="fa-regular fa-eye"></i> Xem sách
                        </a>
                        <c:if test="${pm.trangThai == 'DANG_MUON'}">
                            <form action="${pageContext.request.contextPath}/sach/gia-han/${pm.maMuon}" method="POST" style="margin:0;">
                                <button type="submit" class="btn btn-primary" style="width:100%" onclick="return confirm('Bạn có chắc muốn gửi yêu cầu gia hạn mượn sách này thêm 14 ngày?');">
                                    <i class="fa-solid fa-rotate-right"></i> Yêu cầu gia hạn
                                </button>
                            </form>
                        </c:if>
                        <c:if test="${pm.trangThai == 'CHO_GIA_HAN'}">
                            <button class="btn btn-outline" style="width:100%; cursor:not-allowed; opacity:0.7;" disabled>
                                <i class="fa-solid fa-hourglass-half"></i> Đang chờ duyệt
                            </button>
                        </c:if>
                        <c:if test="${pm.trangThai == 'CHO_DUYET'}">
                            <form action="${pageContext.request.contextPath}/sach/huy-dang-ky/${pm.maMuon}" method="POST" style="margin:0;">
                                <button type="submit" class="btn btn-danger-outline" style="width:100%" onclick="return confirm('Bạn có chắc muốn hủy đăng ký mượn sách này?');">
                                    <i class="fa-solid fa-ban"></i> Hủy đăng ký
                                </button>
                            </form>
                        </c:if>
                    </div>
                </div>
            </c:forEach>
        </div>
        <div class="empty-state" id="emptyState">
            <i class="fa-regular fa-folder-open"></i>
            <p>Không có sách nào phù hợp.</p>
        </div>
    </div>

    <jsp:include page="/WEB-INF/views/common/footer.jsp"/>

    <script>
        let currentStatus = 'ALL';
        let currentSearch = '';

        function filterBooks(status, btnElement) {
            currentStatus = status;
            document.querySelectorAll('.tab-btn').forEach(b => b.classList.remove('active'));
            if (btnElement) btnElement.classList.add('active');
            applyFilters();
        }

        function searchBooks(keyword) {
            currentSearch = keyword.toLowerCase().trim();
            applyFilters();
        }

        function applyFilters() {
            const cards = document.querySelectorAll('.book-card');
            let visibleCount = 0;

            cards.forEach(card => {
                const status = card.getAttribute('data-trang-thai');
                const title = card.getAttribute('data-ten-sach');
                
                const matchStatus = (currentStatus === 'ALL' || status === currentStatus);
                const matchSearch = (!currentSearch || title.includes(currentSearch));

                if (matchStatus && matchSearch) {
                    card.style.display = 'flex';
                    visibleCount++;
                } else {
                    card.style.display = 'none';
                }
            });

            document.getElementById('emptyState').style.display = (visibleCount === 0) ? 'block' : 'none';
        }

        // Format LocalDateTime (2026-06-04T17:30) thành dd/MM/yyyy HH:mm
        document.querySelectorAll('.fmt-datetime').forEach(function(el) {
            var raw = el.textContent.trim();
            if (raw && raw.length >= 10) {
                try {
                    var d = new Date(raw);
                    if (!isNaN(d.getTime())) {
                        var day = String(d.getDate()).padStart(2, '0');
                        var month = String(d.getMonth() + 1).padStart(2, '0');
                        var year = d.getFullYear();
                        var hours = String(d.getHours()).padStart(2, '0');
                        var mins = String(d.getMinutes()).padStart(2, '0');
                        el.textContent = day + '/' + month + '/' + year + ' ' + hours + ':' + mins;
                    }
                } catch(e) { /* giữ nguyên nếu parse lỗi */ }
            }
        });
    </script>
</body>
</html>

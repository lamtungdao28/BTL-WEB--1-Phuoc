<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${taiLieu.tenTaiLieu} - PTIT Library</title>
    <link href="https://fonts.googleapis.com/css2?family=Be+Vietnam+Pro:wght@300;400;500;600;700;800;900&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/giao-dien.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
    <style>
        :root {
            --red: #c8102e;
            --red-dark: #a00d24;
            --red-light: #fde8ec;
            --gray-50: #f9fafb;
            --gray-100: #f3f4f6;
            --gray-200: #e5e7eb;
            --gray-300: #d1d5db;
            --gray-400: #9ca3af;
            --gray-500: #6b7280;
            --gray-700: #374151;
            --gray-900: #111827;
            --white: #ffffff;
            --shadow: 0 1px 3px rgba(0,0,0,.1), 0 1px 2px rgba(0,0,0,.06);
            --shadow-md: 0 4px 12px rgba(0,0,0,.1);
            --shadow-lg: 0 10px 30px rgba(0,0,0,.12);
            --radius: 16px;
        }

        body {
            font-family: 'Be Vietnam Pro', sans-serif;
            background: var(--gray-50);
            color: var(--gray-900);
            margin: 0;
            padding-top: 120px;
        }

        .detail-wrapper {
            max-width: 1200px;
            margin: 0 auto;
            padding: 36px 24px 60px;
        }

        /* Breadcrumb */
        .breadcrumb {
            display: flex; align-items: center; gap: 8px;
            font-size: 13px; color: var(--gray-500); margin-bottom: 28px;
        }
        .breadcrumb a { color: var(--gray-500); text-decoration: none; }
        .breadcrumb a:hover { color: var(--red); }

        /* Hero section */
        .book-hero {
            display: flex; gap: 40px;
            background: var(--white);
            border-radius: var(--radius);
            box-shadow: var(--shadow);
            border: 1px solid var(--gray-100);
            padding: 32px;
            margin-bottom: 28px;
        }

        .book-cover-wrap {
            flex-shrink: 0;
            width: 280px;
        }

        .book-cover-wrap img {
            width: 100%;
            border-radius: 12px;
            box-shadow: var(--shadow-lg);
            object-fit: cover;
        }

        .book-hero-info {
            flex: 1;
            display: flex;
            flex-direction: column;
            gap: 16px;
        }

        .book-category-badge {
            display: inline-flex;
            align-items: center;
            gap: 6px;
            background: var(--red-light);
            color: var(--red);
            padding: 6px 14px;
            border-radius: 20px;
            font-size: 12.5px;
            font-weight: 700;
            text-transform: uppercase;
            width: fit-content;
        }

        .book-hero-info h1 {
            font-size: 32px;
            font-weight: 900;
            line-height: 1.2;
            margin: 0;
            color: var(--gray-900);
        }

        .book-author-line {
            font-size: 16px;
            color: var(--gray-500);
        }

        .book-meta-grid {
            display: grid;
            grid-template-columns: repeat(2, 1fr);
            gap: 12px;
            margin-top: 8px;
        }

        .meta-card {
            background: var(--gray-50);
            border-radius: 10px;
            padding: 14px 16px;
            border: 1px solid var(--gray-100);
        }

        .meta-card .meta-label {
            font-size: 11px;
            font-weight: 600;
            color: var(--gray-400);
            text-transform: uppercase;
            letter-spacing: 0.05em;
            margin-bottom: 4px;
        }

        .meta-card .meta-value {
            font-size: 15px;
            font-weight: 700;
            color: var(--gray-700);
        }

        .meta-card .meta-value.stock-available {
            color: #16a34a;
            font-size: 22px;
        }

        .meta-card .meta-value.stock-out {
            color: var(--red);
        }

        .book-hero-actions {
            display: flex;
            gap: 12px;
            margin-top: auto;
        }

        .btn-borrow {
            display: inline-flex;
            align-items: center;
            gap: 8px;
            background: var(--red);
            color: var(--white);
            border: none;
            padding: 14px 28px;
            border-radius: 10px;
            font-size: 15px;
            font-weight: 700;
            cursor: pointer;
            transition: background .2s;
        }

        .btn-borrow:hover { background: var(--red-dark); }

        .btn-borrow.disabled {
            background: var(--gray-200);
            color: var(--gray-400);
            cursor: not-allowed;
        }

        .btn-back {
            display: inline-flex;
            align-items: center;
            gap: 8px;
            background: var(--white);
            border: 1.5px solid var(--gray-300);
            color: var(--gray-700);
            padding: 14px 28px;
            border-radius: 10px;
            font-size: 15px;
            font-weight: 600;
            cursor: pointer;
            text-decoration: none;
            transition: border-color .2s;
        }

        .btn-back:hover { border-color: var(--red); color: var(--red); }

        /* Tabs */
        .detail-tabs {
            display: flex;
            gap: 4px;
            margin-bottom: 0;
            border-bottom: 2px solid var(--gray-200);
        }

        .detail-tab {
            padding: 14px 24px;
            font-size: 14.5px;
            font-weight: 600;
            color: var(--gray-500);
            cursor: pointer;
            border-bottom: 3px solid transparent;
            margin-bottom: -2px;
            transition: all .2s;
            display: flex;
            align-items: center;
            gap: 8px;
            background: none;
            border-top: none;
            border-left: none;
            border-right: none;
        }

        .detail-tab:hover { color: var(--red); }
        .detail-tab.active {
            color: var(--red);
            border-bottom-color: var(--red);
        }

        /* Tab content */
        .tab-content {
            display: none;
            background: var(--white);
            border-radius: 0 0 var(--radius) var(--radius);
            box-shadow: var(--shadow);
            border: 1px solid var(--gray-100);
            border-top: none;
            padding: 32px;
        }

        .tab-content.active { display: block; }

        /* Description section */
        .section-title {
            font-size: 18px;
            font-weight: 800;
            color: var(--gray-900);
            margin-bottom: 16px;
            display: flex;
            align-items: center;
            gap: 10px;
        }

        .section-title i {
            color: var(--red);
        }

        .description-text {
            line-height: 1.85;
            color: var(--gray-700);
            font-size: 15px;
        }

        .content-divider {
            border: none;
            border-top: 1px solid var(--gray-200);
            margin: 28px 0;
        }

        .detail-content {
            line-height: 1.85;
            color: var(--gray-700);
            font-size: 15px;
            white-space: pre-wrap;
        }

        /* PDF viewer */
        .pdf-viewer-container {
            width: 100%;
            min-height: 700px;
            border-radius: 10px;
            overflow: hidden;
            background: var(--gray-100);
            border: 1px solid var(--gray-200);
        }

        .pdf-viewer-container iframe {
            width: 100%;
            height: 700px;
            border: none;
        }

        .pdf-download-bar {
            display: flex;
            align-items: center;
            justify-content: space-between;
            background: var(--gray-50);
            border: 1px solid var(--gray-200);
            border-radius: 10px;
            padding: 16px 20px;
            margin-bottom: 20px;
        }

        .pdf-download-bar .pdf-info {
            display: flex;
            align-items: center;
            gap: 12px;
            font-size: 14px;
            color: var(--gray-700);
        }

        .pdf-download-bar .pdf-icon {
            width: 44px;
            height: 44px;
            background: var(--red-light);
            color: var(--red);
            border-radius: 10px;
            display: flex;
            align-items: center;
            justify-content: center;
            font-size: 20px;
        }

        .pdf-download-btn {
            display: inline-flex;
            align-items: center;
            gap: 8px;
            background: var(--red);
            color: var(--white);
            padding: 10px 20px;
            border-radius: 8px;
            font-size: 13.5px;
            font-weight: 600;
            text-decoration: none;
            transition: background .2s;
        }

        .pdf-download-btn:hover { background: var(--red-dark); }

        .no-pdf {
            text-align: center;
            padding: 60px 24px;
            color: var(--gray-400);
        }

        .no-pdf i {
            font-size: 48px;
            margin-bottom: 16px;
            display: block;
        }

        @media (max-width: 768px) {
            .book-hero { flex-direction: column; }
            .book-cover-wrap { width: 100%; max-width: 300px; margin: 0 auto; }
            .book-meta-grid { grid-template-columns: 1fr; }
        }
    </style>
</head>
<body>

    <jsp:include page="/WEB-INF/views/common/header.jsp"/>

    <div class="detail-wrapper">
        <!-- Breadcrumb -->
        <div class="breadcrumb">
            <i class="fa-solid fa-house" style="font-size:12px"></i>
            <a href="${pageContext.request.contextPath}/trang-chu">Trang chủ</a>
            <span>›</span>
            <a href="${pageContext.request.contextPath}/sach/muon-sach">Mượn sách</a>
            <span>›</span>
            <span style="color:var(--gray-900); font-weight:500;">${taiLieu.tenTaiLieu}</span>
        </div>

        <!-- Hero Section -->
        <div class="book-hero">
            <div class="book-cover-wrap">
                <c:set var="bookImg" value="${taiLieu.hinhAnh}" />
                <c:if test="${not empty bookImg && !bookImg.startsWith('http')}">
                    <c:set var="bookImg" value="${pageContext.request.contextPath}${bookImg}" />
                </c:if>
                <img src="${bookImg}" alt="${taiLieu.tenTaiLieu}" onerror="this.src='${pageContext.request.contextPath}/tai-nguyen/book.jpg'">
            </div>
            <div class="book-hero-info">
                <span class="book-category-badge">
                    <i class="fa-solid fa-tag"></i> ${taiLieu.danhMuc.tenDanhMuc}
                </span>
                <h1>${taiLieu.tenTaiLieu}</h1>
                <div class="book-author-line">
                    <i class="fa-regular fa-user"></i> Tác giả: <strong>${taiLieu.tacGia}</strong>
                </div>

                <div class="book-meta-grid">
                    <div class="meta-card">
                        <div class="meta-label">Nhà xuất bản</div>
                        <div class="meta-value">${empty taiLieu.nxb ? '—' : taiLieu.nxb}</div>
                    </div>
                    <div class="meta-card">
                        <div class="meta-label">Năm xuất bản</div>
                        <div class="meta-value">${taiLieu.namXuatBan}</div>
                    </div>
                    <div class="meta-card">
                        <div class="meta-label">Số lượng còn</div>
                        <c:choose>
                            <c:when test="${taiLieu.soLuongCon == 0}">
                                <div class="meta-value stock-out"><i class="fa-solid fa-circle-xmark"></i> Hết sách</div>
                            </c:when>
                            <c:otherwise>
                                <div class="meta-value stock-available">${taiLieu.soLuongCon} <span style="font-size:13px; color:var(--gray-400)">/ ${taiLieu.soLuong}</span></div>
                            </c:otherwise>
                        </c:choose>
                    </div>
                    <div class="meta-card">
                        <div class="meta-label">Tài liệu PDF</div>
                        <div class="meta-value">
                            <c:choose>
                                <c:when test="${not empty taiLieu.filePdf}">
                                    <span style="color:#16a34a"><i class="fa-solid fa-circle-check"></i> Có sẵn</span>
                                </c:when>
                                <c:otherwise>
                                    <span style="color:var(--gray-400)">Chưa có</span>
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </div>
                </div>

                <div class="book-hero-actions">
                    <c:choose>
                        <c:when test="${taiLieu.soLuongCon > 0}">
                            <button class="btn-borrow" onclick="window.location.href='${pageContext.request.contextPath}/sach/dang-ky-muon/${taiLieu.maTaiLieu}'">
                                <i class="fa-solid fa-book-bookmark"></i> Mượn sách
                            </button>
                        </c:when>
                        <c:otherwise>
                            <button class="btn-borrow disabled" disabled>
                                <i class="fa-solid fa-ban"></i> Hết sách
                            </button>
                        </c:otherwise>
                    </c:choose>
                    <a href="javascript:history.back()" class="btn-back">
                        <i class="fa-solid fa-arrow-left"></i> Quay lại
                    </a>
                </div>
            </div>
        </div>

        <!-- Tabs -->
        <div class="detail-tabs">
            <button class="detail-tab active" onclick="switchTab('info', this)">
                <i class="fa-solid fa-circle-info"></i> Thông tin sách
            </button>
            <c:if test="${not empty taiLieu.filePdf}">
                <button class="detail-tab" onclick="switchTab('pdf', this)">
                    <i class="fa-solid fa-file-pdf"></i> Tài liệu PDF
                </button>
            </c:if>
        </div>

        <!-- Tab: Thông tin -->
        <div id="tab-info" class="tab-content active">
            <div class="section-title">
                <i class="fa-solid fa-align-left"></i> Mô tả sách
            </div>
            <div class="description-text">
                ${empty taiLieu.moTa ? 'Chưa có mô tả.' : taiLieu.moTa}
            </div>

            <c:if test="${not empty taiLieu.noiDungChiTiet}">
                <hr class="content-divider">
                <div class="section-title">
                    <i class="fa-solid fa-book-open"></i> Nội dung chi tiết
                </div>
                <div class="detail-content">${taiLieu.noiDungChiTiet}</div>
            </c:if>
        </div>

        <!-- Tab: PDF -->
        <c:if test="${not empty taiLieu.filePdf}">
            <div id="tab-pdf" class="tab-content">
                <div class="pdf-download-bar">
                    <div class="pdf-info">
                        <div class="pdf-icon">
                            <i class="fa-solid fa-file-pdf"></i>
                        </div>
                        <div>
                            <div style="font-weight:700;">${taiLieu.tenTaiLieu}</div>
                            <div style="font-size:12px; color:var(--gray-400);">Tài liệu PDF</div>
                        </div>
                    </div>
                    <a href="${pageContext.request.contextPath}${taiLieu.filePdf}" target="_blank" class="pdf-download-btn">
                        <i class="fa-solid fa-download"></i> Tải xuống
                    </a>
                </div>
                <div class="pdf-viewer-container">
                    <iframe src="${pageContext.request.contextPath}${taiLieu.filePdf}" title="PDF Viewer"></iframe>
                </div>
            </div>
        </c:if>
    </div>

    <jsp:include page="/WEB-INF/views/common/footer.jsp"/>

    <script>
        function switchTab(tabId, btnElement) {
            // Deactivate all tabs
            document.querySelectorAll('.detail-tab').forEach(t => t.classList.remove('active'));
            document.querySelectorAll('.tab-content').forEach(c => c.classList.remove('active'));

            // Activate selected tab
            btnElement.classList.add('active');
            document.getElementById('tab-' + tabId).classList.add('active');
        }

        // Sticky header
        window.addEventListener("scroll", function() {
            var header = document.querySelector(".header");
            if (header) {
                if (window.scrollY > 100) {
                    header.classList.add("sticky-active");
                } else {
                    header.classList.remove("sticky-active");
                }
            }
        });
    </script>
</body>
</html>

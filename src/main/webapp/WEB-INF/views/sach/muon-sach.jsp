<%@ page contentType="text/html;charset=UTF-8" language="java" %>
    <%@ taglib prefix="c" uri="jakarta.tags.core" %>
        <!DOCTYPE html>
        <html lang="vi">

        <head>
            <meta charset="UTF-8" />
            <meta name="viewport" content="width=device-width, initial-scale=1.0" />
            <title>Mượn Sách Online - PTIT Library</title>
            <link
                href="https://fonts.googleapis.com/css2?family=Be+Vietnam+Pro:wght@300;400;500;600;700;800;900&display=swap"
                rel="stylesheet" />
            <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css" />
            <!-- Link giao-dien.css cho Header/Footer -->
            <link rel="stylesheet" href="${pageContext.request.contextPath}/css/giao-dien.css">

            <style>
                :root {
                    --red: #c8102e;
                    --red-dark: #a00d24;
                    --red-deeper: #7a0a1b;
                    --red-light: #fde8ec;
                    --green: #16a34a;
                    --green-bg: #dcfce7;
                    --orange: #ea580c;
                    --orange-bg: #fff7ed;
                    --gray-50: #f9fafb;
                    --gray-100: #f3f4f6;
                    --gray-200: #e5e7eb;
                    --gray-300: #d1d5db;
                    --gray-400: #9ca3af;
                    --gray-500: #6b7280;
                    --gray-700: #374151;
                    --gray-900: #111827;
                    --white: #ffffff;
                    --shadow: 0 1px 3px rgba(0, 0, 0, .1), 0 1px 2px rgba(0, 0, 0, .06);
                    --shadow-md: 0 4px 12px rgba(0, 0, 0, .1);
                    --shadow-lg: 0 10px 30px rgba(0, 0, 0, .12);
                    --radius: 14px;
                }

                body {
                    font-family: 'Be Vietnam Pro', sans-serif;
                    background: var(--gray-50);
                    color: var(--gray-900);
                    margin: 0;
                    padding-top: 120px;
                }

                /* ── HERO ── */
                .hero {
                    background: linear-gradient(135deg, var(--red-deeper) 0%, var(--red) 55%, #d4152f 100%);
                    padding: 72px 80px 80px;
                    position: relative;
                    overflow: hidden;
                    color: var(--white);
                }

                /* geometric decoration */
                .hero::before {
                    content: '';
                    position: absolute;
                    top: -60px;
                    right: -60px;
                    width: 460px;
                    height: 460px;
                    background: rgba(255, 255, 255, .06);
                    border-radius: 50%;
                }

                .hero::after {
                    content: '';
                    position: absolute;
                    bottom: -80px;
                    right: 200px;
                    width: 260px;
                    height: 260px;
                    background: rgba(255, 255, 255, .04);
                    border-radius: 50%;
                }

                .hero-inner {
                    max-width: 680px;
                    position: relative;
                    z-index: 2;
                }

                .hero-tag {
                    display: inline-flex;
                    align-items: center;
                    gap: 8px;
                    background: rgba(255, 255, 255, .15);
                    border: 1px solid rgba(255, 255, 255, .25);
                    border-radius: 40px;
                    padding: 6px 16px;
                    font-size: 12.5px;
                    font-weight: 600;
                    letter-spacing: .5px;
                    text-transform: uppercase;
                    margin-bottom: 20px;
                    backdrop-filter: blur(6px);
                }

                .hero h1 {
                    font-size: 52px;
                    font-weight: 900;
                    letter-spacing: -1px;
                    line-height: 1.05;
                    margin-bottom: 16px;
                    color: white;
                }

                .hero h1 span {
                    background: linear-gradient(90deg, #fff 0%, rgba(255, 255, 255, .7) 100%);
                    -webkit-background-clip: text;
                    -webkit-text-fill-color: transparent;
                    background-clip: text;
                }

                .hero p {
                    font-size: 15.5px;
                    opacity: .85;
                    line-height: 1.65;
                    margin-bottom: 32px;
                    color: white;
                }

                .hero-search {
                    display: flex;
                    gap: 10px;
                    max-width: 560px;
                }

                .hero-search input {
                    flex: 1;
                    padding: 15px 20px;
                    border: none;
                    border-radius: 12px;
                    font-size: 14.5px;
                    outline: none;
                    box-shadow: 0 4px 20px rgba(0, 0, 0, .15);
                }

                .hero-search button {
                    background: var(--white);
                    color: var(--red);
                    border: none;
                    padding: 15px 28px;
                    border-radius: 12px;
                    font-size: 14.5px;
                    font-weight: 700;
                    cursor: pointer;
                    box-shadow: 0 4px 20px rgba(0, 0, 0, .15);
                    white-space: nowrap;
                    display: flex;
                    align-items: center;
                    gap: 8px;
                }

                .hero-search button:hover {
                    background: var(--red-light);
                }

                /* ── FILTER ── */
                .filter-bar {
                    background: var(--white);
                    border-bottom: 1px solid var(--gray-200);
                    padding: 0 80px;
                    display: flex;
                    align-items: center;
                    gap: 6px;
                    overflow-x: auto;
                }

                .filter-btn {
                    display: flex;
                    align-items: center;
                    gap: 7px;
                    padding: 16px 18px;
                    border: none;
                    background: transparent;
                    font-size: 14px;
                    font-weight: 500;
                    color: var(--gray-500);
                    cursor: pointer;
                    border-bottom: 3px solid transparent;
                    text-decoration: none;
                }

                .filter-btn:hover,
                .filter-btn.active {
                    color: var(--red);
                    border-bottom-color: var(--red);
                    font-weight: 700;
                }

                .filter-count {
                    background: var(--gray-100);
                    font-size: 11px;
                    padding: 2px 7px;
                    border-radius: 10px;
                    color: var(--gray-500);
                }

                .filter-btn.active .filter-count {
                    background: var(--red-light);
                    color: var(--red);
                }

                /* ── MAIN ── */
                .main {
                    max-width: 1240px;
                    margin: 0 auto;
                    padding: 36px 24px 60px;
                }

                .section-head {
                    display: flex;
                    justify-content: space-between;
                    margin-bottom: 24px;
                }

                .section-head h2 {
                    font-size: 20px;
                    font-weight: 800;
                }

                .book-grid {
                    display: grid;
                    grid-template-columns: repeat(auto-fill, minmax(220px, 1fr));
                    gap: 22px;
                }

                .book-card {
                    background: var(--white);
                    border-radius: var(--radius);
                    box-shadow: var(--shadow);
                    border: 1px solid var(--gray-100);
                    display: flex;
                    flex-direction: column;
                    cursor: pointer;
                    transition: transform .22s;
                    overflow: hidden;
                    text-decoration: none;
                    color: inherit;
                }

                .book-card:hover {
                    transform: translateY(-5px);
                    box-shadow: var(--shadow-lg);
                }

                .book-cover {
                    height: 220px;
                    overflow: hidden;
                    position: relative;
                    background: var(--gray-100);
                }

                .book-cover img {
                    width: 100%;
                    height: 100%;
                    object-fit: cover;
                    transition: transform .4s;
                }

                .book-card:hover .book-cover img {
                    transform: scale(1.06);
                }

                .book-badge {
                    position: absolute;
                    top: 10px;
                    left: 10px;
                    background: var(--white);
                    color: var(--red);
                    font-size: 11px;
                    font-weight: 700;
                    padding: 4px 10px;
                    border-radius: 20px;
                }

                .book-body {
                    padding: 16px;
                    flex: 1;
                    display: flex;
                    flex-direction: column;
                    gap: 6px;
                }

                .book-cat {
                    font-size: 11.5px;
                    font-weight: 700;
                    color: var(--red);
                    text-transform: uppercase;
                }

                .book-title {
                    font-size: 14.5px;
                    font-weight: 700;
                    color: var(--gray-900);
                }

                .book-author {
                    font-size: 12.5px;
                    color: var(--gray-500);
                }

                .book-footer {
                    margin-top: auto;
                    padding-top: 12px;
                    border-top: 1px solid var(--gray-100);
                    display: flex;
                    justify-content: space-between;
                    align-items: center;
                }

                .stock-info .stock-label {
                    font-size: 11px;
                    color: var(--gray-400);
                }

                .stock-info .stock-num {
                    font-size: 20px;
                    font-weight: 800;
                    color: var(--green);
                }

                .stock-info .stock-num.out {
                    color: var(--gray-400);
                    font-size: 14px;
                }

                .borrow-btn {
                    background: var(--red);
                    color: var(--white);
                    border: none;
                    padding: 9px 16px;
                    border-radius: 9px;
                    font-size: 13px;
                    font-weight: 700;
                    cursor: pointer;
                    display: flex;
                    align-items: center;
                    gap: 6px;
                }

                .borrow-btn.disabled-btn {
                    background: var(--gray-200);
                    color: var(--gray-400);
                    cursor: not-allowed;
                }
            </style>
        </head>

        <body>

            <jsp:include page="/WEB-INF/views/common/header.jsp" />

            <section class="hero">
                <div class="hero-inner">
                    <div class="hero-tag">
                        <i class="fa-solid fa-book-open-reader"></i> Hệ thống thư viện thông minh
                    </div>
                    <h1><span>MƯỢN SÁCH</span><br>ONLINE</h1>
                    <p>Tìm kiếm và đăng ký mượn sách trực tuyến dành cho sinh viên PTIT.<br>Nhanh chóng, tiện lợi, mọi
                        lúc mọi nơi.</p>
                    <form action="${pageContext.request.contextPath}/sach/muon-sach" method="GET" class="hero-search">
                        <input type="text" name="tuKhoa" placeholder="Tìm kiếm theo tên sách, tác giả..."
                            value="${tuKhoa}" />
                        <button type="submit">
                            <i class="fa-solid fa-magnifying-glass"></i> Tìm kiếm
                        </button>
                    </form>
                </div>
            </section>

            <div class="filter-bar">
                <a href="${pageContext.request.contextPath}/sach/muon-sach"
                    class="filter-btn ${param.danhMuc == null ? 'active' : ''}">
                    <i class="fa-solid fa-grid-2"></i> Tất cả
                </a>
                <c:forEach items="${dsDanhMuc}" var="dm">
                    <a href="${pageContext.request.contextPath}/sach/muon-sach?danhMuc=${dm.maDanhMuc}"
                        class="filter-btn ${param.danhMuc == dm.maDanhMuc ? 'active' : ''}">
                        ${dm.tenDanhMuc}
                    </a>
                </c:forEach>
            </div>

            <main class="main">
                <div class="section-head">
                    <h2>Tài liệu</h2>
                </div>

                <div class="book-grid">
                    <c:forEach items="${dsTaiLieu}" var="sach">
                        <a href="${pageContext.request.contextPath}/sach/chi-tiet/${sach.maTaiLieu}"
                            class="book-card">
                            <div class="book-cover">
                                <img src="${sach.hinhAnh}"
                                    onerror="this.src='${pageContext.request.contextPath}/tai-nguyen/book.jpg'" />
                                <span class="book-badge">${sach.danhMuc.tenDanhMuc}</span>
                            </div>
                            <div class="book-body">
                                <div class="book-cat">${sach.danhMuc.tenDanhMuc}</div>
                                <div class="book-title">${sach.tenTaiLieu}</div>
                                <div class="book-author">${sach.tacGia} · ${sach.namXuatBan}</div>
                                <div class="book-footer">
                                    <div class="stock-info">
                                        <div class="stock-label">Còn có thể mượn</div>
                                        <div class="stock-num ${sach.soLuongCon == 0 ? 'out' : ''}">${sach.soLuongCon ==
                                            0 ? 'Hết sách' : sach.soLuongCon}</div>
                                    </div>
                                    <button class="borrow-btn ${sach.soLuongCon == 0 ? 'disabled-btn' : ''}"
                                        onclick="event.preventDefault(); window.location.href='${pageContext.request.contextPath}/sach/dang-ky-muon/${sach.maTaiLieu}'"
                                        ${sach.soLuongCon==0 ? 'disabled' : '' }>
                                        ${sach.soLuongCon > 0 ? '<i class="fa-solid fa-book-bookmark"></i> Mượn' : '<i
                                            class="fa-solid fa-ban"></i> Hết'}
                                    </button>
                                </div>
                            </div>
                        </a>
                    </c:forEach>
                </div>
            </main>

            <jsp:include page="/WEB-INF/views/common/footer.jsp" />

        </body>

        </html>
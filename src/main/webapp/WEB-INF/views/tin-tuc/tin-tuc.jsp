<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Tin tức - Thư viện PTIT</title>
    <meta name="description" content="Tin tức mới nhất từ Thư viện Học viện Công nghệ Bưu chính Viễn thông">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/giao-dien.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
    <style>
        .page-banner {
            position: relative;
            height: 280px;
            display: flex;
            align-items: center;
            overflow: hidden;
            background: linear-gradient(135deg, #0d2a5c 0%, #1a4a8a 50%, #c8102e 100%);
        }
        .page-banner::after {
            content: "";
            position: absolute;
            top: 0; left: 0; width: 100%; height: 100%;
            background: rgba(0,0,0,0.3);
            z-index: 1;
        }
        .page-banner-content {
            position: relative;
            z-index: 2;
            color: white;
            padding-left: 50px;
            width: 100%;
        }
        .page-banner-content .breadcrumb {
            font-size: 14px;
            margin-bottom: 10px;
            opacity: 0.9;
        }
        .page-banner-content .breadcrumb a {
            color: white;
            text-decoration: none;
        }
        .page-banner-content .breadcrumb a:hover {
            text-decoration: underline;
        }
        .page-banner-content h1 {
            font-size: 42px;
            font-weight: 800;
            margin: 0;
            text-transform: uppercase;
        }

        /* News Section */
        .news-container {
            max-width: 1200px;
            margin: 0 auto;
            padding: 50px 20px;
        }
        .news-grid {
            display: grid;
            grid-template-columns: repeat(auto-fill, minmax(350px, 1fr));
            gap: 30px;
        }
        .news-card {
            background: white;
            border-radius: 12px;
            overflow: hidden;
            box-shadow: 0 4px 15px rgba(0,0,0,0.08);
            transition: transform 0.3s ease, box-shadow 0.3s ease;
        }
        .news-card:hover {
            transform: translateY(-5px);
            box-shadow: 0 12px 30px rgba(0,0,0,0.15);
        }
        .news-card-image {
            height: 200px;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            display: flex;
            align-items: center;
            justify-content: center;
            color: white;
            font-size: 48px;
        }
        .news-card-image img {
            width: 100%;
            height: 100%;
            object-fit: cover;
        }
        .news-card-body {
            padding: 24px;
        }
        .news-card-category {
            display: inline-block;
            padding: 4px 12px;
            background: #fee2e2;
            color: #c8102e;
            border-radius: 20px;
            font-size: 12px;
            font-weight: 600;
            margin-bottom: 12px;
        }
        .news-card-title {
            font-size: 18px;
            font-weight: 700;
            color: #0d2a5c;
            margin: 0 0 10px 0;
            line-height: 1.4;
        }
        .news-card-title a {
            text-decoration: none;
            color: inherit;
            transition: color 0.2s;
        }
        .news-card-title a:hover {
            color: #c8102e;
        }
        .news-card-summary {
            color: #666;
            font-size: 14px;
            line-height: 1.6;
            margin: 0 0 15px 0;
            display: -webkit-box;
            -webkit-line-clamp: 3;
            -webkit-box-orient: vertical;
            overflow: hidden;
        }
        .news-card-meta {
            display: flex;
            justify-content: space-between;
            align-items: center;
            font-size: 12px;
            color: #999;
            padding-top: 15px;
            border-top: 1px solid #f0f0f0;
        }
        .news-card-meta i {
            margin-right: 5px;
        }

        /* Pagination */
        .pagination-wrapper {
            display: flex;
            justify-content: center;
            gap: 8px;
            margin-top: 40px;
        }
        .page-link {
            display: inline-flex;
            align-items: center;
            justify-content: center;
            width: 40px;
            height: 40px;
            border-radius: 8px;
            background: white;
            color: #0d2a5c;
            text-decoration: none;
            font-weight: 600;
            border: 1px solid #e0e0e0;
            transition: 0.2s;
        }
        .page-link:hover, .page-link.active {
            background: #c8102e;
            color: white;
            border-color: #c8102e;
        }

        /* Empty state */
        .empty-state {
            text-align: center;
            padding: 80px 20px;
            color: #999;
        }
        .empty-state i {
            font-size: 64px;
            color: #ddd;
            margin-bottom: 20px;
        }
        .empty-state h3 {
            color: #666;
            margin-bottom: 10px;
        }
    </style>
</head>
<body>

    <jsp:include page="/WEB-INF/views/common/header.jsp"/>

    <!-- Banner -->
    <div class="page-banner">
        <div class="page-banner-content">
            <div class="breadcrumb">
                <a href="${pageContext.request.contextPath}/trang-chu">Trang chủ</a> / <span>Tin tức</span>
            </div>
            <h1><i class="fas fa-newspaper"></i> Tin tức</h1>
        </div>
    </div>

    <!-- Content -->
    <div class="news-container">
        <c:choose>
            <c:when test="${not empty trang.content}">
                <div class="news-grid">
                    <c:forEach items="${trang.content}" var="tin">
                        <div class="news-card">
                            <div class="news-card-image">
                                <c:set var="newsImg" value="${pageContext.request.contextPath}/tai-nguyen/news-default.png"/>
                                <c:if test="${not empty tin.hinhAnh}">
                                    <c:choose>
                                        <c:when test="${tin.hinhAnh.startsWith('/')}">
                                            <c:set var="newsImg" value="${pageContext.request.contextPath}${tin.hinhAnh}"/>
                                        </c:when>
                                        <c:otherwise>
                                            <c:set var="newsImg" value="${pageContext.request.contextPath}/tai-nguyen/${tin.hinhAnh}"/>
                                        </c:otherwise>
                                    </c:choose>
                                </c:if>
                                <img src="${newsImg}" alt="${tin.tieuDe}"
                                     onerror="this.onerror=null; this.src='${pageContext.request.contextPath}/tai-nguyen/news-default.png';">
                            </div>
                            <div class="news-card-body">
                                <c:if test="${not empty tin.danhMucTin}">
                                    <span class="news-card-category">${tin.danhMucTin}</span>
                                </c:if>
                                <h3 class="news-card-title">
                                    <a href="${pageContext.request.contextPath}/tin-tuc/${tin.maTin}">${tin.tieuDe}</a>
                                </h3>
                                <p class="news-card-summary">${tin.tomTat}</p>
                                <div class="news-card-meta">
                                    <span><i class="far fa-calendar-alt"></i> <fmt:parseDate value="${tin.ngayDang}" pattern="yyyy-MM-dd'T'HH:mm" var="parsedDate" type="both"/><fmt:formatDate value="${parsedDate}" pattern="dd/MM/yyyy"/></span>
                                    <span><i class="far fa-eye"></i> ${tin.luotXem} lượt xem</span>
                                </div>
                            </div>
                        </div>
                    </c:forEach>
                </div>

                <!-- Pagination -->
                <c:if test="${trang.totalPages > 1}">
                    <div class="pagination-wrapper">
                        <c:forEach begin="0" end="${trang.totalPages - 1}" var="i">
                            <a href="${pageContext.request.contextPath}/tin-tuc?page=${i}"
                               class="page-link ${i == trangHienTai ? 'active' : ''}">${i + 1}</a>
                        </c:forEach>
                    </div>
                </c:if>
            </c:when>
            <c:otherwise>
                <div class="empty-state">
                    <i class="fas fa-newspaper"></i>
                    <h3>Chưa có tin tức nào</h3>
                    <p>Các tin tức mới sẽ được cập nhật tại đây.</p>
                </div>
            </c:otherwise>
        </c:choose>
    </div>

    <jsp:include page="/WEB-INF/views/common/footer.jsp"/>

    <script>
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

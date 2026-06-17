<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Chi tiết tin tức - Thư viện PTIT</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/giao-dien.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
    <style>
        .page-banner {
            position: relative;
            height: 250px;
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
            font-size: 32px;
            font-weight: 800;
            margin: 0;
        }

        .article-container {
            max-width: 900px;
            margin: 0 auto;
            padding: 50px 20px;
        }
        .article-meta {
            display: flex;
            gap: 20px;
            align-items: center;
            font-size: 14px;
            color: #888;
            margin-bottom: 30px;
            padding-bottom: 20px;
            border-bottom: 1px solid #eee;
        }
        .article-meta i { margin-right: 5px; }
        .article-body {
            font-size: 16px;
            line-height: 1.9;
            color: #333;
        }
        .article-body p { margin-bottom: 15px; }
        .btn-back {
            display: inline-flex;
            align-items: center;
            gap: 8px;
            padding: 10px 20px;
            background: #0d2a5c;
            color: white;
            border-radius: 8px;
            text-decoration: none;
            font-weight: 600;
            margin-top: 30px;
            transition: 0.3s;
        }
        .btn-back:hover {
            background: #c8102e;
        }
    </style>
</head>
<body>

    <jsp:include page="/WEB-INF/views/common/header.jsp"/>

    <div class="page-banner">
        <div class="page-banner-content">
            <div class="breadcrumb">
                <a href="${pageContext.request.contextPath}/trang-chu">Trang chủ</a> /
                <a href="${pageContext.request.contextPath}/tin-tuc">Tin tức</a> /
                <span>Chi tiết</span>
            </div>
            <h1>${tinTuc.tieuDe}</h1>
        </div>
    </div>

    <div class="article-container">
        <c:if test="${not empty tinTuc}">
            <div class="article-meta">
                <span><i class="far fa-calendar-alt"></i> <fmt:parseDate value="${tinTuc.ngayDang}" pattern="yyyy-MM-dd'T'HH:mm" var="parsedDate" type="both"/><fmt:formatDate value="${parsedDate}" pattern="dd/MM/yyyy HH:mm"/></span>
                <span><i class="far fa-eye"></i> ${tinTuc.luotXem} lượt xem</span>
                <c:if test="${not empty tinTuc.danhMucTin}">
                    <span style="padding: 3px 12px; background: #fee2e2; color: #c8102e; border-radius: 20px; font-size: 12px; font-weight: 600;">${tinTuc.danhMucTin}</span>
                </c:if>
            </div>
            <div class="article-body">
                ${tinTuc.noiDung}
            </div>
        </c:if>
        <a href="${pageContext.request.contextPath}/tin-tuc" class="btn-back">
            <i class="fas fa-arrow-left"></i> Quay lại danh sách
        </a>
    </div>

    <jsp:include page="/WEB-INF/views/common/footer.jsp"/>

    <script>
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

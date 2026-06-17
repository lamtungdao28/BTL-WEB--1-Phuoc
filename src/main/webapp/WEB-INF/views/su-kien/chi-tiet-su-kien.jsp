<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Chi tiết sự kiện - Thư viện PTIT</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/giao-dien.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
    <style>
        .page-banner {
            position: relative;
            height: 250px;
            display: flex;
            align-items: center;
            overflow: hidden;
            background: linear-gradient(135deg, #0d2a5c 0%, #2d5aa0 50%, #c8102e 100%);
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
        .event-info-box {
            background: #f8fafc;
            border-radius: 12px;
            padding: 24px;
            margin-bottom: 30px;
            border-left: 4px solid #c8102e;
        }
        .event-info-item {
            display: flex;
            align-items: center;
            gap: 10px;
            padding: 8px 0;
            font-size: 15px;
            color: #555;
        }
        .event-info-item i {
            color: #c8102e;
            width: 20px;
            text-align: center;
        }
        .article-body {
            font-size: 16px;
            line-height: 1.9;
            color: #333;
        }
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
                <a href="${pageContext.request.contextPath}/su-kien">Sự kiện</a> /
                <span>Chi tiết</span>
            </div>
            <h1>${suKien.tieuDe}</h1>
        </div>
    </div>

    <div class="article-container">
        <c:if test="${not empty suKien}">
            <div class="event-info-box">
                <c:if test="${not empty suKien.ngayBatDau}">
                    <div class="event-info-item">
                        <i class="far fa-calendar-alt"></i>
                        <strong>Ngày:</strong>
                        <fmt:parseDate value="${suKien.ngayBatDau}" pattern="yyyy-MM-dd" var="parsedStart" type="date"/>
                        <fmt:formatDate value="${parsedStart}" pattern="dd/MM/yyyy"/>
                        <c:if test="${not empty suKien.ngayKetThuc}">
                            - <fmt:parseDate value="${suKien.ngayKetThuc}" pattern="yyyy-MM-dd" var="parsedEnd" type="date"/><fmt:formatDate value="${parsedEnd}" pattern="dd/MM/yyyy"/>
                        </c:if>
                    </div>
                </c:if>
                <c:if test="${not empty suKien.gioBatDau}">
                    <div class="event-info-item">
                        <i class="far fa-clock"></i>
                        <strong>Giờ:</strong> ${suKien.gioBatDau}
                        <c:if test="${not empty suKien.gioKetThuc}"> - ${suKien.gioKetThuc}</c:if>
                    </div>
                </c:if>
                <c:if test="${not empty suKien.diaDiem}">
                    <div class="event-info-item">
                        <i class="fas fa-map-marker-alt"></i>
                        <strong>Địa điểm:</strong> ${suKien.diaDiem}
                    </div>
                </c:if>
                <div class="event-info-item">
                    <i class="fas fa-info-circle"></i>
                    <strong>Trạng thái:</strong>
                    <c:choose>
                        <c:when test="${suKien.trangThai == 'SAP_DIEN_RA'}">
                            <span style="padding:3px 12px;background:#dcfce7;color:#15803d;border-radius:20px;font-size:12px;font-weight:600;">Sắp diễn ra</span>
                        </c:when>
                        <c:when test="${suKien.trangThai == 'DANG_DIEN_RA'}">
                            <span style="padding:3px 12px;background:#dbeafe;color:#1d4ed8;border-radius:20px;font-size:12px;font-weight:600;">Đang diễn ra</span>
                        </c:when>
                        <c:otherwise>
                            <span style="padding:3px 12px;background:#f1f5f9;color:#64748b;border-radius:20px;font-size:12px;font-weight:600;">Đã kết thúc</span>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
            <div class="article-body">
                ${suKien.moTa}
            </div>
        </c:if>
        <a href="${pageContext.request.contextPath}/su-kien" class="btn-back">
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

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Sự kiện - Thư viện PTIT</title>
    <meta name="description" content="Sự kiện thư viện Học viện Công nghệ Bưu chính Viễn thông">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/giao-dien.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
    <style>
        .page-banner {
            position: relative;
            height: 280px;
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
        .page-banner-content .breadcrumb a:hover {
            text-decoration: underline;
        }
        .page-banner-content h1 {
            font-size: 42px;
            font-weight: 800;
            margin: 0;
            text-transform: uppercase;
        }

        .events-container {
            max-width: 1200px;
            margin: 0 auto;
            padding: 50px 20px;
        }
        .section-title {
            font-size: 24px;
            font-weight: 800;
            color: #0d2a5c;
            margin-bottom: 30px;
            padding-bottom: 12px;
            border-bottom: 3px solid #c8102e;
            display: inline-block;
        }
        .section-title i {
            color: #c8102e;
            margin-right: 8px;
        }
        .events-section {
            margin-bottom: 50px;
        }

        .event-card {
            display: flex;
            background: white;
            border-radius: 12px;
            overflow: hidden;
            box-shadow: 0 4px 15px rgba(0,0,0,0.08);
            margin-bottom: 20px;
            transition: transform 0.3s ease, box-shadow 0.3s ease;
        }
        .event-card:hover {
            transform: translateY(-3px);
            box-shadow: 0 10px 25px rgba(0,0,0,0.12);
        }
        .event-date-box {
            min-width: 120px;
            display: flex;
            flex-direction: column;
            align-items: center;
            justify-content: center;
            padding: 20px;
            color: white;
        }
        .event-date-box.upcoming {
            background: linear-gradient(135deg, #c8102e, #e53935);
        }
        .event-date-box.past {
            background: linear-gradient(135deg, #64748b, #94a3b8);
        }
        .event-date-day {
            font-size: 36px;
            font-weight: 800;
            line-height: 1;
        }
        .event-date-month {
            font-size: 14px;
            font-weight: 600;
            text-transform: uppercase;
            margin-top: 4px;
        }
        .event-info {
            padding: 20px 24px;
            flex: 1;
        }
        .event-title {
            font-size: 18px;
            font-weight: 700;
            color: #0d2a5c;
            margin: 0 0 10px 0;
        }
        .event-title a {
            text-decoration: none;
            color: inherit;
            transition: color 0.2s;
        }
        .event-title a:hover {
            color: #c8102e;
        }
        .event-details {
            display: flex;
            flex-wrap: wrap;
            gap: 20px;
            font-size: 14px;
            color: #666;
        }
        .event-details span i {
            color: #c8102e;
            margin-right: 6px;
            width: 16px;
        }
        .event-status {
            display: inline-block;
            padding: 4px 12px;
            border-radius: 20px;
            font-size: 12px;
            font-weight: 600;
            margin-top: 10px;
        }
        .status-upcoming {
            background: #dcfce7;
            color: #15803d;
        }
        .status-ongoing {
            background: #dbeafe;
            color: #1d4ed8;
        }
        .status-past {
            background: #f1f5f9;
            color: #64748b;
        }

        .empty-state {
            text-align: center;
            padding: 60px 20px;
            color: #999;
        }
        .empty-state i {
            font-size: 48px;
            color: #ddd;
            margin-bottom: 15px;
        }
    </style>
</head>
<body>

    <jsp:include page="/WEB-INF/views/common/header.jsp"/>

    <!-- Banner -->
    <div class="page-banner">
        <div class="page-banner-content">
            <div class="breadcrumb">
                <a href="${pageContext.request.contextPath}/trang-chu">Trang chủ</a> / <span>Sự kiện</span>
            </div>
            <h1><i class="fas fa-calendar-star"></i> Sự kiện</h1>
        </div>
    </div>

    <!-- Content -->
    <div class="events-container">

        <!-- Sắp diễn ra -->
        <div class="events-section">
            <h2 class="section-title"><i class="fas fa-clock"></i>Sắp diễn ra</h2>
            <c:choose>
                <c:when test="${not empty dsSapDienRa}">
                    <c:forEach items="${dsSapDienRa}" var="sk">
                        <div class="event-card">
                            <div class="event-date-box upcoming">
                                <div class="event-date-day">
                                    <fmt:parseDate value="${sk.ngayBatDau}" pattern="yyyy-MM-dd" var="parsedNgay" type="date"/>
                                    <fmt:formatDate value="${parsedNgay}" pattern="dd"/>
                                </div>
                                <div class="event-date-month">
                                    Thg <fmt:formatDate value="${parsedNgay}" pattern="MM"/>
                                </div>
                            </div>
                            <div class="event-info">
                                <h3 class="event-title">
                                    <a href="${pageContext.request.contextPath}/su-kien/${sk.maSuKien}">${sk.tieuDe}</a>
                                </h3>
                                <div class="event-details">
                                    <span><i class="far fa-calendar-alt"></i>
                                        <fmt:formatDate value="${parsedNgay}" pattern="dd/MM/yyyy"/>
                                        <c:if test="${not empty sk.ngayKetThuc}">
                                            - <fmt:parseDate value="${sk.ngayKetThuc}" pattern="yyyy-MM-dd" var="parsedEnd" type="date"/><fmt:formatDate value="${parsedEnd}" pattern="dd/MM/yyyy"/>
                                        </c:if>
                                    </span>
                                    <c:if test="${not empty sk.gioBatDau}">
                                        <span><i class="far fa-clock"></i> ${sk.gioBatDau}
                                            <c:if test="${not empty sk.gioKetThuc}"> - ${sk.gioKetThuc}</c:if>
                                        </span>
                                    </c:if>
                                    <c:if test="${not empty sk.diaDiem}">
                                        <span><i class="fas fa-map-marker-alt"></i> ${sk.diaDiem}</span>
                                    </c:if>
                                </div>
                                <span class="event-status status-upcoming">Sắp diễn ra</span>
                            </div>
                        </div>
                    </c:forEach>
                </c:when>
                <c:otherwise>
                    <div class="empty-state">
                        <i class="fas fa-calendar-plus"></i>
                        <p>Chưa có sự kiện sắp diễn ra.</p>
                    </div>
                </c:otherwise>
            </c:choose>
        </div>

        <!-- Đã diễn ra -->
        <div class="events-section">
            <h2 class="section-title"><i class="fas fa-history"></i>Đã diễn ra</h2>
            <c:choose>
                <c:when test="${not empty dsDaDienRa}">
                    <c:forEach items="${dsDaDienRa}" var="sk">
                        <div class="event-card">
                            <div class="event-date-box past">
                                <div class="event-date-day">
                                    <fmt:parseDate value="${sk.ngayBatDau}" pattern="yyyy-MM-dd" var="parsedNgay2" type="date"/>
                                    <fmt:formatDate value="${parsedNgay2}" pattern="dd"/>
                                </div>
                                <div class="event-date-month">
                                    Thg <fmt:formatDate value="${parsedNgay2}" pattern="MM"/>
                                </div>
                            </div>
                            <div class="event-info">
                                <h3 class="event-title">
                                    <a href="${pageContext.request.contextPath}/su-kien/${sk.maSuKien}">${sk.tieuDe}</a>
                                </h3>
                                <div class="event-details">
                                    <span><i class="far fa-calendar-alt"></i>
                                        <fmt:formatDate value="${parsedNgay2}" pattern="dd/MM/yyyy"/>
                                    </span>
                                    <c:if test="${not empty sk.diaDiem}">
                                        <span><i class="fas fa-map-marker-alt"></i> ${sk.diaDiem}</span>
                                    </c:if>
                                </div>
                                <span class="event-status status-past">Đã kết thúc</span>
                            </div>
                        </div>
                    </c:forEach>
                </c:when>
                <c:otherwise>
                    <div class="empty-state">
                        <i class="fas fa-calendar-check"></i>
                        <p>Chưa có sự kiện nào đã diễn ra.</p>
                    </div>
                </c:otherwise>
            </c:choose>
        </div>

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

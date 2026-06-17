<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Thông báo - Thư viện PTIT</title>
    <meta name="description" content="Thông báo từ Thư viện Học viện Công nghệ Bưu chính Viễn thông">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/giao-dien.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
    <style>
        .page-banner {
            position: relative;
            height: 280px;
            display: flex;
            align-items: center;
            overflow: hidden;
            background: linear-gradient(135deg, #0d2a5c 0%, #1a4a8a 60%, #c8102e 100%);
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

        .tb-container {
            max-width: 1000px;
            margin: 0 auto;
            padding: 50px 20px;
        }

        .tb-list {
            display: flex;
            flex-direction: column;
            gap: 16px;
        }

        .tb-item {
            display: flex;
            align-items: flex-start;
            gap: 20px;
            padding: 24px;
            background: white;
            border-radius: 12px;
            box-shadow: 0 2px 12px rgba(0,0,0,0.06);
            border-left: 4px solid #c8102e;
            transition: transform 0.2s, box-shadow 0.2s;
        }
        .tb-item:hover {
            transform: translateX(5px);
            box-shadow: 0 6px 20px rgba(0,0,0,0.1);
        }

        .tb-icon {
            width: 48px;
            height: 48px;
            min-width: 48px;
            border-radius: 12px;
            display: flex;
            align-items: center;
            justify-content: center;
            font-size: 20px;
            color: white;
        }
        .tb-icon.important { background: linear-gradient(135deg, #c8102e, #e53935); }
        .tb-icon.muon { background: linear-gradient(135deg, #1d4ed8, #3b82f6); }
        .tb-icon.event { background: linear-gradient(135deg, #15803d, #22c55e); }
        .tb-icon.digital { background: linear-gradient(135deg, #7c3aed, #a78bfa); }
        .tb-icon.default { background: linear-gradient(135deg, #64748b, #94a3b8); }

        .tb-content { flex: 1; }
        .tb-title {
            font-size: 16px;
            font-weight: 700;
            color: #0d2a5c;
            margin: 0 0 6px 0;
        }
        .tb-title a {
            text-decoration: none;
            color: inherit;
            transition: color 0.2s;
        }
        .tb-title a:hover {
            color: #c8102e;
        }
        .tb-excerpt {
            font-size: 14px;
            color: #666;
            line-height: 1.6;
            margin: 0 0 10px 0;
            display: -webkit-box;
            -webkit-line-clamp: 2;
            -webkit-box-orient: vertical;
            overflow: hidden;
        }
        .tb-meta {
            display: flex;
            gap: 15px;
            font-size: 12px;
            color: #999;
        }
        .tb-meta i { margin-right: 4px; }
        .tb-type-badge {
            display: inline-block;
            padding: 2px 10px;
            border-radius: 20px;
            font-size: 11px;
            font-weight: 600;
        }
        .type-important { background: #fee2e2; color: #dc2626; }
        .type-muon { background: #dbeafe; color: #1d4ed8; }
        .type-event { background: #dcfce7; color: #15803d; }
        .type-digital { background: #f3e8ff; color: #7c3aed; }
        .type-default { background: #f1f5f9; color: #64748b; }

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
                <a href="${pageContext.request.contextPath}/trang-chu">Trang chủ</a> / <span>Thông báo</span>
            </div>
            <h1><i class="fas fa-bullhorn"></i> Thông báo</h1>
        </div>
    </div>

    <!-- Content -->
    <div class="tb-container">
        <c:choose>
            <c:when test="${not empty dsThongBao}">
                <div class="tb-list">
                    <c:forEach items="${dsThongBao}" var="tb">
                        <div class="tb-item">
                            <div class="tb-icon ${not empty tb.loaiThongBao ? tb.loaiThongBao : 'default'}">
                                <c:choose>
                                    <c:when test="${tb.loaiThongBao == 'important'}"><i class="fas fa-exclamation-circle"></i></c:when>
                                    <c:when test="${tb.loaiThongBao == 'muon'}"><i class="fas fa-book-reader"></i></c:when>
                                    <c:when test="${tb.loaiThongBao == 'event'}"><i class="fas fa-calendar-alt"></i></c:when>
                                    <c:when test="${tb.loaiThongBao == 'digital'}"><i class="fas fa-laptop"></i></c:when>
                                    <c:otherwise><i class="fas fa-bell"></i></c:otherwise>
                                </c:choose>
                            </div>
                            <div class="tb-content">
                                <h3 class="tb-title">
                                    <a href="${pageContext.request.contextPath}/thong-bao/${tb.maThongBao}">${tb.tieuDe}</a>
                                </h3>
                                <c:if test="${not empty tb.noiDung}">
                                    <p class="tb-excerpt">${tb.noiDung}</p>
                                </c:if>
                                <div class="tb-meta">
                                    <span><i class="far fa-calendar-alt"></i>
                                        <fmt:parseDate value="${tb.ngayDang}" pattern="yyyy-MM-dd'T'HH:mm" var="parsedDate" type="both"/>
                                        <fmt:formatDate value="${parsedDate}" pattern="dd/MM/yyyy"/>
                                    </span>
                                    <span><i class="far fa-eye"></i> ${tb.luotXem} lượt xem</span>
                                    <c:if test="${not empty tb.loaiThongBao}">
                                        <span class="tb-type-badge type-${tb.loaiThongBao}">
                                            <c:choose>
                                                <c:when test="${tb.loaiThongBao == 'important'}">Quan trọng</c:when>
                                                <c:when test="${tb.loaiThongBao == 'muon'}">Mượn sách</c:when>
                                                <c:when test="${tb.loaiThongBao == 'event'}">Sự kiện</c:when>
                                                <c:when test="${tb.loaiThongBao == 'digital'}">Tài nguyên số</c:when>
                                                <c:otherwise>${tb.loaiThongBao}</c:otherwise>
                                            </c:choose>
                                        </span>
                                    </c:if>
                                </div>
                            </div>
                        </div>
                    </c:forEach>
                </div>
            </c:when>
            <c:otherwise>
                <div class="empty-state">
                    <i class="fas fa-bullhorn"></i>
                    <h3>Chưa có thông báo nào</h3>
                    <p>Các thông báo mới sẽ được cập nhật tại đây.</p>
                </div>
            </c:otherwise>
        </c:choose>
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

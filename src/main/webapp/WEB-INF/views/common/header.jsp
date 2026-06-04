<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%-- ===== HEADER COMPONENT (được include bởi các trang JSP) ===== --%>
<div class="topbar">
    <div class="left">
        <i class="fas fa-globe"></i>
        Cổng thông tin điện tử Học viện Công nghệ Bưu chính Viễn thông
    </div>
    <div class="right" style="display: flex; align-items: center; gap: 15px;">
        <span><i class="fas fa-clock"></i> 7h30 - 21h30</span>
        <span><i class="fas fa-envelope"></i> ilc@ptit.edu.vn</span>

        <sec:authorize access="!isAuthenticated()">
            <a href="${pageContext.request.contextPath}/dang-nhap" id="btn-login-header"
               style="color: white; text-decoration: none; font-weight: bold; background: #0d2a5c; padding: 4px 12px; border-radius: 4px;">
                <i class="fas fa-sign-in-alt"></i> Đăng nhập
            </a>
        </sec:authorize>

        <sec:authorize access="isAuthenticated()">
            <span style="color:white;"><i class="fas fa-user-circle"></i> <sec:authentication property="principal.username"/></span>
            <a href="${pageContext.request.contextPath}/dang-xuat" style="color:white;">
                <i class="fas fa-sign-out-alt"></i> Đăng xuất
            </a>
        </sec:authorize>
    </div>
</div>

<header class="header">
    <div class="left">
        <img src="${pageContext.request.contextPath}/tai-nguyen/logo.png" class="logo">
        <div class="title">
            <p class="highlight-title">THƯ VIỆN ĐIỆN TỬ</p>
            <p>HỌC VIỆN CÔNG NGHỆ BƯU CHÍNH VIỄN THÔNG</p>
        </div>
    </div>

    <nav>
        <a href="${pageContext.request.contextPath}/trang-chu" class="menu-home">TRANG CHỦ</a>
        <a href="${pageContext.request.contextPath}/gioi-thieu" class="menu-about">GIỚI THIỆU</a>
        <a href="${pageContext.request.contextPath}/noi-quy" class="menu-rules">NỘI QUY THƯ VIỆN</a>
        <div class="dropdown">
            <a href="#" class="menu-news">BẢN TIN▾</a>
            <div class="dropdown-menu">
                <a href="${pageContext.request.contextPath}/tin-tuc">Tin tức</a>
                <a href="${pageContext.request.contextPath}/su-kien">Sự kiện</a>
                <hr>
                <a href="${pageContext.request.contextPath}/thong-bao">Thông báo</a>
            </div>
        </div>
    </nav>

    <div style="display: flex; gap: 8px;">
        <a href="${pageContext.request.contextPath}/sach/muon-sach" class="btn-header-muon">
            <i class="fas fa-book-reader"></i> Mượn sách
        </a>
        <sec:authorize access="isAuthenticated()">
            <a href="${pageContext.request.contextPath}/sach/sach-cua-toi" class="btn-header-muon btn-header-sach-cua-toi">
                <i class="fas fa-book-open"></i> Sách của tôi
            </a>
        </sec:authorize>
        <sec:authorize access="hasRole('ADMIN')">
            <a href="${pageContext.request.contextPath}/admin/quan-ly-sach" class="btn-header-muon btn-header-admin">
                <i class="fas fa-cog"></i> Quản lý
            </a>
        </sec:authorize>
    </div>
</header>

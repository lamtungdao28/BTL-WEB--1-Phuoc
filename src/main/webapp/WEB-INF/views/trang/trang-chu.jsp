<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Thư viện PTIT</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/giao-dien.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
</head>
<body>

    <jsp:include page="/WEB-INF/views/common/header.jsp"/>

    <%-- NỘI DUNG TRANG CHỦ --%>
    <section class="slider">
        <div class="slides">
            <div class="slide active" style="background-image: url('${pageContext.request.contextPath}/tai-nguyen/slide1.jpg')"></div>
            <div class="slide" style="background-image: url('${pageContext.request.contextPath}/tai-nguyen/slide2.jpg')"></div>
            <div class="slide" style="background-image: url('${pageContext.request.contextPath}/tai-nguyen/slide3.jpg')"></div>
            <div class="slide" style="background-image: url('${pageContext.request.contextPath}/tai-nguyen/slide4.jpg')"></div>
        </div>

        <button class="prev" onclick="prevSlide()">❮</button>
        <button class="next" onclick="nextSlide()">❯</button>

        <div class="dots">
            <span onclick="goToSlide(0)"></span>
            <span onclick="goToSlide(1)"></span>
            <span onclick="goToSlide(2)"></span>
            <span onclick="goToSlide(3)"></span>
        </div>
    </section>

    <!-- Tính năng -->
    <section id="tinh-nang" class="tinh-nang">
        <div class="box">
            <a href="#" class="box-link"></a>
            <div class="circle">
                <img src="${pageContext.request.contextPath}/tai-nguyen/book.jpg" alt="Tài nguyên in">
            </div>
            <h3>TÀI NGUYÊN IN</h3>
            <p>Tìm kiếm sách in, báo, tạp chí tại Thư viện</p>
        </div>

        <div class="box">
            <a href="#" class="box-link"></a>
            <div class="circle">
                <img src="${pageContext.request.contextPath}/tai-nguyen/ebook.jpg" alt="Tài nguyên số">
            </div>
            <h3>TÀI NGUYÊN SỐ</h3>
            <p>Tìm kiếm e-book, e-journal, research paper... thuộc các nhà xuất bản lớn</p>
        </div>

        <div class="box">
            <a href="${pageContext.request.contextPath}/noi-quy" class="box-link"></a>
            <div class="circle">
                <img src="${pageContext.request.contextPath}/tai-nguyen/clock.jpg" alt="Giờ mở cửa">
            </div>
            <h3>GIỜ MỞ CỬA</h3>
            <p><strong>Thời gian mở cửa:</strong> 07h30 - 21h30.<br>Thứ 7 theo giờ hành chính</p>
        </div>
    </section>

    <!-- Chỉ dẫn -->
    <section class="chi-dan-section">
        <div class="left-box">
            <div class="red-line"></div>
            <p class="sub-brand">THƯ VIỆN PTIT</p>
            <h2 class="main-heading">Chỉ dẫn</h2>
            <div class="guide-items">
                <div class="item">
                    <a href="${pageContext.request.contextPath}/noi-quy">
                        <i class="fa-solid fa-circle-play"></i>
                        <span>Nội quy thư viện</span>
                    </a>
                </div>
            </div>
        </div>

        <div class="right-box">
            <div class="navy-frame">
                <div class="white-inner-border">
                    <img src="${pageContext.request.contextPath}/tai-nguyen/anhchidan.jpg" alt="Tài nguyên điện tử">
                </div>
            </div>
        </div>
    </section>

    <!-- Media Thư viện -->
    <section class="thu-vien-media">
        <div class="media-left">
            <img src="${pageContext.request.contextPath}/tai-nguyen/video.jpg" alt="">
            <div class="play-btn"></div>
        </div>
        <div class="media-right">
            <img src="${pageContext.request.contextPath}/tai-nguyen/anh1.jpg" alt="">
            <img src="${pageContext.request.contextPath}/tai-nguyen/anh2.jpg" alt="">
        </div>
    </section>

    <%-- Danh sách tin tức mới nhất --%>
    <section class="news-section" style="padding: 40px 10%; background-color: #f9f9f9;">
        <h2 style="text-align: center; color: #0d2a5c; margin-bottom: 30px;">TIN TỨC MỚI NHẤT</h2>
        <div class="news-list" style="display: flex; gap: 20px; flex-wrap: wrap;">
            <c:forEach items="${dsTinTuc}" var="tin">
                <div class="news-card" style="background: white; padding: 20px; border-radius: 8px; box-shadow: 0 4px 6px rgba(0,0,0,0.1); width: calc(33.333% - 20px);">
                    <h3 style="margin-top: 0;"><a href="${pageContext.request.contextPath}/tin-tuc/${tin.maTin}" style="text-decoration: none; color: #c8102e;">${tin.tieuDe}</a></h3>
                    <p style="color: #666;">${tin.tomTat}</p>
                </div>
            </c:forEach>
        </div>
    </section>

    <jsp:include page="/WEB-INF/views/common/footer.jsp"/>

    <script src="${pageContext.request.contextPath}/js/slider.js"></script>
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

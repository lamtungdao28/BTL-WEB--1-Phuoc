<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Chi tiết sách - PTIT Library</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/chi-tiet-sach.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/giao-dien.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
</head>
<body>

    <jsp:include page="/WEB-INF/views/common/header.jsp"/>

    <section class="book-detail">
        <!-- LEFT: ảnh bìa -->
        <div class="book-image">
            <img src="${taiLieu.hinhAnh}" alt="${taiLieu.tenTaiLieu}" onerror="this.src='${pageContext.request.contextPath}/tai-nguyen/book.jpg'">
        </div>

        <!-- RIGHT: thông tin -->
        <div class="book-info">
            <span class="book-category">${taiLieu.danhMuc.tenDanhMuc}</span>
            <h1>${taiLieu.tenTaiLieu}</h1>
            <p class="author">Tác giả: <span>${taiLieu.tacGia}</span></p>

            <!-- META -->
            <div class="book-meta">
                <div>
                    <strong>Nhà xuất bản</strong>
                    <p>${empty taiLieu.nxb ? '—' : taiLieu.nxb}</p>
                </div>
                <div>
                    <strong>Năm xuất bản</strong>
                    <p>${taiLieu.namXuatBan}</p>
                </div>
                <div>
                    <strong>Vị trí kệ</strong>
                    <p>${empty taiLieu.viTriKe ? '—' : taiLieu.viTriKe}</p>
                </div>
                <div>
                    <strong>Số lượng còn</strong>
                    <p class="stock" style="${taiLieu.soLuongCon == 0 ? 'color: red;' : ''}">${taiLieu.soLuongCon == 0 ? 'Hết sách' : taiLieu.soLuongCon}</p>
                </div>
            </div>

            <div class="description">
                <h3>Mô tả sách</h3>
                <p>${empty taiLieu.moTa ? 'Chưa có mô tả.' : taiLieu.moTa}</p>
            </div>

            <!-- BUTTON -->
            <div class="book-actions">
                <c:choose>
                    <c:when test="${taiLieu.soLuongCon > 0}">
                        <button class="borrow-btn" onclick="window.location.href='${pageContext.request.contextPath}/sach/dang-ky-muon/${taiLieu.maTaiLieu}'">
                            Mượn sách
                        </button>
                    </c:when>
                    <c:otherwise>
                        <button class="borrow-btn" style="background: gray; cursor: not-allowed;" disabled>
                            Hết sách
                        </button>
                    </c:otherwise>
                </c:choose>

                <button class="save-btn" onclick="history.back()">
                    ← Quay lại
                </button>
            </div>
        </div>
    </section>

    <jsp:include page="/WEB-INF/views/common/footer.jsp"/>

</body>
</html>

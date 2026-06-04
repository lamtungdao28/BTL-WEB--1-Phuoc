<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Đăng nhập - Thư viện PTIT</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/giao-dien.css">
    <style>
        body { margin: 0; overflow: hidden; background: #f4f6f9; font-family: Arial, sans-serif; }
        .container {
            position: relative;
            height: 100vh;
            width: 100vw;
            display: flex;
            justify-content: center;
            align-items: center;
        }
        .login-box {
            width: 350px;
            padding: 30px;
            background: white;
            border-radius: 12px;
            box-shadow: 0 8px 25px rgba(0,0,0,0.12);
            text-align: center;
            z-index: 10;
            position: relative;
        }
        .logo { height: 60px; margin-bottom: 15px; }
        h2 { margin: 5px 0; font-size: 24px; }
        .sub { color: #666; margin-bottom: 25px; }
        input[type="text"], input[type="email"], input[type="password"] {
            width: 100%; padding: 12px; margin: 10px 0;
            border: 1px solid #ddd; border-radius: 6px;
            outline: none; font-size: 15px; box-sizing: border-box;
        }
        input:focus { border-color: #c8102e; }
        button {
            width: 100%; padding: 12px; background: #c8102e;
            color: white; border: none; border-radius: 6px;
            font-weight: bold; font-size: 16px; cursor: pointer;
            transition: 0.3s; margin-top: 10px;
        }
        button:hover { background: #a00d25; }
        
        .left-img, .right-img { position: absolute; z-index: 1; pointer-events: none; }
        .left-img { left: 5%; bottom: 10%; }
        .left-img img { width: 280px; }
        .right-img { right: 5%; bottom: 5%; }
        .right-img img { width: 450px; }
        @media (max-width: 1000px) {
            .left-img, .right-img { display: none; }
        }
    </style>
</head>
<body>
    <div class="container">
        <div class="login-box">
            <img src="${pageContext.request.contextPath}/tai-nguyen/logo.png" class="logo" alt="Logo">
            <h2>Đăng nhập</h2>
            <p class="sub">Thư viện PTIT</p>

            <%-- Hiển thị lỗi nếu có --%>
            <c:if test="${param.error != null}">
                <p class="error-msg" style="display:block; color:#c8102e;">
                    Email hoặc mật khẩu không đúng!
                </p>
            </c:if>
            <c:if test="${param.logout != null}">
                <p style="color:green;">Bạn đã đăng xuất thành công.</p>
            </c:if>

            <%-- Form đăng nhập (Spring Security xử lý) --%>
            <form action="${pageContext.request.contextPath}/xu-ly-dang-nhap" method="post">
                <input type="email" name="email" placeholder="Email" required>
                <input type="password" name="matKhau" placeholder="Mật khẩu" required>
                <button type="submit">Đăng nhập</button>
            </form>
            <p style="margin-top:15px; font-size:14px; color:#666;">
                Chưa có tài khoản?
                <a href="${pageContext.request.contextPath}/dang-ky" style="color:#c8102e; font-weight:600;">Đăng ký</a>
            </p>
        </div>
        <div class="left-img"><img src="${pageContext.request.contextPath}/tai-nguyen/cay.png" alt="Decoration"></div>
        <div class="right-img"><img src="${pageContext.request.contextPath}/tai-nguyen/code.png" alt="Decoration"></div>
    </div>
</body>
</html>

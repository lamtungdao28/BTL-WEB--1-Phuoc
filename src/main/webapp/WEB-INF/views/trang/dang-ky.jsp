<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Đăng ký - Thư viện PTIT</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/giao-dien.css">
    <style>
        * { box-sizing: border-box; }
        body { margin: 0; font-family: Arial, sans-serif; background: #f4f6f9; overflow: hidden; }
        .container {
            position: relative;
            min-height: 100vh; display: flex;
            justify-content: center; align-items: center;
        }
        .register-box {
            position: relative; z-index: 10;
            width: 420px; padding: 30px; background: white;
            border-radius: 12px; box-shadow: 0 8px 25px rgba(0,0,0,0.12);
            text-align: center;
        }
        .logo { height: 60px; margin-bottom: 15px; }
        h2 { margin: 5px 0; font-size: 24px; }
        .sub { color: #666; margin-bottom: 20px; }
        .form-group { margin-bottom: 12px; text-align: left; }
        .form-group label { font-size: 13px; font-weight: 600; color: #555; display: block; margin-bottom: 4px; }
        input[type="text"], input[type="password"], input[type="email"] {
            width: 100%; padding: 11px 14px; margin: 0;
            border: 1px solid #ddd; border-radius: 6px;
            outline: none; font-size: 14px;
        }
        input:focus { border-color: #c8102e; }
        .form-row { display: grid; grid-template-columns: 1fr 1fr; gap: 10px; }
        button {
            width: 100%; padding: 12px; background: #c8102e;
            color: white; border: none; border-radius: 6px;
            font-weight: bold; font-size: 16px; cursor: pointer;
            transition: 0.3s; margin-top: 10px;
        }
        button:hover { background: #a00d25; }
        .login-link { margin-top: 15px; font-size: 14px; color: #666; }
        .login-link a { color: #c8102e; text-decoration: none; font-weight: 600; }
        .error-msg { color: #c8102e; font-size: 13px; margin-bottom: 10px; }
        .success-msg { color: #16a34a; font-size: 13px; margin-bottom: 10px; }
        
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
    <div class="register-box">
        <img src="${pageContext.request.contextPath}/tai-nguyen/logo.png" class="logo" alt="Logo">
        <h2>Đăng ký tài khoản</h2>
        <p class="sub">Thư viện PTIT</p>

        <c:if test="${not empty error}">
            <p class="error-msg">${error}</p>
        </c:if>

        <form action="${pageContext.request.contextPath}/dang-ky" method="post">
            <div class="form-group">
                <label>Tài khoản *</label>
                <input type="text" name="taiKhoan" placeholder="vd: nguyenvana.b22.tc001_1"
                       value="${taiKhoan}" required>
            </div>

            <div class="form-group">
                <label>Mật khẩu *</label>
                <input type="password" name="matKhau" placeholder="Nhập mật khẩu" required>
            </div>

            <div class="form-group">
                <label>Họ và tên *</label>
                <input type="text" name="hoTen" placeholder="Nguyễn Văn A"
                       value="${hoTen}" required>
            </div>

            <div class="form-row">
                <div class="form-group">
                    <label>Email *</label>
                    <input type="email" name="email" placeholder="abc@stu.ptit.edu.vn"
                           value="${email}" required>
                </div>
                <div class="form-group">
                    <label>Số điện thoại</label>
                    <input type="text" name="soDienThoai" placeholder="09xxxxxxxx">
                </div>
            </div>

            <div class="form-group">
                <label>Lớp</label>
                <input type="text" name="lop" placeholder="D22CQCN01">
            </div>

            <button type="submit">Đăng ký</button>
        </form>

        <p class="login-link">
            Đã có tài khoản? <a href="${pageContext.request.contextPath}/dang-nhap">Đăng nhập</a>
        </p>
    </div>
    <div class="left-img"><img src="${pageContext.request.contextPath}/tai-nguyen/cay.png" alt="Decoration"></div>
    <div class="right-img"><img src="${pageContext.request.contextPath}/tai-nguyen/code.png" alt="Decoration"></div>
</div>
</body>
</html>

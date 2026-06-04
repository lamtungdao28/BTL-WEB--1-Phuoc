<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Đăng Ký Mượn Sách - PTIT Library</title>
    <link href="https://fonts.googleapis.com/css2?family=Be+Vietnam+Pro:wght@300;400;500;600;700;800;900&display=swap" rel="stylesheet" />
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css" />
    <!-- Global CSS for header/footer -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/giao-dien.css">

    <style>
        :root {
            --red:        #c8102e;
            --red-dark:   #a00d24;
            --red-deeper: #7a0a1b;
            --red-light:  #fde8ec;
            --green:      #16a34a;
            --green-bg:   #dcfce7;
            --gray-50:    #f9fafb;
            --gray-100:   #f3f4f6;
            --gray-200:   #e5e7eb;
            --gray-300:   #d1d5db;
            --gray-400:   #9ca3af;
            --gray-500:   #6b7280;
            --gray-700:   #374151;
            --gray-900:   #111827;
            --white:      #ffffff;
            --shadow-sm:  0 1px 3px rgba(0,0,0,.08);
            --shadow:     0 2px 8px rgba(0,0,0,.09);
            --shadow-md:  0 4px 16px rgba(0,0,0,.1);
            --radius:     14px;
            --radius-sm:  10px;
        }

        body {
            font-family: 'Be Vietnam Pro', sans-serif;
            background: var(--gray-50);
            color: var(--gray-900);
            min-height: 100vh;
            margin: 0; padding: 0;
        }

        /* ── PAGE HEADER STRIP ── */
        .page-strip {
            background: linear-gradient(135deg, var(--red-deeper), var(--red));
            color: var(--white);
            padding: 28px 80px 80px;
        }
        .back-link {
            display: inline-flex; align-items: center; gap: 8px; color: rgba(255,255,255,.8);
            font-size: 13.5px; font-weight: 500; text-decoration: none; cursor: pointer;
            transition: color .18s; margin-bottom: 20px;
        }
        .back-link:hover { color: var(--white); }
        .strip-title { font-size: 28px; font-weight: 900; letter-spacing: -.5px; }
        .strip-sub   { font-size: 14px; opacity: .78; margin-top: 6px; }

        .breadcrumb { display: flex; align-items: center; gap: 6px; font-size: 12.5px; opacity: .75; margin-top: 10px; }
        .breadcrumb a { color: rgba(255,255,255,.75); text-decoration: none; }
        .breadcrumb a:hover { color: var(--white); }
        .breadcrumb .sep { opacity: .5; }

        /* ── CONTENT ── */
        .content {
            max-width: 1200px;
            margin: -48px auto 60px;
            padding: 0 32px;
            position: relative; z-index: 2;
        }

        .steps {
            display: flex; align-items: center; gap: 0; background: var(--white);
            border-radius: 14px 14px 0 0; border: 1px solid var(--gray-200);
            border-bottom: none; padding: 0 28px; overflow: hidden;
        }
        .step { display: flex; align-items: center; gap: 10px; padding: 18px 20px; font-size: 13.5px; font-weight: 500; color: var(--gray-400); white-space: nowrap; }
        .step.active { color: var(--red); font-weight: 700; }
        .step.done   { color: var(--green); }
        .step-num { width: 28px; height: 28px; border-radius: 50%; background: var(--gray-100); color: var(--gray-400); display: flex; align-items: center; justify-content: center; font-size: 12.5px; font-weight: 800; }
        .step.active .step-num { background: var(--red); color: var(--white); }
        .step.done   .step-num { background: var(--green-bg); color: var(--green); }
        .step-sep { flex: 1; height: 1px; background: var(--gray-200); min-width: 20px; max-width: 60px; }

        .card-grid { display: grid; grid-template-columns: 1fr 1fr; gap: 20px; margin-bottom: 20px; }

        .card { background: var(--white); border: 1px solid var(--gray-200); border-radius: var(--radius); padding: 24px 28px; box-shadow: var(--shadow-sm); display: flex; flex-direction: column; }
        .card-title { display: flex; align-items: center; gap: 10px; font-size: 16px; font-weight: 800; color: var(--gray-900); margin-bottom: 20px; padding-bottom: 16px; border-bottom: 1px dashed var(--gray-200); }
        .card-title i { color: var(--red); font-size: 18px; }

        .book-layout { display: flex; gap: 24px; margin-bottom: 24px; }
        .book-cover-wrap { width: 110px; flex-shrink: 0; }
        .book-cover-img { width: 100%; border-radius: 8px; box-shadow: var(--shadow); object-fit: cover; }
        .book-meta h3 { font-size: 17px; font-weight: 700; line-height: 1.35; margin-bottom: 12px; }
        .meta-row { font-size: 13.5px; color: var(--gray-500); display: flex; align-items: center; gap: 8px; margin-bottom: 8px; }
        .meta-row i { width: 16px; text-align: center; color: var(--gray-400); }
        .meta-row strong { color: var(--gray-700); font-weight: 600; min-width: 80px; }
        .meta-row span { color: var(--gray-900); font-weight: 500; }
        .stock-banner { background: var(--green-bg); color: var(--green); padding: 12px 16px; border-radius: var(--radius-sm); font-size: 13px; font-weight: 500; display: flex; align-items: center; gap: 10px; margin-top: auto; }

        .user-form { display: flex; flex-direction: column; gap: 16px; }
        .form-row { display: grid; grid-template-columns: 110px 1fr; align-items: center; gap: 12px; }
        .form-row label { font-size: 13px; font-weight: 600; color: var(--gray-700); }
        .form-row input { padding: 10px 14px; border: 1px solid var(--gray-200); background: var(--gray-50); border-radius: 8px; font-size: 13.5px; font-weight: 500; color: var(--gray-900); font-family: inherit; width: 100%; outline: none; }
        .status-tag { display: inline-flex; align-items: center; gap: 6px; background: var(--green-bg); color: var(--green); padding: 5px 12px; border-radius: 20px; font-size: 12px; font-weight: 700; }

        .info-banner { background: var(--orange-bg); border: 1px solid rgba(234,88,12,.2); color: var(--orange); padding: 16px 24px; border-radius: var(--radius); display: flex; gap: 14px; font-size: 13.5px; line-height: 1.5; margin-bottom: 20px; }
        .info-banner i { font-size: 18px; margin-top: 2px; }

        .borrow-card { background: var(--white); border: 1px solid var(--gray-200); border-radius: var(--radius); padding: 24px 28px; box-shadow: var(--shadow-sm); }
        .borrow-form-grid { display: grid; grid-template-columns: 1fr 1fr 2fr; gap: 20px; margin-bottom: 24px; }
        .field-group label { display: block; font-size: 13px; font-weight: 700; color: var(--gray-700); margin-bottom: 8px; }
        .input-icon-wrap { position: relative; }
        .input-icon-wrap input { width: 100%; padding: 12px 14px 12px 40px; border: 1px solid var(--gray-200); background: var(--gray-50); border-radius: var(--radius-sm); font-size: 14px; font-weight: 600; color: var(--gray-900); outline: none; }
        .input-icon-wrap i { position: absolute; left: 14px; top: 50%; transform: translateY(-50%); color: var(--red); font-size: 15px; }
        .field-group textarea { width: 100%; height: 46px; padding: 12px 14px; border: 1px solid var(--gray-300); border-radius: var(--radius-sm); font-size: 13.5px; font-family: inherit; outline: none; resize: none; transition: border-color .2s; }
        .field-group textarea:focus { border-color: var(--red); }

        .action-bar { display: flex; justify-content: flex-end; gap: 12px; padding-top: 20px; border-top: 1px solid var(--gray-100); }
        .btn { padding: 12px 28px; border-radius: 10px; font-size: 14.5px; font-weight: 700; cursor: pointer; border: none; font-family: inherit; display: inline-flex; align-items: center; gap: 8px; transition: all .2s; }
        .btn-cancel { background: var(--gray-100); color: var(--gray-700); }
        .btn-cancel:hover { background: var(--gray-200); color: var(--gray-900); }
        .btn-confirm { background: linear-gradient(135deg, var(--red) 0%, var(--red-dark) 100%); color: var(--white); box-shadow: 0 4px 16px rgba(200,16,46,.35); }
        .btn-confirm:hover { transform: translateY(-1px); box-shadow: 0 4px 20px rgba(160,13,36,.45); }
    </style>
</head>
<body>

    <jsp:include page="/WEB-INF/views/common/header.jsp"/>

    <div class="page-strip">
        <a class="back-link" onclick="history.back()">
            <i class="fa-solid fa-arrow-left"></i> Quay lại
        </a>
        <div class="strip-title">ĐĂNG KÝ MƯỢN SÁCH</div>
        <div class="strip-sub">Vui lòng kiểm tra thông tin trước khi xác nhận đăng ký</div>
        <div class="breadcrumb">
            <a href="${pageContext.request.contextPath}/trang-chu">Trang chủ</a>
            <span class="sep">›</span>
            <a href="${pageContext.request.contextPath}/sach/muon-sach">Mượn sách</a>
            <span class="sep">›</span>
            <span>Đăng ký mượn</span>
        </div>
    </div>

    <div class="content">
        <div class="steps">
            <div class="step done"><div class="step-num"><i class="fa-solid fa-check" style="font-size:11px"></i></div>Chọn sách</div>
            <div class="step-sep"></div>
            <div class="step active"><div class="step-num">2</div>Xác nhận mượn</div>
            <div class="step-sep"></div>
            <div class="step"><div class="step-num">3</div>Hoàn tất</div>
        </div>

        <div class="card-grid">
            <!-- Thông tin sách -->
            <div class="card">
                <div class="card-title">
                    <i class="fa-solid fa-book-open"></i> Thông tin sách
                </div>
                <div class="book-layout">
                    <div class="book-cover-wrap">
                        <img class="book-cover-img" src="${taiLieu.hinhAnh}" onerror="this.src='${pageContext.request.contextPath}/tai-nguyen/book.jpg'" />
                    </div>
                    <div class="book-meta">
                        <h3>${taiLieu.tenTaiLieu}</h3>
                        <div class="meta-row"><i class="fa-regular fa-user"></i> <strong>Tác giả:</strong> <span>${taiLieu.tacGia}</span></div>
                        <div class="meta-row"><i class="fa-solid fa-building-columns"></i> <strong>NXB:</strong> <span>${taiLieu.nxb}</span></div>
                        <div class="meta-row"><i class="fa-regular fa-calendar"></i> <strong>Năm XB:</strong> <span>${taiLieu.namXuatBan}</span></div>
                        <div class="meta-row"><i class="fa-regular fa-comment"></i> <strong>Thể loại:</strong> <span>${taiLieu.danhMuc.tenDanhMuc}</span></div>
                    </div>
                </div>
                <div class="stock-banner">
                    <i class="fa-solid fa-layer-group"></i>
                    Số lượng còn có thể mượn: <strong>${taiLieu.soLuongCon}</strong> cuốn
                </div>
            </div>

            <!-- Thông tin độc giả -->
            <div class="card">
                <div class="card-title">
                    <i class="fa-solid fa-user-graduate"></i> Thông tin độc giả
                </div>
                <div class="user-form">
                    <div class="form-row">
                        <label>Tài khoản</label>
                        <input type="text" value="${nguoiDung.taiKhoan}" readonly />
                    </div>
                    <div class="form-row">
                        <label>Họ và tên</label>
                        <input type="text" value="${nguoiDung.hoTen}" readonly />
                    </div>
                    <div class="form-row">
                        <label>Email</label>
                        <input type="email" value="${nguoiDung.email}" readonly />
                    </div>
                    <div class="form-row">
                        <label>Số điện thoại</label>
                        <input type="text" value="${nguoiDung.soDienThoai}" readonly />
                    </div>
                    <div class="form-row">
                        <label>Trạng thái</label>
                        <div>
                            <span class="status-tag">
                                <i class="fa-solid fa-circle" style="font-size:8px"></i> Hoạt động
                            </span>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <div class="info-banner">
            <i class="fa-solid fa-circle-info"></i>
            <div>
                <strong>Lưu ý khi mượn sách</strong><br/>
                Thời hạn mượn mặc định là <strong>14 ngày</strong>. Trả sách trễ hạn sẽ bị phạt theo quy định của thư viện.
            </div>
        </div>

        <form action="${pageContext.request.contextPath}/sach/dang-ky-muon" method="POST" class="borrow-card">
            <input type="hidden" name="maTaiLieu" value="${taiLieu.maTaiLieu}" />

            <div class="card-title">
                <i class="fa-regular fa-calendar-check"></i> Thông tin mượn
            </div>
            <div class="borrow-form-grid">
                <!-- Sử dụng js để set ngày mượn/hạn trả lên UI cho đẹp -->
                <div class="field-group">
                    <label>Ngày mượn</label>
                    <div class="input-icon-wrap">
                        <input type="text" id="borrowDate" readonly />
                        <i class="fa-regular fa-calendar"></i>
                    </div>
                </div>
                <div class="field-group">
                    <label>Hạn trả (dự kiến)</label>
                    <div class="input-icon-wrap">
                        <input type="text" id="returnDate" readonly />
                        <i class="fa-regular fa-calendar-days"></i>
                    </div>
                </div>
                <div class="field-group">
                    <label>Ghi chú (tuỳ chọn)</label>
                    <textarea name="ghiChu" placeholder="Nhập ghi chú nếu có..."></textarea>
                </div>
            </div>

            <div class="action-bar">
                <button type="button" class="btn btn-cancel" onclick="window.location.href='${pageContext.request.contextPath}/sach/muon-sach'">
                    <i class="fa-solid fa-xmark"></i> Hủy
                </button>
                <button type="submit" class="btn btn-confirm">
                    <i class="fa-solid fa-circle-check"></i> Xác nhận mượn
                </button>
            </div>
        </form>
    </div>

    <jsp:include page="/WEB-INF/views/common/footer.jsp"/>

    <script>
        const today = new Date();
        const returnDay = new Date();
        returnDay.setDate(today.getDate() + 14);

        const fmt = d => d.toLocaleDateString('vi-VN');
        document.getElementById('borrowDate').value = fmt(today);
        document.getElementById('returnDate').value = fmt(returnDay);
    </script>
</body>
</html>

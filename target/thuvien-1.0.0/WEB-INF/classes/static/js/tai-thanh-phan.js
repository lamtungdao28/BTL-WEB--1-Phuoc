// File: tai-thanh-phan.js

// Tính basePath dựa trên đường dẫn của chính script này
// Script nằm ở js/tai-thanh-phan.js, nên basePath = thư mục cha của js/
var basePath = (function() {
    var scripts = document.querySelectorAll('script[src*="tai-thanh-phan"]');
    if (scripts.length > 0) {
        var src = scripts[scripts.length - 1].getAttribute('src');
        // src có thể là "../js/tai-thanh-phan.js" hoặc "js/tai-thanh-phan.js"
        // Cắt bỏ "js/tai-thanh-phan.js" để lấy base
        return src.replace('js/tai-thanh-phan.js', '');
    }
    return '';
})();

function load(id, file) {
    var el = document.getElementById(id);
    if (!el) return Promise.resolve(); // Bỏ qua nếu không có element

    return fetch(basePath + file)
        .then(function(res) { return res.text(); })
        .then(function(data) {
            el.innerHTML = data;
            
            // Sau khi nạp xong, cập nhật các đường dẫn trong component
            capNhatDuongDan(el);
            
            // 🔥 QUAN TRỌNG: Chỉ khi nào nạp xong "dau-trang" 
            // thì mới chạy hàm bôi xanh menu
            if (id === "dau-trang") {
                activeMenu();
                kiemTraDangNhap();
            }
        });
}

// Hàm cập nhật đường dẫn tương đối trong các component sau khi load
function capNhatDuongDan(container) {
    // Cập nhật tất cả href (trừ javascript:, #, http)
    container.querySelectorAll('a[href]').forEach(function(a) {
        var href = a.getAttribute('href');
        if (href && !href.startsWith('http') && !href.startsWith('#') && !href.startsWith('javascript') && !href.startsWith('/')) {
            // Chuyển đường dẫn cũ sang đường dẫn mới theo cấu trúc folder
            a.setAttribute('href', chuyenDuongDan(href));
        }
    });
    
    // Cập nhật tất cả src (img, ...)
    container.querySelectorAll('img[src]').forEach(function(img) {
        var src = img.getAttribute('src');
        if (src && !src.startsWith('http') && !src.startsWith('/') && !src.startsWith('data:')) {
            // Nếu src bắt đầu bằng ../ (tương đối từ thanh-phan/)
            // thì chuyển sang tương đối từ trang hiện tại
            if (src.startsWith('../')) {
                // src đã là "../tai-nguyen/xxx" (từ thanh-phan/)
                // Cần chuyển thành basePath + "tai-nguyen/xxx"
                img.setAttribute('src', basePath + src.replace('../', ''));
            } else {
                img.setAttribute('src', basePath + src);
            }
        }
    });
}

// Hàm chuyển đường dẫn từ cấu trúc cũ (flat) sang cấu trúc mới (có subfolder)
function chuyenDuongDan(href) {
    // Mapping đường dẫn cũ → mới
    var mapping = {
        'trang-chu.html': 'trang/trang-chu.html',
        'gioi-thieu.html': 'trang/gioi-thieu.html',
        'noi-quy-thu-vien.html': 'trang/noi-quy-thu-vien.html',
        'dang-nhap.html': 'trang/dang-nhap.html',
        'cai-dat.html': 'thanh-phan/cai-dat.html',
        'tin-tuc.html': 'tin-tuc/tin-tuc.html',
        'su-kien.html': 'su-kien/su-kien.html',
        'thong-bao.html': 'su-kien/thong-bao.html',
        'muon-sach.html': 'sach/muon-sach.html',
        'dang-ky-muon.html': 'sach/dang-ky-muon.html',
        'chi-tiet-sach.html': 'sach/chi-tiet-sach.html',
        'sach-cua-toi.html': 'sach/sach-cua-toi.html',
        'quan-ly-sach.html': 'sach/quan-ly-sach.html',
        'tai-lieu-in.html': 'tai-lieu-in.html',
        'tai-lieu-so.html': 'tai-lieu-so.html'
    };
    
    if (mapping[href]) {
        return basePath + mapping[href];
    }
    
    return href;
}

// Hàm nhận diện trang hiện tại để bôi xanh
function activeMenu() {
    var path = window.location.pathname;
    var currentFile = path.split("/").pop(); // Lấy tên file cuối cùng trên thanh địa chỉ
    
    // 1. Xóa hết màu sáng cũ
    document.querySelectorAll("nav a").forEach(function(link) { link.classList.remove("active"); });

    // 2. Kiểm tra từng mục
    if (currentFile.includes("trang-chu") || currentFile === "") {
        var el = document.querySelector(".menu-home");
        if (el) el.classList.add("active");
    }
    else if (currentFile.includes("gioi-thieu")) {
        var el = document.querySelector(".menu-about");
        if (el) el.classList.add("active");
    }
    else if (currentFile.includes("noi-quy-thu-vien")) {
        var el = document.querySelector(".menu-rules");
        if (el) el.classList.add("active");
    }
    // CHỖ QUAN TRỌNG ĐÂY: Nếu file có chữ 'tin-tuc' hoặc 'chi-tiet-tin' là sáng Bản tin
    else if (currentFile.includes("tin-tuc") || currentFile.includes("chi-tiet-tin") || currentFile.includes("su-kien") || currentFile.includes("thong-bao") || currentFile.includes("chi_tiet_sk")) {
        var el = document.querySelector(".menu-news");
        if (el) el.classList.add("active");
    }
}

// Gọi nạp các thành phần
load("dau-trang", "thanh-phan/dau-trang.html");
load("slider", "thanh-phan/slider.html");
load("tim-kiem", "thanh-phan/tim-kiem.html");
load("tinh-nang", "thanh-phan/tinh-nang.html");
load("chan-trang", "thanh-phan/chan-trang.html");
load("chi-dan", "thanh-phan/chi-dan.html");

// --- STICKY HEADER ---
window.onscroll = function() {
    handleStickyHeader();
};

function handleStickyHeader() {
    // Tìm thẻ header (thường nằm trong id 'dau-trang')
    var header = document.querySelector(".header");
    
    if (header) {
        if (window.pageYOffset > 100) {
            // Khi cuộn xuống quá 100px thì thêm class để kích hoạt hiệu ứng
            header.classList.add("sticky-active");
        } else {
            // Khi quay lại đầu trang thì xóa class đi
            header.classList.remove("sticky-active");
        }
    }
}

function kiemTraDangNhap() {
    // Lấy tên user từ bộ nhớ
    var user = localStorage.getItem("username");
    
    // Lấy các phần tử HTML trên Header
    var btnLogin = document.getElementById("btn-login-header");
    var userDropdown = document.getElementById("user-logged-in");
    var displayName = document.getElementById("display-name");

    if (user) {
        // Nếu đã đăng nhập: Ẩn nút Đăng nhập, Hiện Dropdown User, Cập nhật tên
        if (btnLogin) btnLogin.style.display = "none";
        if (userDropdown) userDropdown.style.display = "block";
        if (displayName) displayName.innerText = user; // Viết hoa chữ cái đầu nếu thích
    } else {
        // Nếu chưa đăng nhập: Hiện nút Đăng nhập, Ẩn Dropdown User
        if (btnLogin) btnLogin.style.display = "block";
        if (userDropdown) userDropdown.style.display = "none";
    }
}

function dangXuat() {
    if (confirm("Bạn có chắc chắn muốn đăng xuất?")) {
        // Xóa dữ liệu user khỏi bộ nhớ
        localStorage.removeItem("username");
        
        // Tải lại trang hiện tại (Header sẽ tự động hiện lại nút Đăng nhập)
        window.location.href = basePath + "trang/dang-nhap.html"; 
    }
}

document.addEventListener("DOMContentLoaded", function() {
    // Lấy tên file hiện tại từ URL (ví dụ: chi-tiet-tin.html)
    var currentFile = window.location.pathname.split("/").pop();
    
    // Danh sách các trang thuộc mục "Bản tin"
    var newsPages = [
        "tin-tuc.html", 
        "su-kien.html", 
        "thong-bao.html", 
        "chi-tiet-tin.html", 
        "chi-tiet-tin2.html",
        "chi-tiet-tin3.html",             
        "chi-tiet-tin4.html",
        "chi-tiet-tin5.html",
        "chi_tiet_sk1.html",
        "Chi_tiet_sk2.html"
    ];

    // Kiểm tra nếu file hiện tại nằm trong danh sách tin tức
    if (newsPages.includes(currentFile)) {
        var menuNews = document.querySelector('.menu-news');
        if (menuNews) {
            menuNews.classList.add('active');
        }
    }
});
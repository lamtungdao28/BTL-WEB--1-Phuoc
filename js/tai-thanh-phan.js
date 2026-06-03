// File: tai-thanh-phan.js

function load(id, file) {
    return fetch(file)
        .then(res => res.text())
        .then(data => {
            document.getElementById(id).innerHTML = data;
            
            // 🔥 QUAN TRỌNG: Chỉ khi nào nạp xong "dau-trang" 
            // thì mới chạy hàm bôi xanh menu
            if (id === "dau-trang") {
                activeMenu();
                kiemTraDangNhap();
            }
        });
}

// Hàm nhận diện trang hiện tại để bôi xanh
function activeMenu() {
    const path = window.location.pathname;
    const currentFile = path.split("/").pop(); // Lấy tên file cuối cùng trên thanh địa chỉ
    
    // 1. Xóa hết màu sáng cũ
    document.querySelectorAll("nav a").forEach(link => link.classList.remove("active"));

    // 2. Kiểm tra từng mục
    if (currentFile.includes("trang-chu") || currentFile === "") {
        document.querySelector(".menu-home")?.classList.add("active");
    }
    else if (currentFile.includes("gioi-thieu")) {
        document.querySelector(".menu-about")?.classList.add("active");
    }
    else if (currentFile.includes("noi-quy-thu-vien")) {
        document.querySelector(".menu-rules")?.classList.add("active");
    }
    // CHỖ QUAN TRỌNG ĐÂY: Nếu file có chữ 'tin-tuc' hoặc 'chi-tiet-tin' là sáng Bản tin
    else if (currentFile.includes("tin-tuc") || currentFile.includes("chi-tiet-tin") || currentFile.includes("su-kien") || currentFile.includes("thong-bao")) {
        document.querySelector(".menu-news")?.classList.add("active");
    }
}

// Gọi nạp các thành phần
load("dau-trang", "thanh-phan/dau-trang.html");
load("slider", "thanh-phan/slider.html");
load("tim-kiem", "thanh-phan/tim-kiem.html");
load("tinh-nang", "thanh-phan/tinh-nang.html");
load("chan-trang", "thanh-phan/chan-trang.html");
load("chi-dan", "thanh-phan/chi-dan.html");
// --- THÊM ĐOẠN NÀY VÀO CUỐI FILE tai-thanh-phan.js ---

window.onscroll = function() {
    handleStickyHeader();
};

function handleStickyHeader() {
    // Tìm thẻ header (thường nằm trong id 'dau-trang')
    const header = document.querySelector(".header");
    
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
    const user = localStorage.getItem("username");
    
    // Lấy các phần tử HTML trên Header
    const btnLogin = document.getElementById("btn-login-header");
    const userDropdown = document.getElementById("user-logged-in");
    const displayName = document.getElementById("display-name");

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
        window.location.href = "dang-nhap.html"; 
    }
}
document.addEventListener("DOMContentLoaded", function() {
    // Lấy tên file hiện tại từ URL (ví dụ: chi-tiet-tin.html)
    const currentFile = window.location.pathname.split("/").pop();
    
    // Danh sách các trang thuộc mục "Bản tin"
    const newsPages = [
        "tin-tuc.html", 
        "su-kien.html", 
        "thong-bao.html", 
        "chi-tiet-tin.html", 
        "chi-tiet-tin2.html",
        "chi-tiet-tin3.html",             
         "chi-tiet-tin4.html",
         "chi-tiet-tin5.html"

     
    ];

    // Kiểm tra nếu file hiện tại nằm trong danh sách tin tức
    if (newsPages.includes(currentFile)) {
        const menuNews = document.querySelector('.menu-news');
        if (menuNews) {
            menuNews.classList.add('active');
        }
    }
});
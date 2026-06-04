document.addEventListener("DOMContentLoaded", function() {
    // 1. Xử lý Tab Bộ lọc sự kiện
    const filterBtns = document.querySelectorAll('.filter-btn');
    
    filterBtns.forEach(btn => {
        btn.addEventListener('click', function() {
            // Bỏ class active ở tất cả các nút
            filterBtns.forEach(b => b.classList.remove('active'));
            // Thêm class active vào nút được click
            this.classList.add('active');
            
            // Logic lọc sự kiện sẽ viết ở đây (nếu có data từ server)
            const filterType = this.getAttribute('data-filter');
            console.log("Đang xem danh sách sự kiện:", filterType === 'upcoming' ? "Sắp diễn ra" : "Đã diễn ra");
            
            // Tạm thời hiển thị alert giả lập việc tải dữ liệu mới
            // alert("Đang tải danh sách sự kiện: " + this.innerText);
        });
    });
});
    // Xử lý bật/tắt Form Đăng ký cho NHIỀU NÚT BẤM
    document.addEventListener("DOMContentLoaded", function() {
        const modal = document.getElementById("register-modal");
        // Dùng querySelectorAll để lấy TẤT CẢ các nút có class btn-register
        const btnOpens = document.querySelectorAll(".btn-register"); 
        const btnClose = document.querySelector(".close-modal");
        const form = document.getElementById("register-form");

        // Lặp qua từng nút và gắn sự kiện click
        btnOpens.forEach(function(btn) {
            btn.addEventListener("click", function(e) {
                e.preventDefault();
                if (modal) modal.classList.add("active");
            });
        });

        // Bấm dấu X -> Tắt form
        if(btnClose) {
            btnClose.addEventListener("click", function() {
                modal.classList.remove("active");
            });
        }

        // Bấm ra vùng đen bên ngoài -> Tắt form
        window.addEventListener("click", function(e) {
            if (e.target === modal) {
                modal.classList.remove("active");
            }
        });

        // Xử lý khi bấm nút "Xác nhận đăng ký"
        if(form) {
            form.addEventListener("submit", function(e) {
                e.preventDefault(); // Ngăn load lại trang
                alert("🎉 Đăng ký thành công! Thông tin vé mời đã được gửi qua email của bạn.");
                modal.classList.remove("active"); 
                form.reset(); 
            });
        }
    });

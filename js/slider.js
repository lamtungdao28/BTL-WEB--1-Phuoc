window.addEventListener("load", function () {

    let slides = document.querySelectorAll('.slide');
    let dots = document.querySelectorAll('.dots span');
    let index = 0;

    function showSlide(i) {
        slides.forEach(s => s.classList.remove('active'));
        dots.forEach(d => d.classList.remove('active'));

        slides[i].classList.add('active');
        dots[i].classList.add('active');
    }

    function nextSlide() {
        index = (index + 1) % slides.length;
        showSlide(index);
    }

    function prevSlide() {
        index = (index - 1 + slides.length) % slides.length;
        showSlide(index);
    }

    function goToSlide(i) {
        index = i;
        showSlide(index);
    }

    // Gán ra ngoài để HTML gọi
    window.nextSlide = nextSlide;
    window.prevSlide = prevSlide;
    window.goToSlide = goToSlide;

    // Auto chạy
    setInterval(nextSlide, 3000);

    showSlide(index);
});
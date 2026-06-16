package com.ptit.thuvien.config;

import com.ptit.thuvien.repository.NguoiDungRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/**
 * Cấu hình Spring Security - Phân quyền truy cập
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     * Bean mã hóa mật khẩu
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Cấu hình bảo mật HTTP
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http,
                                            NguoiDungRepository nguoiDungRepository) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) // Tắt CSRF cho đơn giản khi dev

            .authorizeHttpRequests(auth -> auth
                // ===== TRANG CÔNG KHAI (ai cũng vào được) =====
                .requestMatchers(
                    "/", "/trang-chu", "/gioi-thieu", "/noi-quy",
                    "/dang-nhap", "/xu-ly-dang-nhap", "/dang-xuat", "/dang-ky",
                    "/tin-tuc/**", "/su-kien/**",
                    "/css/**", "/js/**", "/tai-nguyen/**", "/images/**", "/uploads/**",
                    "/WEB-INF/**", "/error/**"
                ).permitAll()

                // ===== TRANG ADMIN (chỉ ADMIN) =====
                .requestMatchers("/admin/**").hasRole("ADMIN")

                // ===== TRANG MƯỢN SÁCH (đăng nhập mới vào được) =====
                .requestMatchers("/sach/**").authenticated()

                // ===== CÒN LẠI: phải đăng nhập =====
                .anyRequest().authenticated()
            )

            .formLogin(form -> form
                .loginPage("/dang-nhap")                  // GET - trang hiển thị form
                .loginProcessingUrl("/xu-ly-dang-nhap")   // POST - URL Spring Security xử lý login
                .usernameParameter("email")            // Tên field username (email)
                .passwordParameter("matKhau")             // Tên field password
                .successHandler((request, response, authentication) -> {
                    String emailOrUsername = authentication.getName();
                    nguoiDungRepository.findByEmail(emailOrUsername)
                            .or(() -> nguoiDungRepository.findByTaiKhoan(emailOrUsername))
                            .ifPresent(nd -> {
                                HttpSession session = request.getSession();
                                session.setAttribute("nguoiDung", nd);
                                session.setAttribute("vaiTro", nd.getVaiTro().name());
                            });

                    // Điều hướng theo vai trò
                    boolean isAdmin = authentication.getAuthorities().stream()
                            .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
                    if (isAdmin) {
                        response.sendRedirect(request.getContextPath() + "/admin/quan-ly-sach");
                    } else {
                        response.sendRedirect(request.getContextPath() + "/trang-chu");
                    }
                })
                .failureHandler((request, response, exception) -> {
                    String error = "true";
                    if (exception instanceof org.springframework.security.authentication.LockedException ||
                        exception instanceof org.springframework.security.authentication.DisabledException) {
                        error = "locked";
                    }
                    response.sendRedirect(request.getContextPath() + "/dang-nhap?error=" + error);
                })
                .permitAll()
            )

            .logout(logout -> logout
                .logoutRequestMatcher(new AntPathRequestMatcher("/dang-xuat"))
                .logoutSuccessUrl("/dang-nhap?logout=true")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .permitAll()
            )

            .exceptionHandling(ex -> ex
                .accessDeniedPage("/error/403")
            );

        return http.build();
    }
}

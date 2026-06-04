package com.ptit.thuvien.security;

import com.ptit.thuvien.model.NguoiDung;
import com.ptit.thuvien.repository.NguoiDungRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

/**
 * Service xác thực người dùng cho Spring Security
 * Kết nối với bảng nguoi_dung trong DB
 */
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final NguoiDungRepository nguoiDungRepository;

    @Override
    public UserDetails loadUserByUsername(String taiKhoan) throws UsernameNotFoundException {
        NguoiDung nguoiDung = nguoiDungRepository.findByTaiKhoan(taiKhoan)
                .orElseThrow(() -> new UsernameNotFoundException("Không tìm thấy tài khoản: " + taiKhoan));

        // Chuyển vai trò thành ROLE_XXX (Spring Security yêu cầu prefix ROLE_)
        String role = "ROLE_" + nguoiDung.getVaiTro().name();

        return new User(
                nguoiDung.getTaiKhoan(),
                nguoiDung.getMatKhau(),
                Collections.singletonList(new SimpleGrantedAuthority(role))
        );
    }
}

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
    public UserDetails loadUserByUsername(String emailOrUsername) throws UsernameNotFoundException {
        NguoiDung nguoiDung = nguoiDungRepository.findByEmail(emailOrUsername)
                .or(() -> nguoiDungRepository.findByTaiKhoan(emailOrUsername))
                .orElseThrow(() -> new UsernameNotFoundException("Không tìm thấy người dùng với email hoặc tài khoản: " + emailOrUsername));

        // Chuyển vai trò thành ROLE_XXX (Spring Security yêu cầu prefix ROLE_)
        String role = "ROLE_" + nguoiDung.getVaiTro().name();

        boolean active = nguoiDung.getTrangThai() == NguoiDung.TrangThaiNguoiDung.HOAT_DONG;

        return new User(
                nguoiDung.getEmail(),
                nguoiDung.getMatKhau(),
                active,             // enabled
                true,               // accountNonExpired
                true,               // credentialsNonExpired
                active,             // accountNonLocked
                Collections.singletonList(new SimpleGrantedAuthority(role))
        );
    }
}

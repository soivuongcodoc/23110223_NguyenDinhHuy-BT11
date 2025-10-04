package com.BT1.BT1.service.Impl;

import com.BT1.BT1.entity.Role;
import com.BT1.BT1.entity.Users;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

public class MyUserService implements UserDetails {

    private static final long serialVersionUID = 1L;
    private Users user;

    public MyUserService(Users user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<Role> user_roles = user.getRoles();
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();

        for (Role role : user_roles) {
            authorities.add(new SimpleGrantedAuthority(role.getName()));
        }
        return authorities;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // Giả định tài khoản không hết hạn
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // Giả định tài khoản không bị khóa
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // Giả định thông tin đăng nhập không hết hạn
    }

    @Override
    public boolean isEnabled() {
        return user.isEnabled(); // Sử dụng trạng thái enabled từ Entity
    }
}

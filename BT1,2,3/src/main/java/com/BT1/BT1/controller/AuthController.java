package com.BT1.BT1.controller;

import com.BT1.BT1.dto.LoginDto;
import com.BT1.BT1.dto.SignUpDto;
import com.BT1.BT1.entity.Role;
import com.BT1.BT1.entity.Users;
import com.BT1.BT1.repository.RoleRepository;
import com.BT1.BT1.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/signin")
    public ResponseEntity<String> authenticateUser(@RequestBody LoginDto loginDto) {
        // 1. Thực hiện xác thực
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginDto.getUsernameOrEmail(),
                loginDto.getPassword()));

        // 2. Thiết lập đối tượng xác thực vào SecurityContext
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // 3. Trả về phản hồi thành công
        return new ResponseEntity<>("User signed-in successfully!.", HttpStatus.OK);
    }

    @PostMapping("/signup")
    public ResponseEntity<String> registerUser(@RequestBody SignUpDto signUpDto) {
        // 1. Kiểm tra username đã tồn tại chưa
        if (userRepository.existsByUsername(signUpDto.getUsername())) {
            return new ResponseEntity<>("Username is already taken!", HttpStatus.BAD_REQUEST);
        }

        // 2. Kiểm tra email đã tồn tại chưa
        if (userRepository.existsByEmail(signUpDto.getEmail())) {
            return new ResponseEntity<>("Email is already taken!", HttpStatus.BAD_REQUEST);
        }

        // 3. Tạo đối tượng Users
        Users users = new Users();
        users.setName(signUpDto.getName());
        users.setUsername(signUpDto.getUsername());
        users.setEmail(signUpDto.getEmail());
        users.setEnabled(true);

        // 4. Mã hóa và thiết lập mật khẩu
        users.setPassword(passwordEncoder.encode(signUpDto.getPassword()));

        // 5. Thiết lập Role (lấy từ DB) và lưu vào DB
        Role roles = roleRepository.findByName("USER").get(); // Giả định Role là "USER"
        users.setRoles(Collections.singleton(roles));
        userRepository.save(users);

        return new ResponseEntity<>("User registered successfully", HttpStatus.OK);
    }
}
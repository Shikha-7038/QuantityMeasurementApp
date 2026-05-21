package com.app.quantitymeasurement.controller;

import com.app.quantitymeasurement.model.User;
import com.app.quantitymeasurement.security.JwtUtil;
import com.app.quantitymeasurement.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public void login(HttpServletResponse response) throws IOException {
        response.sendRedirect("/oauth2/authorization/google");
    }

    @GetMapping("/success")
    public void success(OAuth2AuthenticationToken authentication, HttpServletResponse response) throws IOException {

        OAuth2User user = authentication.getPrincipal();

        String email = user.getAttribute("email");
        String name = user.getAttribute("name");

        User savedUser = userService.saveOrUpdateUser(email, name);
        //return jwtUtil.generateToken(savedUser.getEmail());
        String token = jwtUtil.generateToken(savedUser.getEmail());
        response.sendRedirect("http://localhost:3000/?token=" + token);
    }
}
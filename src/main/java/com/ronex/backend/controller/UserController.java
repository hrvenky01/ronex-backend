package com.ronex.backend.controller;

import com.ronex.backend.dto.ProfileResponse;
import com.ronex.backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@CrossOrigin("*")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/profile")
    public ProfileResponse getProfile(@RequestParam String phone) {
        return userService.getProfile(phone);
    }
}
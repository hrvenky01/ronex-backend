// ProfileController.java
package com.ronex.backend.controller;

import org.springframework.web.bind.annotation.*;

import com.ronex.backend.dto.FollowResponse;
import com.ronex.backend.dto.ProfileResponse;
import com.ronex.backend.dto.ProfilesResponse;
import com.ronex.backend.service.ProfileService;

@RestController
@RequestMapping("/api/profile")
@CrossOrigin
public class ProfileController {

    private final ProfileService profileService;

    public ProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }

    @GetMapping("/{userId}")
    public ProfilesResponse getProfile(@PathVariable Long userId) {
        return profileService.getProfile(userId);
    }

    @PostMapping("/{userId}/follow")
    public FollowResponse follow(@PathVariable Long userId) {
        return profileService.toggleFollow(userId);
    }

    @PostMapping("/{userId}/avatar")
    public void updateAvatar(
            @PathVariable Long userId,
            @RequestParam String avatarUrl
    ) {
        profileService.updateAvatar(userId, avatarUrl);
    }
}
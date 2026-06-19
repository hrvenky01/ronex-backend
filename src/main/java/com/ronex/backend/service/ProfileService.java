// ProfileService.java
package com.ronex.backend.service;

import org.springframework.stereotype.Service;

import com.ronex.backend.dto.FollowResponse;
import com.ronex.backend.dto.ProfileResponse;
import com.ronex.backend.dto.ProfilesResponse;
import com.ronex.backend.model.Profile;
import com.ronex.backend.repository.ProfileRepository;

@Service
public class ProfileService {

    private final ProfileRepository profileRepo;

    public ProfileService(ProfileRepository profileRepo) {
        this.profileRepo = profileRepo;
    }

    public ProfilesResponse getProfile(Long userId) {

        Profile profile = profileRepo.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Profile not found"));

        return new ProfilesResponse(
                profile.getUsername(),
                profile.getAvatarUrl(),
                profile.getFollowers(),
                profile.getFollowing()
        );
    }

    public FollowResponse toggleFollow(Long userId) {

        Profile profile = profileRepo.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Profile not found"));

        boolean isFollowing;

        if (profile.getFollowing() > 0) {
            profile.setFollowing(profile.getFollowing() - 1);
            profile.setFollowers(profile.getFollowers() - 1);
            isFollowing = false;
        } else {
            profile.setFollowing(profile.getFollowing() + 1);
            profile.setFollowers(profile.getFollowers() + 1);
            isFollowing = true;
        }

        profileRepo.save(profile);

        return new FollowResponse(isFollowing, profile.getFollowers());
    }

    public void updateAvatar(Long userId, String avatarUrl) {

        Profile profile = profileRepo.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Profile not found"));

        profile.setAvatarUrl(avatarUrl);
        profileRepo.save(profile);
    }
}
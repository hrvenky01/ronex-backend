package com.ronex.backend.controller;

import com.ronex.backend.dto.ReelRequest;
import com.ronex.backend.model.Reel;
import com.ronex.backend.repository.ReelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/reels")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ReelController {

    private final ReelRepository reelRepository;

    @PostMapping
    public Reel createReel(
            @RequestBody ReelRequest request,
            Principal principal
    ) {

        if (principal == null) {
            throw new RuntimeException("User not authenticated");
        }

        Reel reel = new Reel();

        reel.setVideoUrl(request.getVideoUrl());
        reel.setUserName(principal.getName());
        reel.setLikes(0L);

        return reelRepository.save(reel);
    }

    @GetMapping
    public List<Reel> getReels() {
        return reelRepository.findAllByOrderByCreatedAtDesc();
    }
}
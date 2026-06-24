package com.ronex.backend.controller;

import com.ronex.backend.model.Reel;
import com.ronex.backend.model.ReelComment;
import com.ronex.backend.model.ReelLike;
import com.ronex.backend.repository.ReelCommentRepository;
import com.ronex.backend.repository.ReelLikeRepository;
import com.ronex.backend.repository.ReelRepository;

import lombok.RequiredArgsConstructor;

import java.io.File;
import java.security.Principal;
import java.util.List;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/reels")
@RequiredArgsConstructor
public class ReelController {

    private final ReelRepository reelRepository;
    private final ReelLikeRepository reelLikeRepository;
    private final ReelCommentRepository commentRepository;

    // 📤 UPLOAD VIDEO (JWT based user)
    @PostMapping("/upload-video")
    public Reel uploadVideo(
            @RequestParam("file") MultipartFile file,
            Principal principal   // 🔐 comes from JWT
    ) throws Exception {

        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();

        File uploadDir = new File("uploads/");
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }

        File dest = new File(uploadDir, fileName);
        file.transferTo(dest);

        Reel reel = new Reel();
        reel.setVideoUrl("https://ronex-backend.onrender.com/uploads/" + fileName);

        // 🔥 mobile number / username from JWT
        reel.setUserName(principal.getName());

        reel.setLikes(0L);

        return reelRepository.save(reel);
    }

    // 🎬 GET FEED
    @GetMapping
    public List<Reel> getReels() {
        return reelRepository.findAllByOrderByCreatedAtDesc();
    }

    // ❤️ LIKE
    @PostMapping("/like/{reelId}")
    public String likeReel(
            @PathVariable Long reelId,
            Principal principal
    ) {

        ReelLike like = new ReelLike();
        like.setReelId(reelId);

        // 🔐 userId optional – JWT user enough
        like.setUserId(principal.getName().hashCode() * 1L);

        reelLikeRepository.save(like);

        return "Liked";
    }

    // 💬 COMMENT
    @PostMapping("/comment")
    public String addComment(
            @RequestBody ReelComment comment,
            Principal principal
    ) {
        comment.setUserName(principal.getName());
        commentRepository.save(comment);
        return "Comment added";
    }

    @GetMapping("/comment/{reelId}")
    public List<ReelComment> getComments(@PathVariable Long reelId) {
        return commentRepository.findByReelId(reelId);
    }
}
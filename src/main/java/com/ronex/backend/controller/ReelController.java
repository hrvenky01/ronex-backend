package com.ronex.backend.controller;

import com.ronex.backend.model.Reel;
import com.ronex.backend.repository.ReelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/reels")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ReelController {

    private final ReelRepository reelRepository;

    // 📤 UPLOAD VIDEO (FINAL FIXED)
    @PostMapping(
            value = "/upload-video",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public Reel uploadVideo(
            @RequestParam("file") MultipartFile file,   // 🔥 @RequestParam (NOT RequestPart)
            Principal principal
    ) throws Exception {

        if (file.isEmpty()) {
            throw new RuntimeException("File is empty");
        }

        System.out.println("🔥 FILE: " + file.getOriginalFilename());
        System.out.println("📦 SIZE: " + file.getSize());
        System.out.println("👤 USER: " + principal.getName());

        // ✅ PROJECT ROOT/uploads
        String baseDir = System.getProperty("user.dir");
        File uploadDir = new File(baseDir + File.separator + "uploads");

        if (!uploadDir.exists()) {
            boolean created = uploadDir.mkdirs();
            System.out.println("📁 Upload dir created: " + created);
        }

        String fileName =
                System.currentTimeMillis() + "_" + file.getOriginalFilename();

        File dest = new File(uploadDir, fileName);

        // ✅ ACTUAL SAVE
        file.transferTo(dest);

        // ✅ SAVE DB
        Reel reel = new Reel();
        reel.setVideoUrl("/uploads/" + fileName);
        reel.setUserName(principal.getName());
        reel.setLikes(0L);

        return reelRepository.save(reel);
    }

    // 🎬 GET REELS FEED
    @GetMapping
    public List<Reel> getReels() {
        return reelRepository.findAllByOrderByCreatedAtDesc();
    }
}
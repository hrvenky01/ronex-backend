package com.ronex.backend.controller;

import java.io.File;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ronex.backend.model.FriendPost;
import com.ronex.backend.repository.FriendPostRepository;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/friends")
@RequiredArgsConstructor
public class FriendZoneController {

    private final FriendPostRepository repository;

    // GET FEED
    @GetMapping("/feed")
    public List<FriendPost> getFeed() {
        return repository.findAllByOrderByCreatedAtDesc();
    }

    // CREATE POST
    @PostMapping("/upload")
    public FriendPost upload(
            @RequestParam("file") MultipartFile file,
            @RequestParam String userName,
            @RequestParam String caption
    ) throws Exception {

        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();

        File dir = new File("uploads/");
        if (!dir.exists()) dir.mkdirs();

        file.transferTo(new File("uploads/" + fileName));

        FriendPost post = new FriendPost();
        post.setUserName(userName);
        post.setCaption(caption);
        post.setVideoUrl("http://YOUR_IP:8080/uploads/" + fileName);

        return repository.save(post);
    }
}
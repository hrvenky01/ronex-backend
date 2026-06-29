package com.ronex.backend.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cloudinary.Cloudinary;

@RestController
@RequestMapping("/cloudinary")
@CrossOrigin // allow frontend
public class CloudinaryController {

    @Autowired
    private Cloudinary cloudinary;

    @GetMapping("/signature")
    public Map<String, Object> getSignature() {

        long timestamp = System.currentTimeMillis() / 1000;

        Map<String, Object> params = new HashMap<>();

        // ⚠️ MUST include EXACT same fields as frontend sends
        params.put("timestamp", timestamp);
        params.put("folder", "ronex/reels");

        String signature = cloudinary.apiSignRequest(params, cloudinary.config.apiSecret);

        Map<String, Object> res = new HashMap<>();
        res.put("timestamp", timestamp);
        res.put("signature", signature);
        res.put("apiKey", cloudinary.config.apiKey);
        res.put("cloudName", cloudinary.config.cloudName);
        res.put("folder", "ronex/reels"); // 🔥 ADD THIS

        return res;
    }
}
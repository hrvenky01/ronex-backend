package com.ronex.backend.config;



import com.cloudinary.Cloudinary;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class CloudinaryConfig {

    @Bean
    public Cloudinary cloudinary() {

        Map<String, Object> config = new HashMap<>();
        config.put("cloud_name", "dpxddv0zk");
        config.put("api_key", "YOUR_CLOUDINARY_API_KEY");
        config.put("api_secret", "YOUR_CLOUDINARY_API_SECRET");

        return new Cloudinary(config);
    }
}
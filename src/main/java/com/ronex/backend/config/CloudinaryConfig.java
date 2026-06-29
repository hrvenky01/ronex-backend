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
        config.put("api_key", "391517292444842");
        config.put("api_secret", "tRPPt13qgdCaP_7T_kK9bg3CPtg");

        return new Cloudinary(config);
    }
}
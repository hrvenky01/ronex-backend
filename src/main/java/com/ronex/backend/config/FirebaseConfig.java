package com.ronex.backend.config;

import java.io.ByteArrayInputStream;

import org.springframework.context.annotation.Configuration;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

import jakarta.annotation.PostConstruct;

@Configuration
public class FirebaseConfig {

    @PostConstruct
    public void init() {

        String privateKey = System.getenv("FIREBASE_PRIVATE_KEY");
        String projectId  = System.getenv("FIREBASE_PROJECT_ID");

        // 🔥 SAFE GUARD (IMPORTANT)
        if (privateKey == null || projectId == null) {
            System.out.println("⚠️ Firebase env vars not set. Firebase OTP disabled.");
            return; // ❌ DO NOT CRASH APP
        }

        try {
            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(
                            GoogleCredentials.fromStream(
                                    new ByteArrayInputStream(
                                            privateKey.replace("\\n", "\n").getBytes()
                                    )
                            )
                    )
                    .setProjectId(projectId)
                    .build();

            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseApp.initializeApp(options);
                System.out.println("✅ Firebase initialized");
            }

        } catch (Exception e) {
            throw new RuntimeException("Firebase init failed", e);
        }
    }
}
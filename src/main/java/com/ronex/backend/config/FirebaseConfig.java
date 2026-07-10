package com.ronex.backend.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;

@Configuration
public class FirebaseConfig {

    @PostConstruct
    public void init() {

        try {

            String firebaseJson = System.getenv("FIREBASE_SERVICE_ACCOUNT");

            if (firebaseJson == null || firebaseJson.isBlank()) {
                System.out.println("⚠️ FIREBASE_SERVICE_ACCOUNT not found.");
                return;
            }

            GoogleCredentials credentials =
                    GoogleCredentials.fromStream(
                            new ByteArrayInputStream(
                                    firebaseJson.getBytes(StandardCharsets.UTF_8)
                            )
                    );

            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(credentials)
                    .build();

            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseApp.initializeApp(options);
                System.out.println("✅ Firebase initialized successfully.");
            }

        } catch (Exception e) {
            System.out.println("❌ Firebase initialization failed.");
            e.printStackTrace();
        }
    }
}
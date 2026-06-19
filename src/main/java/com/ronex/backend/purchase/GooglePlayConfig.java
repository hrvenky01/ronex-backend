/*
 * package com.ronex.backend.purchase;
 * 
 * import java.io.FileInputStream; import java.util.Collections;
 * 
 * import org.springframework.context.annotation.Bean; import
 * org.springframework.context.annotation.Configuration;
 * 
 * import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
 * import com.google.api.client.json.gson.GsonFactory; import
 * com.google.api.services.androidpublisher.AndroidPublisher; import
 * com.google.api.services.androidpublisher.AndroidPublisherScopes; import
 * com.google.auth.http.HttpCredentialsAdapter; import
 * com.google.auth.oauth2.GoogleCredentials;
 * 
 * @Configuration public class GooglePlayConfig {
 * 
 * @Bean public AndroidPublisher androidPublisher() throws Exception {
 * 
 * GoogleCredentials credentials = GoogleCredentials.fromStream( new
 * FileInputStream("google-play-service.json") ).createScoped(
 * Collections.singleton(AndroidPublisherScopes.ANDROIDPUBLISHER) );
 * 
 * return new AndroidPublisher.Builder(
 * GoogleNetHttpTransport.newTrustedTransport(),
 * GsonFactory.getDefaultInstance(), new HttpCredentialsAdapter(credentials)
 * ).setApplicationName("Ronex").build(); } }
 */
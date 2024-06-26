package com.rparnp.gist_tool.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.FirestoreOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.io.IOException;

@Configuration
@Profile("!dev")
public class FirestoreConfig {

    @Value("${google.cloud.projectId}")
    private String GOOGLE_CLOUD_PROJECT_ID;

    @Bean
    public Firestore getFirestore() throws IOException {
        FirestoreOptions firestoreOptions = FirestoreOptions.getDefaultInstance().toBuilder()
                .setProjectId(GOOGLE_CLOUD_PROJECT_ID).setCredentials(GoogleCredentials.getApplicationDefault()).build();
        return firestoreOptions.getService();
    }
}

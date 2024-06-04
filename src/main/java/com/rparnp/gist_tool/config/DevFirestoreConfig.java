package com.rparnp.gist_tool.config;

import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.FirestoreOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("dev")
public class DevFirestoreConfig {

    @Value("${local.firestore.host}")
    private String HOST;
    @Value("${google.cloud.projectId}")
    private String GOOGLE_CLOUD_PROJECT_ID;

    @Bean
    public Firestore getFirestore() {
        FirestoreOptions firestoreOptions = FirestoreOptions.newBuilder()
                .setEmulatorHost(HOST)
                .setProjectId(GOOGLE_CLOUD_PROJECT_ID)
                .setCredentials(new FirestoreOptions.EmulatorCredentials())
                .build();
        return firestoreOptions.getService();
    }
}

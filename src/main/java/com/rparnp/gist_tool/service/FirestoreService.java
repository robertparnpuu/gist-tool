package com.rparnp.gist_tool.service;

import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.rparnp.gist_tool.model.firestore.GistEntry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@Service
public class FirestoreService {

    private final String COLLECTION_NAME = "gists";
    private final String DOCUMENT_NAME = "recently-scanned";
    private final String PROPERTY_NAME = "gists";

    @Autowired
    private Firestore firestore;

    public List<GistEntry> getGists() throws ExecutionException, InterruptedException {
        DocumentReference docRef = firestore.collection(COLLECTION_NAME).document(DOCUMENT_NAME);
        DocumentSnapshot snapshot = docRef.get().get();

        List<GistEntry> existingGists = (List<GistEntry>) snapshot.get(PROPERTY_NAME);
        return existingGists != null ? existingGists : new ArrayList<>();
    }

    public void addGist(GistEntry entry) throws ExecutionException, InterruptedException {
        DocumentReference docRef = firestore.collection(COLLECTION_NAME).document(DOCUMENT_NAME);
        Map<String, List<GistEntry>> data = new HashMap<>();
        List<GistEntry> gists = getGists();

        gists.add(entry);
        data.put(PROPERTY_NAME, gists);
        docRef.set(data);
    }

    public void resetRecentGists() {
        DocumentReference docRef = firestore.collection(COLLECTION_NAME).document(DOCUMENT_NAME);
        Map<String, List<GistEntry>> data = new HashMap<>();

        data.put(PROPERTY_NAME, new ArrayList<>());
        docRef.set(data);
    }
}

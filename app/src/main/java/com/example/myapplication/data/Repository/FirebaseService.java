package com.example.myapplication.data.Repository;

import android.content.Context;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class FirebaseService {
    private static FirebaseService instance;
    private FirebaseFirestore firestore;
    private FirebaseAuth firebaseAuth;

    private FirebaseService (Context context) {
        firestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
    }

    public static synchronized FirebaseService getInstance(Context context) {
        if (instance == null) {
            instance = new FirebaseService(context);
        }
        return instance;
    }

    public FirebaseFirestore getFireStore() {
        return firestore;
    }

    public FirebaseAuth getAuth() {
        return firebaseAuth;
    }
}

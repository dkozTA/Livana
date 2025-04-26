package com.example.myapplication.data.Repository.Conversation;

import android.content.Context;

import com.example.myapplication.data.Model.Booking.Booking;
import com.example.myapplication.data.Model.Conversation.Conversation;
import com.example.myapplication.data.Model.Conversation.Message;
import com.example.myapplication.data.Repository.FirebaseService;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class ConversationRepository {
    private final FirebaseFirestore db;
    private final String COLLECTION_NAME = "conversations";
    public ConversationRepository(Context context) {
        this.db = FirebaseService.getInstance(context).getFireStore();
    }

    public void createConversation(Conversation conversation, OnSuccessListener<Void> onSuccess, OnFailureListener onFailure) {
        this.db.collection(COLLECTION_NAME).document(conversation.id).set(conversation)
                .addOnSuccessListener(onSuccess)
                .addOnFailureListener(onFailure);
    }

    public void getAllConversationByHostID(String userID, OnSuccessListener<List<Conversation>> onSuccess, OnFailureListener onFailure) {
        this.db.collection(COLLECTION_NAME).whereEqualTo("host_id", userID).get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<Conversation> conversationList = new ArrayList<>();
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        Conversation conversation = document.toObject(Conversation.class);
                        conversationList.add(conversation);
                    }
                    onSuccess.onSuccess(conversationList);
                })
                .addOnFailureListener(onFailure);
    }

    public void getAllConversationByGuestID(String userID, OnSuccessListener<List<Conversation>> onSuccess, OnFailureListener onFailure) {
        this.db.collection(COLLECTION_NAME).whereEqualTo("guest_id", userID).get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<Conversation> conversationList = new ArrayList<>();
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        Conversation conversation = document.toObject(Conversation.class);
                        conversationList.add(conversation);
                    }
                    onSuccess.onSuccess(conversationList);
                })
                .addOnFailureListener(onFailure);
    }

    public void getConversationById(String conversationID, OnSuccessListener<Conversation> onSuccess, OnFailureListener onFailure) {
        db.collection(COLLECTION_NAME)
                .document(conversationID)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        Conversation conversation = documentSnapshot.toObject(Conversation.class);
                        if (conversation != null) {
                            onSuccess.onSuccess(conversation);
                        } else {
                            onFailure.onFailure(new Exception("Property is null"));
                        }
                    } else {
                        onFailure.onFailure(new Exception("Property not found"));
                    }
                })
                .addOnFailureListener(onFailure);
    }

    public void sendMessage(String conversationID, Message message, OnSuccessListener<Void> onSuccess, OnFailureListener onFailure) {
        this.db.collection(COLLECTION_NAME)
                .document(conversationID)
                .collection("messages")
                .add(message)
                .addOnSuccessListener(documentReference -> onSuccess.onSuccess(null))
                .addOnFailureListener(onFailure);
    }

    public void deleteConversation(String conversationID, OnSuccessListener<Void> onSuccess, OnFailureListener onFailure) {
        this.db.collection(COLLECTION_NAME).document(conversationID).delete()
                .addOnSuccessListener(onSuccess)
                .addOnFailureListener(onFailure);
    }

    public ListenerRegistration listenForMessages(String conversationID, OnSuccessListener<Message> onNewMessage, OnFailureListener onFailure) {
        return db.collection(COLLECTION_NAME)
                .document(conversationID)
                .collection("messages")
                .orderBy("time", Query.Direction.ASCENDING)
                .addSnapshotListener((querySnapshot, error) -> {
                    if (error != null) {
                        onFailure.onFailure(error);
                        return;
                    }

                    if (querySnapshot != null) {
                        for (DocumentChange dc : querySnapshot.getDocumentChanges()) {
                            if (dc.getType() == DocumentChange.Type.ADDED) {
                                Message newMessage = dc.getDocument().toObject(Message.class);
                                onNewMessage.onSuccess(newMessage);
                            }
                        }
                    }
                });
    }
}

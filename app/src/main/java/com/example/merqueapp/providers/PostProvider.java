package com.example.merqueapp.providers;

import com.example.merqueapp.models.Post;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;


public class PostProvider {
    CollectionReference mColletion;

    public PostProvider() {
        mColletion = FirebaseFirestore.getInstance().collection("Posts");

    }
    public Task<Void> save (Post post){
        return mColletion.document().set(post);
    }

}

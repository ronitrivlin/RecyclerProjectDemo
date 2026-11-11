package com.example.recyclerprojectdemo;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FirebaseRef {
    public static FirebaseAuth refAuth = FirebaseAuth.getInstance();
    public static FirebaseDatabase FBDB = FirebaseDatabase.getInstance();
    public static DatabaseReference refUserItems=FBDB.getReference("UserItems");


}

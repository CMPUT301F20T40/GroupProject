package com.example.dlpbgj;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.nfc.Tag;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.proto.TargetGlobal;

import java.util.HashMap;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {
    private EditText user;
    private EditText pass;
    private Button login;
    private Button signUp;
    private ArrayList<User> userDataList;
    private String TAG = "Sample";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userDataList = new ArrayList<>();
        user = findViewById(R.id.editUserName);
        pass = findViewById(R.id.editUserPassword);
        String userName = user.getText().toString();
        String password = pass.getText().toString();
        User newUser = new User(userName,password);
        login = findViewById(R.id.login_button);
        signUp = findViewById(R.id.signup_button);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


        FirebaseFirestore userDb;

        //Instance of the User db
        userDb = FirebaseFirestore.getInstance();

        // Creating a top level collection for User database
        final CollectionReference collectionReference = userDb.collection("Users");

        // Creating a Hashmap to store data as key value pairs for storing in db
        HashMap<String, String> data = new HashMap<>();

        //putting data inside Hashmap as key value pair {User1 :password }
            data.put("Password", password);

        collectionReference

                .document(username)
                .set(data)
                .addOnSuccessListener(new OnSuccessListener<Void>() {

                    @Override
                    public void onSuccess(Void aVoid) {
                        // These are a method which gets executed when the task is succeeded
                        Log.d(TAG, "Data has been added successfully!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // These are a method which gets executed if there’s any problem
                        Log.d(TAG, "Data could not be added!" + e.toString());
                    }
                });

        //for realtime database update
        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable
                    FirebaseFirestoreException error) {
            }
        });

    }

}
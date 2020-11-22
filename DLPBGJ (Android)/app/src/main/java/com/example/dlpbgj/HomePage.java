package com.example.dlpbgj;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Map;

//As soon as the user successfully logs in, this activity gets invoked. This is the home page of the user.
public class HomePage extends AppCompatActivity implements ImageFragment.OnFragmentInteractionListener {
    public static final String EXTRA_MESSAGE2 = "com.example.dlpbgj.MESSAGE2";
    Button info_button;
    Button myBooksButton;
    Button search;
    Button requests;
    Button borrowed;
    Button signOut;
    Button bookRequests;
    Button returnBook;
    Button acceptBook;
    Button getLocation;
    FirebaseStorage storage;
    FirebaseFirestore Userdb;
    private User currentUser;

    /**
     * Activity is launched when a user successfully signs in.
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        currentUser = (User) getIntent().getSerializableExtra(MainActivity.EXTRA_MESSAGE1);//Catching the user object given by the MainActivity
        info_button = findViewById(R.id.MyInfo);
        myBooksButton = findViewById(R.id.MyBooks);
        search = findViewById(R.id.Search);
        requests = findViewById(R.id.Requests);
        borrowed = findViewById(R.id.Borrowed);
        signOut = findViewById(R.id.SignOut);
        bookRequests = findViewById(R.id.BookRequests);
        returnBook = findViewById(R.id.Return);
        acceptBook = findViewById(R.id.Accept);
        getLocation = findViewById(R.id.getLocation);
        final ImageView profile = findViewById(R.id.Profile);
        storage = FirebaseStorage.getInstance();
        final StorageReference storageReference = storage.getReference();
        final TextView userName = findViewById(R.id.MyName);
        Userdb = FirebaseFirestore.getInstance();
        final String success = "Signed Out!";


        final CollectionReference userBookCollectionReference = Userdb.collection("Users");
        DocumentReference docRef = userBookCollectionReference.document(currentUser.getUsername()); //If username does not exist then prompt for a sign-up
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()){
                        String name="";
                        Map<String,Object> data = document.getData();
                        String firstName = (String) data.get("First Name");
                        String lastName = (String) data.get("Last Name");
                        if(firstName==null || lastName==null){
                            name="Unknown";
                        }
                        else {
                            if (firstName.equals("")) {
                                if (lastName.equals("")) {
                                    name = "Unknown";
                                } else {
                                    name = lastName;
                                }
                            } else {
                                if (lastName.equals("")) {
                                    name = firstName;
                                }
                                name = firstName + " " + lastName;
                            }
                        }
                        name += "'s Library";

                        userName.setText(name);
                        StorageReference imagesRef = storageReference.child("images/" + currentUser.getUsername());
                        imagesRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri downloadUrl) {
                                Glide
                                        .with(getApplicationContext())
                                        .load(downloadUrl.toString())
                                        .centerCrop()
                                        .into(profile);
                            }
                        });
                    }
                } else {
                    Log.d("HomePage", "User get failed with ", task.getException());
                }
            }
        });


        myBooksButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {        //When user clicks this button, a list of all the books that the owner owns is shown
                Intent intent = new Intent(getApplicationContext(), MyBooks.class);
                intent.putExtra(EXTRA_MESSAGE2, currentUser);   //Sending the current user as a parameter to the MyBooks activity
                startActivity(intent);
            }
        });

        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent nIntent = new Intent(view.getContext(), MainActivity.class);
                Toast toast = Toast.makeText(view.getContext(), success, Toast.LENGTH_SHORT);
                toast.show();
                startActivity(nIntent);
            }
        });

        info_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), UserProfile.class);
                intent.putExtra("User", currentUser);
                startActivity(intent);
            }
        });

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Search_by_descr.class);
                intent.putExtra("User", currentUser);
                startActivity(intent);
            }
        });

        requests.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), View_Requests.class);
                intent.putExtra("User", currentUser);
                startActivity(intent);
            }
        });

        borrowed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), View_Borrowed.class);
                intent.putExtra("User", currentUser);
                startActivity(intent);
            }
        });

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImageFragment fragment = ImageFragment.newInstance(currentUser);
                fragment.show(getSupportFragmentManager(), "Profile Picture");
            }
        });

        bookRequests.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), BookRequests.class);
                intent.putExtra("User", currentUser);
                startActivity(intent);
            }
        });

        returnBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ReturnBook.class);
                intent.putExtra("User", currentUser);
                startActivity(intent);
            }
        });

        acceptBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AcceptBook.class);
                intent.putExtra("User", currentUser);
                startActivity(intent);
            }
        });
        getLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LocationDetails.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        //do nothing
    }
}
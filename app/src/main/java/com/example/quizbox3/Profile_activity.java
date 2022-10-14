package com.example.quizbox3;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.quizbox3.databinding.ActivityProfileBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Source;

public class Profile_activity extends AppCompatActivity {
    ActivityProfileBinding binding;

   FirebaseAuth mAuth;
    FirebaseFirestore database;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding =ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

//        userID = mAuth.getUid();

    }

    @Override
    protected void onStart() {
        super.onStart();

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseFirestore.getInstance();
        user = mAuth.getCurrentUser();
        String currentuser = user.getUid().toString();

        database.collection("users").document(currentuser)
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){

                    String profilename = task.getResult().getString("name");
                    String profileemail = task.getResult().getString("email");
                    String profilephn = task.getResult().getString("mobile");

                    binding.profilename.setText(profilename);
                    binding.profileemail.setText(profileemail);
                    binding.profilephn.setText(profilephn);

                }else {
                    Toast.makeText(Profile_activity.this, "Something Went wrong", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}
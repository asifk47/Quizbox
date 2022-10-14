package com.example.quizbox3;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.quizbox3.databinding.ActivitySignUpBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class SignUp extends AppCompatActivity {
    ActivitySignUpBinding binding;
    private FirebaseAuth mAuth;
    private FirebaseFirestore database;
    ProgressDialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseFirestore.getInstance();
        dialog = new ProgressDialog(this);
        dialog.setMessage(" We're creating new account...");

        binding.sgregbtn.setEnabled(false);

        binding.checkBox2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    binding.sgregbtn.setEnabled(true);
                }else{
                    binding.sgregbtn.setEnabled(false);
                }
            }
        });

        binding.sgregbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String name = binding.sgname.getText().toString().trim();
                final String pass = binding.sgpass.getText().toString().trim();
                final String mobile = binding.sgphn.getText().toString().trim();
                final String email = binding.sgemail.getText().toString().trim();

                if(name.isEmpty()){
                    binding.sgname.setError(" Enter Fullname ! ");
                    binding.sgname.requestFocus();
                    return;
                }
                if(email.isEmpty()){
                    binding.sgemail.setError(" Enter your email address !");
                    binding.sgemail.requestFocus();
                    return;
                }
                if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    binding.sgemail.setError(" provide valid email address");
                    binding.sgemail.requestFocus();
                    return;
                }
                if(mobile.isEmpty()){
                    binding.sgphn.setError(" Enter your mobile no. ");
                    binding.sgphn.requestFocus();
                    return;
                }
                if (mobile.length() < 10){
                    binding.sgphn.setError("provide your valid mobile no. ");
                    binding.sgphn.requestFocus();
                    return;
                }
                if(pass.isEmpty()){
                    binding.sgpass.setError(" Enetr your password !");
                    binding.sgpass.requestFocus();
                    return;
                }
                if(pass.length() < 6 ){
                    binding.sgpass.setError(" min password length should be 6 characters !");
                    binding.sgpass.requestFocus();
                    return;
                }


               User user = new User(name,email, mobile, pass);
//                Profile profile = Profile

                dialog.show();

              mAuth.createUserWithEmailAndPassword( email, pass).addOnCompleteListener( new OnCompleteListener<AuthResult>() {
                  @Override
                  public void onComplete(@NonNull Task<AuthResult> task) {
                      if(task.isSuccessful()) {

//                          String uid = task.getResult().getUser().getUid();
                          String uid = binding.sgname.getText().toString().trim();

                          database.collection("users")
                                  .document(uid)
                                  .set(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                      @Override
                                      public void onComplete(@NonNull Task<Void> task) {
                                          if (task.isSuccessful()) {
                                              dialog.dismiss();
                                              Toast.makeText(SignUp.this, "Register Successful", Toast.LENGTH_SHORT).show();
                                              Intent intent = new Intent(SignUp.this, LogIn.class);
                                              startActivity(intent);
                                              finish();
                                          } else {
                                              Toast.makeText(SignUp.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                          }
                                      }
                                  });
                      }
                      else{
                          dialog.dismiss();
                          Toast.makeText(SignUp.this, "Failure", Toast.LENGTH_SHORT).show();
                      }
                  }
              });
            }
        });

        binding.sgloginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignUp.this, LogIn.class));
            }
        });

    }
}
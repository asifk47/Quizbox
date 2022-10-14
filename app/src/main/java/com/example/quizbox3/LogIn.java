package com.example.quizbox3;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.quizbox3.databinding.ActivityLogInBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LogIn extends AppCompatActivity {
    ActivityLogInBinding binding;
    FirebaseAuth mAuth;
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLogInBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();
        dialog = new ProgressDialog(this);
        dialog.setMessage(" Logging In...please wait");

        binding.lgbtn.setEnabled(false);

        binding.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    binding.lgbtn.setEnabled(true);
                }else {
                    binding.lgbtn.setEnabled(false);
                }
            }
        });

        if(mAuth.getCurrentUser() != null){
            startActivity(new Intent(LogIn.this, MainActivity.class));
            this.finish();
        }

        binding.lgbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = binding.lgemail.getText().toString().trim();
                String pass = binding.lgpass.getText().toString().trim();

                dialog.show();

                mAuth.signInWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){

                            dialog.dismiss();
                            Toast.makeText(LogIn.this, "login Successful", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(LogIn.this,MainActivity.class);
                            startActivity(intent);

                            finish();
                        }else{
                            dialog.dismiss();
                            Toast.makeText(LogIn.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

       binding.lgcrtebtn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               startActivity( new Intent(LogIn.this, SignUp.class));
           }
       });
    }
}
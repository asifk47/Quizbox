package com.example.quizbox3;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.quizbox3.databinding.ActivityResultBinding;

public class Result extends AppCompatActivity {
    ActivityResultBinding binding;
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityResultBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        dialog = new ProgressDialog(this);
        dialog.setMessage("Please Wait...");

        int correctAnswers = getIntent().getIntExtra("correct",0);
        int totalQuestions = getIntent().getIntExtra("total", 0);

        binding.score.setText(String.format("%d/%d", correctAnswers, totalQuestions));

        binding.restartbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if( correctAnswers == totalQuestions || correctAnswers != totalQuestions){
                    dialog.show();

                    startActivity(new Intent(Result.this, MainActivity.class));
                    finish();
                }
                dialog.dismiss();
            }
        });

    }

    }

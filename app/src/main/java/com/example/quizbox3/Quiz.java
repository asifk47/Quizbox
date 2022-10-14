package com.example.quizbox3;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.quizbox3.databinding.ActivityQuizBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Random;

public class Quiz extends AppCompatActivity {

    ActivityQuizBinding binding;
    ProgressDialog dialog;

    ArrayList<Question> questions;
    Question question;
    CountDownTimer timer;
    FirebaseFirestore database;
    int correctAnswers;
    int index=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityQuizBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        dialog = new ProgressDialog(this);
        dialog.setMessage("Loading Questions...");

        questions = new ArrayList<>();
        database = FirebaseFirestore.getInstance();

        final String catid = getIntent().getStringExtra("catid");

        Random random = new Random();
        final int rand = random.nextInt(3);

        final String cid = getIntent().getStringExtra("catid");
        final String cname = getIntent().getStringExtra("catname");

        binding.caname.setText(cname);

        Random random1 = new Random();
        final int  ran = random1.nextInt(12);

        // questions add from Firestore database
        dialog.show();

        database.collection("categories")
                .document(cid)
                .collection("questions")
                .whereGreaterThanOrEqualTo("index",ran)
                .orderBy("index").limit(10).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                        if(queryDocumentSnapshots.getDocuments().size() < 10){

                            database.collection("categories")
                                    .document(cid)
                                    .collection("questions")
                                    .whereLessThanOrEqualTo("index",ran)
                                    .orderBy("index").limit(5).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                        @Override
                                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                            dialog.dismiss();
                                            for(DocumentSnapshot snapshot : queryDocumentSnapshots){
                                                Question question = snapshot.toObject(Question.class);
                                                questions.add(question);
                                            }
                                            setnextQuestion();
                                        }
                                    });
                        }else {
                            dialog.dismiss();
                            for(DocumentSnapshot snapshot : queryDocumentSnapshots){
                                Question question = snapshot.toObject(Question.class);
                                questions.add(question);
                            }
                            setnextQuestion();
                        }

                    }
                });
//        questions.add(new Question("Who is the CEO of Google ?", " SRK",
//                "Ratan Tata", "Sundar Pichai", "mukhesh Ambani","Sundar Pichai"));
//        questions.add(new Question("Who is King of the bollywood ? ", "Salman Khan",
//                "SRK", "Amir Khan", "Tiger Shiroof","SRK"));
        setTimer();

    }

    void setnextQuestion(){

        if(timer != null)
            timer.cancel();
        timer.start();

        if( index < questions.size() ){

            binding.questioncounter.setText(String.format("%d/%d",(index+1),questions.size()));
            question = questions.get(index);
            binding.questionbox.setText(question.getQuestions());
            binding.option1.setText(question.getOption1());
            binding.option2.setText(question.getOption2());
            binding.option3.setText(question.getOption3());
            binding.option4.setText(question.getOption4());
        } else {

            Intent intent = new Intent(this, Result.class);
            intent.putExtra("correct", correctAnswers);
            intent.putExtra("total", questions.size());
            startActivity(intent);
            finish();
            Toast.makeText(this, "Quiz Finished", Toast.LENGTH_SHORT).show();

        }
    }

    void setTimer(){
        timer = new CountDownTimer(30000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                binding.timer.setText(String.valueOf(millisUntilFinished/1000));

            }

            @Override
            public void onFinish() {

            }
        };
    }
    void showAnswer(){
        if(question.getAnswer().equals(binding.option1.getText().toString()))
            binding.option1.setBackground(getResources().getDrawable(R.drawable.question_right));
        else if(question.getAnswer().equals(binding.option2.getText().toString()))
            binding.option2.setBackground(getResources().getDrawable(R.drawable.question_right));
        else if(question.getAnswer().equals(binding.option3.getText().toString()))
            binding.option3.setBackground(getResources().getDrawable(R.drawable.question_right));
        else if(question.getAnswer().equals(binding.option4.getText().toString()))
            binding.option4.setBackground(getResources().getDrawable(R.drawable.question_right));

    }

    void chekAnswer(TextView textView){
        String selectedAnswer = textView.getText().toString();
        if(selectedAnswer.equals(question.getAnswer())){
            correctAnswers++;
            textView.setBackground(getResources().getDrawable(R.drawable.question_right));
            Toast.makeText(this, "Correct Answer", Toast.LENGTH_SHORT).show();
        }else {
            showAnswer();
            textView.setBackground(getResources().getDrawable(R.drawable.question_wrong));
            Toast.makeText(this, "Wrong Answer", Toast.LENGTH_SHORT).show();
            setnextQuestion();
        }
    }

    void reset(){
        binding.option1.setBackground(getResources().getDrawable(R.drawable.question_option));
        binding.option2.setBackground(getResources().getDrawable(R.drawable.question_option));
        binding.option3.setBackground(getResources().getDrawable(R.drawable.question_option));
        binding.option4.setBackground(getResources().getDrawable(R.drawable.question_option));
    }

    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.option1:
            case R.id.option2:
            case R.id.option3:
            case R.id.option4:

                if (timer != null) {
                    timer.cancel();
                }
                TextView selected = (TextView) view;
                chekAnswer(selected);
                break;

            case R.id.nextbtn:
                reset();
                if ((index+1) <= questions.size()) {
                    index++;
                    setnextQuestion();

                } else {
                    Intent intent = new Intent(this, Result.class);
                    intent.putExtra("correct", correctAnswers);
                    intent.putExtra("total", questions.size());
                    startActivity(intent);
                    finish();
                    Toast.makeText(this, "Quiz Finished", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.quitbtn:

                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                finish();

        }

    }
}

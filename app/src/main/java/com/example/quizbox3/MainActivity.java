package com.example.quizbox3;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.quizbox3.databinding.ActivityMainBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    FirebaseAuth mAuth;
    FirebaseFirestore database;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseFirestore.getInstance();



        if(mAuth.getCurrentUser() == null){
            startActivity(new Intent(MainActivity.this, LogIn.class));
            finish();
        }

        binding.profile123.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Profile_activity.class);
                startActivity(intent);
            }
        });

        binding.logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                startActivity(new Intent(MainActivity.this,LogIn.class));
                finish();
            }
        });

        ArrayList<CategoryModel> categories =  new ArrayList<>();

//        categories.add( new CategoryModel("","JAVA","https://1000logos.net/wp-content/uploads/2020/09/Java-Emblem.jpg"));
//        categories.add( new CategoryModel("","C/C++","https://e7.pngegg.com/pngimages/46/626/png-clipart-c-logo-the-c-programming-language-computer-icons-computer-programming-source-code-programming-miscellaneous-template.png"));
//        categories.add( new CategoryModel("","Python","https://mpng.subpng.com/20180712/yka/kisspng-professional-python-programmer-computer-programmin-python-logo-download-5b47725c1cc0d6.3474912915314089881178.jpg"));
//        categories.add( new CategoryModel("","HTML","https://cdn.pixabay.com/photo/2017/08/05/11/16/logo-2582748_1280.png"));

        CategoryAdapter adapter = new CategoryAdapter(this, categories);

        database.collection("categories")
                        .addSnapshotListener(new EventListener<QuerySnapshot>() {
                            @Override
                            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                                categories.clear();
                                for(DocumentSnapshot snapshot : value.getDocuments()){
                                    CategoryModel model = snapshot.toObject(CategoryModel.class);
                                    model.setCatid(snapshot.getId());
                                    categories.add(model);
                                }
                                adapter.notifyDataSetChanged();
                            }
                        });

        binding.categoryList.setLayoutManager(new GridLayoutManager(this,2));
        binding.categoryList.setAdapter(adapter);

    }
}
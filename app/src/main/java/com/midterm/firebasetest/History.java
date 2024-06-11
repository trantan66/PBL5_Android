package com.midterm.firebasetest;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class History extends AppCompatActivity {
    RecyclerView rvViewHistory;
    HistoryAdapter historyAdapter;
    String username, password;
    ImageButton btnSearch, btnResult, btnProfile, btnDelete, btnHome;
    ArrayList<String> labelNameArr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        Intent intent = getIntent();
        labelNameArr = intent.getStringArrayListExtra("labelNameArr");
        username = intent.getStringExtra("USERNAME");
        password = intent.getStringExtra("PASSWORD");
        rvViewHistory = findViewById(R.id.rv_viewhistory);
        rvViewHistory.setLayoutManager(new LinearLayoutManager(this));

        txtSearch(username);

        btnDelete = findViewById(R.id.btn_delete);
        btnSearch = findViewById(R.id.btn_search);
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(History.this, Search.class);
                intent.putStringArrayListExtra("labelNameArr", labelNameArr);
                intent.putExtra("USERNAME", username);
                intent.putExtra("PASSWORD", password);
                startActivity(intent);
            }
        });
        btnResult = findViewById(R.id.btn_result);
        btnResult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!labelNameArr.isEmpty()){
                    Intent intent = new Intent(History.this, Result.class);
                    intent.putStringArrayListExtra("labelNameArr", labelNameArr);
                    intent.putExtra("USERNAME", username);
                    intent.putExtra("PASSWORD", password);
                    startActivity(intent);
                    labelNameArr.clear();
                }
            }
        });

        btnProfile = findViewById(R.id.btn_profile);
        btnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(History.this, ProfileActivity.class);
                intent.putStringArrayListExtra("labelNameArr", labelNameArr);
                intent.putExtra("USERNAME", username);
                intent.putExtra("PASSWORD", password);
                startActivity(intent);
            }
        });
        btnHome = findViewById(R.id.btn_home);
        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(History.this, DetectPage.class);
                if(labelNameArr.isEmpty()){
                    labelNameArr.add("1");
                }
                intent.putStringArrayListExtra("labelNameArr", labelNameArr);
                intent.putExtra("USERNAME", username);
                intent.putExtra("PASSWORD", password);
                startActivity(intent);
            }
        });
    }
    @Override
    protected void onStart() {
        super.onStart();
        historyAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        historyAdapter.stopListening();
    }
    private void txtSearch(String str){
        FirebaseRecyclerOptions<HistoryModel> options =
                new FirebaseRecyclerOptions.Builder<HistoryModel>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("History").orderByChild("username").equalTo(str), HistoryModel.class)
                        .build();
        historyAdapter = new HistoryAdapter(options);
        historyAdapter.startListening();
        rvViewHistory.setAdapter(historyAdapter);
    }
}
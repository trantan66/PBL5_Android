package com.midterm.firebasetest;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

public class History extends AppCompatActivity {
    RecyclerView rvViewHistory;
    HistoryAdapter historyAdapter;
    String username;
    Button btnDelete;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        Intent intent = getIntent();
        username = intent.getStringExtra("USERNAME");
        rvViewHistory = findViewById(R.id.rv_viewhistory);
        rvViewHistory.setLayoutManager(new LinearLayoutManager(this));

        txtSearch(username);

        btnDelete = findViewById(R.id.btn_delete);
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
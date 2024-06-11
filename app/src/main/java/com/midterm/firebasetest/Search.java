package com.midterm.firebasetest;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SearchView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class Search extends AppCompatActivity {
    RecyclerView rvView;
    SearchAdapter searchAdapter;
    String username, password;
    ArrayList<String> labelNameArr;
    ImageButton btnResult, btnHistory, btnProfile, btnHome;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_page);
        setSupportActionBar(findViewById(R.id.main_toolbar));
        Intent intent = getIntent();

        labelNameArr = intent.getStringArrayListExtra("labelNameArr");
        username = intent.getStringExtra("USERNAME");
        password = intent.getStringExtra("PASSWORD");
        rvView = findViewById(R.id.rv_view);
        rvView.setLayoutManager(new LinearLayoutManager(this));

        FirebaseRecyclerOptions<SearchModel> options =
                new FirebaseRecyclerOptions.Builder<SearchModel>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("dataset"), SearchModel.class)
                        .build();
        searchAdapter = new SearchAdapter(options);

        rvView.setAdapter(searchAdapter);

        btnResult = findViewById(R.id.btn_result);
        btnResult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!labelNameArr.isEmpty()){
                    Intent intent = new Intent(Search.this, Result.class);
                    intent.putStringArrayListExtra("labelNameArr", labelNameArr);
                    intent.putExtra("USERNAME", username);
                    intent.putExtra("PASSWORD", password);
                    startActivity(intent);
                    labelNameArr.clear();
                }
            }
        });
        btnHistory = findViewById(R.id.btn_history);
        btnHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Search.this, History.class);
                intent.putStringArrayListExtra("labelNameArr", labelNameArr);
                intent.putExtra("USERNAME", username);
                intent.putExtra("PASSWORD", password);
                startActivity(intent);
            }
        });

        btnProfile = findViewById(R.id.btn_profile);
        btnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Search.this, ProfileActivity.class);
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
                Intent intent = new Intent(Search.this, DetectPage.class);
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
        searchAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        searchAdapter.stopListening();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search, menu);
        MenuItem item = menu.findItem(R.id.search_button);
        SearchView searchView = (SearchView) item.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                txtSearch(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                txtSearch(query);
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }
    private void txtSearch(String str){
        FirebaseRecyclerOptions<SearchModel> options =
                new FirebaseRecyclerOptions.Builder<SearchModel>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("dataset").orderByChild("name").startAt(str).endAt(str+"\uf8ff"), SearchModel.class)
                        .build();
        searchAdapter = new SearchAdapter(options);
        searchAdapter.startListening();
        rvView.setAdapter(searchAdapter);
    }
}

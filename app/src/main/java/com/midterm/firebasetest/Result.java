package com.midterm.firebasetest;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class Result extends AppCompatActivity {
    TextView tvPlantname;
    RecyclerView rvView;
    ResultAdapter resultAdapter;
    ArrayList<String> danhSach;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.result_page);
        tvPlantname = findViewById(R.id.tv_nameplant);

        rvView = findViewById(R.id.rv_resultview);
        rvView.setLayoutManager(new LinearLayoutManager(this));

        Intent intent = getIntent();
        danhSach = new ArrayList<>();
        danhSach.clear();
        danhSach = intent.getStringArrayListExtra("labelNameArr");

        Set<String> ds = new HashSet<>(danhSach);
        String str = "";
        for (String i : ds){
            str += i;
        }
        tvPlantname.setText(str.toUpperCase());
        FirebaseRecyclerOptions<ResultModel> options =
                new FirebaseRecyclerOptions.Builder<ResultModel>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("dataset").orderByChild("name").equalTo(str.toUpperCase()), ResultModel.class)
                        .build();
        resultAdapter = new ResultAdapter(options);
        resultAdapter.startListening();
        rvView.setAdapter(resultAdapter);
    }
    @Override
    protected void onStart() {
        super.onStart();
        resultAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        resultAdapter.stopListening();
    }

}

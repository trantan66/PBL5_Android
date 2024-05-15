package com.midterm.firebasetest;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class Result extends AppCompatActivity {
    TextView txtResult;
    ArrayList<String> danhSach;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.result_page);
        txtResult = findViewById(R.id.txt_name);
        Intent intent = getIntent();
        danhSach = new ArrayList<>();
        danhSach.clear();
        danhSach = intent.getStringArrayListExtra("labelNameArr");
        Set<String> ds = new HashSet<>(danhSach);
        String temp = "";
        txtResult.setText(temp);
    }
}

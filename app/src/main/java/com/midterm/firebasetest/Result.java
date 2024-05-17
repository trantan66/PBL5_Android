package com.midterm.firebasetest;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class Result extends AppCompatActivity {

    private RecyclerView rvView;
    private ResultAdapter resultAdapter;
    private ArrayList<ResultModel> resultArr;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.result_page);

        rvView = findViewById(R.id.rv_resultview);
        rvView.setLayoutManager(new LinearLayoutManager(this));

        resultArr = new ArrayList<>();
        resultAdapter = new ResultAdapter(resultArr);
        rvView.setAdapter(resultAdapter);

        Intent intent = getIntent();
        ArrayList<String> danhSach = intent.getStringArrayListExtra("labelNameArr");

        if (danhSach != null && !danhSach.isEmpty()) {
            Set<String> ds = new HashSet<>(danhSach);
            String str = "";
            for (String i : ds) {
                searchItems(i);
            }
        }
    }

    private void searchItems(String keyword) {
        Log.d("Result", "Searching for keyword: " + keyword); // Log từ khóa tìm kiếm
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("dataset");
        Query query = databaseReference.orderByChild("name").equalTo(keyword);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
//                resultArr.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    ResultModel model = snapshot.getValue(ResultModel.class);
                    if (model != null) {
                        Log.d("Result", "Found: " + model.getName()); // Log các mục tìm thấy
                        resultArr.add(model);
                    }
                }
                resultAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("Result", "Search failed: " + databaseError.getMessage()); // Log lỗi nếu có
            }
        });
    }

}

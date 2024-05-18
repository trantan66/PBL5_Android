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

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class Result extends AppCompatActivity {

    private RecyclerView rvView;
    private ResultAdapter resultAdapter;
    private ArrayList<ResultModel> resultArr;
    String username;

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
        username = intent.getStringExtra("USERNAME");

        if (danhSach != null && !danhSach.isEmpty()) {
            Set<String> ds = new HashSet<>(danhSach);
            for (String i : ds) {
                searchItems(i);
            }
        }
    }

    private void searchItems(String keyword) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("dataset");
        Query query = databaseReference.orderByChild("name").equalTo(keyword);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    ResultModel model = snapshot.getValue(ResultModel.class);
                    if (model != null) {
                        resultArr.add(model);
                        addToHistory(model, username);
                    }
                }
                resultAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("Result", "Search failed: " + databaseError.getMessage());
            }
        });
    }

    private void addToHistory(ResultModel model, String username) {
        DatabaseReference historyReference = FirebaseDatabase.getInstance().getReference("History");
        String historyId = historyReference.push().getKey();


        if (historyId != null) {
            HistoryModel historyModel = new HistoryModel(model, username);
            historyReference.child(historyId).setValue(historyModel);
        }
    }
    public static class HistoryModel {
        private String name;
        private String alternativename;
        private String image;
        private String sciencename;
        private String family;
        private String partused;
        private String uses;
        private String timestamp;
        private String username;

        public HistoryModel() {
        }

        public HistoryModel(ResultModel model, String username) {
            this.name = model.getName();
            this.alternativename = model.getAlternativename();
            this.image = model.getImage();
            this.sciencename = model.getSciencename();
            this.family = model.getFamily();
            this.partused = model.getPartused();
            this.uses = model.getUses();
            this.timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            this.username = username;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getAlternativename() {
            return alternativename;
        }

        public void setAlternativename(String alternativename) {
            this.alternativename = alternativename;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getSciencename() {
            return sciencename;
        }

        public void setSciencename(String sciencename) {
            this.sciencename = sciencename;
        }

        public String getFamily() {
            return family;
        }

        public void setFamily(String family) {
            this.family = family;
        }

        public String getPartused() {
            return partused;
        }

        public void setPartused(String partused) {
            this.partused = partused;
        }

        public String getUses() {
            return uses;
        }

        public void setUses(String uses) {
            this.uses = uses;
        }

        public String getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(String timestamp) {
            this.timestamp = timestamp;
        }
    }
}

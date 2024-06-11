package com.midterm.firebasetest;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toolbar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class DetectPage extends AppCompatActivity {
    private final int IMAGE_PICK = 100;
    ImageView imageView;
    Bitmap bitmap;
    Yolov5TFLiteDetector yolov5TFLiteDetector;
    Paint boxPaint = new Paint();
    Paint textPain = new Paint();

    ArrayList<String> labelNameArr;
    ImageButton btnSearch, btnResult, btnHistory, btnProfile;
    String username, password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detect_page);

        Intent intent = getIntent();
        username = intent.getStringExtra("USERNAME");
        password = intent.getStringExtra("PASSWORD");
        labelNameArr = new ArrayList<>();
        labelNameArr = intent.getStringArrayListExtra("labelNameArr");
        if(labelNameArr.get(0).equals("1")){
            labelNameArr.clear();
        }
        imageView = findViewById(R.id.imageView);

        yolov5TFLiteDetector = new Yolov5TFLiteDetector();
//        yolov5TFLiteDetector.setModelFile("yolov5s-fp16.tflite");
        yolov5TFLiteDetector.setModelFile("best-fp16.tflite");
        yolov5TFLiteDetector.initialModel(this);

        boxPaint.setStrokeWidth(5);
        boxPaint.setStyle(Paint.Style.STROKE);
        boxPaint.setColor(Color.RED);

        textPain.setTextSize(50);
        textPain.setColor(Color.GREEN);
        textPain.setStyle(Paint.Style.FILL);


        btnSearch = findViewById(R.id.btn_search);
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetectPage.this, Search.class);
                intent.putStringArrayListExtra("labelNameArr", labelNameArr);
                intent.putExtra("USERNAME", username);
                intent.putExtra("PASSWORD", password);
                startActivity(intent);
                labelNameArr.clear();
            }
        });
        btnResult = findViewById(R.id.btn_result);
        btnResult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!labelNameArr.isEmpty()){
                    Intent intent = new Intent(DetectPage.this, Result.class);
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
                Intent intent = new Intent(DetectPage.this, History.class);
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
                Intent intent = new Intent(DetectPage.this, ProfileActivity.class);
                intent.putStringArrayListExtra("labelNameArr", labelNameArr);
                intent.putExtra("USERNAME", username);
                intent.putExtra("PASSWORD", password);
                startActivity(intent);
            }
        });
    }
    public void selectImage(View view){
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, IMAGE_PICK);
    }

    public void predict(View view){
        ArrayList<Recognition> recognitions =  yolov5TFLiteDetector.detect(bitmap);
        Bitmap mutableBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
        Canvas canvas = new Canvas(mutableBitmap);

        labelNameArr.clear();

        for(Recognition recognition: recognitions){
            if(recognition.getConfidence() > 0.4){
                RectF location = recognition.getLocation();
                canvas.drawRect(location, boxPaint);
                DecimalFormat df = new DecimalFormat("#.##");
                canvas.drawText(recognition.getLabelName() + ":" + df.format(recognition.getConfidence()), location.left, location.top, textPain);
                labelNameArr.add(recognition.getLabelName());
            }
        }

        //recognition.getLabelName()

        imageView.setImageBitmap(mutableBitmap);

        if (!labelNameArr.isEmpty()) {
            Set<String> ds = new HashSet<>(labelNameArr);
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
                        addToHistory(model, username);
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("Result", "Search failed: " + databaseError.getMessage());
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == IMAGE_PICK && data != null){
            Uri uri = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                imageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
    private void addToHistory(ResultModel model, String username) {
        DatabaseReference historyReference = FirebaseDatabase.getInstance().getReference("History");
        String historyId = historyReference.push().getKey();


        if (historyId != null) {
            Result.HistoryModel historyModel = new Result.HistoryModel(model, username);
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

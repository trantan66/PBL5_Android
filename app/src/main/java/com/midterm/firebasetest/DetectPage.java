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
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toolbar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class DetectPage extends AppCompatActivity {
    private final int IMAGE_PICK = 100;
    ImageView imageView;
    Bitmap bitmap;
    Yolov5TFLiteDetector yolov5TFLiteDetector;
    Paint boxPaint = new Paint();
    Paint textPain = new Paint();

    ArrayList<String> labelNameArr;
    Button btnSearch, btnResult, btnProfile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detect_page);

        Intent intent = getIntent();
        String username = intent.getStringExtra("USERNAME");
        String password = intent.getStringExtra("PASSWORD");

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

        labelNameArr = new ArrayList<>();

        btnSearch = findViewById(R.id.btn_search);
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetectPage.this, Search.class);
                startActivity(intent);
            }
        });
        btnResult = findViewById(R.id.btn_result);
        btnResult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!labelNameArr.isEmpty()){
                    Intent intent = new Intent(DetectPage.this, Result.class);
                    intent.putStringArrayListExtra("labelNameArr", labelNameArr);
                    startActivity(intent);
                    labelNameArr.clear();
                }
            }
        });
        btnProfile = findViewById(R.id.btn_profile);
        btnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetectPage.this, ProfileActivity.class);
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
}

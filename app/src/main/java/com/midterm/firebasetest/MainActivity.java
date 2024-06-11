package com.midterm.firebasetest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    TextView txtUsername;
    TextView txtPassword;
    TextView txtRegister;
    Button btnLogin;
    ArrayList<String> labelNameArr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_page);

        txtUsername = findViewById(R.id.txt_username);
        txtPassword = findViewById(R.id.txt_password);
        btnLogin = findViewById(R.id.btn_login);
        txtRegister = findViewById(R.id.txt_register);
        labelNameArr = new ArrayList<>();
        labelNameArr.add("1");
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!validateUsername() || !validatePassword()){

                }else {
                    checkUser();
                }
            }
        });
        txtRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Register.class);
                startActivity(intent);
            }
        });

    }
    public Boolean validateUsername(){
        String val = txtUsername.getText().toString();
        if(val.isEmpty()){
            txtUsername.setError("Username cannot be empty");
            return false;
        }else{
            txtUsername.setError(null);
            return true;
        }
    }
    public Boolean validatePassword(){
        String val = txtPassword.getText().toString();
        if(val.isEmpty()){
            txtPassword.setError("Password cannot be empty");
            return false;
        }else{
            txtPassword.setError(null);
            return true;
        }
    }
    public void checkUser(){
        String username = txtUsername.getText().toString().trim();
        String password = txtPassword.getText().toString().trim();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("account");
        Query checkUserDatabase = reference.orderByChild("username").equalTo(username);

        checkUserDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    txtUsername.setError(null);
                    String passwordFromDB = snapshot.child(username).child("password").getValue(String.class);

                    if(passwordFromDB.equals(password)){
                        txtUsername.setError(null);
                        Intent intent = new Intent(MainActivity.this, DetectPage.class);
                        intent.putExtra("USERNAME", username);
                        intent.putExtra("PASSWORD", password);
                        intent.putExtra("labelNameArr", labelNameArr);
                        startActivity(intent);
                    }else{
                        txtPassword.setError("Username or password is wrong");
                        txtPassword.requestFocus();
                    }
                }else{
                    txtUsername.setError("User is not exist");
                    txtUsername.requestFocus();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
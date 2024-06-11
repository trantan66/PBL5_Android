package com.midterm.firebasetest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ProfileActivity extends AppCompatActivity {
    Button btnChangeInfo, btnChangePassword, btnLogout;
    EditText txtNameInfo, txtOldPassword, txtNewPassword, txtConfirmPassword;
    DatabaseReference databaseReference;
    String username, password, name;
    ImageButton btnSearch, btnResult, btnHistory, btnHome;
    ArrayList<String> labelNameArr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Intent intent = getIntent();

        labelNameArr = intent.getStringArrayListExtra("labelNameArr");
        username = intent.getStringExtra("USERNAME");
        password = intent.getStringExtra("PASSWORD");
        databaseReference = FirebaseDatabase.getInstance().getReference("account");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String data = snapshot.child(username).child("name").getValue(String.class);
                name = data;
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String data = snapshot.child(username).child("password").getValue(String.class);
                password = data;
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        btnChangeInfo = findViewById(R.id.btn_changeinfo);
        btnChangePassword = findViewById(R.id.btn_changepassword);
        btnLogout = findViewById(R.id.btn_logout);

        btnChangeInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final DialogPlus dialogPlus = DialogPlus.newDialog(ProfileActivity.this)
                        .setContentHolder(new ViewHolder(R.layout.changeinfo))
                        .setExpanded(true, 1603)
                        .create();

                View view = dialogPlus.getHolderView();
                txtNameInfo = view.findViewById(R.id.txt_nameinfo);
                Button btnChangeinfo = view.findViewById(R.id.btn_changeinfo);

                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String data = snapshot.child(username).child("name").getValue(String.class);
                        txtNameInfo.setText(data);
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                dialogPlus.show();

                btnChangeinfo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Map<String, Object> map = new HashMap<>();
                        map.put("username", username);
                        map.put("password", password);
                        map.put("name", txtNameInfo.getText().toString());

                        databaseReference.child(username).updateChildren(map)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(ProfileActivity.this, "Update success", Toast.LENGTH_SHORT).show();
                                        dialogPlus.dismiss();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(ProfileActivity.this, "Update error", Toast.LENGTH_SHORT).show();
                                        dialogPlus.dismiss();
                                    }
                                });
                    }
                });
            }
        });
        btnChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final DialogPlus dialogPlus = DialogPlus.newDialog(ProfileActivity.this)
                        .setContentHolder(new ViewHolder(R.layout.changepassword))
                        .setExpanded(true, 1603)
                        .create();

                View view = dialogPlus.getHolderView();

                Button btnChangepw = view.findViewById(R.id.btn_changepassword);

                dialogPlus.show();
                btnChangepw.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        txtOldPassword = view.findViewById(R.id.txt_password);
                        txtNewPassword = view.findViewById(R.id.txt_newpassword);
                        txtConfirmPassword = view.findViewById(R.id.txt_confirmpassword);

                        String strOldPassword = txtOldPassword.getText().toString();
                        String strNewPassword = txtNewPassword.getText().toString();
                        String strConfirmPassword = txtConfirmPassword.getText().toString();

                        if(!validateOldPassword(strOldPassword) || !validateNewPassword(strNewPassword) || !validateConfirmPassword(strConfirmPassword)){

                        }else {
                            if(checkPassword(username, strOldPassword, strNewPassword, strConfirmPassword)){
                                dialogPlus.dismiss();
                            }
                        }
                    }
                });

            }
        });
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        btnSearch = findViewById(R.id.btn_search);
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, Search.class);
                startActivity(intent);
            }
        });
        btnResult = findViewById(R.id.btn_result);
        btnResult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!labelNameArr.isEmpty()){
                    Intent intent = new Intent(ProfileActivity.this, Result.class);
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
                Intent intent = new Intent(ProfileActivity.this, History.class);
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
                Intent intent = new Intent(ProfileActivity.this, DetectPage.class);
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
    public Boolean validateOldPassword(String str){
        if(str.isEmpty()){
            txtOldPassword.setError("Password cannot be empty");
            return false;
        }else{
            txtOldPassword.setError(null);
            return true;
        }
    }
    public Boolean validateNewPassword(String str){
        if(str.isEmpty()){
            txtNewPassword.setError("New password cannot be empty");
            return false;
        }else{
            txtNewPassword.setError(null);
            return true;
        }
    }
    public Boolean validateConfirmPassword(String str){
        if(str.isEmpty()){
            txtConfirmPassword.setError("Confirm password cannot be empty");
            return false;
        }else{
            txtConfirmPassword.setError(null);
            return true;
        }
    }
    public Boolean checkPassword(String username, String oldpassword, String newpassword, String confirmpassword){
        final Boolean[] check = {true};

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("account");
        Query checkUserDatabase = reference.orderByChild("username").equalTo(username);

        checkUserDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    txtOldPassword.setError(null);
                    String passwordFromDB = snapshot.child(username).child("password").getValue(String.class);
                    String name1 = snapshot.child(username).child("name").getValue(String.class);

                    if(passwordFromDB.equals(oldpassword)){
                        txtOldPassword.setError(null);

                        Map<String, Object> map = new HashMap<>();
                        map.put("username", username);
                        map.put("password", newpassword);
                        map.put("name", name1);

                        databaseReference.child(username).updateChildren(map)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(ProfileActivity.this, "Update success", Toast.LENGTH_SHORT).show();
                                        check[0] = false;
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(ProfileActivity.this, "Update error", Toast.LENGTH_SHORT).show();
                                    }
                                });

                        if(!newpassword.equals(confirmpassword)){
                            Toast.makeText(ProfileActivity.this, "The new password is not same confirm password", Toast.LENGTH_SHORT).show();
                            txtNewPassword.requestFocus();
                            check[0] = false;
                        }

                    }else{
                        txtOldPassword.setError("Password is wrong");
                        txtOldPassword.requestFocus();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return check[0];
    }
}
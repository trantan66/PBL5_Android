package com.midterm.firebasetest;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class Register extends AppCompatActivity {
    EditText txtUsername, txtName, txtPassword, txtConfirmpassword;
    Button btnRegister;
    FirebaseDatabase database;
    DatabaseReference reference;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_page);
        txtUsername = findViewById(R.id.txt_username);
        txtName = findViewById(R.id.txt_name);
        txtPassword = findViewById(R.id.txt_password);
        txtConfirmpassword = findViewById(R.id.txt_confirmpassword);
        btnRegister = findViewById(R.id.btn_register);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!validateUsername() || !validatePassword() || !validateConfirmPassword() || !validateName() || !checkUser()){

                }else{
                    database = FirebaseDatabase.getInstance();
                    reference = database.getReference("account");
                    String username = txtUsername.getText().toString();
                    String name = txtName.getText().toString();
                    String password = txtPassword.getText().toString();
                    String confirmpassword = txtConfirmpassword.getText().toString();
                    if(checkConfirmpassword(password, confirmpassword)){
                        LoginModel loginModel = new LoginModel(username, password, name);
                        reference.child(username).setValue(loginModel);

                        Toast.makeText(Register.this, "Successful", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(Register.this, MainActivity.class);
                        startActivity(intent);
                    }

                }

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
    public Boolean validateName(){
        String val = txtName.getText().toString();
        if(val.isEmpty()){
            txtName.setError("Name cannot be empty");
            return false;
        }else{
            txtName.setError(null);
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
    public Boolean validateConfirmPassword(){
        String val = txtConfirmpassword.getText().toString();
        if(val.isEmpty()){
            txtConfirmpassword.setError("Confirm password cannot be empty");
            return false;
        }else{
            txtConfirmpassword.setError(null);
            return true;
        }
    }
    public Boolean checkConfirmpassword(String password, String confirmpassword){
        if(!password.equals(confirmpassword)){
            txtConfirmpassword.setError("Password and confirm password are not same");
            return false;
        }else{
            txtConfirmpassword.setError(null);
            return true;
        }
    }
    private Boolean check = false;
    public Boolean checkUser(){
        String username = txtUsername.getText().toString().trim();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("account");
        Query checkUserDatabase = reference.orderByChild("username").equalTo(username);

        checkUserDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    check = false;
                    txtUsername.setError("Username is already exist!");
                }else{
                    check = true;
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return check;
    }
}

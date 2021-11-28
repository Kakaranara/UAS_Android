package umn.ac.id.uasmobileapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Login extends AppCompatActivity {
    EditText inputEmail, inputPassword;
    Button btnLogin;
    String passwordFromDB;
    public static boolean loginUser = false;
    public static boolean loginAdmin = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        inputEmail = findViewById(R.id.inputEmail);
        inputPassword = findViewById(R.id.inputPassword);

//        btnLogin.setOnClickListener(this::loginUser);
    }

    public static String md5(final String s) {
        final String MD5 = "MD5";
        try {
            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest
                    .getInstance(MD5);
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuilder hexString = new StringBuilder();
            for (byte aMessageDigest : messageDigest) {
                String h = Integer.toHexString(0xFF & aMessageDigest);
                while (h.length() < 2)
                    h = "0" + h;
                hexString.append(h);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    private Boolean validateEmail() {
        String val = inputEmail.getText().toString();
        //String noWhiteSpace = "\\A\\w{4,20}\\z";

        if(val.isEmpty()) {
            Toast.makeText(Login.this,"Field cannot be empty",Toast.LENGTH_SHORT).show();
            inputEmail.setError("Field cannot be empty");
            return false;
        }else{
            inputEmail.setError(null);
            return true;
        }
    }

    private Boolean validatePassword() {
        String val = inputPassword.getText().toString();

        if(val.isEmpty()) {
            Toast.makeText(Login.this,"Field cannot be empty",Toast.LENGTH_SHORT).show();
            inputPassword.setError("Field cannot be empty");
            return false;
        } else{
            inputPassword.setError(null);
            return true;
        }
    }

    public void loginUser(View view) {
        Toast.makeText(Login.this,"Login User Enter",Toast.LENGTH_SHORT).show();
        if(!validateEmail() | !validatePassword()){
            Toast.makeText(Login.this,"Not Valid",Toast.LENGTH_SHORT).show();
        } else{
            Toast.makeText(Login.this,"Valid",Toast.LENGTH_SHORT).show();
            isUser();
        }
    }

    private void isUser() {
        String userEnteredEmail = inputEmail.getText().toString();
        String userEnteredPassword = inputPassword.getText().toString();
        DatabaseReference reference = FirebaseDatabase.getInstance("https://final-project-mobile-app-98d46-default-rtdb.firebaseio.com/").getReference("accounts");
        Query query = reference.orderByChild("email").equalTo(userEnteredEmail);
        System.out.println(query);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    System.out.print(dataSnapshot);
                    inputEmail.setError(null);
                    for(DataSnapshot userSnapshot: dataSnapshot.getChildren()){
                        passwordFromDB = userSnapshot.child("password").getValue(String.class);
                    }

                    if(passwordFromDB.equals(md5(userEnteredPassword + "user"))){
                        inputPassword.setError(null);
                        loginUser = true;
                        Toast.makeText(Login.this,"User Login Success",Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(Login.this, UserActivity.class);
                        intent.putExtra("businessName", dataSnapshot.child("business_name").getValue(String.class));
                        startActivity(intent);
                    } else if(passwordFromDB.equals(md5(userEnteredPassword + "admin"))){
                        loginAdmin = true;
                        Toast.makeText(Login.this, "Admin Login Success", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(Login.this, AdminActivity.class);
                        intent.putExtra("businessName", dataSnapshot.child("bName").getValue(String.class));
                        startActivity(intent);
                    }
                    else{
                        Toast.makeText(Login.this,"Wrong Password",Toast.LENGTH_SHORT).show();
                        //inputPassword.setError("Wrong Password");
                    }
                } else {
                    Toast.makeText(Login.this,"No such User exist",Toast.LENGTH_SHORT).show();
                    //inputEmail.setError("No such User exist");
                    inputEmail.requestFocus();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
package umn.ac.id.uasmobileapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Login extends AppCompatActivity {
    EditText inputEmail, inputPassword;
    Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        inputEmail = findViewById(R.id.inputEmail);
        inputPassword = findViewById(R.id.inputPassword);
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
            return;
        } else{
            Toast.makeText(Login.this,"Valid",Toast.LENGTH_SHORT).show();
            isUser();
        }
    }

    private void isUser() {
        String userEnteredEmail = inputEmail.getText().toString().replace(".",",");
        String userEnteredPassword = inputPassword.getText().toString();

        DatabaseReference reference = FirebaseDatabase.getInstance("https://final-project-mobile-app-98d46-default-rtdb.firebaseio.com/").getReference("users");
//        String checkUser = reference.child("").equalTo(userEnteredEmail);


//        System.out.println(checkUser);

        reference.child("U" + userEnteredEmail).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    inputEmail.setError(null);
                    String passwordFromDB = dataSnapshot.child("password").getValue(String.class);
                    if(passwordFromDB.equals(userEnteredPassword)){
                        System.out.println("sini1");
                        inputPassword.setError(null);
                        Toast.makeText(Login.this,"Login Success",Toast.LENGTH_SHORT).show();
                    } else{
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
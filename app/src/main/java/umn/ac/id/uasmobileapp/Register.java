package umn.ac.id.uasmobileapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.UUID;

public class Register extends AppCompatActivity {
    EditText inputBName, inputEmail, inputPassword, inputRePassword;
    Button btnRegister;

    FirebaseDatabase rootNode;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        inputBName = findViewById(R.id.inputBName);
        inputEmail = findViewById(R.id.inputEmail);
        inputPassword = findViewById(R.id.inputPassword);
        inputRePassword = findViewById(R.id.inputRePassword);
        btnRegister = findViewById(R.id.btnRegister);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rootNode = FirebaseDatabase.getInstance("https://final-project-mobile-app-98d46-default-rtdb.firebaseio.com/");
                reference = rootNode.getReference("users");

                //Get user input values
                String bName = inputBName.getText().toString();
                String email = inputEmail.getText().toString();
                String password = inputPassword.getText().toString();
                String passwordVal = inputRePassword.getText().toString();

                if(validate(password, passwordVal)) {
                    UserHelperClass helperClass = new UserHelperClass(bName, email, password);

                    String userID = "U" + UUID.randomUUID().toString();

                    reference.child(userID).setValue(helperClass);
                } else{
                    Toast.makeText(Register.this,"Password Not matching",Toast.LENGTH_SHORT).show();
                }
            }

            private boolean validate(String password, String passwordVal) {
                boolean temp=true;

                if(!password.equals(passwordVal)){
                    temp=false;
                }
                return temp;
            }
        });
    }
}
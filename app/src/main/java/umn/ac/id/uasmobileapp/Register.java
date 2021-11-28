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
    EditText inputBName, inputEmail, inputAddress, inputPhoneNumber;
    Button btnRegister;

    FirebaseDatabase rootNode;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        inputBName = findViewById(R.id.inputBName);
        inputEmail = findViewById(R.id.inputEmail);
        inputAddress = findViewById(R.id.inputAddress);
        inputPhoneNumber = findViewById(R.id.inputPhoneNumber);
        btnRegister = findViewById(R.id.btnRegister);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rootNode = FirebaseDatabase.getInstance("https://final-project-mobile-app-98d46-default-rtdb.firebaseio.com/");
                reference = rootNode.getReference("business");
                

                //Get user input values
                String business_name = inputBName.getText().toString();
                String email = inputEmail.getText().toString().replace(".",",");
                String address = inputAddress.getText().toString();
                String phone_number = inputPhoneNumber.getText().toString();

                UserHelperClass helperClass = new UserHelperClass(business_name, email, address, phone_number);
                reference.child(email).setValue(helperClass);
                Toast.makeText(Register.this, "Succesfully Registered", Toast.LENGTH_SHORT).show();
//                if(validate(password, passwordVal)) {
//
//                } else{
//                    Toast.makeText(Register.this,"Password Not matching",Toast.LENGTH_SHORT).show();
//                }
            }

//            private boolean validate(String password, String passwordVal) {
//                boolean temp=true;
//
//                if(!password.equals(passwordVal)){
//                    temp=false;
//                }
//                return temp;
//            }
        });
    }
}
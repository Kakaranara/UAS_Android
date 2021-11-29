package umn.ac.id.uasmobileapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.LinkedList;

public class UserActivity extends AppCompatActivity {
    ImageButton btnHome;
    TextView namaBisnis;
//    Constraints navbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        namaBisnis = findViewById(R.id.nama_bisnis);

        Bundle extras = getIntent().getExtras();
        String bName = extras.getString("businessName");
        namaBisnis.setText(bName);
        Toast.makeText(UserActivity.this, "Business name: " + bName, Toast.LENGTH_SHORT).show();

        if(savedInstanceState == null){
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.user_container_fragment, new UserBarangFragment())
                    .commit();
        }

        btnHome = findViewById(R.id.btnHome);
//        navbar = findViewById(R.id.navbar);

        btnHome.setOnClickListener(view ->{
            Fragment UserBarangFragment = new UserBarangFragment();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

            transaction.replace(R.id.user_container_fragment, UserBarangFragment,null);
            transaction.commit();
            btnHome.setSelected(true);
        });
    }
}
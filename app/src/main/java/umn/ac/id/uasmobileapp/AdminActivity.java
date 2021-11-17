package umn.ac.id.uasmobileapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.constraintlayout.widget.Constraints;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;

public class AdminActivity extends AppCompatActivity {
    ImageButton btnBarangAdmin,btnHome,btnPesananAdmin;
//    Constraints navbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        if(savedInstanceState == null){
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.adminContainerFragment, new AdminHomeFragment()).commit();
        }

        btnHome = findViewById(R.id.btnHome);
        btnPesananAdmin = findViewById(R.id.btnPesanan);
        btnBarangAdmin = findViewById(R.id.btnBarang);
        btnBarangAdmin = findViewById(R.id.btnBarang);
//        navbar = findViewById(R.id.navbar);


        btnHome.setOnClickListener(view ->{
            Fragment AdminHomeFragment = new AdminHomeFragment();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

            transaction.replace(R.id.adminContainerFragment, AdminHomeFragment,null);
            transaction.commit();
        });

        btnPesananAdmin.setOnClickListener(view -> {
            Fragment AdminPesananFragment = new AdminPesananFragment();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

            transaction.replace(R.id.adminContainerFragment, AdminPesananFragment, null);
            transaction.commit();
        });

        btnBarangAdmin.setOnClickListener(view->{
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.adminContainerFragment, new AdminBarangFragment(),null);
            transaction.commit();
        });



    }
}
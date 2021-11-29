package umn.ac.id.uasmobileapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Debug;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.LinkedList;

public class UserActivity extends AppCompatActivity {
    ImageButton btnHome;
    TextView tvNamaBisnis;
    String namaBisnis;
    Session session;
//    Constraints navbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        tvNamaBisnis = findViewById(R.id.nama_bisnis);
        session = new Session(getApplicationContext());
        String key = session.getKey();

        DatabaseReference reference = FirebaseDatabase.getInstance("https://final-project-mobile-app-98d46-default-rtdb.firebaseio.com/").getReference("business");
        Query query = reference.orderByChild("Employee/" + key).equalTo(true);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for(DataSnapshot businessSnapshot : dataSnapshot.getChildren()){
                        namaBisnis = businessSnapshot.child("business_name").getValue(String.class);
                    }
                    tvNamaBisnis.setText(namaBisnis);
                }
                else{
                    Log.d("Datasnapshot", "onDataChange: NULL");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

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
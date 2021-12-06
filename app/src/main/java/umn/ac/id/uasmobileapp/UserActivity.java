package umn.ac.id.uasmobileapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class UserActivity extends AppCompatActivity {
    ImageButton btnHome;
    TextView tvNamaBisnis, tvBusinessId;
    String namaBisnis, businessId;

    Session session;
    UserBarangFragment userBarangFragment;
    UserCartFragment userCartFragment;
//    Constraints navbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        session = new Session(getApplicationContext());

        String key = session.getKey();

        DatabaseReference reference = FirebaseDatabase.getInstance("https://final-project-mobile-app-98d46-default-rtdb.firebaseio.com/").getReference("business");
        Query query = reference.orderByChild("Employee/" + key).equalTo(true);
        setContentView(R.layout.activity_user);
        tvNamaBisnis = findViewById(R.id.nama_bisnis);
        tvBusinessId = findViewById(R.id.business_id);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for(DataSnapshot businessSnapshot : dataSnapshot.getChildren()){
                        namaBisnis = businessSnapshot.child("business_name").getValue(String.class);
//                        Toast.makeText(UserActivity.this,"Business name: " + namaBisnis,Toast.LENGTH_SHORT).show();

                        businessId = businessSnapshot.getKey();
//                        Toast.makeText(UserActivity.this,"Business ID KEY: " + businessId,Toast.LENGTH_SHORT).show();


                    }
                    tvNamaBisnis.setText(namaBisnis);

                    Bundle bundle = new Bundle();
                    bundle.putString("businessId", businessId);
                    bundle.putString("businessName", namaBisnis);

                    userBarangFragment = new UserBarangFragment();
                    userBarangFragment.setArguments(bundle);

                    userCartFragment = new UserCartFragment();
                    userCartFragment.setArguments(bundle);
                    if(savedInstanceState == null){
                        getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.user_container_fragment, userBarangFragment)
                                .replace(R.id.user_cart_fragment, userCartFragment)
                                .commit();
                    }

                }
                else{
                    Log.d("Datasnapshot", "onDataChange: NULL");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void logout(View view){
        session.logout();
        Intent intent = new Intent(UserActivity.this,Login.class);
        startActivity(intent);
        finish();
    }
}
package umn.ac.id.uasmobileapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AdminHomeFragment extends Fragment {

    TextView tvTotalRevenue, tvTotalDish, tvTotalCustomer;
    Session session;
    DatabaseReference orderRef, cartRef;
    ArrayList<String> total_customer = new ArrayList<>();

    public AdminHomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        session = new Session(getContext());


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {



        return inflater.inflate(R.layout.fragment_admin_home, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();

        tvTotalCustomer = getView().findViewById(R.id.AdminHomeTotalCustomer);
        tvTotalRevenue = getView().findViewById(R.id.homeAdminTotalRevenue);
        tvTotalDish = getView().findViewById(R.id.homeAdminTotalDish);

        cartRef = FirebaseDatabase.getInstance("https://final-project-mobile-app-98d46-default-rtdb.firebaseio.com/").getReference().child("carts");
        orderRef = FirebaseDatabase.getInstance("https://final-project-mobile-app-98d46-default-rtdb.firebaseio.com/").getReference().child("orders");

        orderRef.orderByChild("business_id").equalTo(session.getBusinessKey()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot orderSnapshot : snapshot.getChildren()){
                    total_customer.add(orderSnapshot.getKey());
                }
                tvTotalCustomer.setText("" + total_customer.toArray().length);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //TODO : bikin total revenue + dish
        cartRef.orderByChild("business_id").equalTo(session.getBusinessKey()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}
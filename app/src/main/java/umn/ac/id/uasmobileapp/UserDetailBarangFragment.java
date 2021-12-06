package umn.ac.id.uasmobileapp;

import static java.util.Objects.isNull;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Currency;

public class UserDetailBarangFragment extends Fragment {
    private View view;
    DatabaseReference mbase;
    String product_key;
    TextView mName, mDescription, mPrice, mStock;
    Button backBtn;
    private Query query;

    public UserDetailBarangFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_user_detail_barang, container, false);
        mName = view.findViewById(R.id.product_name);
        mDescription = view.findViewById(R.id.description_body);
        mPrice = view.findViewById(R.id.price);
        mStock = view.findViewById(R.id.stock);
        backBtn = view.findViewById(R.id.back_button);

        backBtn.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(), UserActivity.class);
            startActivity(intent);
        });

        if (getArguments() != null) {
            product_key = getArguments().getString("product_key", "");
        }

        mbase = FirebaseDatabase.getInstance("https://final-project-mobile-app-98d46-default-rtdb.firebaseio.com/").getReference("products");
        query = mbase.orderByKey().equalTo(product_key);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot data: snapshot.getChildren()) {
                    mName.setText(data.child("product_name").getValue(String.class));
                    mDescription.setText(data.child("description").getValue(String.class));
                    mStock.setText(data.child("stock").getValue().toString());

                    // Convert long to currency format
                    NumberFormat formatCurrency = new DecimalFormat("#,###");
                    mPrice.setText("Rp " + formatCurrency.format(data.child("price").getValue()));
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) { throw error.toException(); }
        });


        // Inflate the layout for this fragment
        return view;
    }
}
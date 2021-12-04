package umn.ac.id.uasmobileapp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UserDetailBarangFragment extends Fragment {
    private View view;
    DatabaseReference mbase;
    String product_key;
    TextView mName, mDescription, mPrice, mStock;

    public UserDetailBarangFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_user_detail_barang, container, false)
        mName = view.findViewById(R.id.product_name);
        mDescription = view.findViewById(R.id.description_body);
        mPrice = view.findViewById(R.id.price);
        mStock = view.findViewById(R.id.stock);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            product_key = bundle.getString("productKey", "");
        }

        mbase = FirebaseDatabase.getInstance("https://final-project-mobile-app-98d46-default-rtdb.firebaseio.com/").getReference().child("products");

        // Inflate the layout for this fragment
        return view;
    }
}
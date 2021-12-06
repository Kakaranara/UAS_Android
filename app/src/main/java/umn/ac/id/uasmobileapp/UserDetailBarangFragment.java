package umn.ac.id.uasmobileapp;

import static java.util.Objects.isNull;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Currency;

public class UserDetailBarangFragment extends Fragment {
    private View view;
    DatabaseReference mbase;
    String product_key, order_id;
    TextView mName, mDescription, mPrice, mStock;
    Button backBtn;
    Boolean bought;
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
//            NavHostFragment.findNavController(FragmentManager.findFragment(view)).
//                    navigate(R.id.action_userDetailBarangFragment_to_userBarangFragment);
            Fragment fragment = new UserBarangFragment();
            FragmentTransaction fragmentTransaction = getFragmentManager()
                    .beginTransaction()
                    .replace(R.id.user_container_fragment, fragment)
                    .detach(fragment)
                    .attach(fragment)
                    .addToBackStack(null);
            fragmentTransaction.commit();
        });

        Button buyBtn = view.findViewById(R.id.cartBtn);


        if (getArguments() != null) {
            product_key = getArguments().getString("product_key", "");
            order_id = getArguments().getString("order_id", "");
            bought = getArguments().getBoolean("bought", false);
            if(bought) buyBtn.setEnabled(false);
            else buyBtn.setEnabled(true);
        }


        buyBtn.setOnClickListener(view -> {
            try {
                int stockNow = Integer.parseInt(mStock.getText().toString());
                if(stockNow > 1) {
                    if(order_id != null) {
                        Cart newCart = new Cart(
                                "-",
                                Integer.parseInt(mPrice.getText().toString()
                                        .replace("Rp ", "")
                                        .replace(",", "")),
                                1);
                        System.out.println("PRODUCT ID: " + product_key + " | StOCK NOW: " + stockNow);
                        DatabaseReference mCarts = FirebaseDatabase.getInstance("https://final-project-mobile-app-98d46-default-rtdb.firebaseio.com/").getReference().child("carts");
                        mCarts.child(order_id).child(product_key).setValue(newCart);
                        buyBtn.setEnabled(false);
                        buyBtn.setVisibility(View.INVISIBLE);

                        Toast.makeText(view.getContext(), "Your order for " + mName.getText().toString() + " has been successfully added to the cart.", Toast.LENGTH_SHORT).show();
                    } else {
                        buyBtn.setEnabled(true);
                        buyBtn.setVisibility(View.VISIBLE);
                    }
                }
            } catch(NumberFormatException nfe) {
                System.out.println("Could not parse " + nfe);
            }
        });

        mbase = FirebaseDatabase.getInstance("https://final-project-mobile-app-98d46-default-rtdb.firebaseio.com/").getReference("products");
        query = mbase.orderByKey().equalTo(product_key);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot data: snapshot.getChildren()) {
                    mName.setText(data.child("product_name").getValue(String.class));
                    mDescription.setText(data.child("description").getValue(String.class));
                    mStock.setText(data.child("stock").getValue().toString());
                    Picasso.get()
                            .load(snapshot.child("picture_path").getValue(String.class))
                            .placeholder(R.mipmap.ic_launcher)
                            .error(R.drawable.basket_white)
                            .into((ImageView) view.findViewById(R.id.product_image));

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
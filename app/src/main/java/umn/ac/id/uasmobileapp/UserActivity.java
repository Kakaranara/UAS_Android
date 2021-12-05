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
    String currentOrderId, newOrderId;

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
                        Toast.makeText(UserActivity.this,"Business name: " + namaBisnis,Toast.LENGTH_SHORT).show();

                        businessId = businessSnapshot.getKey();
                        Toast.makeText(UserActivity.this,"Business ID KEY: " + businessId,Toast.LENGTH_SHORT).show();

                        // Search for order where account_id equals to session user key
                        System.out.println("Search for order where account_id equals to " + key);
                        DatabaseReference mCarts = FirebaseDatabase.getInstance("https://final-project-mobile-app-98d46-default-rtdb.firebaseio.com/").getReference().child("carts");
                        DatabaseReference mOrders = FirebaseDatabase.getInstance("https://final-project-mobile-app-98d46-default-rtdb.firebaseio.com/").getReference().child("orders");

                        mOrders.orderByKey().get().addOnCompleteListener(task -> {
                            if (!task.isSuccessful()) {
                                Log.e("firebase", "Error getting data", task.getException());
                            } else {
                                Log.d("firebase", String.valueOf(task.getResult().getValue()));

                                DataSnapshot snapshot = task.getResult();

                                // Get snapshot size
                                int dataSize = (int) snapshot.getChildrenCount();
                                System.out.println("DATA SIZE | Snapshot size: " + dataSize);
                                boolean hasCartStatus = false;
                                for(DataSnapshot data: snapshot.getChildren()) {
                                    currentOrderId = data.getKey();
                                    String currentOrderStatus = data.child("status").getValue(String.class);

                                    System.out.println("AAAAA currentOrderId: " + currentOrderId);

                                    int i = 0;
                                    newOrderId = currentOrderId;


                                    // If there's order data with status cart then clear order and cart data
                                    if(currentOrderStatus.equals("cart")) {
                                        hasCartStatus = true;
                                        System.out.println("BBBBB currentOrderId has \"cart\" as status ");

                                        // Get cart data where account key equals to current order id
                                        Query queryCart = mCarts.orderByKey().equalTo(currentOrderId);
                                        queryCart.addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                System.out.println("CCCCC Current Cart Snapshot: " + snapshot);

                                                Query queryCartOrderByKey = snapshot.getRef().child(currentOrderId).orderByValue();
                                                queryCartOrderByKey.addValueEventListener(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                        if(snapshot.exists()) {
                                                            System.out.println("DDDDD Current OrderID " + currentOrderId + " Snapshot: " + snapshot);

                                                            // Remove data inside cart where order status is "cart"
                                                            String cartOrderId = snapshot.getKey();
                                                            System.out.println("DELETING data at carts -> cartOrderId: " + cartOrderId);
                                                            snapshot.getRef().removeValue();
                                                        }
                                                    }

                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError error) { throw error.toException(); }
                                                });

                                            }

                                            public void onCancelled(@NonNull DatabaseError error) { throw error.toException(); }
                                        });
                                    }

                                }

                                // If order with "cart" status is not found, then create new order id
                                if (!hasCartStatus) {
                                    // Create new Order iD
                                    int newOrderIndex = Integer.parseInt(currentOrderId.substring(2,6));
                                    System.out.println("NEW ORDER INDEX IS: " + newOrderIndex);
                                    newOrderId = "OR" + String.format("%05d", newOrderIndex+1);
                                    System.out.println("PRINTLN | New Order ID is " + newOrderId);

                                    // Generate datetime
                                    Date c = Calendar.getInstance().getTime();
                                    SimpleDateFormat df = new SimpleDateFormat("YYYYMMddHHmmssZ", Locale.getDefault());
                                    String formattedDate = df.format(c);

                                    // Write to firebase
                                    Order newOrder = new Order(key, formattedDate, "cart");
                                    mOrders.child(newOrderId).setValue(newOrder);
                                }
                            }
                        });
                    }
                    tvNamaBisnis.setText(namaBisnis);



                    Bundle bundle = new Bundle();
                    bundle.putString("businessId", businessId);
                    bundle.putString("businessName", namaBisnis);
                    bundle.putString("currentOrderId", newOrderId);

                    System.out.println("NEW ORDER ID: " + newOrderId);

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





        btnHome = findViewById(R.id.btnHome);

        //for first selected item
        btnHome.setSelected(true);
        btnHome.setOnClickListener(view ->{
            Fragment UserBarangFragment = new UserBarangFragment();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

            transaction.replace(R.id.user_container_fragment, UserBarangFragment,null);
            transaction.commit();
            btnHome.setSelected(true);
        });
    }

    public void logout(View view){
        session.logout();
        Intent intent = new Intent(UserActivity.this,Login.class);
        startActivity(intent);
        finish();
    }
}
package umn.ac.id.uasmobileapp;

import android.graphics.Rect;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class UserCartFragment extends Fragment {
    private View view;
    private RecyclerView recyclerView;
    private String sessionBusinessId, sessionBusinessName, currentOrderId, newProductId, currentSessionAccountId;

    private FirebaseRecyclerAdapter<Cart, UserCartFragment.UserCartViewholder> adapter;
    private DatabaseReference mCarts, mOrders, mProducts;
    private FirebaseStorage storage;
    private Session session;
    Query queryOrder, queryCart;

    public UserCartFragment() {}

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        session = new Session(getActivity().getApplicationContext());
        currentSessionAccountId = session.getKey();

        // Create a instance of the database and get its reference
        mCarts = FirebaseDatabase.getInstance("https://final-project-mobile-app-98d46-default-rtdb.firebaseio.com/").getReference().child("carts");
        mOrders = FirebaseDatabase.getInstance("https://final-project-mobile-app-98d46-default-rtdb.firebaseio.com/").getReference().child("orders");
        mProducts = FirebaseDatabase.getInstance("https://final-project-mobile-app-98d46-default-rtdb.firebaseio.com/").getReference().child("products");
        storage = FirebaseStorage.getInstance("gs://final-project-mobile-app-98d46.appspot.com");


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_user_cart, container, false);

        // Set current order ID to view
        TextView tvOrderId = view.findViewById(R.id.order_id);
        tvOrderId.setText(currentOrderId);

        TextView tvBusinessName = view.findViewById(R.id.business_name);
        tvBusinessName.setText(sessionBusinessName);

        recyclerView = (RecyclerView) view.findViewById(R.id.user_cart_list_recycler_view);

        // To display the Recycler view with two columns
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity().getApplicationContext(), 1);
        recyclerView.setLayoutManager(mLayoutManager);

        // Connecting Adapter class with the Recycler view
        return view;
    }

    // Function to tell the app to start getting
    // data from database on starting of the activity
    @Override
    public void onStart()
    {
        super.onStart();
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            sessionBusinessId = bundle.getString("businessId", "");
            sessionBusinessName = bundle.getString("businessName", "");
            newProductId = bundle.getString("product_key", "");
            currentOrderId = bundle.getString("currentOrderId", "");
        }

        System.out.println("START | new Order ID: " + currentOrderId);

        // It is a class provide by the FirebaseUI to make a query in the database to fetch appropriate data
        FirebaseRecyclerOptions<Cart> options
                = new FirebaseRecyclerOptions.Builder<Cart>()
                .setQuery(mCarts.child(currentOrderId), Cart.class)
                .build();

        adapter = new FirebaseRecyclerAdapter<Cart, UserCartFragment.UserCartViewholder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull UserCartFragment.UserCartViewholder holder, int position, @NonNull Cart model) {
//                final String product_key = getRef(position).getKey();
//                holder.product_key.setText(product_key);

                // Get product name value
                Query getCarts = getRef(position).orderByKey();
                getCarts.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        System.out.println("CART LIST | AAAAA | Data Snapshot: " + dataSnapshot);
                        for(DataSnapshot data: dataSnapshot.getChildren()) {
                            System.out.println("CART LIST | BBBBB | Product Now: " + data);
                            String productKeyNow = data.getKey();
                            System.out.println("CART LIST | CCCCC | Product Key Now: " + productKeyNow);

                            Query queryProduct = mProducts.orderByKey().equalTo(productKeyNow);
                            queryProduct.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    System.out.println("CART LIST | DDDDD | Product Now: " + snapshot);

                                    if(snapshot.exists()) {
                                        assert productKeyNow != null;
                                        DataSnapshot productData = snapshot.child(productKeyNow);
                                        holder.product_name.setText(productData.child("product_name").getValue(String.class));

                                        // Convert long to currency format
                                        NumberFormat formatCurrency = new DecimalFormat("#,###");
                                        holder.product_price.setText("Rp " + formatCurrency.format(productData.child("price").getValue()));

                                        String quantity = productData.child("quantity").getValue(String.class);
                                        if(quantity != null && !quantity.equals("")){
                                            holder.cart_qty.setText(productData.child("quantity").getValue(String.class));
                                            holder.product_total_price.setText(
                                                    Integer.parseInt(snapshot.child(productKeyNow).child("price").getValue().toString()) *
                                                            Integer.parseInt(holder.cart_qty.getText().toString()));

                                        } else {
                                            holder.cart_qty.setText("1");
                                            holder.product_total_price.setText(holder.product_price.getText());
                                        }


                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) { throw error.toException(); }
                            });
                        }

//                        getCartsByProductId.addValueEventListener(new ValueEventListener() {
//
//                            @Override
//                            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                                System.out.println("CART LIST | BBBBB | Data Snapshot: " + snapshot);
//
//                            }
//
//                            @Override
//                            public void onCancelled(@NonNull DatabaseError databaseError) { throw databaseError.toException(); }
//                        });
                        
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) { throw databaseError.toException(); }
                });
            }

            @NonNull
            @Override
            public UserCartFragment.UserCartViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_list_user, parent, false);
                return new UserCartFragment.UserCartViewholder(view);
            }
        };

        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    public static class UserCartViewholder extends RecyclerView.ViewHolder{
        TextView product_key, product_name, product_price, product_total_price;
        EditText cart_qty, cart_notes;
        Button remove_btn;
        ImageView product_image;
        public UserCartViewholder(@NonNull View itemView) {
            super(itemView);
            product_key = itemView.findViewById(R.id.key_item);
            product_name = itemView.findViewById(R.id.nama_item);
            product_price = itemView.findViewById(R.id.harga_item);
            product_total_price = itemView.findViewById(R.id.price_body);
            cart_qty = itemView.findViewById(R.id.cart_qty);
            product_image = itemView.findViewById(R.id.product_order_image);
            cart_notes = itemView.findViewById(R.id.footer_notes);
            remove_btn = itemView.findViewById(R.id.footer_remove_button);

            remove_btn.setOnClickListener(v -> {
//                mClickListener.onItemClick(v, getAdapterPosition());
                Toast.makeText(v.getContext(),"Navigating to " + product_name + " detail.",Toast.LENGTH_SHORT).show();
//                    Intent detailIntent = new Intent(Intent.)
            });
        }

//        private UserCartListViewholder.ClickListener mClickListener;
//
//        //Interface to send callbacks...
//        public interface ClickListener{
//            public void onItemClick(View view, int position);
//            public void onItemLongClick(View view, int position);
//        }
//
//        public void setOnClickListener(UserCartListViewholder.ClickListener clickListener){
//            mClickListener = clickListener;
//        }
    }





    // Function to tell the app to stop getting
    // data from database on stopping of the activity
    @Override
    public void onStop() {
        super.onStop();
    }
    // Used to put space between items in recycler view
    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }
}
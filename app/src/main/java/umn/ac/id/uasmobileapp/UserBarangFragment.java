package umn.ac.id.uasmobileapp;

import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentResultListener;
import androidx.lifecycle.LifecycleOwner;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Objects;

public class UserBarangFragment extends Fragment {
    private View view;
    private RecyclerView recyclerView;
    DatabaseReference mProducts, mCarts;
    FirebaseStorage storage;
    private String sessionBusinessId;
    static FragmentManager fm;
    String order_id;
    FirebaseRecyclerAdapter<Product, UserBarangFragment.UserProductViewholder> adapter;
    RecyclerView.LayoutManager mLayoutManager;
    private LifecycleOwner lifecycle_owner;

    public UserBarangFragment() {}

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            sessionBusinessId = bundle.getString("businessId", "");
        }
        // Create a instance of the database and get its reference
        mProducts = FirebaseDatabase.getInstance("https://final-project-mobile-app-98d46-default-rtdb.firebaseio.com/").getReference().child("products");
        mCarts = FirebaseDatabase.getInstance("https://final-project-mobile-app-98d46-default-rtdb.firebaseio.com/").getReference().child("carts");

        storage = FirebaseStorage.getInstance("gs://final-project-mobile-app-98d46.appspot.com");


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_user_barang, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.products_recycler_view);
        lifecycle_owner = getViewLifecycleOwner();
        fm = getParentFragmentManager();

        fm.setFragmentResultListener("requestKey", lifecycle_owner,
                (requestKey, bundle1) -> {
                    // We use a String here, but any type that can be put in a Bundle is supported
                    order_id = bundle1.getString("order_id");
//                    System.out.println("INPUTTING TO CART | Order ID is " + order_id);
                });

        fm.setFragmentResultListener("cart_removed", lifecycle_owner,
                (requestKey, bundle1) -> {
                    // We use a String here, but any type that can be put in a Bundle is supported
                    order_id = bundle1.getString("order_id");
//                    System.out.println("INPUTTING TO CART | Order ID is " + order_id);
                });

        // To display the Recycler view with two columns
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity().getApplicationContext(), 2);
        recyclerView.setLayoutManager(mLayoutManager);

        // Add spacing between columns
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, 30, false));

        // It is a class provide by the FirebaseUI to make a query in the database to fetch appropriate data
        FirebaseRecyclerOptions<Product> options
                = new FirebaseRecyclerOptions.Builder<Product>()
                .setQuery(mProducts.orderByChild("business_id").equalTo(sessionBusinessId), Product.class)
                .build();

        adapter = new FirebaseRecyclerAdapter<Product, UserBarangFragment.UserProductViewholder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull UserBarangFragment.UserProductViewholder holder, int position, @NonNull Product model) {
//                Log.d("FIREBASE User Barang", String.valueOf(getRef(position).getKey()));

                                final String product_key = getRef(position).getKey();
                                holder.product_key.setText(product_key);

                                // Get product name value
                                holder.product_name.setText(model.getProduct_name());

                                // Get price value
                                NumberFormat formatCurrency = new DecimalFormat("#,###");
                                holder.product_price.setText("Rp " + formatCurrency.format(model.getPrice()));

                                // Get quantity/stock value
                                holder.product_stock.setText("Sisa: " + model.getStock());

                                // Get image path value
                                Picasso.get()
                                        .load(model.getPicture_path())
                                        .placeholder(R.mipmap.ic_launcher)
                                        .error(R.drawable.basket_white)
                                        .into(holder.product_image);

            }

            @NonNull
            @Override
            public UserBarangFragment.UserProductViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_list_user, parent, false);
                UserProductViewholder vh = new UserProductViewholder(view);
                //                    @Override
//                    public void onItemLongClick(View view, int position) {
//                        Toast.makeText(view.getContext(), "Click dtected on " + position, Toast.LENGTH_SHORT).show();
//                    }
//                vh.setOnClickListener((view1, position) -> {});
                return vh;
            }
        };

        System.out.println("BERHASIL BARANG");
                    recyclerView.setAdapter(adapter);
                    adapter.startListening();

        // Connecting Adapter class with the Recycler view
        return view;
    }

    // Function to tell the app to start getting
    // data from database on starting of the activity
    @Override
    public void onStart()
    {
        super.onStart();
    }

    public class UserProductViewholder extends RecyclerView.ViewHolder{
        TextView product_key, product_name, product_price, product_stock;
        LinearLayout product_list_linear_layout;
        Button info_btn, cart_btn;
        ImageView product_image;
        public UserProductViewholder(@NonNull View itemView) {
            super(itemView);
            product_list_linear_layout = itemView.findViewById(R.id.product_list_linear_layout);
            product_key = itemView.findViewById(R.id.product_key);
            product_name = itemView.findViewById(R.id.product_name);
            product_price = itemView.findViewById(R.id.product_price);
            product_stock = itemView.findViewById(R.id.product_stock);
            product_image = itemView.findViewById(R.id.product_image);
            info_btn = itemView.findViewById(R.id.infoBtn);
            cart_btn = itemView.findViewById(R.id.cartBtn);

            info_btn.setOnClickListener(v -> {
                Bundle bundle = new Bundle();
                bundle.putString("product_key", product_key.getText().toString());
                bundle.putString("order_id", order_id);
                if(cart_btn.isEnabled()) bundle.putBoolean("bought", false);
                else bundle.putBoolean("bought", true);
                NavHostFragment.findNavController(FragmentManager.findFragment(v)).
                        navigate(R.id.action_userBarangFragment_to_userDetailBarangFragment2, bundle);
            });

            product_key.addTextChangedListener(new TextWatcher() {

                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    cart_btn.setVisibility(View.VISIBLE);
                    cart_btn.setEnabled(true);
                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (order_id != null) {
                        String product_key_now = product_key.getText().toString();
                        mCarts.child(order_id).child(product_key_now).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.exists()) {
                                    cart_btn.setVisibility(View.INVISIBLE);
                                    cart_btn.setEnabled(false);
                                } else {
                                    cart_btn.setVisibility(View.VISIBLE);
                                    cart_btn.setEnabled(true);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) { throw error.toException(); }
                        });

                    }
                }
            });

            cart_btn.setOnClickListener(v -> {
                try {
                    int stockNow = Integer.parseInt(product_stock.getText().toString().substring(6));
                    if(stockNow > 1) {
                        if(order_id != null) {
                            Cart newCart = new Cart(
                                    "-",
                                    Integer.parseInt(product_price.getText().toString()
                                            .replace("Rp ", "")
                                            .replace(",", "")),
                                    1);
                            String product_key_now = product_key.getText().toString();
                            System.out.println("PRODUCT ID: " + product_key_now + " | StOCK NOW: " + stockNow);
                            Toast.makeText(view.getContext(), "Your order for " + product_name.getText().toString() + " has been added to the cart.", Toast.LENGTH_SHORT).show();

                            mCarts.child(order_id).child(product_key_now).setValue(newCart);
                            cart_btn.setEnabled(false);
                            cart_btn.setVisibility(View.INVISIBLE);
                        } else {
                            cart_btn.setEnabled(true);
                            cart_btn.setVisibility(View.VISIBLE);
                        }
                    }
                } catch(NumberFormatException nfe) {
                    System.out.println("Could not parse " + nfe);
                }
            });
        }

//        private UserProductViewholder.ClickListener mClickListener;
//
//        //Interface to send callbacks...
//        public interface ClickListener{
//            public void onItemClick(View view, int position);
//            public void onItemLongClick(View view, int position);
//        }

//        public void setOnClickListener(UserProductViewholder.ClickListener clickListener){
//            mClickListener = clickListener;
//        }
    }




    // Function to tell the app to stop getting
    // data from database on stopping of the activity
    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
        adapter.stopListening();
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
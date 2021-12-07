package umn.ac.id.uasmobileapp;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
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
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class UserCartFragment extends Fragment {
    private View view;
    private RecyclerView recyclerView;
    private String sessionBusinessId, sessionBusinessName, currentOrderId, newProductId, currentSessionAccountId;

    private FirebaseRecyclerAdapter<Cart, UserCartFragment.UserCartViewholder> adapter;
    private static DatabaseReference mCarts;
    private DatabaseReference mOrders;
    private DatabaseReference mProducts;
    private FirebaseStorage storage;
    private Session session;
    Query queryOrder, queryCart;
    private String newOrderId;
    static String orderIdForViewHolder;
    static TextView tvOrderId;
    boolean orderIdSet;

    public UserCartFragment() {}

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            sessionBusinessId = bundle.getString("businessId", "");
            sessionBusinessName = bundle.getString("businessName", "");
        }

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
        tvOrderId = view.findViewById(R.id.order_id);

        // Add listener to proceed button
        Button proceedOrder = view.findViewById(R.id.proceed_button);
        proceedOrder.setOnClickListener(v -> {
            // Get data of carts by newOrderId
            mCarts.child(newOrderId).addListenerForSingleValueEvent(new ValueEventListener() {

                @Override
                public void onDataChange(@NonNull DataSnapshot cartSnapshot) {
                    System.out.println("SNAPSHOT GETKEY  cartSnapshot|: " + cartSnapshot);
                    // If carts data where order_id == newOrderId exists and has more than zero data
                    if(cartSnapshot.exists() && cartSnapshot.getChildrenCount() > 0) {
                        for(DataSnapshot dP : cartSnapshot.getChildren()) {
                            int quantity_di_cart = Integer.parseInt(cartSnapshot.child(dP.getKey()).child("quantity").getValue().toString());

                            System.out.println("SNAPSHOT GETKEY  DP DPDP|: " + dP);
                            mProducts.child(String.valueOf(dP.getKey())).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    System.out.println("SNAPSHOT GETKEY QUANTITY PRODUCTS|: " + snapshot);
                                int stock_di_products = Integer.parseInt(snapshot.child("stock").getValue().toString());
                                if(stock_di_products >= quantity_di_cart) {
                                    Map<String, Object> childUpdates = new HashMap<>();
//                                // Update isCart to false
                                childUpdates.put("/" + newOrderId + "/isCart", false);
                                childUpdates.put("/" + newOrderId + "/status", "pending");
                                childUpdates.put("/" + newOrderId + "/business_cart", sessionBusinessId + "_false");
                                mOrders.updateChildren(childUpdates);

                                    Map<String, Object> productUpdates = new HashMap<>();
//                                // Update isCart to false
                                    productUpdates.put("/" + dP.getKey() + "/stock", Integer.parseInt(snapshot.child("stock").getValue().toString()) - quantity_di_cart);
                                    mProducts.updateChildren(productUpdates);

                                Toast.makeText(getActivity().getApplication(), "Your order has been successfully made with order ID " + newOrderId, Toast.LENGTH_SHORT).show();

                                startActivity(new Intent(getActivity(), UserActivity.class));
                                } else {
                                    Toast.makeText(getActivity().getApplication(), "Your order exceed the stock available.", Toast.LENGTH_SHORT).show();
                                }

//
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
throw error.toException();
                                }
                            });
                        }


                    } else {
                        Toast.makeText(getActivity().getApplication(), "Your cart is empty.", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) { throw error.toException(); }
            });
        });

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

        // Search for order where account_id equals to session user key
//        System.out.println("Search for order where account_id equals to " + currentSessionAccountId);

        mOrders.orderByKey().get().addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                Log.e("FIREBASE User Cart", "Error getting data", task.getException());
            }
            else {
//                Log.d("FIREBASE User Cart", String.valueOf(task.getResult().getValue()));

                boolean hasCartStatus = false;

                DataSnapshot snapshot = task.getResult();

                for(DataSnapshot data: snapshot.getChildren()) {
                    currentOrderId = data.getKey();
                    String currentOrderStatus = data.child("status").getValue(String.class);
//                    System.out.println("AAAAA currentOrderId: " + currentOrderId);
                    // If there's order data with status cart then clear order and cart data
                    if(currentOrderStatus.equals("cart")) {
                        newOrderId = currentOrderId;

                        hasCartStatus = true;
//                        System.out.println("BBBBB currentOrderId has \"cart\" as status ");

                        // Get cart data where account key equals to current order id
                        Query queryCart = mCarts.orderByKey().equalTo(currentOrderId);
                        queryCart.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                                System.out.println("CCCCC Current Cart Snapshot: " + snapshot);

                                Query queryCartOrderByKey = snapshot.getRef().child(currentOrderId).orderByValue();
                                queryCartOrderByKey.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        if(snapshot.exists()) {
//                                            System.out.println("DDDDD Current OrderID " + currentOrderId + " Snapshot: " + snapshot);

                                            // Remove data inside cart where order status is "cart"
//                                            String cartOrderId = snapshot.getKey();
//                                            System.out.println("DELETING data at carts -> cartOrderId: " + cartOrderId);
                                            snapshot.getRef().removeValue();

                                            // Update order date time
                                            Map<String, Object> childUpdates = new HashMap<>();

                                            // Generate datetime
                                            Date c = Calendar.getInstance().getTime();
                                            SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmssZ", Locale.getDefault());
                                            String formattedDate = df.format(c);

                                            childUpdates.put("/" + orderIdForViewHolder + "/order_datetime", formattedDate);
                                            mOrders.updateChildren(childUpdates);
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) { throw error.toException(); }
                                });

                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) { throw error.toException(); }
                        });
                        break;
                    }

                }

                // If order with "cart" status is not found, then create new order id
                if (!hasCartStatus) {
                    // Create new Order iD
                    int newOrderIndex = Integer.parseInt(currentOrderId.substring(2,7));
//                    System.out.println("NEW ORDER INDEX IS: " + newOrderIndex);
                    newOrderId = "OR" + String.format("%05d", newOrderIndex+1);
//                    System.out.println("PRINTLN | New Order ID is " + newOrderId);

                    // Generate datetime
                    Date c = Calendar.getInstance().getTime();
                    SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmssZ", Locale.getDefault());
                    String formattedDate = df.format(c);

                    // Write to firebase
                    Order newOrder = new Order(currentSessionAccountId, formattedDate, "cart", sessionBusinessId);
                    mOrders.child(newOrderId).setValue(newOrder);
                }

//                System.out.println("START | new Order ID: " + newOrderId);
                if(newOrderId != null && newOrderId.startsWith("OR")) {
                    Bundle result = new Bundle();
                    result.putString("order_id", newOrderId);
                    getParentFragmentManager().setFragmentResult("requestKey", result);
                    orderIdForViewHolder = newOrderId;
                    tvOrderId.setText(newOrderId);
                }

                // It is a class provide by the FirebaseUI to make a query in the database to fetch appropriate data
                FirebaseRecyclerOptions<Cart> options
                        = new FirebaseRecyclerOptions.Builder<Cart>()
                        .setQuery(mCarts.child(newOrderId), Cart.class)
                        .build();

                adapter = new FirebaseRecyclerAdapter<Cart, UserCartViewholder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull UserCartViewholder holder, int position, @NonNull Cart model) {
//                final String product_key = getRef(position).getKey();
//                holder.product_key.setText(product_key);


//                                System.out.println("CART LIST | AAAAA | Data Snapshot: " + getRef(position));
                                DatabaseReference data = getRef(position);
//                                System.out.println("CART LIST | BBBBB | Product Now: " + data);
                                String productKeyNow = data.getKey();
//                                System.out.println("CART LIST | CCCCC | Product Key Now: " + productKeyNow);

                                Query queryProduct = mProducts.orderByKey().equalTo(productKeyNow);
                                queryProduct.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                                        System.out.println("CART LIST | DDDDD | Product Now: " + snapshot);
                                        if(snapshot.exists()) {
                                            assert productKeyNow != null;
                                            DataSnapshot productData = snapshot.child(productKeyNow);                                                    holder.product_name.setText(productData.child("product_name").getValue(String.class));

                                            holder.product_key.setText(productData.getKey());

                                                    // Convert long to currency format
                                                    NumberFormat formatCurrency = new DecimalFormat("#,###");
                                                    holder.product_price.setText("Rp " + formatCurrency.format(productData.child("price").getValue()));

                                                        holder.product_total_price.setText(holder.product_price.getText());
                                            Picasso.get()
                                                    .load(productData.child("picture_path").getValue().toString())
                                                    .placeholder(R.mipmap.ic_launcher)
                                                    .error(R.drawable.basket_white)
                                                    .into(holder.product_image);

                                                }
                                            }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) { throw error.toException(); }
                                });


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

                    @NonNull
                    @Override
                    public UserCartViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_list_user, parent, false);
                        return new UserCartViewholder(view);
                    }
                };
                System.out.println("BERHASIL CART");

                recyclerView.setAdapter(adapter);
                adapter.startListening();
            }

        });



    }

    public static class UserCartViewholder extends RecyclerView.ViewHolder {
        LinearLayout cart_list_user_layout;
        TextView product_key, product_name, product_price, product_total_price, cart_qty;
        EditText cart_notes;
        Button remove_btn;
        ImageView product_image;
        public UserCartViewholder(@NonNull View itemView) {
            super(itemView);
            cart_list_user_layout = itemView.findViewById(R.id.cart_list_user_layout);
            product_key = itemView.findViewById(R.id.key_item);
            product_name = itemView.findViewById(R.id.nama_item);
            product_price = itemView.findViewById(R.id.harga_item);
            product_total_price = itemView.findViewById(R.id.price_body);
            cart_qty = itemView.findViewById(R.id.cart_qty);
            product_image = itemView.findViewById(R.id.product_order_image);
            cart_notes = itemView.findViewById(R.id.footer_notes);
            remove_btn = itemView.findViewById(R.id.footer_remove_button);

            remove_btn.setOnClickListener(v -> {
                Toast.makeText(v.getContext(), "Removed " + product_name.getText().toString() + " from cart.", Toast.LENGTH_SHORT).show();
                mCarts.child(orderIdForViewHolder).child(product_key.getText().toString()).removeValue();
            });
//
//            cart_qty.setTransformationMethod(new NumericKeyBoardTransformationMethod());
//            cart_qty.setOnKeyListener((v, keyCode, event) -> {
//                // If the event is a key-down event on the "enter" button
//                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
//                    cart_qty_helper.setText("0");
//                    Log.d("PRODUCT KEY CART QTY", "TEST 2");
//                    String product_key_now = product_key.getText().toString();
//                    Log.d("PRODUCT KEY CART QTY", product_key_now);
//
//                    Map<String, Object> childUpdates = new HashMap<>();
//                    // Update isCart to false
//                    childUpdates.put("/" + orderIdForViewHolder + "/" + product_key_now + "/quantity", Integer.parseInt(cart_qty.getText().toString()));
//                    mCarts.updateChildren(childUpdates);
//                    return true;
//                }
//                return false;
//            });

//            cart_qty.addTextChangedListener(new TextWatcher() {
//
//                public void afterTextChanged(Editable s) {
//                    Log.d("PRODUCT KEY CART QTY", "TEST");
//                    Log.d("PRODUCT KEY CART QTY", "Editable s: " + s);
//
//                    if(cart_qty_helper.getText().toString().equals("1")) {
//                        cart_qty_helper.setText("0");
//                        Log.d("PRODUCT KEY CART QTY", "TEST 2");
//                        String product_key_now = product_key.getText().toString();
//                        Log.d("PRODUCT KEY CART QTY", product_key_now);
//
//                        Map<String, Object> childUpdates = new HashMap<>();
//                        // Update isCart to false
//                        childUpdates.put("/" + orderIdForViewHolder + "/" + product_key_now + "/quantity", Integer.parseInt(cart_qty.getText().toString()));
//                        mCarts.updateChildren(childUpdates);
//                    }
//                }
//
//                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
//
//                public void onTextChanged(CharSequence s, int start, int before, int count) {}
//            });

//           product_key.addTextChangedListener(new TextWatcher() {
//
//                 @Override
//                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                    if(orderIdForViewHolder.equals(tvOrderId.getText())) {
//                        String product_key_now = product_key.getText().toString();
//                        mCarts.child(orderIdForViewHolder).child(product_key_now).addListenerForSingleValueEvent(new ValueEventListener() {
//                            @Override
//                            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                                if (snapshot.exists()) {
//                                    cart_list_user_layout.setVisibility(View.GONE);
//                                    cart_list_user_layout.setEnabled(false);
//                                } else {
//                                    cart_list_user_layout.setVisibility(View.VISIBLE);
//                                    cart_list_user_layout.setEnabled(true);
//                                }
//                            }
//
//                            @Override
//                            public void onCancelled(@NonNull DatabaseError error) { throw error.toException(); }
//                        });
//                    }
//                }
//
//                @Override
//                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//                }
//
//                @Override
//                public void afterTextChanged(Editable s) {}
//            });
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

    // Used to make cart_qty input only numbers
    private static class NumericKeyBoardTransformationMethod extends PasswordTransformationMethod {
        @Override
        public CharSequence getTransformation(CharSequence source, View view) {
            return source;
        }
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
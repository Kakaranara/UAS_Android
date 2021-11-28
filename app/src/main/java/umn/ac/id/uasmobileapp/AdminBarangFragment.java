package umn.ac.id.uasmobileapp;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class AdminBarangFragment extends Fragment implements View.OnClickListener{
    private View view;
    private RecyclerView recyclerView;
    AdminProductAdapter adapter; // Create Object of the Adapter class
    DatabaseReference mbase, productRef;
    private String currentUser = "BU00001";
    public Button button;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AdminBarangFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    /*public static AdminBarangFragment newInstance(String param1, String param2) {
        AdminBarangFragment fragment = new AdminBarangFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }*/

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_admin_barang, container, false);
        button = view.findViewById(R.id.manage_k);
        button.setOnClickListener(this);

        // Create a instance of the database and get
        // its reference
        mbase = FirebaseDatabase.getInstance("https://final-project-mobile-app-98d46-default-rtdb.firebaseio.com/").getReference().child("products");

        recyclerView = (RecyclerView) view.findViewById(R.id.rvProduct);

        // To display the Recycler view linearly
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getContext(), 3);
        recyclerView.setLayoutManager(mLayoutManager);

        // Connecting Adapter class with the Recycler view*/
        return view;
    }

    // Function to tell the app to start getting
    // data from database on starting of the activity
    @Override
    public void onStart()
    {
        super.onStart();
        // It is a class provide by the FirebaseUI to make a query in the database to fetch appropriate data
        FirebaseRecyclerOptions<Product> options
                = new FirebaseRecyclerOptions.Builder<Product>()
                .setQuery(mbase, Product.class)
                .build();

        FirebaseRecyclerAdapter<Product, AdminProductViewholder> adapter
                = new FirebaseRecyclerAdapter<Product, AdminProductViewholder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull AdminProductViewholder holder, int position, @NonNull Product model) {
                        final String product_name = getRef(position).getKey();

                        holder.product_name.setText(product_name);

                        //DatabaseReference getTypeRef = getRef(position).;

//                        mbase.orderByChild("business_id").equalTo(currentUser).addValueEventListener(new ValueEventListener() {
//                            @Override
//                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                                if(dataSnapshot.exists()){
//                                    String price = dataSnapshot.child("price").getValue().toString();
//                                    holder.product_price.setText(price);
//                                }
//                            }
//
//                            @Override
//                            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                            }
//                        });

                        //holder.product_quantity.setText(model.getStock());
                    }

                    @NonNull
                    @Override
                    public AdminProductViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_list_admin, parent, false);
                        return new AdminProductViewholder(view);
                    }
                };

        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    @Override
    public void onClick(View view) {
        showCustomDialog();
    }

    void showCustomDialog(){
        final Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);

        dialog.setContentView(R.layout.edit_list_product);
        final EditText nameEt = dialog.findViewById(R.id.name_fill);
        final EditText priceEt = dialog.findViewById(R.id.price_fill);
        final EditText discountEt = dialog.findViewById(R.id.discount_fill);
        final EditText stockEt = dialog.findViewById(R.id.stock_fill);
        final EditText descEt = dialog.findViewById(R.id.desc_fill);
        Button submit = dialog.findViewById(R.id.submit);
        Button cancel = dialog.findViewById(R.id.cancel);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = nameEt.getText().toString();
                String price = priceEt.getText().toString();
                String discount = discountEt.getText().toString();
                String stock = stockEt.getText().toString();
                String desc = descEt.getText().toString();

                //Insert Data to Database

            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    public static class AdminProductViewholder extends RecyclerView.ViewHolder {
        TextView product_name, product_price, product_quantity;
        public AdminProductViewholder(@NonNull View itemView)
        {
            super(itemView);
            product_name = itemView.findViewById(R.id.product_name);
            product_price = itemView.findViewById(R.id.product_price);
            product_quantity = itemView.findViewById(R.id.prduct_quantity);
        }
    }

    // Function to tell the app to stop getting
    // data from database on stopping of the activity
    @Override
    public void onStop()
    {
        super.onStop();
        adapter.stopListening();
    }
}
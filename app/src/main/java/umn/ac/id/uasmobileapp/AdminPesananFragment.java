package umn.ac.id.uasmobileapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AdminPesananFragment extends Fragment {
    private View view;
    private RecyclerView recyclerView;
    DatabaseReference mbase, productRef;



    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AdminPesananFragment() {
        // Required empty public constructor
    }


//    // TODO: Rename and change types and number of parameters
//    public static AdminPesananFragment newInstance(String param1, String param2) {
//        AdminPesananFragment fragment = new AdminPesananFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
//        return fragment;
//    }
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_admin_barang, container, false);

        mbase = FirebaseDatabase.getInstance("https://final-project-mobile-app-98d46-default-rtdb.firebaseio.com/").getReference().child("products");

        recyclerView = (RecyclerView) view.findViewById(R.id.rvOrder);

        // To display the Recycler view linearly
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Order> options
                = new FirebaseRecyclerOptions.Builder<Order>()
                .setQuery(mbase, Order.class)
                .build();

        FirebaseRecyclerAdapter<Order, AdminPesananFragment.AdminPesananViewholder> adapter
                = new FirebaseRecyclerAdapter<Order, AdminPesananFragment.AdminPesananViewholder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull AdminPesananViewholder holder, int position, @NonNull Order model) {
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
                    holder.editBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            //showEditDialog();
                        }
                    });
                }

                @NonNull
                @Override
                public AdminPesananFragment.AdminPesananViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_list_admin, parent, false);
                    return new AdminPesananFragment.AdminPesananViewholder(view);
                }
            };

        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    public static class AdminPesananViewholder extends RecyclerView.ViewHolder {
        TextView product_name, product_price, product_quantity;
        Button editBtn;
        public AdminPesananViewholder(@NonNull View itemView)
        {
            super(itemView);
            product_name = itemView.findViewById(R.id.product_name);
            product_price = itemView.findViewById(R.id.product_price);
            product_quantity = itemView.findViewById(R.id.prduct_quantity);
            editBtn = itemView.findViewById(R.id.editBtn);
        }
    }
}
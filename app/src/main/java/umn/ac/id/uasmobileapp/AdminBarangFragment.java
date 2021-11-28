package umn.ac.id.uasmobileapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AdminBarangFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AdminBarangFragment extends Fragment {
    private View view;
    private RecyclerView recyclerView;
    AdminProductAdapter adapter; // Create Object of the Adapter class
    DatabaseReference mbase;
    private String currentUser = "Wkwk";

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

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AdminBarangFragment.
     */
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

        // Create a instance of the database and get
        // its reference
        mbase = FirebaseDatabase.getInstance("https://final-project-mobile-app-98d46-default-rtdb.firebaseio.com/").getReference().child("products");

        recyclerView = (RecyclerView) view.findViewById(R.id.rvProduct);

        // To display the Recycler view linearly
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

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
                .setQuery(mbase.child(currentUser), Product.class)
                .build();

        FirebaseRecyclerAdapter<Product, AdminProductViewholder> adapter
                = new FirebaseRecyclerAdapter<Product, AdminProductViewholder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull AdminProductViewholder holder, int position, @NonNull Product model) {
                        final String product_name = getRef(position).getKey();

                        holder.product_name.setText(product_name);

                        DatabaseReference getTypeRef = getRef(position).child("price").getRef();

                        getTypeRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if(dataSnapshot.exists()){
                                    String price = dataSnapshot.getValue().toString();
                                    holder.product_price.setText(price);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

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
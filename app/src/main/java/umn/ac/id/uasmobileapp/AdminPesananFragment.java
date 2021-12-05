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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AdminPesananFragment extends Fragment {
    private View view;
    private RecyclerView recyclerView;
    DatabaseReference mbase, productRef;
    Session session;

    public AdminPesananFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        session = new Session(getContext());
        System.out.println("KEY pesanan : " + session.getKey());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_admin_pesanan, container, false);

        mbase = FirebaseDatabase.getInstance("https://final-project-mobile-app-98d46-default-rtdb.firebaseio.com/").getReference().child("orders");

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

            }

                @NonNull
                @Override
                public AdminPesananFragment.AdminPesananViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_pesanan, parent, false);
                    return new AdminPesananFragment.AdminPesananViewholder(view);
                }
            };

        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    void editDialog(){
        final Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.edit_order_admin);

        dialog.show();
    }

    public class AdminPesananViewholder extends RecyclerView.ViewHolder {
        TextView product_name, product_price, product_quantity;
        ImageView btnEdit;

        public AdminPesananViewholder(@NonNull View itemView)
        {
            super(itemView);
            btnEdit = itemView.findViewById(R.id.btnAdmin_pesanan_card);
            btnEdit.setOnClickListener(v->{
                editDialog();
            });
        }
    }
}
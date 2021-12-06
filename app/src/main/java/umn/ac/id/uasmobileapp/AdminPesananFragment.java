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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class AdminPesananFragment extends Fragment {
    private View view;
    private RecyclerView recyclerView;
    Query mbaseQuery;
    DatabaseReference mbase, productRef, reference,cartRef;
    Session session;

    public AdminPesananFragment() { }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        session = new Session(getContext());
        System.out.println("KEY pesanan : " + session.getKey());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_admin_pesanan, container, false);
        mbase = FirebaseDatabase.getInstance("https://final-project-mobile-app-98d46-default-rtdb.firebaseio.com/").getReference().child("orders");
        cartRef = FirebaseDatabase.getInstance("https://final-project-mobile-app-98d46-default-rtdb.firebaseio.com/").getReference().child("carts");
        recyclerView = (RecyclerView) view.findViewById(R.id.rvOrder);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        mbaseQuery = mbase.orderByChild("isCart").equalTo(false);


        return view;
    }

    @Override
    public void onStart() {
        super.onStart();


        FirebaseRecyclerOptions<Order> options
                = new FirebaseRecyclerOptions.Builder<Order>()
                .setQuery(mbaseQuery, Order.class)
                .build();

        FirebaseRecyclerAdapter<Order, AdminPesananFragment.AdminPesananViewholder> adapter
                = new FirebaseRecyclerAdapter<Order, AdminPesananFragment.AdminPesananViewholder>(options) {

            @Override
            public void onDataChanged() {
                super.onDataChanged();

            }

            @Override
            public void onError(@NonNull DatabaseError error) {
                super.onError(error);
            }

            @NonNull
            @Override
            public AdminPesananFragment.AdminPesananViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_pesanan, parent, false);
                return new AdminPesananFragment.AdminPesananViewholder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull AdminPesananViewholder holder, int position, @NonNull Order model) {
                holder.statusPesanan.setText(model.getStatus());
                cartRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            System.out.println("ALL SNAPSHOT" + snapshot);
                            System.out.println("TESTING" + snapshot.getChildren());
                            System.out.println("TESTING 2 : " + snapshot.getValue());
                            System.out.println("TESTING 3 : " + snapshot.getValue().getClass());
                            for(DataSnapshot orderSnapshot : snapshot.getChildren()){
                                System.out.println(orderSnapshot.getKey());
                                holder.idPesanan.setText(orderSnapshot.getKey());
                                for(DataSnapshot productSnapshot : orderSnapshot.getChildren()){

                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }

            @Override
            public int getItemCount() {
                return super.getItemCount();
            }
        };

        recyclerView.setAdapter(adapter);
        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getContext(), recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        pembayaranDialog();
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {

                    }
                })
        );
        adapter.startListening();
    }

    public class AdminPesananViewholder extends RecyclerView.ViewHolder {
        ImageView btnEdit, btnDelete;
        TextView idPesanan,statusPesanan;



        public AdminPesananViewholder(@NonNull View itemView)
        {
            super(itemView);
            btnEdit = itemView.findViewById(R.id.btnEditAdmin_pesanan_card);
            btnDelete = itemView.findViewById(R.id.btnDeleteAdmin_pesanan_card);
            idPesanan = itemView.findViewById(R.id.card_id_order_admin);
            statusPesanan = itemView.findViewById(R.id.card_order_status);
            btnEdit.setOnClickListener(v->{
                editDialog();
            });
        }
    }

    void editDialog(){
        final Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.edit_order_admin);

        dialog.show();
    }

    void pembayaranDialog(){
        final Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.fragment_admin_pembayaran);

        dialog.show();
    }

}
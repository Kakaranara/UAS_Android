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

import java.util.ArrayList;

public class AdminPesananFragment extends Fragment {
    private View view;
    private RecyclerView recyclerView;
    Query mbaseQuery;
    DatabaseReference mbase, productRef, reference,cartRef;
    Session session;
    ArrayList<Pesanan> listPesanan;

    private String orderID;
    private String[] menu;
    private int[] harga;
    private int[] jumlahPemesanan;
    private int pembayaran;
    private String status;




    public AdminPesananFragment() { }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        session = new Session(getContext());
        System.out.println("KEY pesanan : " + session.getKey());

        listPesanan = new ArrayList<>();

        cartRef = FirebaseDatabase.getInstance("https://final-project-mobile-app-98d46-default-rtdb.firebaseio.com/").getReference().child("carts");
        productRef = FirebaseDatabase.getInstance("https://final-project-mobile-app-98d46-default-rtdb.firebaseio.com/").getReference().child("products");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_admin_pesanan, container, false);
        mbase = FirebaseDatabase.getInstance("https://final-project-mobile-app-98d46-default-rtdb.firebaseio.com/").getReference().child("orders");
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
                holder.idPesanan.setText(getRef(position).getKey());
                holder.statusPesanan.setText(model.getStatus());
                getRef(position).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            System.out.println("SNAPSHOT UTAMA : " + snapshot);
                            cartRef.orderByKey().equalTo(snapshot.getKey()).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    for(DataSnapshot orderSnapshot : snapshot.getChildren()){
                                        int harga_total = 0;
                                        for(DataSnapshot productSnapshot: orderSnapshot.getChildren()){
                                            harga_total += productSnapshot.child("price").getValue(Integer.class) * productSnapshot.child("quantity").getValue(Integer.class);
                                            productRef.orderByKey().equalTo(productSnapshot.getKey()).addValueEventListener(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                    System.out.println("SNAPSHOT PRODUCT : " + snapshot);
                                                    for(DataSnapshot productSnapshot : snapshot.getChildren()){
                                                        System.out.println("DATA SNAPSHOT : " + productSnapshot);
                                                        holder.namaMenu.setText(productSnapshot.child("product_name").getValue(String.class));
                                                    }
                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError error) {

                                                }
                                            });
                                        }
                                        holder.hargaTotal.setText("Rp. " + harga_total );
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                        }
                        else{

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
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getContext(), recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        pembayaranDialog();
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {

                    }
                }));
        adapter.startListening();
    }

    public class AdminPesananViewholder extends RecyclerView.ViewHolder {
        ImageView btnEdit, btnDelete;
        TextView idPesanan,statusPesanan,namaMenu,hargaTotal;

        public AdminPesananViewholder(@NonNull View itemView) {
            super(itemView);
            btnEdit = itemView.findViewById(R.id.btnEditAdmin_pesanan_card);
            btnDelete = itemView.findViewById(R.id.btnDeleteAdmin_pesanan_card);
            idPesanan = itemView.findViewById(R.id.card_id_order_admin);
            statusPesanan = itemView.findViewById(R.id.card_order_status);
            namaMenu = itemView.findViewById(R.id.card_order_nama_makanan);
            hargaTotal = itemView.findViewById(R.id.card_order_harga);
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
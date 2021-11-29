package umn.ac.id.uasmobileapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Registry;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.InputStream;

public class UserCartListFragment extends Fragment {
    private View view;
    private RecyclerView recyclerView;
    DatabaseReference mbase;
    FirebaseStorage storage;
    private String currentUser = "Wkwk";

    public UserCartListFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_user_cart_list, container, false);

        // Create a instance of the database and get its reference
        mbase = FirebaseDatabase.getInstance("https://final-project-mobile-app-98d46-default-rtdb.firebaseio.com/").getReference().child("products");
        storage = FirebaseStorage.getInstance("gs://final-project-mobile-app-98d46.appspot.com");

        recyclerView = (RecyclerView) view.findViewById(R.id.products_recycler_view);

        // To display the Recycler view with two columns
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity().getApplicationContext(), 2);
        recyclerView.setLayoutManager(mLayoutManager);

        // Add spacing between columns
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, 20, false));

        // Connecting Adapter class with the Recycler view
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


        FirebaseRecyclerAdapter<Product, UserCartListFragment.UserCartListViewholder> adapter
                = new FirebaseRecyclerAdapter<Product, UserCartListFragment.UserCartListViewholder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull UserCartListFragment.UserCartListViewholder holder, int position, @NonNull Product model) {
                final String product_key = getRef(position).getKey();
                holder.product_key.setText(product_key);

//                // Get product name value
//                DatabaseReference getName = getRef(position).child("product_name").getRef();
//                getName.addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                        if(dataSnapshot.exists()){
//                            String name = dataSnapshot.getValue().toString();
//                            holder.product_name.setText(name);
//                        }
//                    }
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError databaseError) {}
//                });
//
//                // Get image path value
//                DatabaseReference getImagePath = getRef(position).child("picture_path").getRef();
//                getImagePath.addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                        if(dataSnapshot.exists()){
//                            String imagePath = dataSnapshot.getValue().toString();
//
//                            // Create a reference with an initial file path and name
//                            // Points to the root reference
//                            StorageReference storageRef = storage
//                                    .getReference()
//                                    .child("images/products/" + imagePath);
//
//                            Toast.makeText(getActivity().getApplication(), "Image path: images/products/" + imagePath, Toast.LENGTH_SHORT).show();
//
//                            final long ONE_MEGABYTE = 1024 * 1024;
//                            storageRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(bytes -> {
//                                Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
//                                Glide.with(getActivity().getApplicationContext())
//                                        .load(storageRef)
//                                        .into(holder.product_image);
//                            }).addOnFailureListener(exception -> Toast.makeText(getActivity().getApplicationContext(),
//                                    "Product picture is not found.",
//                                    Toast.LENGTH_LONG).show());;
//
//                        } else Toast.makeText(getActivity().getApplication(), "Image path not found.", Toast.LENGTH_SHORT).show();
//
//                    }
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError databaseError) {
//                        Toast.makeText(getActivity().getApplication(), "Image path retrieve cancelled.", Toast.LENGTH_SHORT).show();
//                    }
//                });
            }

            @NonNull
            @Override
            public UserCartListFragment.UserCartListViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_list_user, parent, false);
                return new UserCartListFragment.UserCartListViewholder(view);
            }
        };

        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    public static class UserCartListViewholder extends RecyclerView.ViewHolder{
        TextView product_key, product_name, product_price, product_total_price;
        EditText cart_qty, cart_notes;
        Button remove_btn;
        ImageView product_image;
        public UserCartListViewholder(@NonNull View itemView) {
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
                mClickListener.onItemClick(v, getAdapterPosition());
                Toast.makeText(v.getContext(),"Navigating to " + product_name + " detail.",Toast.LENGTH_SHORT).show();
//                    Intent detailIntent = new Intent(Intent.)
            });
        }

        private UserCartListViewholder.ClickListener mClickListener;

        //Interface to send callbacks...
        public interface ClickListener{
            public void onItemClick(View view, int position);
            public void onItemLongClick(View view, int position);
        }

        public void setOnClickListener(UserCartListViewholder.ClickListener clickListener){
            mClickListener = clickListener;
        }
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
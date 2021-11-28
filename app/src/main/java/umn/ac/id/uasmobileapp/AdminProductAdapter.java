package umn.ac.id.uasmobileapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class AdminProductAdapter extends FirebaseRecyclerAdapter<Product, AdminProductAdapter.AdminProductViewholder> {
    public AdminProductAdapter(@NonNull FirebaseRecyclerOptions<Product> options)
    {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull AdminProductViewholder holder, int position, @NonNull Product model)
    {
        holder.product_name.setText(model.getProdName());

        holder.product_price.setText(model.getPrice());

        holder.product_quantity.setText(model.getStock());
    }

    // Function to tell the class about the Card view (here
    // "person.xml")in
    // which the data will be shown
    @NonNull
    @Override
    public AdminProductViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_list_admin, parent, false);
        return new AdminProductAdapter.AdminProductViewholder(view);
    }

    // Sub Class to create references of the views in Crad
    // view (here "person.xml")
    class AdminProductViewholder extends RecyclerView.ViewHolder {
        TextView product_name, product_price, product_quantity;
        public AdminProductViewholder(@NonNull View itemView)
        {
            super(itemView);
            product_name = itemView.findViewById(R.id.product_name);
            product_price = itemView.findViewById(R.id.product_price);
            product_quantity = itemView.findViewById(R.id.prduct_quantity);
        }
    }
}

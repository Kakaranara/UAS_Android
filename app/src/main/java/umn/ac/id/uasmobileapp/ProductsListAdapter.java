package umn.ac.id.uasmobileapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.LinkedList;

public class ProductsListAdapter extends
        RecyclerView.Adapter<ProductsListAdapter.ProductViewHolder> {

    private final LinkedList<String> mDaftarKata;
    private LayoutInflater mInflater;

    ProductsListAdapter(Context context, LinkedList<String> productsList) {
        mInflater = LayoutInflater.from(context);
        mDaftarKata = productsList;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder
            (@NonNull ViewGroup parent, int viewType) {
        View mItemView = mInflater.inflate(R.layout.products_list,
                parent, false);
        return new ProductViewHolder(mItemView, this);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        String mCurrent = mDaftarKata.get(position);
        holder.kataItemView.setText(mCurrent);
    }

    @Override
    public int getItemCount() {
        return mDaftarKata.size();
    }

    class ProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final TextView kataItemView;
        final ProductsListAdapter mAdapter;

        public ProductViewHolder(@NonNull View itemView,
                                 ProductsListAdapter adapter) {
            super(itemView);
            kataItemView = itemView.findViewById(R.id.product_name);
            this.mAdapter = adapter;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int mPosition = getLayoutPosition();
            String element = mDaftarKata.get(mPosition);
//            mDaftarKata.set(mPosition, element + " pressed.");
            mAdapter.notifyDataSetChanged();
            Toast.makeText(v.getContext(),
                    element + " pressed.",
                    Toast.LENGTH_LONG).show();
        }
    }
}

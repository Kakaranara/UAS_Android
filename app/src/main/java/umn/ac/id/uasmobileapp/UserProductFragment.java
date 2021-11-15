package umn.ac.id.uasmobileapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.LinkedList;

public class UserProductFragment extends Fragment {
    private final LinkedList<String> mProductsList = new LinkedList<>();
    private RecyclerView mRecyclerView;
    private ProductsListAdapter mAdapter;

//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater,
//                             @Nullable ViewGroup container,
//                             @Nullable Bundle savedInstanceStat) {
//        final View view = inflater.inflate(R.layout.fragment_user_products_list, container, false);
//
//        mRecyclerView = (RecyclerView) view.findViewById(R.id.products_recycler_view);
//        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
//        mAdapter = new ProductsListAdapter(getContext(), mProductsList);
//        mRecyclerView.setAdapter(mAdapter);

//        return view;
//    }
}
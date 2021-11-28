package umn.ac.id.uasmobileapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.LinkedList;

public class UserMainMenu extends AppCompatActivity {
    private final LinkedList<String> mProductsList = new LinkedList<>();
    private RecyclerView mRecyclerView;
    private ProductsListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_main_menu);

        for (int i = 1; i < 11; i++) {
            mProductsList.add("Menu " + i);
        }

        mRecyclerView = (RecyclerView) findViewById(R.id.products_recycler_view);
        //mAdapter = new ProductsListAdapter(this, mProductsList);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

    }
}
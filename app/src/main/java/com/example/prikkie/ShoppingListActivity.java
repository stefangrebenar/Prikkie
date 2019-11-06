package com.example.prikkie;

import android.app.Activity;
import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prikkie.Api.IngredientApi.AHAPI;
import com.example.prikkie.Api.IngredientApi.Product;

import java.util.ArrayList;
import java.util.List;

public class ShoppingListActivity extends Activity {

    private RecyclerView mRecyclerView;
    private MyAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    final ArrayList<ExampleItem> exampleList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_list);

        AHAPI api = new AHAPI(){
            @Override
            public void onLoad(List<Product> products) {
                super.onLoad(products);

                for (Product prod:products) {
                    exampleList.add(new ExampleItem(prod.imgURL, prod.name, Double.toString(prod.price)));
                }
                buildRecyclerView();
            }
        };

        api.getProducts(this,"Frikandel");

    }

    public void changeItem(int position, String text){
        exampleList.get(position).changeText1(text);
        mAdapter.notifyItemChanged(position);
    }

    final void buildRecyclerView(){
        mRecyclerView = findViewById(R.id.shoppingList);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new MyAdapter(exampleList);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new MyAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                changeItem(position, "Clicked");
            }
        });
    }
    // ...
}


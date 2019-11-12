package com.example.prikkie;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prikkie.Api.IngredientApi.AHAPI;
import com.example.prikkie.Api.IngredientApi.Product;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ShoppingListFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private ShoppingListAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<ExampleItem> mExampleList = new ArrayList<>();
    private FloatingActionButton addButton;
    private RecyclerView recyclerView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shopping_list, viewGroup, false);

        addButton = view.findViewById(R.id.addProductButton);
        recyclerView = view.findViewById(R.id.shoppingList);

        createExampleList();
        setupButtons(view.getContext());

        return view;
    }

    //Gives functionality to the add_buttons
    public void setupButtons(final Context context){
       addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 AHAPI api = new AHAPI(){
                    @Override
                    public void onLoad(List<Product> products) {
                        super.onLoad(products);

                        Product prod = products.get(new Random().nextInt(products.size()));

                        insertItem(new Random().nextInt(mExampleList.size()), new ExampleItem(prod.imgURL, prod.name, "€" + prod.price, false));
                    }
                };
                api.getProducts(context,"Tomaat");
            }
        });
    }

    //Fills the shoppinglist with random products for testing
    public void createExampleList(){
        AHAPI api = new AHAPI(){
            @Override
            public void onLoad(List<Product> products) {
                super.onLoad(products);

                for (Product prod:products) {
                    mExampleList.add(new ExampleItem(prod.imgURL, prod.name, "€" + prod.price, false));
                }
                buildRecyclerView();
            }
        };

        api.getProducts((MainActivity)getContext(),"Brood");
    }

    //Builds the recylerView and sets up the adapter
    public void buildRecyclerView(){
        mRecyclerView = recyclerView;
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager((MainActivity)getContext());
        mAdapter = new ShoppingListAdapter(mExampleList);

        mRecyclerView.setLayoutManager(mLayoutManager);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(mRecyclerView);
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new ShoppingListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                flipCheckbox(position);
            }
        });
    }

    public void insertItem(int position, ExampleItem item){
        mExampleList.add(position, item);
        mAdapter.notifyDataSetChanged();
    }

    public void flipCheckbox(int position){
        mExampleList.get(position).flipCheckBox();
        mAdapter.notifyItemChanged(position);
    }

    //Code for swiping to delete.
    ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            final ExampleItem lastItem =  mExampleList.get(viewHolder.getAdapterPosition());
            final int pos = viewHolder.getAdapterPosition();
            mExampleList.remove(pos);
            Snackbar mySnackbar = Snackbar.make(getActivity().findViewById(R.id.constraintLayout), lastItem.getTopText() + " verwijderd", 5000);
            mySnackbar.setAction("Ongedaan maken", new MyUndoListener(pos, lastItem));
            mySnackbar.show();

            mAdapter.notifyItemRemoved(pos);
        }
    };

    public class MyUndoListener implements View.OnClickListener {
        private ExampleItem mItem;
        private int mPos;

        public MyUndoListener(int position, ExampleItem item){
            mPos = position;
            mItem = item;
        }

        @Override
        public void onClick(View v) {
            mExampleList.add(mPos, mItem);
            mAdapter.notifyItemInserted(mPos);
        }
    }


}


package com.example.prikkie;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prikkie.Api.IngredientApi.AHAPI;
import com.example.prikkie.Api.IngredientApi.Product;

import java.util.ArrayList;
import java.util.List;

import static androidx.core.content.ContextCompat.getSystemService;

public class SearchFragment extends Fragment {

    private RecyclerView resultRecycler;
    private EditText editText;
    private ArrayList<ExampleItem> resultItems = new ArrayList<>();
    private ProductListAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private View mView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, viewGroup, false);
        mView = view;

        resultRecycler = (RecyclerView) view.findViewById(R.id.resultRecycler);
        editText = (EditText) view.findViewById(R.id.editText);
        final ImageView imageView = (ImageView) view.findViewById(R.id.imageView);

        buildRecyclerView();

        Button search = (Button) view.findViewById(R.id.button);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getProductsButton(v);
            }
        });
        return view;
    }

    public void getProductsButton(View view) {
        final Product prod1;

        final  AHAPI ahGetter = new AHAPI(72, editText.getText().toString(), new AHAPI.onResultLoadedListener() {
            @Override
            public void onResultLoaded(List<Product> products) {
                resultItems.clear();

                String text = "";
                for (Product product:products) {
                    resultItems.add(new ExampleItem(product.imgURL, product.name, "€" + product.price));
                }
                mAdapter.notifyDataSetChanged();
            }
        });
        ahGetter.orderBy(AHAPI.orderBy.ASC);
        ahGetter.setTaxonomy("tomaten");
        ahGetter.getProducts(getContext());

        hideKeyboardFrom(getContext(), mView);
    }

    public static void hideKeyboardFrom(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    //Builds the recylerView and sets up the adapter
    public void buildRecyclerView(){
        resultRecycler.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager((MainActivity)getContext());
        mAdapter = new ProductListAdapter(resultItems);

        resultRecycler.setLayoutManager(mLayoutManager);
        resultRecycler.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new ProductListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
            }
        });
    }
}
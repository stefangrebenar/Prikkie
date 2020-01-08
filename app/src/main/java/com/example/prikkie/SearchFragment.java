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
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prikkie.Api.IngredientApi.AHAPI;
import com.example.prikkie.Api.IngredientApi.AHAPIAsync;
import com.example.prikkie.Api.IngredientApi.Product;
import com.example.prikkie.Helpers.ImageManager;
import com.example.prikkie.RoomShoppingList.ShoppingListItem;
import com.example.prikkie.RoomShoppingList.ShoppingListViewModel;
import com.google.android.material.snackbar.Snackbar;

import java.lang.annotation.Target;
import java.util.ArrayList;
import java.util.List;

import static androidx.core.content.ContextCompat.getSystemService;
import static com.example.prikkie.App.hideKeyboardFrom;

public class SearchFragment extends Fragment {
    private static SearchFragment m_fragment;
    public static SearchFragment getFragment(){
        if(m_fragment == null){
            m_fragment = new SearchFragment();
        }
        return m_fragment;
    }
    private SearchFragment(){}

    private RecyclerView resultRecycler;
    private EditText editText;
    private ArrayList<ExampleItem> resultItems = new ArrayList<>();
    private ProductListAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private View mView;
    private ShoppingListViewModel shoppingListViewModel;

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

        shoppingListViewModel = ViewModelProviders.of(getActivity()).get(ShoppingListViewModel.class);
        return view;
    }

    public void getProductsButton(View view) {
        final Product prod1;

        final AHAPIAsync ahGetter = new AHAPIAsync(72);
        ahGetter.setQuery(editText.getText().toString());
        ahGetter.orderBy(AHAPI.orderBy.ASC);
        ahGetter.setListener(new AHAPIAsync.onResultLoadedListener() {
            @Override
            public void onResultLoaded(List<Product> products) {
                resultItems.clear();

                String text = "";
                for (Product product:products) {
                    resultItems.add(new ExampleItem(product.imgURL, product.name, Double.toString(product.price)));
                }
                mAdapter.notifyDataSetChanged();
            }
        });
        ahGetter.execute();

        hideKeyboardFrom(getContext(), mView);
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
                ExampleItem mItem = resultItems.get(position);

                ShoppingListItem item = new ShoppingListItem(mItem.getTopText(), Double.parseDouble(mItem.getBottomText()), mItem.getImageResource(), false);

                ImageManager.imageDownload(getActivity(), item.getImageUrl(), Integer.toString(item.getId()));

                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(getContext(), mItem.getTopText() + "toegevoegd aan de boodschappenlijst", duration);
                toast.show();

                shoppingListViewModel.insert(item);
            }
        });
    }
}
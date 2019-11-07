package com.example.prikkie;

import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.prikkie.Api.IngredientApi.AHAPI;
import com.example.prikkie.Api.IngredientApi.Product;

import java.util.List;

public class SearchFragment extends Fragment {

    private TextView productText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, viewGroup, false);

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
        productText = (TextView) view.findViewById(R.id.productText);
        final EditText editText = (EditText) view.findViewById(R.id.editText);
        final ImageView imageView = (ImageView) view.findViewById(R.id.imageView);
        productText.setMovementMethod(new ScrollingMovementMethod());
        final AHAPI ahGetter = new AHAPI(){
            @Override
            public void onLoad(List<Product> products) {
                super.onLoad(products);

                String text = "";
                for (Product product:products) {
                    text += product.name + "\n\n";
                }

                productText.setText(text);
            }
        };

        ahGetter.getProducts((MainActivity)getContext(), editText.getText().toString());
    }
}
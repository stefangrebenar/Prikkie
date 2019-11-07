package com.example.prikkie;

import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.prikkie.Api.IngredientApi.AHAPI;
import com.example.prikkie.Api.IngredientApi.Product;
import com.example.prikkie.R;

import java.util.List;


public class IngredientApiActivity extends AppCompatActivity {
    public static final String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingredients);
    }

    public void getProductsButton(View view) {
        final Product prod1;
        final TextView productText = (TextView) findViewById(R.id.productText);
        final EditText editText = (EditText) findViewById(R.id.editText);
        final ImageView imageView = (ImageView) findViewById(R.id.imageView);
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

        ahGetter.getProducts(this, editText.getText().toString());
    }

}

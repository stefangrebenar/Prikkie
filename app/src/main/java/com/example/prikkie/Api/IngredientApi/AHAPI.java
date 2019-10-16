package com.example.prikkie.Api.IngredientApi;


import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AHAPI {


    //gets the result of a query
    public List<Product> getProducts(Context context, String query) {
        final List<Product> products = new ArrayList<>();
        RequestQueue queue = Volley.newRequestQueue(context);

        String url = "https://www.ah.nl/zoeken/api/products/search?size=72&&query=" + query;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            JSONArray productsArray = response.getJSONArray("cards");

                            for (int i = 0; i < productsArray.length(); i++) {
                                JSONObject base = response.getJSONArray("cards").getJSONObject(i).getJSONArray("products").getJSONObject(0);

                                Product product = new Product();

                                product.name = base.getString("title");
                                product.description = base.getString("summary");
                                product.weight = base.getJSONObject("price").getString("unitSize");
//                                product.kgPrice = base.getJSONObject("price").getJSONObject("unitInfo").getDouble("price");
                                product.price = base.getJSONObject("price").getDouble("now");
                                product.imgURL = base.getJSONArray("images").getJSONObject(0).getString("url");

                                products.add(product);
                            }
                            onLoad(products);

                        } catch (JSONException e) {
                            e.printStackTrace();
                            onFail();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error

                    }
                });

        queue.add(jsonObjectRequest);

        return products;
    }

    public void onLoad(List<Product> products) {

    }

    public void onFail() {

    }
}




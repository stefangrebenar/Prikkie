package com.example.prikkie.Api.IngredientApi;


import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.Volley;
import com.example.prikkie.Api.VolleyCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class AHAPI {
    private String urlQuery = "https://www.ah.nl/zoeken/api/products/search";
    private onResultLoadedListener mListener;
    private List<Product> products;
//    private String searchQuery = "";

    public AHAPI(int resultSize, onResultLoadedListener listener){
        urlQuery += "?size=" + resultSize;
        mListener = listener;
    }

    public enum orderBy{
        ASC,
        DESC
    }

    public void orderBy(orderBy order){
        if(order == orderBy.ASC){
            urlQuery += "&sortBy=price";
        }else if(order == orderBy.DESC){
            urlQuery += "&sortBy=-price";
        }
    }

    public void setTaxonomy(String taxonomy){
//        urlQuery += "&&taxonomySlug=" + taxonomy;
    }

    public void setQuery(String query){
        urlQuery += "&query=" + query;
    }

   public interface onResultLoadedListener{
        void onResultLoaded(List<Product> products);
    }

    //gets the result of a query
    public List<Product> getProducts(Context context) {
        Log.d("TEST", "STARTING CALL: "+urlQuery);
        RequestFuture<JSONObject> future = RequestFuture.newFuture();

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, urlQuery, null, future, future);
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(request);

        try {
            JSONArray productsArray = future.get(10, TimeUnit.SECONDS).getJSONArray("cards");
            for (int i = 0; i < productsArray.length(); i++) {
                if (products == null) {
                    products = new ArrayList<Product>();
                }
                JSONObject base = productsArray.getJSONObject(i).getJSONArray("products").getJSONObject(0);

                Product product = new Product();

                if (base.has("title"))
                    product.name = base.getString("title");
                if (base.has("summary"))
                    product.description = base.getString("summary");
                if (base.getJSONObject("price").has("unitSize"))
                    product.weight = base.getJSONObject("price").getString("unitSize");
                if (base.getJSONObject("price").has("unitInfo"))
                    product.kgPrice = base.getJSONObject("price").getJSONObject("unitInfo").getDouble("price");
                if (base.getJSONObject("price").has("now"))
                    product.price = base.getJSONObject("price").getDouble("now");
                if (base.getJSONArray("images").length() > 0 && base.getJSONArray("images").getJSONObject(0).has("url"))
                    product.imgURL = base.getJSONArray("images").getJSONObject(0).getString("url");
                Log.d("TEST", "Found product: " + product.name);
                products.add(product);
            }
            Log.d("TEST", "Returning products");
            return products;
        }
        catch(InterruptedException | TimeoutException | ExecutionException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    public void onFail() {
        Log.d("TEST", "FAILED LOADING");
    }

    public void getResponse(int method, Context context, JSONObject jsonValue, final VolleyCallback callback) {

        RequestQueue queue = Volley.newRequestQueue(context);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, urlQuery, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject Response) {
                        callback.onSuccessResponse(Response);
                    }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError e) {
                e.printStackTrace();
            }
        });
        queue.add(jsonObjectRequest);
    }

}




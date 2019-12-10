package com.example.prikkie.Api.IngredientApi;

import android.os.AsyncTask;

import com.example.prikkie.App;
import com.example.prikkie.R;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AHAPIAsync extends AsyncTask<String, Void, List<Product>> {
    private String urlQuery = App.getContext().getString(R.string.ah_api);
    private List<Product> products;

    public AHAPIAsync(int resultSize){
        urlQuery += App.getContext().getString(R.string.ah_size) + resultSize;
    }

    public void orderBy(AHAPI.orderBy order){
        if(order == AHAPI.orderBy.ASC){
            urlQuery += "&sortBy=price";
        }else if(order == AHAPI.orderBy.DESC){
            urlQuery += "&sortBy=-price";
        }
    }

    public void setTaxonomy(String taxonomy){
        taxonomy = taxonomy.replaceAll("\\s", "%20");
        urlQuery += App.getContext().getString(R.string.ah_taxonomy) + taxonomy;
    }

    public void setQuery(String query){
        query = query.replaceAll("\\s", "%20");
        urlQuery += App.getContext().getString(R.string.ah_query) + query;
    }



    @Override
    protected List<Product> doInBackground(String... strings) {
        try{
            HttpGet httppost = new HttpGet(urlQuery);
            HttpClient httpclient = new DefaultHttpClient();
            HttpResponse response = httpclient.execute(httppost);        // StatusLine stat = response.getStatusLine();
            int status = response.getStatusLine().getStatusCode();

            if (status == 200) {
                HttpEntity entity = response.getEntity();
                String data = EntityUtils.toString(entity);


                JSONObject json = new JSONObject(data);

                JSONArray productsArray = json.getJSONArray("cards");
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
                    products.add(product);
                }
                return products;
            }

        } catch (IOException e) {
        e.printStackTrace();
    } catch (JSONException e) {

        e.printStackTrace();
    }
        return null;
    }
}

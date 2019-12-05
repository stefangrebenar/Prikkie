package com.example.prikkie.Api.recipe_api.PrikkieApi;

import android.os.AsyncTask;
import android.util.Log;

import com.example.prikkie.App;
import com.example.prikkie.R;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

public class PrikkieAmountOfRecipesAsync  extends AsyncTask<String, Void, Integer> {
    private final String urlQuery = App.getContext().getString(R.string.prikkie_api) + "totalrecipeamount";

    @Override
    protected Integer doInBackground(String... strings) {

        // Preform request
        HttpGet httppost = new HttpGet(urlQuery);
        HttpClient httpclient = new DefaultHttpClient();
        try {
            HttpResponse response = httpclient.execute(httppost);        // StatusLine stat = response.getStatusLine();
            int status = response.getStatusLine().getStatusCode();

            // If response is Ok 200
            if (status == 200) {
                HttpEntity entity = response.getEntity();
                String data = EntityUtils.toString(entity);
                return Integer.parseInt(data);
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return 0;
    }
}

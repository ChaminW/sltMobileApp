package com.example.thusitha.slt;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

public class Material extends AppCompatActivity {
    String myJSON;
    private static final String TAG_RESULTS="result";
    private static final String TAG_ITEMNAME="item_name";
    private static final String TAG_QUANTITYAVAILABLE = "quantity_available";


    JSONArray items = null;

    ArrayList<HashMap<String, String>> itemList;

    ListView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_material);
        list = (ListView) findViewById(R.id.listView);
        itemList = new ArrayList<HashMap<String,String>>();
        getData();
    }
    protected void showList(){
        try {
            JSONObject jsonObj = new JSONObject(myJSON);
            items = jsonObj.getJSONArray(TAG_RESULTS);

            for(int i=0;i<items.length();i++){
                JSONObject c = items.getJSONObject(i);
                String item_name = c.getString(TAG_ITEMNAME);
                String quantity_available = c.getString(TAG_QUANTITYAVAILABLE);


                HashMap<String,String> item = new HashMap<String,String>();

                item.put(TAG_ITEMNAME,item_name);
                item.put(TAG_QUANTITYAVAILABLE,quantity_available);

                itemList.add(item);
            }

            ListAdapter adapter = new SimpleAdapter(
                    Material.this, itemList, R.layout.list_item,
                    new String[]{TAG_ITEMNAME,TAG_QUANTITYAVAILABLE},
                    new int[]{R.id.item_name, R.id.quantity_available}
            );

            list.setAdapter(adapter);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void getData(){
        class GetDataJSON extends AsyncTask<String, Void, String> {

            @Override
            protected String doInBackground(String... params) {
                DefaultHttpClient httpclient = new DefaultHttpClient(new BasicHttpParams());
                HttpPost httppost = new HttpPost("http://192.168.43.67/getMaterial.php");

                // Depends on your web service
                httppost.setHeader("Content-type", "application/json");

                InputStream inputStream = null;
                String result = null;
                try {
                    HttpResponse response = httpclient.execute(httppost);
                    HttpEntity entity = response.getEntity();

                    inputStream = entity.getContent();
                    // json is UTF-8 by default
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"), 8);
                    StringBuilder sb = new StringBuilder();

                    String line = null;
                    while ((line = reader.readLine()) != null)
                    {
                        sb.append(line + "\n");
                    }
                    result = sb.toString();
                } catch (Exception e) {
                    // Oops
                }
                finally {
                    try{if(inputStream != null)inputStream.close();}catch(Exception squish){}
                }
                return result;
            }

            @Override
            protected void onPostExecute(String result){
                myJSON=result;
                showList();
            }
        }
        GetDataJSON g = new GetDataJSON();
        g.execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void icon(View view) {
        Intent intent = new Intent(Material.this, Home.class);
        startActivity(intent);

    }
}

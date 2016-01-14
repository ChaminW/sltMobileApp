package com.example.oshaniperera.slt;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class customer_view extends AppCompatActivity {
    String myJSON;
    private static final String TAG_RESULTS = "result";
    private static final String TAG_NAME="name";
    private static final String TAG_ID="id";
    private static final String TAG_TPNO = "tpno";
    private static final String TAG_ANOTHERTPNO = "anothertpno";
    private static final String TAG_ADDRESS = "address";


    JSONArray items = null;

    ArrayList<HashMap<String, String>> itemList;

    ListView list;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_view);
        getData();
    }


    protected void showList(){
        try {
            JSONObject jsonObj = new JSONObject(myJSON);
            items = jsonObj.getJSONArray(TAG_RESULTS);


            for(int i=0;i<items.length();i++){
                JSONObject c = items.getJSONObject(i);
                String name = c.getString(TAG_NAME);
                String id = c.getString(TAG_ID);
                String tpno = c.getString(TAG_TPNO);
                String anothertpno = c.getString(TAG_ANOTHERTPNO);
                String address = c.getString(TAG_ADDRESS);


                HashMap<String,String> item = new HashMap<String,String>();
                itemList = new ArrayList<>();

                item.put(TAG_NAME,name);
                item.put(TAG_ID,id);
                item.put(TAG_TPNO,tpno);
                item.put(TAG_ANOTHERTPNO,anothertpno);
                item.put(TAG_ADDRESS,address);

                itemList.add(item);
            }

            ListAdapter adapter = new SimpleAdapter(
                    customer_view.this, itemList, R.layout.customer_list_item,
                    new String[]{TAG_NAME,TAG_ID,TAG_TPNO,TAG_ANOTHERTPNO,TAG_ADDRESS},
                    new int[]{R.id.name, R.id.id, R.id.tpno, R.id.anothertpno, R.id.address}
            );
            list = ListView();
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
                HttpPost httppost = new HttpPost("http://100.68.254.81:8080/getCustomer.php");

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
}

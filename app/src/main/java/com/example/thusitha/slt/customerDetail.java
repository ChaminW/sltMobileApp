package com.example.thusitha.slt;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class customerDetail extends AppCompatActivity {
    private static final int MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION = 1;
    private static final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    List<LatLng> peerList = null;
    List<String> idList = null;

    String myJSON;
    private static final String TAG_RESULTS="result";
    private static final String TAG_ID= "id";
    private static final String TAG_NAME="name";
    private static final String TAG_TPNO= "tpno";
    private static final String TAG_ANOTHERTPNO= "anothertpno";
    private static final String TAG_ADDRESS= "address";

    Map<String, String> sortedDistanceMap= null;





    JSONArray customer = null;

    ArrayList<HashMap<String, String>> itemList;

    ListView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_detail);
        list = (ListView) findViewById(R.id.listView);
        itemList = new ArrayList<HashMap<String,String>>();


        updateList();




    }

    private void updateList(){
        //retrieve peer list from web service*****************
        peerList = new LinkedList<>();
        idList = new LinkedList<>();
        peerList.add(new LatLng(7.1, 80.12));
        idList.add("1");
        peerList.add(new LatLng(6.23, 82.12));
        idList.add("3");
        peerList.add(new LatLng(6.33, 81.56));
        idList.add("5");
        peerList.add(new LatLng(6.33, 81.1456));
        idList.add("7");
        peerList.add(new LatLng(6.53, 81.4562));
        idList.add("9");
        peerList.add(new LatLng(6.63, 82.4562));
        idList.add("10");
        //**********************************************

        LatLng origin = new LatLng(6.33, 81.56);


        //Location originLoc = getLastBestLocation();
        //origin = new LatLng(originLoc.getLatitude(), originLoc.getLongitude());


        Map<String, String> distanceMap = new HashMap<String, String>();

        for (int i = 0; i < peerList.size(); i++) {
            double dist = distance(peerList.get(i).latitude, peerList.get(i).longitude, origin.latitude, origin.longitude);

            distanceMap.put(idList.get(i), String.valueOf(dist));
        }

        sortedDistanceMap = sortByValue(distanceMap);
        System.out.println(sortedDistanceMap);

        final ArrayList<HashMap<String, String>> itemList2 = new ArrayList<>();

        HashMap<String,String> customer;

        for (Map.Entry<String,String> entry :sortedDistanceMap.entrySet()) {
            customer= new HashMap<String,String>();
            customer.put( TAG_ID,entry.getKey());

            itemList2.add(customer);
        }

        ListAdapter adapter = new SimpleAdapter(
                    customerDetail.this, itemList2, R.layout.customer_list_item,
                    new String[]{TAG_ID},
                    new int[]{R.id.id}
            );

            list.setAdapter(adapter);
            list.setOnItemClickListener(
                    new AdapterView.OnItemClickListener() {
                                            @Override
                                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                                LatLng locLatLang=null;
                                                int j =0;
                                                for (Map.Entry<String,String> entry :sortedDistanceMap.entrySet()) {

                                                    if (j==position) {
                                                        HashMap<String, String> item = new HashMap<String, String>();
                                                        int index = idList.indexOf(entry.getKey());
                                                         locLatLang = peerList.get(index);
                                                        System.out.println( "selected location");
                                                        System.out.println( locLatLang);
                                                        break;

                                                    }
                                                    j++;
                                                }


                                                //Starting login activity
                                                Intent intent = new Intent(customerDetail.this, com.example.thusitha.slt.Map.class);
                                                intent.putExtra("loc",new String[]{String.valueOf(locLatLang.latitude), String.valueOf(locLatLang.longitude)});
                                                startActivity(intent);

                                            }
                                        }


            );


    }


//    protected void showList(){
//        try {
//            JSONObject jsonObj = new JSONObject(myJSON);
//            customer = jsonObj.getJSONArray(TAG_RESULTS);
//
//            for(int i=0;i<customer.length();i++){
//                JSONObject c = customer.getJSONObject(i);
//                String id = c.getString(TAG_ID);
//                String name = c.getString(TAG_NAME);
//                String tpno = c.getString(TAG_TPNO);
//                String anothertpno = c.getString(TAG_ANOTHERTPNO);
//                String address = c.getString(TAG_ADDRESS);
//
//
//                HashMap<String,String> costomer = new HashMap<String,String>();
//
//                costomer.put(TAG_ID,id);
//                costomer.put(TAG_NAME,name);
//                costomer.put(TAG_TPNO,tpno);
//                costomer.put(TAG_ANOTHERTPNO,anothertpno);
//                costomer.put(TAG_ADDRESS,address);
//
//                itemList.add(costomer);
//            }
//
//
//
//            ListAdapter adapter = new SimpleAdapter(
//                    customerDetail.this, itemList, R.layout.customer_list_item,
//                    new String[]{TAG_ID,TAG_NAME,TAG_TPNO,TAG_ANOTHERTPNO,TAG_ADDRESS},
//                    new int[]{R.id.id, R.id.name,R.id.tpno,R.id.anothertpno,R.id.address}
//            );
//
//            list.setAdapter(adapter);
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//    }
//
//    public void getData(){
//        class GetDataJSON extends AsyncTask<String, Void, String> {
//
//            @Override
//            protected String doInBackground(String... params) {
//                DefaultHttpClient httpclient = new DefaultHttpClient(new BasicHttpParams());
//                HttpPost httppost = new HttpPost("http://192.168.43.67/getCustomer.php");
//
//                // Depends on your web service
//                httppost.setHeader("Content-type", "application/json");
//
//                InputStream inputStream = null;
//                String result = null;
//                try {
//                    HttpResponse response = httpclient.execute(httppost);
//                    HttpEntity entity = response.getEntity();
//
//                    inputStream = entity.getContent();
//                    // json is UTF-8 by default
//                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"), 8);
//                    StringBuilder sb = new StringBuilder();
//
//                    String line = null;
//                    while ((line = reader.readLine()) != null)
//                    {
//                        sb.append(line + "\n");
//                    }
//                    result = sb.toString();
//                } catch (Exception e) {
//                    // Oops
//                }
//                finally {
//                    try{if(inputStream != null)inputStream.close();}catch(Exception squish){}
//                }
//                return result;
//            }
//
//            @Override
//            protected void onPostExecute(String result){
//                myJSON=result;
//                showList();
//            }
//        }
//        GetDataJSON g = new GetDataJSON();
//        g.execute();
//    }

    private double distance(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;

        dist = dist * 1.609344;

        return (dist);
    }

    /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
    /*::  This function converts decimal degrees to radians             :*/
    /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
    /*::  This function converts radians to decimal degrees             :*/
    /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }

    public static <K, V extends Comparable<? super V>> Map<K, V>
    sortByValue(Map<K, V> map) {
        List<Map.Entry<K, V>> list =
                new LinkedList<>(map.entrySet());
        Collections.sort(list, new Comparator<Map.Entry<K, V>>() {
            @Override
            public int compare(Map.Entry<K, V> o1, Map.Entry<K, V> o2) {
                return (o1.getValue()).compareTo(o2.getValue());
            }
        });

        Map<K, V> result = new LinkedHashMap<>();
        for (Map.Entry<K, V> entry : list) {
            result.put(entry.getKey(), entry.getValue());
        }
        return result;
    }


    private Location getLastBestLocation() {

        LocationManager mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
// check whether Gps and internet services are activated
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION},
                    MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION);
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);

        }

        Location locationGPS = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        Location locationNet = mLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

        long GPSLocationTime = 0;
        if (null != locationGPS) { GPSLocationTime = locationGPS.getTime(); }

        long NetLocationTime = 0;

        if (null != locationNet) {
            NetLocationTime = locationNet.getTime();
        }

        if ( 0 < GPSLocationTime - NetLocationTime ) {
            return locationGPS;
        }
        else {
            return locationNet;
        }
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
        Intent intent = new Intent(customerDetail.this, Home.class);
        startActivity(intent);

    }


}

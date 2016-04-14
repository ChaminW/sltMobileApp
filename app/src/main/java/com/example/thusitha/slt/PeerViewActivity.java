package com.example.thusitha.slt;

import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.example.thusitha.slt.controller.DirectionJSONParser;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class PeerViewActivity extends FragmentActivity implements OnMapReadyCallback,GoogleMap.OnMyLocationChangeListener  {


    private static final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1 ;
    private static final int MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION = 1 ;
    private GoogleMap mMap;
    LatLng origin = null;
    List<LatLng> peerList = null ;
    List<Marker> peerMarkerArray = null ;
    Polyline polyline = null;
    Marker  origin_marker= null;
    int origin_change_state=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_peer_view);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // check whether Gps and internet services are activated
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {



                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION},
                        MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION);
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);



        }else{
            mMap.setMyLocationEnabled(true);
        }

        mMap.setOnMyLocationChangeListener(this);
//        mMap.setContentDescription("axsaxasx");

        //retrieve peer list from web service
        peerList = new LinkedList<>();
        peerList.add(new LatLng(6.13, 80.12));
        peerList.add(new LatLng(6.23, 82.12));
        peerList.add(new LatLng(6.33, 81.56));
        peerList.add(new LatLng(6.33, 81.1456));
        peerList.add(new LatLng(6.53, 81.4562));
        peerList.add(new LatLng(6.63, 82.4562));



        peerMarkerArray = new ArrayList<>();
        for( int i=0;i<peerList.size();i++ ){
            peerMarkerArray.add(addPeerMarker(peerList.get(i)));
        }


        // Showing the current location in Google Map
        mMap.moveCamera(CameraUpdateFactory.newLatLng(peerList.get(0)));

        // Zoom in the Google Map
        mMap.animateCamera(CameraUpdateFactory.zoomTo(5));
        origin_change_state=0;






    }

    private Marker addOriginMarker(LatLng location_LatLang) {
        MarkerOptions markerOptions=new MarkerOptions().position(location_LatLang).title("Current Location");
        Marker marker= mMap.addMarker(markerOptions);
        return marker;
    }

    private Marker addPeerMarker(LatLng location_LatLang) {
        MarkerOptions markerOptions=new MarkerOptions().position(location_LatLang).title("Peer technician").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
        markerOptions.snippet("Technician location is far from "+" and "+" duration.");
        Marker marker= mMap.addMarker(markerOptions);
        return marker;
    }

    @Override
    public void onMyLocationChange(Location location) {

        if (origin==null){
            origin_change_state=1;
        }

        // Getting latitude of the current location
        double latitude = location.getLatitude();

        // Getting longitude of the current location
        double longitude = location.getLongitude();

        // Creating a LatLng object for the current location
        LatLng origin = new LatLng(latitude, longitude);

        if(origin_marker!=null){
            origin_marker.remove();
        }

        origin_marker = addOriginMarker(origin);

//        if (origin_change_state==1) {
//            // Showing the current location in Google Map
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(origin));
//
//            // Zoom in the Google Map
//        mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
//            origin_change_state=0;
//        }

        for( int i=0;i<peerList.size();i++ ) {
            // Getting URL to the Google Directions API
            String url = getDirectionsUrl(origin, peerList.get(i));

            DownloadTask downloadTask = new DownloadTask();

            // Start downloading json data from Google Directions API
            downloadTask.execute(url);
        }


    }

    private String getDirectionsUrl(LatLng origin,LatLng dest){

        // Origin of route
        String str_origin = "origin="+origin.latitude+","+origin.longitude;

        // Destination of route
        String str_dest = "destination="+dest.latitude+","+dest.longitude;

        // Sensor enabled
        String sensor = "sensor=false";

        // Building the parameters to the web service
        String parameters = str_origin+"&"+str_dest+"&"+sensor;

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/"+output+"?"+parameters;

        return url;
    }



    // Fetches data from url passed
    private class DownloadTask extends AsyncTask<String, Void, String> {

        // Downloading data in non-ui thread
        @Override
        protected String doInBackground(String... url) {

            // For storing data from web service
            String data = "";

            try{
                // Fetching the data from web service
                data = downloadUrl(url[0]);
            }catch(Exception e){
                Log.d("Background Task",e.toString());
            }
            return data;
        }

        // Executes in UI thread, after the execution of
        // doInBackground()
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            ParserTask parserTask = new ParserTask();

            // Invokes the thread for parsing the JSON data
            parserTask.execute(result);
        }
    }

    /** A method to download json data from url */
    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try{
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while( ( line = br.readLine()) != null){
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        }catch(Exception e){
            Log.d("Exception in download", e.toString());
        }finally{
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }


    /** A class to parse the Google Places in JSON format */
    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String,String>>> > {

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try{
                jObject = new JSONObject(jsonData[0]);
                DirectionJSONParser parser = new DirectionJSONParser();

                // Starts parsing data
                routes = parser.parse(jObject);
            }catch(Exception e){
                e.printStackTrace();
            }
            return routes;
        }

        // Executes in UI thread, after the parsing process
        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList<LatLng> points = null;
            PolylineOptions lineOptions = null;
            MarkerOptions markerOptions = new MarkerOptions();


            // Traversing through all the routes
            for(int i=0;i<result.size();i++){
                points = new ArrayList<LatLng>();
                lineOptions = new PolylineOptions();

                // Fetching i-th route
                List<HashMap<String, String>> path = result.get(i);

                // Fetching all the points in i-th route
                for(int j=0;j<path.size();j++){
                    HashMap<String,String> point = path.get(j);

                    if(j==0){    // Get distance from the list
//                        distance = (String)point.get("distance");

                        continue;
                    }else if(j==1){ // Get duration from the list
//                        duration = (String)point.get("duration");

                        continue;
                    }


                }


            }


        }
    }

}

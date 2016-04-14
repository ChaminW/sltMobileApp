package com.example.thusitha.slt.controller;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Thusitha on 3/17/2017.
 */
public class DistanceMatrixJSONParser {
    /** Receives a JSONObject and returns a list of lists containing latitude and longitude */
    public List<List<HashMap<String,String>>> parse(JSONObject jObject)  {

        List list = new ArrayList<HashMap<String, String>>();
        JSONArray jElements = null;
        JSONArray jLegs = null;
        JSONArray jSteps = null;
        JSONObject jDistance = null;
        JSONObject jDuration = null;

        try {

            jElements = (JSONArray) jObject.getJSONArray("rows").get(0);

            /** Traversing all elements */
            for(int i=0;i<jElements.length();i++){

                List dist_dura = new ArrayList<HashMap<String, String>>();

                /** Getting distance from the json data */
                jDistance = ((JSONObject) jElements.get(i)).getJSONObject("distance");
                HashMap<String, String> hmDistance = new HashMap<String, String>();
                hmDistance.put("distance", jDistance.getString("text"));

                /** Getting duration from the json data */
                jDuration = ((JSONObject) jLegs.get(i)).getJSONObject("duration");
                HashMap<String, String> hmDuration = new HashMap<String, String>();
                hmDuration.put("duration", jDuration.getString("text"));

                /** Adding distance object to the path */
                dist_dura.add(hmDistance);

                /** Adding duration object to the path */
                dist_dura.add(hmDuration);


                list.add(dist_dura);
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }catch (Exception e){

        }

        return list;
    }


}

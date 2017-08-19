package com.yinghuizou.placesnearme;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by yinghuizou on 8/17/17.
 */

//Working with the DownloadUrl class, put the Jason format data and parse in to list of HashMap
public class DataParser {


    //Hash Map only can hold one piece of data
    private HashMap<String, String> getPlace(JSONObject jasonData) {
        HashMap<String, String> googlePlaceMap = new HashMap<String, String>();

        String placeName = "-NA-";
        String vicinity = "-NA-";
        String latitude = "";
        String longitude = "";
        String reference = "";
        String rating="-NA-";
        String international_phone_number="-NA-";

        try {
            if (!jasonData.isNull("name")) {
                placeName = jasonData.getString("name");
            }

            if (!jasonData.isNull("website")) {
                placeName = jasonData.getString("website");
            }


            if (!jasonData.isNull("vicinity")) {
                vicinity = jasonData.getString("vicinity");
            }


            if(!jasonData.isNull("rating")){
                rating = jasonData.getString("rating");
            }

            if(!jasonData.isNull("international_phone_number")){
                international_phone_number = jasonData.getString("international_phone_number");
            }


            latitude = jasonData.getJSONObject("geometry").getJSONObject("location").getString("lat");
            longitude = jasonData.getJSONObject("geometry").getJSONObject("location").getString("lng");
            reference = jasonData.getString("reference");





            //Store the data take from the jason put in to Hashmap
            googlePlaceMap.put("Hashplace_name", placeName);
            googlePlaceMap.put("Hashvicinity", vicinity);
            googlePlaceMap.put("Hashlatitude", latitude);
            googlePlaceMap.put("Hashlongitude", longitude);
            googlePlaceMap.put("Hashreference", reference);

            googlePlaceMap.put("Hashrating", rating);

            googlePlaceMap.put("international_phone_number", international_phone_number);



        }catch (Exception e){
            e.printStackTrace();


        }


        return googlePlaceMap;


    }


    private List<HashMap<String, String>> getPlaces(JSONArray jsonArray) {
        int placeAmount = jsonArray.length();
        List<HashMap<String, String>> placesList = new ArrayList<>();


        HashMap<String, String> placeMap = null;

        int i = 0;

        while (i<placeAmount){


            try{
                //Get the data for each of the placeMap
                placeMap = getPlace((JSONObject) jsonArray.get(i));

                //put each of the data in the list

                placesList.add(placeMap);


            }catch (Exception e){

                e.printStackTrace();

            }




            i++;
        }



        return placesList;

    }


    public List<HashMap<String, String>> parse(String jsonData) {
        JSONArray jsonArray = null;
        JSONObject jsonObject;

        try {

            jsonObject = new JSONObject((String) jsonData);
            jsonArray = jsonObject.getJSONArray("results");
        } catch (Exception e) {

            e.printStackTrace();
        }
        //Parse all the jasonData and transfer in to List of the HashMap
        return getPlaces(jsonArray);
    }





}

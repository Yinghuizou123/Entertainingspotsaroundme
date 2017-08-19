package com.yinghuizou.placesnearme;

import android.os.AsyncTask;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.HashMap;
import java.util.List;

/**
 * Created by yinghuizou on 8/17/17.
 */

public class GetNearbyPlacesData extends AsyncTask<Object, String, String> {


    String googlePlacesData;
    GoogleMap mMap;
    String url;


    @Override
    protected String doInBackground(Object... params) {

        mMap = (GoogleMap)params[0];
        url = (String) params[1];

        //We are reading the Url data from the website and turn in jason formate, store
        // in the googlePlacesData
        DownloadUrl downloadUrl = new DownloadUrl();
        try {

            googlePlacesData = downloadUrl.readUrl(url);

        }catch (Exception e){

        e.printStackTrace();
        }

        //Data we get from using downloadUrl in Jason formate.
        return googlePlacesData;
    }


    @Override
    protected void onPostExecute(String s) {

        List<HashMap<String, String>> nearbyPlacesList = null;
        DataParser dataParser = new DataParser();
        nearbyPlacesList =  dataParser.parse(s);
        //Get the data in Jason, and put in to HashMap, and then display them using ShowNearbyPlaces
        ShowNearbyPlaces(nearbyPlacesList);
    }


    private void ShowNearbyPlaces(List<HashMap<String, String>> nearbyPlacesList) {
        //Display the marker for the nearByPlacesList data


        for (int i = 0; i < nearbyPlacesList.size(); i++) {

            MarkerOptions markerOptions = new MarkerOptions();

            //Get each one of the data from nearbyPlacesList
            HashMap<String, String> placedata = nearbyPlacesList.get(i);


            double latitude = Double.parseDouble(placedata.get("Hashlatitude"));
            double longtitude = Double.parseDouble(placedata.get("Hashlongitude"));
            String placeName = placedata.get("Hashplace_name");
            String placevicinity = placedata.get("Hashvicinity");
            String rating  = placedata.get("Hashrating");



            LatLng latLng = new LatLng(latitude, longtitude);

            markerOptions.position(latLng);

            String number  = placedata.get("international_phone_number");

            markerOptions.title( "Name: " + placeName +" "+ rating);


            markerOptions.snippet("Address: " + placevicinity);


            mMap.addMarker(markerOptions);
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
            //move map camera
            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(10));


        }





    }



}

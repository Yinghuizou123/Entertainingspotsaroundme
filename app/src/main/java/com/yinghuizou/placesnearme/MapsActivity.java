package com.yinghuizou.placesnearme;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.LocationListener;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.w3c.dom.Text;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener{


    //Our Map
    private GoogleMap mMap;
    //User Client that we need to connect
    private GoogleApiClient  mGoogleApiClient;
    private LocationRequest locationRequest;
    private Location mLastLocation;
    private Marker mCurrLocationMarker;
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    double latitude;
    double longitude;
    private int PROXIMITY_RADIUS = 10000;
    private CharSequence[] item = {"Amusement park","Movie Theater","Shopping Mall","Museum","Bar","Art Gallery","Park","Zoo","Spa","Aquarium"};






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.


            //Give the current location
            checkLocationPermission();


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

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            //intialize Google Play Services.
            buildGoogleApiClient();
            mMap.setMyLocationEnabled(true);
        }


    }






















    private String getUrl(double latitude , double longitude , String nearbyPlace)
    {

        StringBuilder googlePlaceUrl = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        googlePlaceUrl.append("location="+latitude+","+longitude);
        googlePlaceUrl.append("&radius="+PROXIMITY_RADIUS);
        googlePlaceUrl.append("&type="+nearbyPlace);
        googlePlaceUrl.append("&sensor=true");
        googlePlaceUrl.append("&key="+"AIzaSyCxGP7RjtggZoSTT-ueea8Rk-i7La8Lwt4");


        return googlePlaceUrl.toString();
    }



    //GoogleApiClient.ConnectionCallbacks method call when the device is connect
    @Override
    public void onConnected(@Nullable Bundle bundle) {

        // updatecurrent location of user at regular intervals using locationRequest
        locationRequest = new LocationRequest();


        locationRequest.setInterval(100);
        locationRequest.setFastestInterval(1000);
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        //Check if the permission is granted
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION ) == PackageManager.PERMISSION_GRANTED)
        {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, locationRequest, this);
        }




    }

    @Override
    public void onConnectionSuspended(int i) {

    }
    // GoogleApiClient.OnConnectionFailedListener method when the connection is fail
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    //LocationListener method  call when the location is change
    @Override
    public void onLocationChanged(Location location) {
        mLastLocation = location;

        latitude = location.getLatitude();
        longitude = location.getLongitude();





        //if the marker still exist, we remove the marker
        if(mCurrLocationMarker!=null){
            mCurrLocationMarker.remove();

        }

        //Getting the current address for the  marker
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());



        //Seeting the property of the marker
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("Current Position");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));

        //Adding the marker on the map to markerOption. So CurrentLocationMarker will have those value
        mCurrLocationMarker = mMap.addMarker(markerOptions);


        //move map camera to the postion
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(10));


        //stop location updates if we already are in that position.
        //If mGoogleApiClient is null, then mean there is no location set
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }




    }


    //GoogleApiClient.Builder is used to connect client
    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }


    public boolean checkLocationPermission(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            // Show user if they want explanation to grant the permission
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an expanation to the user
                if (ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.ACCESS_FINE_LOCATION))
                {
                    ActivityCompat.requestPermissions(this,new String[] {Manifest.permission.ACCESS_FINE_LOCATION },MY_PERMISSIONS_REQUEST_LOCATION);
                }


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }


    ////This method pop out when the dialog box is presented when the app need to have the permission, and when the user responds, it will invokes this method
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch(requestCode)
        {
            case MY_PERMISSIONS_REQUEST_LOCATION:

                if(grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    // //Permission is granted
                    if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) !=  PackageManager.PERMISSION_GRANTED)
                    {

                        //If client is null, we will set another client
                        if(mGoogleApiClient == null)
                        {
                            buildGoogleApiClient();
                        }
                        mMap.setMyLocationEnabled(true);
                    }
                }
                else
                {
                    Toast.makeText(this,"Permission Denied" , Toast.LENGTH_LONG).show();
                }
        }
    }



public void checklist(View view){

    new AlertDialog.Builder(this)
            .setSingleChoiceItems(item, 0, null)
            .setTitle("Entertainment list")
            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int whichButton) {
                    dialog.dismiss();
                    int selectedPosition = ((AlertDialog)dialog).getListView().getCheckedItemPosition();

                    if(selectedPosition==0){

                        mMap.clear();

                        String data = "amusement_park";
                        //Lat and lon using to check for the location
                        String urldata  = getUrl(latitude,  longitude, data);

                        Object placedata[]  = new Object[2];

                        placedata[0]=mMap;
                        placedata[1]=urldata;

                        GetNearbyPlacesData getNearbyPlacesData = new GetNearbyPlacesData();
                        getNearbyPlacesData.execute(placedata);

                    }
                    if(selectedPosition==1){

                        mMap.clear();

                        String data = "movie_theater";
                        //Lat and lon using to check for the location
                        String urldata  = getUrl(latitude,  longitude, data);

                        Object placedata[]  = new Object[2];

                        placedata[0]=mMap;
                        placedata[1]=urldata;

                        GetNearbyPlacesData getNearbyPlacesData = new GetNearbyPlacesData();
                        getNearbyPlacesData.execute(placedata);

                    }

                    if(selectedPosition==2){


                        mMap.clear();

                        String data = "shopping_mall";
                        //Lat and lon using to check for the location
                        String urldata  = getUrl(latitude, longitude, data);

                        Object placedata[]  = new Object[2];

                        placedata[0]=mMap;
                        placedata[1]=urldata;

                        GetNearbyPlacesData getNearbyPlacesData = new GetNearbyPlacesData();
                        getNearbyPlacesData.execute(placedata);

                    }

                    if(selectedPosition==3){


                        mMap.clear();

                        String data = "museum";
                        //Lat and lon using to check for the location
                        String urldata  = getUrl(latitude,  longitude, data);

                        Object placedata[]  = new Object[2];

                        placedata[0]=mMap;
                        placedata[1]=urldata;

                        GetNearbyPlacesData getNearbyPlacesData = new GetNearbyPlacesData();
                        getNearbyPlacesData.execute(placedata);


                    }

                    if(selectedPosition==4){

                        mMap.clear();

                        String data = "bar";
                        //Lat and lon using to check for the location
                        String urldata  = getUrl(latitude, longitude, data);

                        Object placedata[]  = new Object[2];

                        placedata[0]=mMap;
                        placedata[1]=urldata;

                        GetNearbyPlacesData getNearbyPlacesData = new GetNearbyPlacesData();
                        getNearbyPlacesData.execute(placedata);


                    }
                    if(selectedPosition==5){

                        mMap.clear();

                        String data = "art_gallery";
                        //Lat and lon using to check for the location
                        String urldata  = getUrl(latitude, longitude, data);

                        Object placedata[]  = new Object[2];

                        placedata[0]=mMap;
                        placedata[1]=urldata;

                        GetNearbyPlacesData getNearbyPlacesData = new GetNearbyPlacesData();
                        getNearbyPlacesData.execute(placedata);



                    }


                    if(selectedPosition==6){

                        mMap.clear();

                        String data = "park";
                        //Lat and lon using to check for the location
                        String urldata  = getUrl(latitude, longitude, data);

                        Object placedata[]  = new Object[2];

                        placedata[0]=mMap;
                        placedata[1]=urldata;

                        GetNearbyPlacesData getNearbyPlacesData = new GetNearbyPlacesData();
                        getNearbyPlacesData.execute(placedata);


                    }
                    if(selectedPosition==7){
                        mMap.clear();

                        String data = "zoo";
                        //Lat and lon using to check for the location
                        String urldata  = getUrl(latitude, longitude, data);

                        Object placedata[]  = new Object[2];

                        placedata[0]=mMap;
                        placedata[1]=urldata;

                        GetNearbyPlacesData getNearbyPlacesData = new GetNearbyPlacesData();
                        getNearbyPlacesData.execute(placedata);



                    }
                    if(selectedPosition==8){
                        mMap.clear();

                        String data = "spa";
                        //Lat and lon using to check for the location
                        String urldata  = getUrl(latitude, longitude, data);

                        Object placedata[]  = new Object[2];

                        placedata[0]=mMap;
                        placedata[1]=urldata;

                        GetNearbyPlacesData getNearbyPlacesData = new GetNearbyPlacesData();
                        getNearbyPlacesData.execute(placedata);




                    }
                    if(selectedPosition==9){

                        mMap.clear();

                        String data = "aquarium";
                        //Lat and lon using to check for the location
                        String urldata  = getUrl(latitude, longitude, data);

                        Object placedata[]  = new Object[2];

                        placedata[0]=mMap;
                        placedata[1]=urldata;

                        GetNearbyPlacesData getNearbyPlacesData = new GetNearbyPlacesData();
                        getNearbyPlacesData.execute(placedata);


                    }
                    if(selectedPosition==10){




                    }
                    if(selectedPosition==11){




                    }

                }
            })
            .show();
}





}

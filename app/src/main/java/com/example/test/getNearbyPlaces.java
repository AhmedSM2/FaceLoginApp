package com.example.test;

import android.os.AsyncTask;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class getNearbyPlaces extends AsyncTask<Object , String , String>
{
    private String googlePlaceData , url;
    private GoogleMap mMap;

    @Override
    protected String doInBackground(Object... objects)
    {
        mMap = (GoogleMap) objects[0];
        url = (String) objects[1];

        DownloadUrl downloadUrl = new DownloadUrl();

        try
        {
            googlePlaceData = downloadUrl.ReadTheUrl(url);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        return googlePlaceData;
    }

    @Override
    protected void onPostExecute(String s)
    {
        List<HashMap< String ,String>> nearbyPlacesList = null;
        DataParser dataParser = new DataParser();
        nearbyPlacesList = dataParser.parse(s);
        displayNearbyPlaces(nearbyPlacesList);
    }


    private void displayNearbyPlaces(List<HashMap< String ,String>> nearbyPlacesList)
    {
        for(int i =0 ; i <nearbyPlacesList.size() ;i++)
        {
            MarkerOptions markerOptions = new MarkerOptions();

            HashMap< String , String> googleNearbyPlaces = nearbyPlacesList.get(i);

            String nameOfPlace = googleNearbyPlaces.get("place_name");
            String vicinity = googleNearbyPlaces.get("vicinity");
            Double lat = Double.parseDouble(googleNearbyPlaces.get("lat"));
            Double lng = Double.parseDouble(googleNearbyPlaces.get("lng"));

            LatLng latLng = new LatLng(lat , lng);

            markerOptions.position(latLng);
            markerOptions.title(nameOfPlace + " : " + vicinity);
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW));
            mMap.addMarker(markerOptions);
            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(10));



        }
    }
}

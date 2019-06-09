package com.example.test;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DataParser
{
    private HashMap< String , String > getSingleNearbyPlace(JSONObject googlePlaceJSON)
    {
        HashMap<String , String> googlePlaceMap = new HashMap<>();
        String NameOfPlace = "-NA-";
        String vicinity = "-NA-";
        String latitude = "";
        String longitude = "";
        String reference = "";


      try
      {
          if(!googlePlaceJSON.isNull("name"))
          {
                  NameOfPlace = googlePlaceJSON.getString("name");
          }
          if(!googlePlaceJSON.isNull("vicinity"))
          {
              NameOfPlace = googlePlaceJSON.getString("vicinity");
          }

          latitude =googlePlaceJSON.getJSONObject("geometry").getJSONObject("location").getString("lat");
          longitude =googlePlaceJSON.getJSONObject("geometry").getJSONObject("location").getString("lng");
          reference = googlePlaceJSON.getString("reference");

          googlePlaceMap.put("place_name" , NameOfPlace);
          googlePlaceMap.put("vicinity" , vicinity);
          googlePlaceMap.put("lat" , latitude);
          googlePlaceMap.put("lng" , longitude);
          googlePlaceMap.put("reference" , reference);


      }
      catch (JSONException e)
      {
          e.printStackTrace();
      }

     return googlePlaceMap;
    }


    private List<HashMap< String , String>> getAllNearbyPlaces(JSONArray jsonArray)
    {
        int counter = jsonArray.length();

        List<HashMap< String , String>> nearbybyPlacesList = new ArrayList<>();

        HashMap< String , String> nearbyPlaceMap = null;

        for(int i =0 ; i <counter ; i++)
        {
            try
            {
                nearbyPlaceMap = getSingleNearbyPlace((JSONObject) jsonArray.get(i));
                nearbybyPlacesList.add(nearbyPlaceMap);
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
        }


        return nearbybyPlacesList;
    }


    public List<HashMap< String , String>> parse (String JsonData)
    {
        JSONArray jsonArray =null;
        JSONObject jsonObject ;

        try
        {
            jsonObject = new JSONObject(JsonData);
            jsonArray = jsonObject.getJSONArray("results");

        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        return getAllNearbyPlaces(jsonArray);
    }
}

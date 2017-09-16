package maps;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Hendry on 9/16/2017.
 */

public class DataParser {

    private HashMap<String, String> getPlace(JSONObject googlePlaceJson){
        HashMap<String, String> googlePlacesMap = new HashMap<>();
        String placeName = "";
        String address = "";
        String latitude = "";
        String longitude = "";

        Log.d("DataParser","jsonobject = "+googlePlaceJson.toString());

//        try {
//            if (!googlePlaceJson.isNull("name")){
//                placeName = googlePlaceJson.getString("name");
//            }
//            if (!googlePlaceJson.isNull("vicinity")){
//                vicinity = googlePlaceJson.getString("vicinity");
//            }
//            latitude = googlePlaceJson.getJSONObject("geometry").getJSONObject("location").getString("lat");
//            longitude = googlePlaceJson.getJSONObject("geometry").getJSONObject("location").getString("lng");
//
//            reference = googlePlaceJson.getString("reference");
//
//            googlePlacesMap.put("place_name", placeName);
//            googlePlacesMap.put("vicinity", vicinity);
//            googlePlacesMap.put("lat", latitude);
//            googlePlacesMap.put("lng", longitude);
//            googlePlacesMap.put("reference", reference);
//
//        }
//        catch (JSONException e){
//            e.printStackTrace();
//        }
        try {
            if (!googlePlaceJson.isNull("nm_outlet")){
                placeName = googlePlaceJson.getString("nm_outlet");
                Log.d("nama outlet", placeName);
            }
            if (!googlePlaceJson.isNull("almt_outlet")){
                address = googlePlaceJson.getString("almt_outlet");
            }
            latitude = googlePlaceJson.getString("latitude");
            longitude = googlePlaceJson.getString("longitude");

            //reference = googlePlaceJson.getString("reference");

            googlePlacesMap.put("nm_outlet", placeName);
            googlePlacesMap.put("almt_outlet", address);
            googlePlacesMap.put("latitude", latitude);
            googlePlacesMap.put("longitude", longitude);
        }
        catch (JSONException e){
            e.printStackTrace();
        }

        return googlePlacesMap;
    }

    private List<HashMap<String, String>> getPlaces(JSONArray jsonArray)
    {
        int count = jsonArray.length();
        List<HashMap<String, String>> placelist = new ArrayList<>();
        HashMap<String, String> placeMap = null;

        for(int i = 0; i<count;i++)
        {
            try {
                placeMap = getPlace((JSONObject) jsonArray.get(i));
                placelist.add(placeMap);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return placelist;
    }

    public List<HashMap<String, String>> parse(String jsonData)
    {
        JSONArray jsonArray = null;
        JSONObject jsonObject, jsonObj;

        Log.d("json data", jsonData);

        try {
            jsonObject = new JSONObject(jsonData);
            //jsonArray = jsonObject.getJSONArray("results");
            jsonObj = jsonObject.getJSONObject("data");
            //Log.d("json data2", String.valueOf(jsonObj));
            jsonArray = jsonObj.getJSONArray("outlet");
            //Log.d("json array", String.valueOf(jsonArray));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return getPlaces(jsonArray);
    }
}

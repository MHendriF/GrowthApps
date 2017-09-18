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

        try {
            if (!googlePlaceJson.isNull("nm_outlet")){
                placeName = googlePlaceJson.getString("nm_outlet");
            }
            if (!googlePlaceJson.isNull("almt_outlet")){
                address = googlePlaceJson.getString("almt_outlet");
            }
            latitude = googlePlaceJson.getString("latitude");
            longitude = googlePlaceJson.getString("longitude");

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
            jsonObj = jsonObject.getJSONObject("data");
            jsonArray = jsonObj.getJSONArray("outlet");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return getPlaces(jsonArray);
    }
}

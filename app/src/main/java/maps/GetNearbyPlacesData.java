package maps;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Hendry on 9/16/2017.
 */

public class GetNearbyPlacesData extends AsyncTask<Object, String, String> {

    String googlePlacesData;
    GoogleMap mMap;
    String url;
    int PROXIMITY_RADIUS = 2000;

    private static Context context;
    public GetNearbyPlacesData(Context c) {
        context = c;
    }

    @Override
    protected String doInBackground(Object... objects) {
        mMap = (GoogleMap)objects[0];
        url = (String)objects[1];

        DownloadURL downloadURL = new DownloadURL();
        try {
            googlePlacesData = downloadURL.readUrl(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return googlePlacesData;
    }

    @Override
    protected void onPostExecute(String s){
        List<HashMap<String, String>> nearbyPlaceList;
        DataParser parser = new DataParser();
        nearbyPlaceList = parser.parse(s);
        Log.d("nearbyplacesdata","called parse method");
        showNearbyPlaces(nearbyPlaceList);

    }

    private void showNearbyPlaces(List<HashMap<String, String>> nearbyPlaceList)
    {
        if (nearbyPlaceList!=null) {
            for (int i = 0; i < nearbyPlaceList.size(); i++) {
                MarkerOptions markerOptions = new MarkerOptions();
                HashMap<String, String> googlePlace = nearbyPlaceList.get(i);

                String placeName = googlePlace.get("nm_outlet");
                String address = googlePlace.get("almt_outlet");
                double lat = Double.parseDouble(googlePlace.get("latitude"));
                double lng = Double.parseDouble(googlePlace.get("longitude"));

                LatLng latLng = new LatLng(lat, lng);
                markerOptions.position(latLng);
                markerOptions.title(placeName);
                markerOptions.snippet(address);
                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));

                mMap.addMarker(markerOptions);
                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                mMap.animateCamera(CameraUpdateFactory.zoomTo(10));
            }
            Log.d("Max outlet", String.valueOf(nearbyPlaceList.size()));
            if(nearbyPlaceList.size() == 0){
                Toast.makeText(context, "Tidak ada outlet disekitar anda", Toast.LENGTH_SHORT).show();
            }
            else if (nearbyPlaceList.size() > 0){
                Toast.makeText(context, "Menampilkan outlet disekitar anda dalam radius "+PROXIMITY_RADIUS+"  meter", Toast.LENGTH_SHORT).show();
            }
        }
        else {
            Log.d("NUll", "ada null lagi");
        }
    }
}

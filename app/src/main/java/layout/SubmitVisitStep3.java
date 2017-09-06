package layout;

import android.app.AlertDialog;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.trikarya.growth.GetLocationCallback;
import android.trikarya.growth.LocationGetter;
import android.trikarya.growth.R;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import java.text.DateFormatSymbols;
import java.util.Calendar;

import Master.DatabaseHandler;

/**
 *   PT Trikarya Teknologi
 */
public class SubmitVisitStep3 extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    Button getLokasi,submit;
    EditText lokasi,tgl;
    double lat,longi,latitude,longitude;
    int kd_visit,status_area;
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public static SubmitVisitStep3 newInstance(String param1, String param2) {
        SubmitVisitStep3 fragment = new SubmitVisitStep3();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public SubmitVisitStep3() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_submit_visit_step3, container, false);
        tgl = (EditText) view.findViewById(R.id.sv_et_date);
        Calendar calendar = Calendar.getInstance();
        tgl.setText(String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)) + " " +
                new DateFormatSymbols().getMonths()[calendar.get(Calendar.MONTH)] + " " + String.valueOf(calendar.get(Calendar.YEAR)));
        getLokasi = (Button) view.findViewById(R.id.sv_bt_location);
        lokasi = (EditText) view.findViewById(R.id.sv_et_location);
        submit = (Button) view.findViewById(R.id.sv_bt_submit);
        latitude = Double.parseDouble(getArguments().getString("latitude"));
        longitude = Double.parseDouble(getArguments().getString("longitude"));
        kd_visit = getArguments().getInt("kd_visit");
        status_area = getArguments().getInt("status_area");
        getLokasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LocationGetter.requestSingleUpdate(getActivity(), new GetLocationCallback() {
                    @Override
                    public void Done(Location location) {
                        lat = location.getLatitude();
                        longi = location.getLongitude();
                        lokasi.setText(String.valueOf(lat)+","+String.valueOf(longi));
                    }
                });
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double jarak = calculateDistance(latitude,lat,longitude,longi);
                if (jarak > (new DatabaseHandler(getActivity())).getUser().getToleransi() && status_area == 1) {
                    showError("your coordinate is not valid");
                }
                else {
                    SubmitVisitComplete submitVisitComplete = new SubmitVisitComplete();
                    Bundle bundle = new Bundle();
                    bundle.putInt("kd_visit",kd_visit);
                    submitVisitComplete.setArguments(bundle);
                    getFragmentManager().beginTransaction().replace(((ViewGroup) getView().getParent()).getId(), submitVisitComplete)
                            .addToBackStack(null)
                            .commit();
                }
            }
        });
        return view;
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }
    private void showError(String message) {
        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
        alert.setMessage(message);
        alert.setPositiveButton("Got It", null);
        alert.show();
    }
    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
    private double calculateDistance(double latitude1,double latitude2,double longitude1,double longitude2){
        double r = 6371;
        double latRadian = getRadian(latitude2-latitude1);
        double longRadian = getRadian(longitude2-longitude1);
        double a = Math.pow(Math.sin(latRadian/2),2) + Math.cos(getRadian(latitude1)) * Math.cos(getRadian(latitude2)) * Math.pow(Math.sin(longRadian/2),2);
        return r * 2 * Math.atan2(Math.sqrt(a),Math.sqrt(1-a))*1000;
    }
    private double getRadian(double degree){
        return degree*(Math.PI/180);
    }

    public interface OnFragmentInteractionListener {
        public void onFragmentInteraction(Uri uri);
    }

}

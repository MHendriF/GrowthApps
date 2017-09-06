package layout;

import android.app.AlertDialog;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.trikarya.growth.GetLocationCallback;
import android.trikarya.growth.LocationGetter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.trikarya.growth.R;
import android.widget.Button;
import android.widget.EditText;

import java.text.DateFormatSymbols;
import java.util.Calendar;

import Master.DatabaseHandler;
import Master.Logging;
import Master.Outlet;
import Master.ServerRequest;
import Master.StoreOutletCalback;
import Master.VisitPlanDb;

/**
 *   PT Trikarya Teknologi
 */
public class RegisterOutlet3 extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    Button getLocation,submit;
    EditText lokasi;
    String nama,alamat,foto="",latitude=null,longitude=null,telp_outlet;
    int kd_kota, kd_dist, kd_rank,kd_tipe,take_order;
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public RegisterOutlet3() {
    }

    public static RegisterOutlet3 newInstance(String param1, String param2) {
        RegisterOutlet3 fragment = new RegisterOutlet3();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
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
        nama = getArguments().getString("nm_outlet");
        alamat = getArguments().getString("almt_outlet");
        kd_kota = getArguments().getInt("kd_kota");
        kd_dist = getArguments().getInt("kd_dist");
        kd_rank = getArguments().getInt("rank");
        kd_tipe = getArguments().getInt("kd_tipe");
        telp_outlet = getArguments().getString("telp_outlet");
        take_order = getArguments().getInt("take_order");
        View view = inflater.inflate(R.layout.fragment_register_outlet3, container, false);
        lokasi = (EditText) view.findViewById(R.id.ro_et_location);
        getLocation = (Button) view.findViewById(R.id.ro_bt_location);
        submit = (Button) view.findViewById(R.id.ro_submit);
        getLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LocationGetter.requestSingleUpdate(getActivity(), new GetLocationCallback() {
                    @Override
                    public void Done(Location location) {
                        lokasi.setText(String.valueOf(location.getLatitude()) + " , " + String.valueOf(location.getLongitude()));
                        latitude = String.valueOf(location.getLatitude());
                        longitude = String.valueOf(location.getLongitude());
                    }
                });
            }
        });
        //submit data to server
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(latitude != null && longitude != null) {
                    final DatabaseHandler databaseHandler = new DatabaseHandler(getActivity());
                    String ranking;
                    switch (kd_rank) {
                        case 1:
                            ranking = "A";
                            break;
                        case 2:
                            ranking = "B";
                            break;
                        case 3:
                            ranking = "C";
                            break;
                        case 4:
                            ranking = "D";
                            break;
                        case 5:
                            ranking = "E";
                            break;
                        default:
                            ranking = "F";
                            break;
                    }
                    final Outlet outlet = new Outlet(0, kd_kota, databaseHandler.getUser().getKode(), kd_dist, nama, alamat, kd_tipe, ranking, telp_outlet, "YES", latitude, longitude);
                    ServerRequest serverRequest = new ServerRequest(getActivity());
                    serverRequest.uploadOutlet(take_order, outlet, getActivity(), foto, new StoreOutletCalback() {
                        @Override
                        public void Done(String Response, int kd_outlet, int kd_visit) {
                            if (Response.equals("success")) {
                                getFragmentManager().beginTransaction().
                                        replace(((ViewGroup) getView().getParent()).getId(), RegisterOutlet4.
                                                newInstance(null, null)).addToBackStack(null).commit();
                                outlet.setKode(kd_outlet);
                                Calendar calendar = Calendar.getInstance();
                                if(take_order == 1) {
                                    String tgl = String.valueOf(calendar.get(calendar.YEAR)) + "-" + String.valueOf(calendar.get(calendar.MONTH) + 1) + "-" + String.valueOf(calendar.get(calendar.DAY_OF_MONTH) + 1) +
                                            " " + String.valueOf(calendar.get(calendar.HOUR_OF_DAY)) + ":" + String.valueOf(calendar.get(calendar.MINUTE)) + ":" + String.valueOf(calendar.get(calendar.SECOND));
                                    VisitPlanDb visitPlanDb = new VisitPlanDb(kd_visit, kd_outlet,tgl,tgl
                                            ,1,0,"","","");
                                    databaseHandler.createVisitPlan(visitPlanDb);
                                    databaseHandler.createLog(new Logging(0, "Register an visit plan to " + nama, String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)) + " " +
                                            new DateFormatSymbols().getMonths()[calendar.get(Calendar.MONTH)] + " " + String.valueOf(calendar.get(Calendar.YEAR))));
                                }
                                databaseHandler.createOutlet(outlet);
                                databaseHandler.createLog(new Logging(0, "Register an outlet " + nama, String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)) + " " +
                                        new DateFormatSymbols().getMonths()[calendar.get(Calendar.MONTH)] + " " + String.valueOf(calendar.get(Calendar.YEAR))));
                            }
                            else
                                showError(Response);
                        }
                    });
                }
            }
        });
        return view;
    }
    private void showError(String message) {
        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
        alert.setMessage(message);
        alert.setPositiveButton("Got It", null);
        alert.show();
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}

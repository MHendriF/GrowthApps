package layout;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.trikarya.growth.R;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import Master.City;
import Master.DatabaseHandler;
import Master.Tipe;


/**
 *   PT Trikarya Teknologi
 */
public class SubmitVisitStep2 extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    Button next;
    String rank,telpon,nama_sf;
    int kd_kota,kd_tipe;
    City city;
    Tipe tipe;
    DatabaseHandler databaseHandler;
    EditText nama_kota,tipe_outlet,ranking_outlet,telp,sf;
    FragmentManager fragmentManager;
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public static SubmitVisitStep2 newInstance(String param1, String param2) {
        SubmitVisitStep2 fragment = new SubmitVisitStep2();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public SubmitVisitStep2() {
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
        final View view = inflater.inflate(R.layout.fragment_submit_visit_step2, container, false);
        databaseHandler = new DatabaseHandler(getActivity());
        fragmentManager = getFragmentManager();
        rank = getArguments().getString("rank");
        telpon = getArguments().getString("telp_outlet");
        nama_sf = getArguments().getString("nama_sf");
        kd_kota = getArguments().getInt("kd_kota");
        kd_tipe = getArguments().getInt("kd_tipe");
        city = databaseHandler.getCity(kd_kota);
        tipe = databaseHandler.getTipe(kd_tipe);
        nama_kota = (EditText) view.findViewById(R.id.sv_city);
        nama_kota.setText(city.getNama());
        tipe_outlet = (EditText) view.findViewById(R.id.sv_outlettype);
        tipe_outlet.setText(tipe.getNm_tipe());
        ranking_outlet = (EditText) view.findViewById(R.id.sv_outletrank);
        ranking_outlet.setText(rank);
        telp = (EditText) view.findViewById(R.id.sv_outletphone);
        telp.setText(telpon);
        sf = (EditText) view.findViewById(R.id.sv_sf);
        sf.setText(nama_sf);
        next = (Button)view.findViewById(R.id.sv_s2_next);
        final SubmitVisitStep3 submitVisitStep3 = new SubmitVisitStep3();
        Bundle bundle = new Bundle();
        bundle.putString("latitude", getArguments().getString("latitude"));
        bundle.putString("longitude", getArguments().getString("longitude"));
        bundle.putInt("kd_visit", getArguments().getInt("kd_visit"));
        bundle.putInt("status_area", getArguments().getInt("status_area"));
        submitVisitStep3.setArguments(bundle);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentManager.beginTransaction().replace(((ViewGroup) getView().getParent()).getId(), submitVisitStep3)
                        .addToBackStack(null)
                        .commit();
            }
        });
        return view;
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
        public void onFragmentInteraction(Uri uri);
    }

}

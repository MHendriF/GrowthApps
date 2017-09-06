package layout;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.trikarya.growth.Dashboard;
import android.trikarya.growth.OutletSearch;
import android.trikarya.growth.R;
import android.trikarya.growth.RegisterVisitPlan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import Master.DatabaseHandler;
import Master.Distributor;
import Master.Outlet;
import Master.VisitPlanDb;


/**
 *   PT Trikarya Teknologi
 */
public class SubmitVisitStep1 extends Fragment {
    Button next;
    TextView outlet;
    EditText distributor,alamat;
    FragmentManager fragmentManager;
    DatabaseHandler databaseHandler;
    Outlet selectedOutlet;
    Distributor dist;
    List<VisitPlanDb> visitPlanTerpakai,visitPlanDbs;
    int kd_visit=0;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public static SubmitVisitStep1 newInstance(String param1, String param2) {
        SubmitVisitStep1 fragment = new SubmitVisitStep1();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public SubmitVisitStep1() {
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if(resultCode == Activity.RESULT_OK){
                kd_visit = data.getIntExtra("kd_visit",0);
                if(kd_visit != 0) {
                    selectedOutlet = databaseHandler.getOutlet(databaseHandler.getVisitPlan(kd_visit).getKd_outlet());
                    outlet.setText(selectedOutlet.getNama());
                    int kd_dist = selectedOutlet.getKode_distributor();
                    dist = databaseHandler.getDistributor(kd_dist);
                    if (dist != null)
                        distributor.setText(dist.getNm_dist());
                    else
                        distributor.setText("Tidak ada distributor di area anda. Silahkan kontak atasan anda");
                    alamat.setText(selectedOutlet.getAlamat());
                }
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                kd_visit = 0;
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_submit_visit_step1, container, false);
        fragmentManager = getFragmentManager();
        outlet = (TextView) view.findViewById(R.id.sv_outletname);
        outlet.setClickable(true);
        databaseHandler = new DatabaseHandler(getActivity());
        visitPlanDbs = databaseHandler.getAllVisitPlan();
        visitPlanTerpakai = new ArrayList<VisitPlanDb>();
        for (int i = 0; i < visitPlanDbs.size(); i++) {
            if (visitPlanDbs.get(i).getStatus_visit() == 0 && compare(visitPlanDbs.get(i).getDate_visit())) {
                visitPlanTerpakai.add(visitPlanDbs.get(i));
            }
        }
        if(visitPlanTerpakai.size() == 0){
            new AlertDialog.Builder(getActivity())
                    .setTitle("Tidak Rencana Kunjungan")
                    .setMessage("Anda mau membuat rencana kunjungan?")
                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            startActivity(new Intent(getActivity(), Dashboard.class));
                            getActivity().finish();
                        }
                    })
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface arg0, int arg1) {
                            startActivity(new Intent(getActivity(), RegisterVisitPlan.class));
                            getActivity().finish();
                        }
                    }).create().show();
        }
        else {
            outlet.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), OutletSearch.class);
                    intent.putExtra("tipe","visitplan");
                    startActivityForResult(intent,1);
                }
            });
            distributor = (EditText) view.findViewById(R.id.sv_distributor);
            alamat = (EditText) view.findViewById(R.id.sv_address);
            next = (Button) view.findViewById(R.id.sv_s1_next);
            next.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (kd_visit != 0) {
                        Bundle bundle = new Bundle();
                        bundle.putString("rank", selectedOutlet.getRank());
                        bundle.putString("telp_outlet", selectedOutlet.getTelpon());
                        bundle.putInt("kd_kota", selectedOutlet.getKode_kota());
                        bundle.putInt("status_area", selectedOutlet.getStatus_area());
                        bundle.putInt("kd_tipe", selectedOutlet.getTipe());
                        bundle.putString("nama_sf", databaseHandler.getUser().getNama());
                        bundle.putString("latitude", selectedOutlet.getLatitude());
                        bundle.putString("longitude", selectedOutlet.getLongitude());
                        bundle.putInt("kd_visit", kd_visit);
                        SubmitVisitStep2 submitVisitStep2 = new SubmitVisitStep2();
                        submitVisitStep2.setArguments(bundle);
                        fragmentManager.beginTransaction().replace(((ViewGroup) getView().getParent()).getId(), submitVisitStep2)
                                .addToBackStack(null)
                                .commit();
                    }
                    else
                        Toast.makeText(getContext(),"You must select outlet",Toast.LENGTH_LONG).show();
                }
            });
        }
        return view;
    }

    private boolean compare(String date_visit) {
        try {
            String tanggal = date_visit.split(" ")[0];
            String[] sp_tanggal = tanggal.split("-");
            Calendar now = Calendar.getInstance();
            Calendar visit = Calendar.getInstance();
            visit.set(Integer.parseInt(sp_tanggal[0]), Integer.parseInt(sp_tanggal[1]) - 1, Integer.parseInt(sp_tanggal[2]));
            if (visit.getTimeInMillis() >= now.getTimeInMillis())
                return true;
            return false;
        }
        catch (Exception e){
            return false;
        }
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

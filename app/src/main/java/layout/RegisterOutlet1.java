package layout;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.trikarya.growth.R;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.List;

import Master.City;
import Master.DatabaseHandler;
import Master.Distributor;
/**
 *   PT Trikarya Teknologi
 */
public class RegisterOutlet1 extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    Button next;
    Spinner spinnerDistributor,autoCity;
    EditText alamat,nama;
    int kd_kota,kd_dist,take_order = 0;
    DatabaseHandler databaseHandler;
    private String mParam1;
    private String mParam2;
    private OnFragmentInteractionListener mListener;

    public RegisterOutlet1() {
    }

    public static RegisterOutlet1 newInstance(String param1, String param2) {
        RegisterOutlet1 fragment = new RegisterOutlet1();
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
        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
        alert.setMessage("Apakah langsung take order?");
        alert.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                take_order = 1;
            }
        });
        alert.setNegativeButton("Tidak",null);
        alert.show();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final FragmentManager fragmentManager = getFragmentManager();
        View view = inflater.inflate(R.layout.fragment_register_outlet1, container, false);
        autoCity = (Spinner) view.findViewById(R.id.ro_city);
        databaseHandler = new DatabaseHandler(getActivity());
        List<City> cities = databaseHandler.getAllCity();
        ArrayAdapter arrayAdapter = new ArrayAdapter(getActivity(),R.layout.custom_spinner,cities);
        autoCity.setAdapter(arrayAdapter);
        autoCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                City city = (City) parent.getAdapter().getItem(position);
                kd_kota = city.getKode();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                kd_kota = 0;
            }
        });
        spinnerDistributor = (Spinner) view.findViewById(R.id.ro_distributor);
        List<Distributor> distributors = databaseHandler.getAllDistributor();
        ArrayAdapter adapter = new ArrayAdapter(getActivity(),R.layout.custom_spinner,distributors);
        spinnerDistributor.setAdapter(adapter);
        spinnerDistributor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Distributor distributor = (Distributor) spinnerDistributor.getSelectedItem();
                kd_dist = distributor.getId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                kd_dist = 0;
            }
        });
        alamat = (EditText) view.findViewById(R.id.ro_address);
        nama = (EditText) view.findViewById(R.id.ro_outletname);
        next = (Button) view.findViewById(R.id.ro_s1_next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isEmpty(nama) && !isEmpty(alamat) && kd_kota != 0) {
                    Bundle bundle = new Bundle();
                    bundle.putString("nm_outlet", nama.getText().toString());
                    bundle.putString("almt_outlet", alamat.getText().toString());
                    bundle.putInt("kd_kota", kd_kota);
                    bundle.putInt("take_order", take_order);
                    bundle.putInt("kd_dist", kd_dist);
                    RegisterOutlet2 registerOutlet2 = new RegisterOutlet2();
                    registerOutlet2.setArguments(bundle);
                    fragmentManager.beginTransaction().
                            replace(((ViewGroup) getView().getParent()).getId(), registerOutlet2).
                            addToBackStack(null).commit();
                }
                else {
                    if(kd_kota == 0)
                        Toast.makeText(getActivity(), "Kamu harus memilih data kota yang sudah tersedia",
                                Toast.LENGTH_LONG).show();
                    else
                        Toast.makeText(getActivity(), "Kamu harus mengisi semua isian",
                                Toast.LENGTH_LONG).show();
                }
            }
        });
        return view;
    }
    private boolean isEmpty(EditText etText) {
        return etText.getText().toString().trim().length() == 0;
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

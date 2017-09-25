package layout;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Base64;
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


import java.io.ByteArrayOutputStream;
import java.util.List;

import master.DatabaseHandler;
import master.Tipe;

/**
 *   PT Trikarya Teknologi
 */
public class RegisterOutlet2 extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    EditText outletPhone;
    Spinner outletType,rank;
    String[] isi = new String[]{"Rank A","Rank B","Rank C","Rank D","Rank E","Rank F"};
    Button next;
    Bitmap bitmap = null;
    String nama,alamat,foto,mCurrentPhotoPath;
    int kd_kota, kd_dist, kd_rank,kd_tipe,take_order;
    static final int REQUEST_TAKE_PHOTO = 1;
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public RegisterOutlet2() {
    }

    public static RegisterOutlet2 newInstance(String param1, String param2) {
        RegisterOutlet2 fragment = new RegisterOutlet2();
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
        DatabaseHandler databaseHandler = new DatabaseHandler(getActivity());
        nama = getArguments().getString("nm_outlet");
        alamat = getArguments().getString("almt_outlet");
        kd_kota = getArguments().getInt("kd_kota");
        kd_dist = getArguments().getInt("kd_dist");
        take_order = getArguments().getInt("take_order");
        View view = inflater.inflate(R.layout.fragment_register_outlet2, container, false);
        outletPhone = (EditText) view.findViewById(R.id.ro_outletphone);
        rank = (Spinner) view.findViewById(R.id.ro_rank);
        ArrayAdapter arrayAdapter = new ArrayAdapter(getActivity(),R.layout.custom_spinner,isi);
        rank.setAdapter(arrayAdapter);
        rank.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                kd_rank = position+1;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        outletType = (Spinner) view.findViewById(R.id.ro_outlettype);
        List<Tipe> tipes = databaseHandler.getAllTipe();
        ArrayAdapter adapter = new ArrayAdapter(getActivity(),R.layout.custom_spinner,tipes);
        outletType.setAdapter(adapter);
        outletType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                kd_tipe = ((Tipe) outletType.getSelectedItem()).getId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        foto = "";
        next = (Button) view.findViewById(R.id.ro_s2_next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isEmpty(outletPhone)) {
                    Bundle bundle = new Bundle();
                    bundle.putString("nm_outlet", nama);
                    bundle.putString("almt_outlet", alamat);
                    bundle.putInt("kd_kota", kd_kota);
                    bundle.putInt("kd_dist", kd_dist);
                    bundle.putInt("rank", kd_rank);
                    bundle.putInt("kd_tipe", kd_tipe);
                    bundle.putInt("take_order", take_order);
                    bundle.putString("telp_outlet", outletPhone.getText().toString());
                    RegisterOutlet3 registerOutlet3 = new RegisterOutlet3();
                    registerOutlet3.setArguments(bundle);
                    getFragmentManager().beginTransaction().
                            replace(((ViewGroup) getView().getParent()).getId(), registerOutlet3).
                            addToBackStack(null).commit();
                }
                else
                    Toast.makeText(getActivity(), "You must fill all field to proceed",
                            Toast.LENGTH_LONG).show();
            }
        });
        return view;
    }
    private boolean isEmpty(EditText etText) {
        return etText.getText().toString().trim().length() == 0;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == Activity.RESULT_OK) {
            Bundle extras = data.getExtras();
            bitmap = (Bitmap) extras.get("data");
        }

    }
    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 50, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
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

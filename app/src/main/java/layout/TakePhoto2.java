package layout;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.trikarya.growth.Dashboard;
import android.trikarya.growth.R;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.text.DateFormatSymbols;
import java.util.Calendar;

import master.DatabaseHandler;
import master.Logging;
import master.PhotoActivity;
import master.ServerRequest;
import master.StoreOutletCalback;


/**
 *   PT Trikarya Teknologi
 */
public class TakePhoto2 extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    private int id,kd_user,kd_outlet,kd_jenis,kd_photo;
    private String nama,jenis,tgl_take,alamat,foto;
    private Button submit,save;
    private EditText note;
    private PhotoActivity photoActivity;
    private OnFragmentInteractionListener mListener;
    DatabaseHandler databaseHandler;

    public TakePhoto2() {
    }

    public static TakePhoto2 newInstance(String param1, String param2) {
        TakePhoto2 fragment = new TakePhoto2();
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
        View view = inflater.inflate(R.layout.fragment_take_photo2, container, false);
        kd_photo = getArguments().getInt("kd_photo");
        kd_jenis = getArguments().getInt("id_tipe");
        kd_outlet = getArguments().getInt("kd_outlet");
        nama = getArguments().getString("title");
        tgl_take = getArguments().getString("tgl_take");
        foto = getArguments().getString("foto");
        note = (EditText) view.findViewById(R.id.tp_note);
        if(!getArguments().getString("note").equals(""))
            note.setText(getArguments().getString("note"));
        save = (Button) view.findViewById(R.id.tp_save);
        save.setOnClickListener(onClickListener);
        submit = (Button) view.findViewById(R.id.tp_submit);
        submit.setOnClickListener(onClickListener);
        databaseHandler = new DatabaseHandler(getActivity());
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
    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.tp_submit:
                    photoActivity = new PhotoActivity(kd_photo, databaseHandler.getUser().getKode(), kd_outlet, 0, kd_jenis, nama,
                            tgl_take, databaseHandler.getOutlet(kd_outlet).getAlamat(), note.getText().toString(),tgl_take, foto);
                    ServerRequest serverRequest = new ServerRequest(getActivity());
                    serverRequest.uploadFoto(getActivity(), photoActivity, new StoreOutletCalback() {
                        @Override
                        public void Done(String Response, int kode_outlet, int kd_visit) {
                            if (Response.equals("success")) {
                                databaseHandler.deletePhoto(photoActivity.getId());
                                Calendar calendar = Calendar.getInstance();
                                databaseHandler.createLog(new Logging(0, "Take photo of " + databaseHandler.getOutlet(kd_outlet).getNama(), String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)) + " " +
                                        new DateFormatSymbols().getMonths()[calendar.get(Calendar.MONTH)] + " " + String.valueOf(calendar.get(Calendar.YEAR))));
                                Toast.makeText(getActivity(), "Record has been uploaded",
                                        Toast.LENGTH_LONG).show();
                                startActivity(new Intent(getActivity(), Dashboard.class));
                                getActivity().finish();
                            }
                        }
                    });
                    break;
                case R.id.tp_save:
                    photoActivity = new PhotoActivity(kd_photo, databaseHandler.getUser().getKode(), kd_outlet, 0, kd_jenis, nama,
                            tgl_take, databaseHandler.getOutlet(kd_outlet).getAlamat(), note.getText().toString(),"", foto);
                    if(kd_photo != 0)
                        databaseHandler.updatePhoto(photoActivity);
                    else
                        databaseHandler.createPhoto(photoActivity);
                    Toast.makeText(getActivity(), "Record has been saved",
                            Toast.LENGTH_LONG).show();
                    startActivity(new Intent(getActivity(), Dashboard.class));
                    getActivity().finish();
                    break;
                default:
                    break;
            }
        }
    };
    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}

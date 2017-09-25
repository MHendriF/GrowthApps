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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.DateFormatSymbols;
import java.util.Calendar;

import master.DatabaseHandler;
import master.Logging;
import master.PhotoActivity;
import master.ServerRequest;
import master.StoreOutletCalback;
import master.TipePhoto;


/**
 *   PT Trikarya Teknologi
 */
public class Competitor2 extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    EditText title,note;
    Button submit,save;
    String alamat,foto,keterangan,tgl_take,nama;
    int kd_kota,kd_com,kd_foto;
    Spinner tipe;
    PhotoActivity photoActivity;
    DatabaseHandler databaseHandler;
    private OnFragmentInteractionListener mListener;

    public Competitor2() {
    }

    public static Competitor2 newInstance(String param1, String param2) {
        Competitor2 fragment = new Competitor2();
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
        View view = inflater.inflate(R.layout.fragment_competitor2, container, false);
        alamat = getArguments().getString("alamat");
        foto = getArguments().getString("foto");
        keterangan = getArguments().getString("note");
        nama = getArguments().getString("title");
        tgl_take = getArguments().getString("tgl_take");
        kd_kota = getArguments().getInt("kd_kota");
        kd_com = getArguments().getInt("kd_com");
        kd_foto = getArguments().getInt("kd_foto");

        databaseHandler = new DatabaseHandler(getActivity());
        tipe = (Spinner) view.findViewById(R.id.com_tipe);
        ArrayAdapter arrayAdapter1 = new ArrayAdapter(getActivity(),R.layout.custom_spinner,databaseHandler.getAllTipePhoto());
        tipe.setAdapter(arrayAdapter1);
        note = (EditText) view.findViewById(R.id.com_note);
        note.setText(keterangan);
        title = (EditText) view.findViewById(R.id.com_phototitle);
        title.setText(nama);
        save = (Button) view.findViewById(R.id.com_save);
        save.setOnClickListener(onClickListener);
        submit = (Button) view.findViewById(R.id.com_submit);
        submit.setOnClickListener(onClickListener);
        return view;
    }
    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.com_submit:
                    if(!title.getText().equals("")) {
                        photoActivity = new PhotoActivity(kd_foto, databaseHandler.getUser().getKode(), 0, kd_com,((TipePhoto) tipe.getSelectedItem()).getId(), title.getText().toString(),
                                tgl_take, alamat, note.getText().toString(), tgl_take, foto);
                        ServerRequest serverRequest = new ServerRequest(getActivity());
                        serverRequest.uploadFoto(getActivity(), photoActivity, new StoreOutletCalback() {
                            @Override
                            public void Done(String Response, int kd_outlet, int kd_visit) {
                                if (Response.equals("success")) {
                                    databaseHandler.deletePhoto(photoActivity.getId());
                                    Calendar calendar = Calendar.getInstance();
                                    databaseHandler.createLog(new Logging(0, "Take photo of " + databaseHandler.getCompetitor(kd_com).getNm_competitor(), String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)) + " " +
                                            new DateFormatSymbols().getMonths()[calendar.get(Calendar.MONTH)] + " " + String.valueOf(calendar.get(Calendar.YEAR))));
                                    Toast.makeText(getActivity(), "Record has been uploaded",
                                            Toast.LENGTH_LONG).show();
                                    startActivity(new Intent(getActivity(), Dashboard.class));
                                    getActivity().finish();
                                }
                            }
                        });
                    }
                    else
                        Toast.makeText(getActivity(), "Field photo title must be filled",
                                Toast.LENGTH_LONG).show();
                    break;
                case R.id.com_save:
                    if(!title.getText().equals("")) {
                        photoActivity = new PhotoActivity(kd_foto, databaseHandler.getUser().getKode(), 0, kd_com, ((TipePhoto) tipe.getSelectedItem()).getId(), title.getText().toString(),
                                tgl_take, alamat, note.getText().toString(), "", foto);
                        databaseHandler.createPhoto(photoActivity);
                        Toast.makeText(getActivity(), "Record has been saved",
                                Toast.LENGTH_LONG).show();
                        startActivity(new Intent(getActivity(), Dashboard.class));
                        getActivity().finish();
                    }
                    else
                        Toast.makeText(getActivity(), "Field photo title must be filled",
                                Toast.LENGTH_LONG).show();
                    break;
                default:
                    break;
            }
        }
    };
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

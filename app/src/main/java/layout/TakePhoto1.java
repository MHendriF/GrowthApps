package layout;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.trikarya.growth.R;
import android.trikarya.growth.TakePhoto;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import master.DatabaseHandler;
import master.Outlet;
import master.TipePhoto;

/**
 *   PT Trikarya Teknologi
 */
public class TakePhoto1 extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2,note = "";
    private ImageView take_photo;
    private EditText date,title;
    private Spinner outlet,tipe;
    private Button next;
    private Bitmap bitmap;
    private DatabaseHandler databaseHandler;
    static final int REQUEST_TAKE_PHOTO = 1;
    float x1,x2;
    String mCurrentPhotoPath,foto;
    File photoFile = null;
    Calendar calendar;
    int kd_photo = 0;
    private OnFragmentInteractionListener mListener;

    public TakePhoto1() {
    }

    public static TakePhoto1 newInstance(String param1, String param2) {
        TakePhoto1 fragment = new TakePhoto1();
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
        View view = inflater.inflate(R.layout.fragment_take_photo1, container, false);
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getActionMasked()) {
                    case MotionEvent.ACTION_DOWN:
                        x1 = event.getX();
                        break;
                    case MotionEvent.ACTION_UP:
                        x2 = event.getX();
                        if (x2 > x1) {

                        } else {
                            ((TakePhoto) getActivity()).loadFragment(2);
                        }
                        break;
                }
                return true;
            }
        });
        databaseHandler = new DatabaseHandler(getActivity());
        List<Outlet> outlets = databaseHandler.getAllOutlet();
        take_photo = (ImageView) view.findViewById(R.id.tp_takephoto);
        take_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                    try {
                        photoFile = createImageFile();
                    } catch (IOException ex) {
                        String err = ex.toString();
                    }
                    if (photoFile != null) {
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                                Uri.fromFile(photoFile));
                        startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
                    }

                }
            }
        });
        outlet = (Spinner) view.findViewById(R.id.tp_outletname);
        ArrayAdapter arrayAdapter = new ArrayAdapter(getActivity(),R.layout.custom_spinner,outlets);
        outlet.setAdapter(arrayAdapter);
        final List<TipePhoto> tipePhotos = databaseHandler.getAllTipePhoto();
        tipe = (Spinner) view.findViewById(R.id.tp_tipe);
        ArrayAdapter arrayAdapter1 = new ArrayAdapter(getActivity(),R.layout.custom_spinner,tipePhotos);
        tipe.setAdapter(arrayAdapter1);
        date = (EditText) view.findViewById(R.id.tp_date);
        calendar = Calendar.getInstance();
        date.setText(String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)) + " " +
                new DateFormatSymbols().getMonths()[calendar.get(Calendar.MONTH)] + " " + String.valueOf(calendar.get(Calendar.YEAR)));
        title = (EditText) view.findViewById(R.id.tp_phototitle);
        next = (Button) view.findViewById(R.id.tp_s1_next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isEmpty(title) && !isEmpty(date) && bitmap != null) {
                    Bundle bundle = new Bundle();
                    bundle.putInt("id_tipe", ((TipePhoto) tipe.getSelectedItem()).getId());
                    bundle.putString("title", title.getText().toString());
                    bundle.putString("foto", foto);
                    bundle.putInt("kd_outlet", ((Outlet) outlet.getSelectedItem()).getKode());
                    bundle.putInt("kd_photo", kd_photo);
                    bundle.putString("tgl_take", String.valueOf(calendar.get(calendar.YEAR))+"-"+String.valueOf(calendar.get(calendar.MONTH)+1)+"-"+String.valueOf(calendar.get(calendar.DAY_OF_MONTH))+
                            " "+String.valueOf(calendar.get(calendar.HOUR_OF_DAY))+":"+String.valueOf(calendar.get(calendar.MINUTE))+":"+String.valueOf(calendar.get(calendar.SECOND)));
                    bundle.putString("note", note);
                    TakePhoto2 takePhoto2 = new TakePhoto2();
                    takePhoto2.setArguments(bundle);
                    getFragmentManager().beginTransaction().
                            replace(((ViewGroup) getView().getParent()).getId(), takePhoto2).
                            addToBackStack(null).commit();
                }
                else
                    Toast.makeText(getActivity(), "You must fill all field to proceed",
                            Toast.LENGTH_LONG).show();
            }
        });
        return view;
    }
    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,
                ".jpg",
                storageDir
        );

        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        foto = getStringImage();
    }
    public String getStringImage(){
        Bitmap tmp = BitmapFactory.decodeFile(mCurrentPhotoPath);
        int height = 900,width = 600;
        if(tmp.getWidth() > tmp.getHeight()){
            width = 900;
            height = 600;
        }
        bitmap = getResizedBitmap(tmp,width,height);
        take_photo.setImageBitmap(bitmap);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }
    public Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap resizedBitmap = Bitmap.createBitmap(
                bm, 0, 0, width, height, matrix, false);
        bm.recycle();
        return resizedBitmap;
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

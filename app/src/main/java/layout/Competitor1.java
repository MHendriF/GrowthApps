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
import android.trikarya.growth.TakePhoto;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import android.trikarya.growth.R;
import android.widget.AdapterView;
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

import Master.City;
import Master.Competitor;
import Master.DatabaseHandler;
import Master.PhotoActivity;

/**
 *   PT Trikarya Teknologi
 */
public class Competitor1 extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    float x1,x2;
    ImageView take_photo;
    ArrayAdapter arrayAdapter,arrayAdapter1;
    EditText date,address;
    Spinner kota,brand;
    Button lanjut;
    static final int REQUEST_TAKE_PHOTO = 1;
    DatabaseHandler databaseHandler;
    Bitmap bitmap;
    PhotoActivity photoActivity;
    int kd_com,kd_foto;
    String note,title;
    String mCurrentPhotoPath,foto;
    File photoFile = null;

    Calendar calendar;

    private OnFragmentInteractionListener mListener;

    public Competitor1() {
    }

    public static Competitor1 newInstance(String param1, String param2) {
        Competitor1 fragment = new Competitor1();
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
    private Bitmap decodeImage(String enImage)
    {
        byte[] decodedString = Base64.decode(enImage, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_competitor1, container, false);
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getActionMasked())
                {
                    case MotionEvent.ACTION_DOWN:
                        x1 = event.getX();
                        break;
                    case MotionEvent.ACTION_UP:
                        x2 = event.getX();
                        if(x2>x1) {
                            ((TakePhoto)getActivity()).loadFragment(1);
                        }
                        else{

                        }
                        break;
                }
                return true;
            }
        });
        take_photo = (ImageView) view.findViewById(R.id.com_take_photo);
        take_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                    try {
                        photoFile = createImageFile();
                    } catch (IOException ex) {
                        String err = ex.toString();
                        int x = 0;
                    }
                    if (photoFile != null) {
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                                Uri.fromFile(photoFile));
                        startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
                    }

                }
            }
        });
        date = (EditText) view.findViewById(R.id.com_date);
        calendar = Calendar.getInstance();
        date.setText(String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)) + " " +
                new DateFormatSymbols().getMonths()[calendar.get(Calendar.MONTH)] + " " + String.valueOf(calendar.get(Calendar.YEAR)));
        address = (EditText) view.findViewById(R.id.com_address);
        brand = (Spinner) view.findViewById(R.id.com_brand);
        databaseHandler = new DatabaseHandler(getActivity());
        arrayAdapter1 = new ArrayAdapter(getActivity(),R.layout.custom_spinner,databaseHandler.getAllCompetitor());
        brand.setAdapter(arrayAdapter1);
        brand.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Competitor competitor = (Competitor) parent.getAdapter().getItem(position);
                photoActivity = databaseHandler.getPhotoCom(competitor.getId());
                if (photoActivity != null) {
                    address.setText(photoActivity.getAlamat());
                    kota.setSelection(arrayAdapter.getPosition(databaseHandler.getCity(competitor.getKd_kota()).getNama()));
                    kd_foto = photoActivity.getId();
                    note = photoActivity.getKeterangan();
                    title = photoActivity.getNama();
                    bitmap = decodeImage(photoActivity.getFoto());
                    take_photo.setImageBitmap(bitmap);
                }
                kd_com = competitor.getId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                photoActivity = null;
                kd_com = 0;
                note = "";
                title = "";
                kd_foto = 0;
            }
        });
        kota = (Spinner) view.findViewById(R.id.com_city);
        arrayAdapter = new ArrayAdapter(getActivity(),R.layout.custom_spinner,databaseHandler.getAllCity());
        kota.setAdapter(arrayAdapter);
        lanjut = (Button) view.findViewById(R.id.com_next);
        lanjut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isEmpty(address) && kd_com != 0 && !isEmpty(date) && bitmap != null) {
                    Bundle bundle = new Bundle();
                    bundle.putString("alamat", address.getText().toString());
                    bundle.putString("foto", foto);
                    bundle.putInt("kd_com", kd_com);
                    bundle.putInt("kd_foto", kd_foto);
                    bundle.putString("title", title);
                    bundle.putString("note", note);
                    bundle.putString("tgl_take", String.valueOf(calendar.get(calendar.YEAR))+"-"+String.valueOf(calendar.get(calendar.MONTH)+1)+"-"+String.valueOf(calendar.get(calendar.DAY_OF_MONTH))+
                            " "+String.valueOf(calendar.get(calendar.HOUR_OF_DAY))+":"+String.valueOf(calendar.get(calendar.MINUTE))+":"+String.valueOf(calendar.get(calendar.SECOND)));
                    bundle.putInt("kd_kota", ((City)kota.getSelectedItem()).getKode());
                    Competitor2 competitor2 = new Competitor2();
                    competitor2.setArguments(bundle);
                    getFragmentManager().beginTransaction().
                            replace(((ViewGroup) getView().getParent()).getId(), competitor2).
                            addToBackStack(null).commit();
                } else
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

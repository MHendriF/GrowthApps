package android.trikarya.growth;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormatSymbols;
import java.util.Calendar;
import java.util.List;

import Master.DatabaseHandler;
import Master.Logging;
import Master.PhotoActivity;
import Master.ServerRequest;
import Master.StoreOutletCalback;
/**
 *   PT Trikarya Teknologi
 */
public class Draft extends AppCompatActivity {
    DatabaseHandler databaseHandler;
    ArrayAdapter<PhotoActivity> adapter;
    ListView listView;
    List<PhotoActivity> fromDatabase;
    Calendar calendar = Calendar.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(android.trikarya.growth.R.layout.activity_draft);
        listView = (ListView) findViewById(android.trikarya.growth.R.id.draft_list);
        databaseHandler = new DatabaseHandler(this);
        instanceView();
    }

    @Override
    public void onBackPressed() {
        finish();
        startActivity(new Intent(this, Dashboard.class));
    }

    public void instanceView()
    {
        fromDatabase = databaseHandler.getAllPhoto();
        populateList();
    }
    public void populateList()
    {
        if(adapter == null)
            adapter = new PhotoListAdapter();
        listView.setAdapter(adapter);
    }
    private class PhotoListAdapter extends ArrayAdapter<PhotoActivity> {
        public PhotoListAdapter(){
            super(Draft.this, android.trikarya.growth.R.layout.draft_item,fromDatabase);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(convertView == null)
                convertView = getLayoutInflater().inflate(android.trikarya.growth.R.layout.draft_item,parent,false);
            final PhotoActivity currentPhoto = fromDatabase.get(position);
            LinearLayout draft = (LinearLayout) convertView.findViewById(android.trikarya.growth.R.id.draft_group);
            draft.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showAction(currentPhoto.getId(),"upload");
                }
            });
            ImageView delete = (ImageView) convertView.findViewById(android.trikarya.growth.R.id.dr_delete);
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showAction(currentPhoto.getId(),"delete");
                }
            });
            TextView nama = (TextView) convertView.findViewById(android.trikarya.growth.R.id.dr_nama);
            nama.setText(currentPhoto.getNama());
            TextView namaOutOrCom = (TextView) convertView.findViewById(android.trikarya.growth.R.id.dr_nama_OutOrCom);
            if (currentPhoto.getKd_outlet()!=0)
                namaOutOrCom.setText(databaseHandler.getOutlet(currentPhoto.getKd_outlet()).getNama());
            else if(currentPhoto.getKd_kompetitor()!=0)
                namaOutOrCom.setText(databaseHandler.getCompetitor(currentPhoto.getKd_kompetitor()).getNm_competitor());
            TextView tgl = (TextView) convertView.findViewById(android.trikarya.growth.R.id.dr_tgl);
            tgl.setText(currentPhoto.getTgl_take());
            ImageView imageView = (ImageView) convertView.findViewById(android.trikarya.growth.R.id.dr_foto);
            imageView.setImageBitmap(decodeImage(currentPhoto.getFoto()));
            return convertView;
        }
    }
    private Bitmap decodeImage(String enImage)
    {
        byte[] decodedString = Base64.decode(enImage, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
    }
    private void showAction(final int kd_photo,String status) {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        if(status.equals("upload")) {
            alert.setMessage("Upload foto ini?");
            alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    final PhotoActivity photoActivity = databaseHandler.getPhoto(kd_photo);
                    photoActivity.setTgl_upload(String.valueOf(calendar.get(calendar.YEAR))+"-"+String.valueOf(calendar.get(calendar.MONTH)+1)+"-"+String.valueOf(calendar.get(calendar.DAY_OF_MONTH)+1)+
                            " "+String.valueOf(calendar.get(calendar.HOUR_OF_DAY))+":"+String.valueOf(calendar.get(calendar.MINUTE))+":"+String.valueOf(calendar.get(calendar.SECOND)));
                    ServerRequest serverRequest = new ServerRequest(Draft.this);
                    serverRequest.uploadFoto(Draft.this, photoActivity, new StoreOutletCalback() {
                        @Override
                        public void Done(String Response, int kd_outlet, int kd_visit) {
                            if (Response.equals("success")) {
                                databaseHandler.createLog(new Logging(0, "Uploading Photo from draft", String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)) + " " +
                                        new DateFormatSymbols().getMonths()[calendar.get(Calendar.MONTH)] + " " + String.valueOf(calendar.get(Calendar.YEAR))));
                                databaseHandler.deletePhoto(photoActivity.getId());
                                Toast.makeText(Draft.this, "Record has been uploaded",
                                        Toast.LENGTH_LONG).show();
                                adapter = null;
                                instanceView();
                            }
                        }
                    });
                }
            });
        }
        else if (status.equals("delete")){
            alert.setMessage("Hapus foto ini?");
            alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    databaseHandler.deletePhoto(kd_photo);
                    adapter = null;
                    instanceView();
                }
            });
        }
        alert.setNegativeButton("Cancel",null);
        alert.show();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return true;
    }
}

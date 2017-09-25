package android.trikarya.growth;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import master.City;
import master.DatabaseHandler;
import master.Outlet;
import master.Tipe;
/**
 *   PT Trikarya Teknologi
 */
public class DetailOutlet extends AppCompatActivity {
    private City city;
    private Tipe tipeDB;
    private Outlet outlet;
    private DatabaseHandler databaseHandler;
    TextView nama,alamat,telp,ranking,kota,tipe;

    @Override
    public void onBackPressed() {
        finish();
        startActivity(new Intent(this,OutletList.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(android.trikarya.growth.R.layout.activity_detail_outlet);
        databaseHandler = new DatabaseHandler(this);
        outlet = databaseHandler.getOutlet((Integer)getIntent().getSerializableExtra("kd_outlet"));
        if(outlet != null){
            nama = (TextView) findViewById(android.trikarya.growth.R.id.out_nama);
            nama.setText(nama.getText().toString()+" "+outlet.getNama());
            alamat = (TextView) findViewById(android.trikarya.growth.R.id.out_alamat);
            alamat.setText(alamat.getText().toString()+" "+outlet.getAlamat());
            ranking = (TextView) findViewById(android.trikarya.growth.R.id.out_rank);
            ranking.setText(ranking.getText().toString()+" "+outlet.getRank());
            telp = (TextView) findViewById(android.trikarya.growth.R.id.out_telp);
            telp.setText(telp.getText().toString()+" "+outlet.getTelpon());
            kota = (TextView) findViewById(android.trikarya.growth.R.id.out_kota);
            city = databaseHandler.getCity(outlet.getKode_kota());
            if(city != null)
                kota.setText(kota.getText().toString()+" "+city.getNama());
            tipe = (TextView) findViewById(android.trikarya.growth.R.id.out_tipe);
            tipeDB = databaseHandler.getTipe(outlet.getTipe());
            if(tipeDB != null)
                tipe.setText(tipe.getText().toString()+" "+tipeDB.getNm_tipe());
        }
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

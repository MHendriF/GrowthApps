package android.trikarya.growth;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import java.text.DateFormatSymbols;

import master.City;
import master.DatabaseHandler;
import master.Outlet;
import master.Tipe;
import master.VisitPlanDb;
/**
 *   PT Trikarya Teknologi
 */
public class DetailVisitPlan extends AppCompatActivity {
    private VisitPlanDb visitPlanDb;
    private City city;
    private Tipe tipeDB;
    private Outlet outlet;
    private DatabaseHandler databaseHandler;
    TextView nama,alamat,telp,ranking,kota,tipe,tgl;

    @Override
    public void onBackPressed() {
        finish();
        startActivity(new Intent(this, VisitPlan.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(android.trikarya.growth.R.layout.activity_detail_visit_plan);
        databaseHandler = new DatabaseHandler(this);
        visitPlanDb = databaseHandler.getVisitPlan((Integer) getIntent().getSerializableExtra("kd_visitplan"));
        if(visitPlanDb != null) {
            outlet = databaseHandler.getOutlet(visitPlanDb.getKd_outlet());
            if(outlet != null){
                nama = (TextView) findViewById(android.trikarya.growth.R.id.vp_nama);
                nama.setText(nama.getText().toString()+" "+outlet.getNama());
                alamat = (TextView) findViewById(android.trikarya.growth.R.id.vp_alamat);
                alamat.setText(alamat.getText().toString()+" "+outlet.getAlamat());
                ranking = (TextView) findViewById(android.trikarya.growth.R.id.vp_rank);
                ranking.setText(ranking.getText().toString()+" "+outlet.getRank());
                telp = (TextView) findViewById(android.trikarya.growth.R.id.vp_telp);
                telp.setText(telp.getText().toString()+" "+outlet.getTelpon());
                kota = (TextView) findViewById(android.trikarya.growth.R.id.vp_kota);
                city = databaseHandler.getCity(outlet.getKode_kota());
                if(city != null)
                    kota.setText(kota.getText().toString()+" "+city.getNama());
                tipe = (TextView) findViewById(android.trikarya.growth.R.id.vp_tipe);
                tipeDB = databaseHandler.getTipe(outlet.getTipe());
                if(tipeDB != null)
                    tipe.setText(tipe.getText().toString()+" "+tipeDB.getNm_tipe());
            }
            tgl = (TextView) findViewById(android.trikarya.growth.R.id.vp_tgl);
            Integer tahun = Integer.parseInt(visitPlanDb.getDate_visit().split(" ")[0].split("-")[0]);
            Integer bulan = Integer.parseInt(visitPlanDb.getDate_visit().split(" ")[0].split("-")[1]);
            Integer tanggal = Integer.parseInt(visitPlanDb.getDate_visit().split(" ")[0].split("-")[2]);
            String tmp = String.valueOf(tanggal + " " + new DateFormatSymbols().getMonths()[bulan-1] + " " + String.valueOf(tahun));
            tgl.setText(tgl.getText().toString()+" "+tmp);
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

package android.trikarya.growth;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import Master.DatabaseHandler;
import Master.User;
/**
 *   PT Trikarya Teknologi
 */
public class ProfilUser extends AppCompatActivity {
    private User user;
    private DatabaseHandler databaseHandler;
    TextView nama,alamat,nik,telp,email;

    @Override
    public void onBackPressed() {
        finish();
        startActivity(new Intent(this, Dashboard.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(android.trikarya.growth.R.layout.activity_profil_user);
        databaseHandler = new DatabaseHandler(this);
        user = databaseHandler.getUser();
        nama = (TextView) findViewById(android.trikarya.growth.R.id.prof_nama);
        nama.setText(nama.getText().toString()+" "+user.getNama());
        alamat = (TextView) findViewById(android.trikarya.growth.R.id.prof_alamat);
        alamat.setText(alamat.getText().toString()+" "+user.getAlamat());
        nik = (TextView) findViewById(android.trikarya.growth.R.id.prof_nik);
        nik.setText(nik.getText().toString()+" "+user.getNIK());
        telp = (TextView) findViewById(android.trikarya.growth.R.id.prof_telp);
        telp.setText(telp.getText().toString()+" "+user.getTelepon());
        email = (TextView) findViewById(android.trikarya.growth.R.id.prof_email);
        email.setText(email.getText().toString()+" "+user.getEmail());
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

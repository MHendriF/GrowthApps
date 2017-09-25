package android.trikarya.growth;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormatSymbols;

import java.util.Calendar;
import java.util.List;

import master.DatabaseHandler;
import master.Logging;
import master.Outlet;
import master.ServerRequest;
import master.StoreOutletCalback;
import master.VisitPlanDb;
/**
 *   PT Trikarya Teknologi
 */
public class RegisterVisitPlan extends AppCompatActivity {

    TextView outlet;
    TextView visitDate;
    Button submit;
    String dateVisit = "";
    DatabaseHandler databaseHandler;
    int kd_outlet = 0;
    String dateNow;
    VisitPlanDb visitPlanDb;

    @Override
    public void onBackPressed() {
        finish();
        startActivity(new Intent(this,VisitPlan.class));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if(resultCode == Activity.RESULT_OK){
                kd_outlet = data.getIntExtra("kd_outlet",0);
                if(kd_outlet != 0)
                    outlet.setText(databaseHandler.getOutlet(kd_outlet).getNama());
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                kd_outlet = 0;
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(android.trikarya.growth.R.layout.activity_register_visit_plan);
        databaseHandler = new DatabaseHandler(this);
        outlet = (TextView) findViewById(android.trikarya.growth.R.id.rvp_outletname);
        outlet.setClickable(true);
        final List<Outlet> outlets = databaseHandler.getAllOutlet();
        outlet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterVisitPlan.this, OutletSearch.class);
                intent.putExtra("tipe","outlet");
                startActivityForResult(intent,1);
            }
        });

        visitDate = (TextView) findViewById(android.trikarya.growth.R.id.visitDate);
        visitDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendar = Calendar.getInstance();
                dateNow = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)) + " " +
                        new DateFormatSymbols().getMonths()[calendar.get(Calendar.MONTH)] + " " + String.valueOf(calendar.get(Calendar.YEAR));
                Dialog mDialog = new DatePickerDialog(RegisterVisitPlan.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        dateVisit = String.valueOf(year)+"-"+String.valueOf(monthOfYear+1)+"-"+String.valueOf(dayOfMonth)+" "+calendar.get(calendar.HOUR_OF_DAY)+":"+calendar.get(calendar.MINUTE)+":"+calendar.get(calendar.SECOND);
                        visitDate.setText(String.valueOf(dayOfMonth) + " " +
                                new DateFormatSymbols().getMonths()[monthOfYear] + " " + String.valueOf(year));
                    }
                },calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH));
                mDialog.show();
            }
        });
        submit = (Button) findViewById(android.trikarya.growth.R.id.rvp_submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(kd_outlet != 0 && !dateVisit.equals("")) {
                    visitPlanDb = new VisitPlanDb(0, kd_outlet, dateVisit);
                    ServerRequest serverRequest = new ServerRequest(RegisterVisitPlan.this);
                    serverRequest.storeVisitDataInBackground(visitPlanDb, new StoreOutletCalback() {
                        @Override
                        public void Done(String Response, int id, int kd_visit) {
                            showMessage(Response);
                        }
                    });
                }
                else
                    Toast.makeText(RegisterVisitPlan.this,"You must fill all field",Toast.LENGTH_LONG).show();
            }
        });
    }
    private void showMessage(String message) {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setMessage(message);
        if(message.equals("success"))
            alert.setPositiveButton("Back to Home", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    databaseHandler.createLog(new Logging(0,"Registering a visit plan to "+databaseHandler.getOutlet(kd_outlet).getNama(),dateNow));
                    startActivity(new Intent(RegisterVisitPlan.this, Dashboard.class));
                    RegisterVisitPlan.this.finish();
                }
            });
        else
            alert.setPositiveButton("Try Again", null);
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

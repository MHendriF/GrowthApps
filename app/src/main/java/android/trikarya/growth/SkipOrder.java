package android.trikarya.growth;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.DateFormatSymbols;
import java.util.Calendar;

import Master.DatabaseHandler;
import Master.Logging;
import Master.ServerRequest;
import Master.StoreOutletCalback;
import Master.TakeOrder;
import Master.VisitPlanDb;

/**
 *   PT Trikarya Teknologi
 */
public class SkipOrder extends AppCompatActivity {
    EditText outletName,tgl,other;
    Spinner cause;
    Button submit;
    DatabaseHandler databaseHandler;
    VisitPlanDb visitPlanDb;
    String[] sat = new String[]{"Masih ada stok","Tidak ada budget","Order dari luar","Lainnya"};
    int kd_visit;
    Calendar calendar = Calendar.getInstance();
    String reason;
    @Override
    public void onBackPressed() {
        Toast.makeText(this, "You must complete this action",
                Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(android.trikarya.growth.R.layout.activity_skip_order);
        kd_visit = Integer.parseInt(getIntent().getSerializableExtra("kd_visit").toString());
        databaseHandler = new DatabaseHandler(this);
        outletName = (EditText) findViewById(android.trikarya.growth.R.id.so_outletname);
        visitPlanDb = databaseHandler.getVisitPlan(kd_visit);
        outletName.setText(databaseHandler.getOutlet(visitPlanDb.getKd_outlet()).getNama());
        tgl = (EditText) findViewById(android.trikarya.growth.R.id.so_date);
        other = (EditText) findViewById(android.trikarya.growth.R.id.so_other);
        tgl.setText(String.valueOf(calendar.get(calendar.YEAR))+"-"+String.valueOf(calendar.get(calendar.MONTH)+1)+"-"+String.valueOf(calendar.get(calendar.DAY_OF_MONTH))+
                " "+String.valueOf(calendar.get(calendar.HOUR_OF_DAY))+":"+String.valueOf(calendar.get(calendar.MINUTE))+":"+String.valueOf(calendar.get(calendar.SECOND)));
        cause = (Spinner) findViewById(android.trikarya.growth.R.id.so_cause);
        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.trikarya.growth.R.layout.custom_spinner,sat);
        cause.setAdapter(arrayAdapter);
        cause.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (cause.getSelectedItem().toString().equals("Lainnya"))
                    other.setVisibility(View.VISIBLE);
                else
                    other.setVisibility(View.GONE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        submit = (Button) findViewById(android.trikarya.growth.R.id.so_submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reason = "";
                if(cause.getSelectedItem().toString().equals("Lainnya")){
                    if(!isEmpty(other)){
                        reason = other.getText().toString();
                    }
                }
                else
                    reason = cause.getSelectedItem().toString();
                if(!reason.equals("")) {
                    ServerRequest serverRequest = new ServerRequest(SkipOrder.this);
                    visitPlanDb.setStatus_visit(1);
                    visitPlanDb.setDate_visiting(tgl.getText().toString());
                    visitPlanDb.setSkip_order_reason(reason);
                    TakeOrder takeOrder = new TakeOrder(0, kd_visit, 0, 0, 0, tgl.getText().toString(), "null");
                    serverRequest.submitVisitDataInBackground(visitPlanDb, takeOrder, new StoreOutletCalback() {
                        @Override
                        public void Done(String Response, int id, int kd_visit) {
                            showError(Response);
                        }
                    });
                }
                else
                    Toast.makeText(SkipOrder.this,"Kamu harus mengisi alasan skip order",Toast.LENGTH_LONG).show();
            }
        });
    }
    private boolean isEmpty(EditText etText) {
        return etText.getText().toString().trim().length() == 0;
    }
    private void showError(String message) {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setMessage(message);
        if(message.equals("success")) {
            databaseHandler.updateVisitPlan(visitPlanDb);
            databaseHandler.createLog(new Logging(0,"Submit a visit plan to "+databaseHandler.getOutlet(visitPlanDb.getKd_outlet()).getNama(),String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)) + " " +
                    new DateFormatSymbols().getMonths()[calendar.get(Calendar.MONTH)] + " " + String.valueOf(calendar.get(Calendar.YEAR))));
            alert.setPositiveButton("Got It", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    startActivity(new Intent(SkipOrder.this, Dashboard.class));
                    SkipOrder.this.finish();
                }
            });
        }
        else
            alert.setPositiveButton("Got It", null);
        alert.show();
    }
}

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
import java.util.List;

import master.DatabaseHandler;
import master.Logging;
import master.Produk;
import master.ServerRequest;
import master.StoreOutletCalback;
import master.TakeOrder;
import master.VisitPlanDb;
/**
 *   PT Trikarya Teknologi
 */
public class ProcessOrder extends AppCompatActivity {

    EditText outletName,quantity,other;
    Spinner produk, satuan;
    Button submit;
    String[] sat = new String[]{"sachet","pack","pieces","bottle","tube","lainnya"};
    int kd_visit;
    DatabaseHandler databaseHandler;
    VisitPlanDb visitPlanDb;
    Calendar calendar = Calendar.getInstance();

    @Override
    public void onBackPressed() {
        Toast.makeText(this, "You must complete this action",
                Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(android.trikarya.growth.R.layout.activity_process_order);
        kd_visit = Integer.parseInt(getIntent().getSerializableExtra("kd_visit").toString());
        databaseHandler = new DatabaseHandler(this);
        outletName = (EditText) findViewById(android.trikarya.growth.R.id.po_outletname);
        visitPlanDb = databaseHandler.getVisitPlan(kd_visit);
        outletName.setText(databaseHandler.getOutlet(visitPlanDb.getKd_outlet()).getNama());
        quantity = (EditText) findViewById(android.trikarya.growth.R.id.po_qty);
        other = (EditText) findViewById(android.trikarya.growth.R.id.po_other);
        produk = (Spinner) findViewById(android.trikarya.growth.R.id.po_productname);
        List<Produk> produkList = databaseHandler.getAllProduk();
        ArrayAdapter adapter = new ArrayAdapter(this, android.trikarya.growth.R.layout.custom_spinner,produkList);
        produk.setAdapter(adapter);
        satuan = (Spinner) findViewById(android.trikarya.growth.R.id.po_satuan);
        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.trikarya.growth.R.layout.custom_spinner,sat);
        satuan.setAdapter(arrayAdapter);
        satuan.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (satuan.getSelectedItem().toString().equals("lainnya"))
                    other.setVisibility(View.VISIBLE);
                else
                    other.setVisibility(View.GONE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        submit = (Button) findViewById(android.trikarya.growth.R.id.po_submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String detailSatuan = "";
                if(satuan.getSelectedItem().toString().equals("lainnya")) {
                    if (!isEmpty(other)) {
                        detailSatuan = other.getText().toString();
                    }
                }
                else
                    detailSatuan = satuan.getSelectedItem().toString();
                if(!detailSatuan.equals("") && isInteger(quantity.getText().toString())) {
                    ServerRequest serverRequest = new ServerRequest(ProcessOrder.this);
                    visitPlanDb.setStatus_visit(1);
                    String tgl = String.valueOf(calendar.get(calendar.YEAR)) + "-" + String.valueOf(calendar.get(calendar.MONTH) + 1) + "-" + String.valueOf(calendar.get(calendar.DAY_OF_MONTH)) +
                            " " + String.valueOf(calendar.get(calendar.HOUR_OF_DAY)) + ":" + String.valueOf(calendar.get(calendar.MINUTE)) + ":" + String.valueOf(calendar.get(calendar.SECOND));
                    visitPlanDb.setDate_visiting(tgl);
                    TakeOrder takeOrder = new TakeOrder(0, kd_visit, ((Produk) produk.getSelectedItem()).getId(), Integer.parseInt(quantity.getText().toString()), 1, tgl, detailSatuan);
                    serverRequest.submitVisitDataInBackground(visitPlanDb, takeOrder, new StoreOutletCalback() {
                        @Override
                        public void Done(String Response, int id, int kd_visit) {
                            showError(Response);
                        }
                    });
                }
                else
                    Toast.makeText(ProcessOrder.this,"Kamu harus mengisi satuan",Toast.LENGTH_LONG).show();
            }
        });
    }
    public boolean isInteger( String input ) {
        try {
            Integer.parseInt(input);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    private boolean isEmpty(EditText etText) {
        return etText.getText().toString().trim().length() == 0;
    }
    private void showError(String message) {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setMessage(message);
        if(message.equals("success")) {
            databaseHandler.createLog(new Logging(0,"Submit a visit plan to "+databaseHandler.getOutlet(visitPlanDb.getKd_outlet()).getNama(),String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)) + " " +
                    new DateFormatSymbols().getMonths()[calendar.get(Calendar.MONTH)] + " " + String.valueOf(calendar.get(Calendar.YEAR))));
            databaseHandler.updateVisitPlan(visitPlanDb);
            alert.setPositiveButton("Got It", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    startActivity(new Intent(ProcessOrder.this, Dashboard.class));
                    ProcessOrder.this.finish();
                }
            });
        }
        else
            alert.setPositiveButton("Got It", null);
        alert.show();
    }
}

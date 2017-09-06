package android.trikarya.growth;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import Master.DatabaseHandler;
import Master.Outlet;
import Master.VisitPlanDb;
/**
 *   PT Trikarya Teknologi
 */
public class VisitPlan extends AppCompatActivity {
    DatabaseHandler databaseHandler;
    ArrayAdapter<Outlet> adapter;
    ListView listView;
    List<VisitPlanDb> fromDatabase,terpilih;
    List<Outlet> outlets;

    @Override
    public void onBackPressed() {
        finish();
        startActivity(new Intent(this, Dashboard.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visit_plan);
        listView = (ListView) findViewById(R.id.visitplan_list);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.vp_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(VisitPlan.this, RegisterVisitPlan.class));
                VisitPlan.this.finish();
            }
        });
        databaseHandler = new DatabaseHandler(this);
        outlets = new ArrayList<Outlet>();
        terpilih = new ArrayList<VisitPlanDb>();
        instanceView();
    }
    public void instanceView()
    {
        fromDatabase = databaseHandler.getAllVisitPlan();
        int count = fromDatabase.size();
        if(count != 0) {
            for (int i = 0; i < count; i++) {
                if(fromDatabase.get(i).getStatus_visit() == 0 && compare(fromDatabase.get(i).getDate_visit())) {
                    outlets.add(databaseHandler.getOutlet(fromDatabase.get(i).getKd_outlet()));
                    terpilih.add(fromDatabase.get(i));
                }
            }
            populateList();
        }
    }
    private boolean compare(String date_visit) {
        try {
            String tanggal = date_visit.split(" ")[0];
            String[] sp_tanggal = tanggal.split("-");
            Calendar now = Calendar.getInstance();
            Calendar visit = Calendar.getInstance();
            visit.set(Integer.parseInt(sp_tanggal[0]), Integer.parseInt(sp_tanggal[1]) - 1, Integer.parseInt(sp_tanggal[2]));
            if (visit.getTimeInMillis() >= now.getTimeInMillis())
                return true;
            return false;
        }
        catch (Exception e){
            return false;
        }
    }
    public void populateList()
    {
        if(adapter == null)
            adapter = new OutletListAdapter();
        listView.setAdapter(adapter);
    }
    private class OutletListAdapter extends ArrayAdapter<Outlet> {
        public OutletListAdapter(){
            super(VisitPlan.this, R.layout.listview_item,outlets);
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            if(convertView == null)
                convertView = getLayoutInflater().inflate(R.layout.listview_item,parent,false);
            final Outlet currentOutlet = outlets.get(position);
            if(currentOutlet!=null) {
                TextView nama = (TextView) convertView.findViewById(R.id.namaOutlet);
                nama.setText(currentOutlet.getNama());
                TextView alamat = (TextView) convertView.findViewById(R.id.alamatOutlet);
                alamat.setText(currentOutlet.getAlamat());
                TextView rank = (TextView) convertView.findViewById(R.id.rankOutlet);
                rank.setText(currentOutlet.getRank());
                TextView tipe = (TextView) convertView.findViewById(R.id.tipeOutlet);
                tipe.setText(databaseHandler.getTipe(currentOutlet.getTipe()).getNm_tipe());
                LinearLayout view = (LinearLayout) convertView.findViewById(R.id.group);
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getApplicationContext(), DetailVisitPlan.class);
                        intent.putExtra("kd_visitplan", terpilih.get(position).getKd_visit());
                        startActivity(intent);
                    }
                });
            }
            return convertView;
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

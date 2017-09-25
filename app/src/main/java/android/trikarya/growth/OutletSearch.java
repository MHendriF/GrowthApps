package android.trikarya.growth;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import master.DatabaseHandler;
import master.Outlet;
import master.VisitPlanDb;

public class OutletSearch extends AppCompatActivity {
    DatabaseHandler databaseHandler;
    ArrayAdapter<Outlet> adapter;
    ListView listView;
    List<Outlet> fromDatabase,outletPart;
    List<VisitPlanDb> visitPlanDbs, visitPlanTerpakai;
    Button load ;
    String tipe;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(android.trikarya.growth.R.layout.activity_outlet_search);
        tipe = getIntent().getStringExtra("tipe");
        listView = (ListView) findViewById(android.trikarya.growth.R.id.os_outlet_list);
        load = (Button) findViewById(android.trikarya.growth.R.id.os_load_more);
        load.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                instanceView();
            }
        });
        databaseHandler = new DatabaseHandler(this);
        outletPart = new ArrayList<Outlet>();
        instanceView();
    }
    public void instanceView()
    {
        if(fromDatabase == null) {
            if(tipe.equals("outlet"))
                fromDatabase = databaseHandler.getAllOutlet();
            else if(tipe.equals("visitplan")){
                visitPlanDbs = databaseHandler.getAllVisitPlan();
                fromDatabase = new ArrayList<Outlet>();
                visitPlanTerpakai = new ArrayList<VisitPlanDb>();
                for (int i = 0; i < visitPlanDbs.size(); i++) {
                    if (visitPlanDbs.get(i).getStatus_visit() == 0 && compare(visitPlanDbs.get(i).getDate_visit())) {
                        fromDatabase.add(databaseHandler.getOutlet(visitPlanDbs.get(i).getKd_outlet()));
                        visitPlanTerpakai.add(visitPlanDbs.get(i));
                    }
                }
            }
        }
        int count = fromDatabase.size();
        if(count > 0){
            if(count > 20){
                outletPart.clear();
                for(int i = 0;i<20;i++){
                    outletPart.add(fromDatabase.get(0));
                    fromDatabase.remove(0);
                }
                load.setVisibility(View.VISIBLE);
            }
            else {
                outletPart.clear();
                outletPart = fromDatabase;
                load.setVisibility(View.GONE);
            }
            populateList();
        }
    }
    public void populateList()
    {
        adapter = new OutletListAdapter();
        listView.setAdapter(adapter);
    }
    private class OutletListAdapter extends ArrayAdapter<Outlet> {
        public OutletListAdapter(){
            super(OutletSearch.this, android.trikarya.growth.R.layout.listview_item,outletPart);
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            if(convertView == null)
                convertView = getLayoutInflater().inflate(android.trikarya.growth.R.layout.listview_item,parent,false);
            final Outlet currentOutlet = outletPart.get(position);
            TextView nama = (TextView) convertView.findViewById(android.trikarya.growth.R.id.namaOutlet);
            nama.setText(currentOutlet.getNama());
            TextView alamat = (TextView) convertView.findViewById(android.trikarya.growth.R.id.alamatOutlet);
            alamat.setText(currentOutlet.getAlamat());
            TextView rank = (TextView) convertView.findViewById(android.trikarya.growth.R.id.rankOutlet);
            rank.setText(currentOutlet.getRank());
            final TextView type = (TextView) convertView.findViewById(android.trikarya.growth.R.id.tipeOutlet);
            type.setText(databaseHandler.getTipe(currentOutlet.getTipe()).getNm_tipe());
            LinearLayout view = (LinearLayout) convertView.findViewById(android.trikarya.growth.R.id.group);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent returnIntent = new Intent();
                    if(tipe.equals("outlet"))
                        returnIntent.putExtra("kd_outlet", currentOutlet.getKode());
                    else if(tipe.equals("visitplan"))
                        returnIntent.putExtra("kd_visit", visitPlanTerpakai.get(position).getKd_visit());
                    setResult(Activity.RESULT_OK, returnIntent);
                    finish();
                }
            });
            return convertView;
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
    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }
}

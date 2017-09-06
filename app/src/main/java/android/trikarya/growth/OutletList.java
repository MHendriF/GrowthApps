package android.trikarya.growth;

import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import Master.DatabaseHandler;
import Master.Outlet;
/**
 *   PT Trikarya Teknologi
 */
public class OutletList extends AppCompatActivity {

    DatabaseHandler databaseHandler;
    ArrayAdapter<Outlet> adapter;
    ListView listView;
    List<Outlet> fromDatabase,outletPart;
    Button load ;

    @Override
    public void onBackPressed() {
        finish();
        startActivity(new Intent(this, Dashboard.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(android.trikarya.growth.R.layout.activity_outlet_list);
        listView = (ListView) findViewById(android.trikarya.growth.R.id.outlet_list);
        FloatingActionButton fab = (FloatingActionButton) findViewById(android.trikarya.growth.R.id.outlet_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(OutletList.this, RegisterOutlet.class));
                OutletList.this.finish();
            }
        });
        load = (Button) findViewById(android.trikarya.growth.R.id.load_more);
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
        if(fromDatabase == null)
            fromDatabase = databaseHandler.getAllOutlet();
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
            super(OutletList.this, android.trikarya.growth.R.layout.listview_item,outletPart);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(convertView == null)
                convertView = getLayoutInflater().inflate(android.trikarya.growth.R.layout.listview_item,parent,false);
            final Outlet currentOutlet = outletPart.get(position);
            TextView nama = (TextView) convertView.findViewById(android.trikarya.growth.R.id.namaOutlet);
            nama.setText(currentOutlet.getNama());
            TextView alamat = (TextView) convertView.findViewById(android.trikarya.growth.R.id.alamatOutlet);
            alamat.setText(currentOutlet.getAlamat());
            TextView rank = (TextView) convertView.findViewById(android.trikarya.growth.R.id.rankOutlet);
            rank.setText(currentOutlet.getRank());
            TextView tipe = (TextView) convertView.findViewById(android.trikarya.growth.R.id.tipeOutlet);
            tipe.setText(databaseHandler.getTipe(currentOutlet.getTipe()).getNm_tipe());
            LinearLayout view = (LinearLayout) convertView.findViewById(android.trikarya.growth.R.id.group);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(),DetailOutlet.class);
                    intent.putExtra("kd_outlet", currentOutlet.getKode());
                    startActivity(intent);
                    OutletList.this.finish();
                }
            });
            return convertView;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(android.trikarya.growth.R.menu.outlet_menu, menu);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            SearchManager searchManager =
                    (SearchManager) getSystemService(Context.SEARCH_SERVICE);
            MenuItem menuItem = menu.findItem(android.trikarya.growth.R.id.search);
            SearchView searchView = (SearchView) menuItem.getActionView();
            searchView.setSearchableInfo(
                    searchManager.getSearchableInfo(new ComponentName(this, SearchResultsActivity.class)));
            searchView.setQueryHint(getResources().getString(android.trikarya.growth.R.string.search_hint));
            searchView.setIconifiedByDefault(false);
        }
        return true;
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

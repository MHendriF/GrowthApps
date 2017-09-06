package android.trikarya.growth;

import android.app.SearchManager;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import Master.DatabaseHandler;
import Master.Outlet;
/**
 *   PT Trikarya Teknologi
 */
public class SearchResultsActivity extends AppCompatActivity {
    private DatabaseHandler databaseHandler;
    List<Outlet> outlets;
    ArrayAdapter<Outlet> adapter;
    ListView listView;

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(android.trikarya.growth.R.layout.activity_search_results);
        databaseHandler = new DatabaseHandler(this);
        listView = (ListView) findViewById(android.trikarya.growth.R.id.sr_outlet_list);
        handleIntent(getIntent());
    }

    private void handleIntent(Intent intent) {

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            outlets = databaseHandler.searchOutlet(query);
            instanceView();
        }
    }
    public void instanceView()
    {
        int count = outlets.size();
        if(count != 0)
            populateList();
    }
    public void populateList()
    {
        if(adapter == null)
            adapter = new OutletListAdapter();
        listView.setAdapter(adapter);
    }
    private class OutletListAdapter extends ArrayAdapter<Outlet> {
        public OutletListAdapter(){
            super(SearchResultsActivity.this, android.trikarya.growth.R.layout.listview_item,outlets);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(convertView == null)
                convertView = getLayoutInflater().inflate(android.trikarya.growth.R.layout.listview_item,parent,false);
            final Outlet currentOutlet = outlets.get(position);
            TextView nama = (TextView) convertView.findViewById(android.trikarya.growth.R.id.namaOutlet);
            nama.setText(currentOutlet.getNama());
            TextView alamat = (TextView) convertView.findViewById(android.trikarya.growth.R.id.alamatOutlet);
            alamat.setText(currentOutlet.getAlamat());
            TextView rank = (TextView) convertView.findViewById(android.trikarya.growth.R.id.rankOutlet);
            rank.setText(currentOutlet.getRank());
            TextView tipe = (TextView) convertView.findViewById(android.trikarya.growth.R.id.tipeOutlet);
            tipe.setText(databaseHandler.getTipe(currentOutlet.getTipe()).getNm_tipe());
            return convertView;
        }
    }
}

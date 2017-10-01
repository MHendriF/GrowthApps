package android.trikarya.growth;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import master.DatabaseHandler;
import master.Logging;
/**
 *   PT Trikarya Teknologi
 */
public class History extends AppCompatActivity {
    DatabaseHandler databaseHandler;
    ArrayAdapter<Logging> adapter;
    ListView listView;
    List<Logging> fromDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.custom_bar_no_icon);
        ((TextView) getSupportActionBar().getCustomView().findViewById(R.id.title)).setText("HISTORY");
        ((LinearLayout) getSupportActionBar().getCustomView().findViewById(R.id.back)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        listView = (ListView) findViewById(R.id.history_list);

        databaseHandler = new DatabaseHandler(this);
        instanceView();
    }

    @Override
    public void onBackPressed() {
        finish();
        startActivity(new Intent(this, MainActivity.class));
    }

    public void instanceView()
    {
        fromDatabase = databaseHandler.getAllLog();
        populateList();
    }
    public void populateList()
    {
        if(adapter == null)
            adapter = new LoggingListAdapter();
        listView.setAdapter(adapter);
    }
    private class LoggingListAdapter extends ArrayAdapter<Logging> {
        public LoggingListAdapter(){
            super(History.this, android.trikarya.growth.R.layout.historylist_item,fromDatabase);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(convertView == null)
                convertView = getLayoutInflater().inflate(android.trikarya.growth.R.layout.historylist_item,parent,false);
            final Logging logging = fromDatabase.get(position);
            TextView nama = (TextView) convertView.findViewById(android.trikarya.growth.R.id.deskripsi);
            nama.setText(logging.getDescription());
            TextView alamat = (TextView) convertView.findViewById(android.trikarya.growth.R.id.tgl_log);
            alamat.setText(logging.getLog_time());
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

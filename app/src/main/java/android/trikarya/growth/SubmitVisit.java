package android.trikarya.growth;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import layout.SubmitVisitStep1;
/**
 *   PT Trikarya Teknologi
 */
public class SubmitVisit extends AppCompatActivity {
    FragmentManager fragmentManager;

    @Override
    public void onBackPressed() {
        finish();
        startActivity(new Intent(this, Dashboard.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(android.trikarya.growth.R.layout.activity_submit_visit);
        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(android.trikarya.growth.R.id.frame_layout,SubmitVisitStep1.newInstance(null,null)).commit();
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

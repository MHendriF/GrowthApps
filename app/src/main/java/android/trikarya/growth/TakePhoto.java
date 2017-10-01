package android.trikarya.growth;

import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import layout.Competitor1;
import layout.TakePhoto1;
/**
 *   PT Trikarya Teknologi
 */
public class TakePhoto extends AppCompatActivity {

    FragmentManager fragmentManager;
    View outlet,competitor;
    TextView tv_outlet,tv_competitor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(android.trikarya.growth.R.layout.activity_take_photo);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.custom_bar_no_icon);
        ((TextView) getSupportActionBar().getCustomView().findViewById(R.id.title)).setText("TAKE PHOTO");
        ((LinearLayout) getSupportActionBar().getCustomView().findViewById(R.id.back)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        fragmentManager = getSupportFragmentManager();
        outlet = (View) findViewById(android.trikarya.growth.R.id.v_outlet);
        competitor = (View) findViewById(android.trikarya.growth.R.id.v_competitor);
        tv_outlet = (TextView) findViewById(android.trikarya.growth.R.id.tv_home);
        tv_competitor = (TextView) findViewById(android.trikarya.growth.R.id.tv_info);
        loadFragment(1);
    }

    @Override
    public void onBackPressed() {
        finish();
        startActivity(new Intent(this, MainActivity.class));
    }

    public void selectFragment(View v)
    {
        switch (v.getId())
        {
            case android.trikarya.growth.R.id.view_outlet:
                loadFragment(1);
                break;
            case android.trikarya.growth.R.id.view_competitor:
                loadFragment(2);
                break;
            default:
                loadFragment(1);
                break;
        }
    }
    public void loadFragment(int pos)
    {
        switch (pos){
            case 1:
                fragmentManager.beginTransaction().replace(android.trikarya.growth.R.id.tp_frame_layout,new TakePhoto1()).addToBackStack(null).commit();
                changeColor(1);
                break;
            case 2:
                fragmentManager.beginTransaction().replace(android.trikarya.growth.R.id.tp_frame_layout,new Competitor1()).addToBackStack(null).commit();
                changeColor(2);
                break;
            default:
                fragmentManager.beginTransaction().replace(android.trikarya.growth.R.id.tp_frame_layout,new TakePhoto1()).addToBackStack(null).commit();
                changeColor(1);
                break;
        }
    }
    private void changeColor(int pos)
    {
        for(int i = 1; i < 3; i++){
            if(i == pos)
            {
                setColor(i,"aktif");
            }
            else
                setColor(i,"nonaktif");
        }
    }
    private void setColor(int pos,String status)
    {
        int warna1,warna2;
        if(status.equals("aktif")) {
            warna1 = getResources().getColor(android.trikarya.growth.R.color.oldgreen);
            warna2 = getResources().getColor(android.trikarya.growth.R.color.oldgreen);
        }
        else
        {
            warna1 = Color.BLACK;
            warna2 = Color.WHITE;
        }
        switch (pos){
            case 1:
                outlet.setBackgroundColor(warna2);
                tv_outlet.setTextColor(warna1);
                break;
            case 2:
                competitor.setBackgroundColor(warna2);
                tv_competitor.setTextColor(warna1);
                break;
            default:
                outlet.setBackgroundColor(warna2);
                tv_outlet.setTextColor(warna1);
                break;
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

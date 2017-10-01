package android.trikarya.growth;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import fragment.DashboardFragment;
import fragment.NewsFragment;

/**
 * Created by Hendry on 9/25/2017.
 */

public class MainActivity extends AppCompatActivity{
    LinearLayout beranda, news;
    int activeTab = 0;
    List<Bitmap> bitmaps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setElevation(0);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.abs_layout);

        setContentView(R.layout.activity_main);
        beranda = (LinearLayout) findViewById(R.id.beranda);
        news = (LinearLayout) findViewById(R.id.news);
        bitmaps = new ArrayList<>();
        if (getIntent().hasExtra("tipe") && getIntent().getStringExtra("tipe").toString().equals("news")) {
            selectTab(news);
        } else
            selectTab(beranda);
    }

    public void selectTab(View view) {
        view.setActivated(true);
        if(activeTab != view.getId()) {
            switch (view.getId()) {
                case R.id.beranda:
                    DashboardFragment dashboardFragment = new DashboardFragment();
                    getSupportFragmentManager().beginTransaction().replace(R.id.frame_dashboard, dashboardFragment).commit();
                    break;
                case R.id.news:
                    NewsFragment newsFragment = new NewsFragment();
                    getSupportFragmentManager().beginTransaction().replace(R.id.frame_dashboard, newsFragment).commit();
                    break;
            }
            if (activeTab != 0)
                findViewById(activeTab).setActivated(false);
            activeTab = view.getId();
        }
    }

    public List<Bitmap> getBitmaps() {
        return bitmaps;
    }

    public void setBitmaps(List<Bitmap> bitmaps) {
        this.bitmaps = bitmaps;
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
        .setTitle("Really Exit?")
        .setMessage("Are you sure you want to exit?")
        .setNegativeButton(android.R.string.no, null)
        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface arg0, int arg1) {
                MainActivity.super.onBackPressed();
            }
        }).create().show();
    }
}

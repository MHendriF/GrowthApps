package android.trikarya.growth;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import adapter.BannerAdapter;
import fragment.DashboardFragment;
import fragment.NewsFragment;
import master.Banner;
import master.DatabaseHandler;
import master.GetAllDataCallback;
import master.GetVisitPlanCallback;
import master.ServerRequest;
import master.User;
import network.ConnectionHandler;
import network.JsonCallback;
import network.UserNetwork;
import utility.ImageHelper;

/**
 *   PT Trikarya Teknologi
 */
public class Dashboard extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        View.OnClickListener{

        TextView username, warning;
        ServerRequest serverRequest;
        private String PROJECT_NUMBER = "1095564458173";

        DatabaseHandler databaseHandler;
        User user;
        UserNetwork userNetwork;
        ProgressBar progressBar;
        ImageHelper imageHelper;
        BannerAdapter bannerAdapter;
        ViewPager viewPager;
        List<Bitmap> bitmaps;
        List<Banner> banners;
        View ind1, ind2, ind3, aktif;
        int width;
        LinearLayout beranda, news;
    int activeTab = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        bitmaps = new ArrayList<>();

        Point size = new Point();
        this.getWindowManager().getDefaultDisplay().getSize(size);
        width = size.x;

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, android.trikarya.growth.R.string.navigation_drawer_open, android.trikarya.growth.R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        userNetwork = new UserNetwork(this);
        imageHelper = new ImageHelper(this);
        databaseHandler = new DatabaseHandler(this);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        warning = (TextView) findViewById(R.id.message);

        viewPager = (ViewPager) findViewById(R.id.slider);
        ind1 = findViewById(R.id.ind1);
        ind2 = findViewById(R.id.ind2);
        ind3 = findViewById(R.id.ind3);
        bitmaps = getBitmaps();
        final Calendar cal = Calendar.getInstance();

        NavigationView navigationView = (NavigationView) findViewById(android.trikarya.growth.R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        if(databaseHandler.getUserCount() == 0)
            startActivity(new Intent(this,Login.class));
        else {
            username = (TextView) navigationView.getHeaderView(0).findViewById(android.trikarya.growth.R.id.tvusername);
            username.setText(databaseHandler.getUser().getNama());
            user = databaseHandler.getUser();
            if (user.getStatus() == 0) {
                GCMClientManager pushClientManager = new GCMClientManager(this, PROJECT_NUMBER);
                pushClientManager.registerIfNeeded(new GCMClientManager.RegistrationCompletedHandler() {
                    @Override
                    public void onSuccess(String registrationId, boolean isNewRegistration) {
                        if(isNewRegistration)
                            databaseHandler.createKonfigurasi(1);
                        getData(0);
                        user.setStatus(1);
                        databaseHandler.updateUser(user);
                    }

                    @Override
                    public void onFailure(String ex) {
                        super.onFailure(ex);
                    }
                });
            }
        }

        beranda = (LinearLayout) findViewById(R.id.beranda);
        news = (LinearLayout) findViewById(R.id.news);
        bitmaps = new ArrayList<>();
        if (getIntent().hasExtra("tipe") && getIntent().getStringExtra("tipe").toString().equals("news")) {
            selectTab(news);
        } else
            selectTab(beranda);

        //if(banners.size() == 0 ){
            //banners = databaseHandler.getAllBanner();
            userNetwork.getBanner(new JsonCallback() {
                @Override
                public void Done(JSONObject jsonObject, String message) {
                    if(message.equals(ConnectionHandler.response_message_success) && jsonObject != null){
                        try {
                            JSONArray jsonArray = new JSONArray(jsonObject.getString(ConnectionHandler.response_data));
                            //databaseHandler.deleteAllBanner();
                            Log.d("response banner >>> ", String.valueOf(jsonArray));
                            for(int i = 0; i < jsonArray.length(); i++){
                                byte[] decodedString = Base64.decode(jsonArray.getJSONObject(i).getString("path_image"), Base64.DEFAULT);

                                Log.d("decoded", String.valueOf(decodedString));

                                Bitmap bitmap = imageHelper.getFitScreenBitmap(BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length), width);
                                bitmaps.add(bitmap);
                                databaseHandler.createBanner(
                                        new Banner(0,jsonArray.getJSONObject(i).getString("path_image"),String.valueOf(cal.getTimeInMillis())));
                            }
                            progressBar.setVisibility(View.GONE);
                            if(bitmaps.size() >= 1) {
                                instanceBanner();
                                setBitmaps(bitmaps);
                            }
                            else {
                                warning.setVisibility(View.VISIBLE);
                            }
                        } catch (Exception e) {
                            progressBar.setVisibility(View.GONE);
                            warning.setVisibility(View.VISIBLE);
                            e.printStackTrace();
                        }
                    }
                }
            });
        //}
    }

    public void selectTab(View view) {
        view.setActivated(true);
        if(activeTab != view.getId()) {
            switch (view.getId()) {
                case R.id.beranda:
                    DashboardFragment dashboardFragment = new DashboardFragment();
                    //Log.d("masuk", "basong");
                    getSupportFragmentManager().beginTransaction().replace(R.id.frame_dashboard, dashboardFragment).commit();
                    break;
                case R.id.news:
                    NewsFragment newsFragment = new NewsFragment();
                    Log.d("masuk", "sundel");
                    //DashboardFragment dashboardFragments = new DashboardFragment();
                    //getSupportFragmentManager().beginTransaction().replace(R.id.frame_dashboard, newsFragment).commit();
                    break;
            }
            if (activeTab != 0)
                findViewById(activeTab).setActivated(false);
            activeTab = view.getId();
        }
    }

    private void instanceBanner(){
        ind1.setVisibility(View.VISIBLE);
        if(bitmaps.size() >= 2) {
            ind2.setVisibility(View.VISIBLE);
            if(bitmaps.size() >= 3)
                ind3.setVisibility(View.VISIBLE);
        }
        ind1.setActivated(true);
        aktif = ind1;
        bannerAdapter = new BannerAdapter(this, bitmaps);
        viewPager.setVisibility(View.VISIBLE);
        viewPager.setAdapter(bannerAdapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (aktif != null)
                    aktif.setActivated(false);
                switch (position) {
                    case 0:
                        ind1.setActivated(true);
                        aktif = ind1;
                        break;
                    case 1:
                        ind2.setActivated(true);
                        aktif = ind2;
                        break;
                    default:
                        ind3.setActivated(true);
                        aktif = ind3;
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void getData(int status)
    {
        Toast.makeText(this, "Getting data from server, please wait",
                Toast.LENGTH_LONG).show();
        serverRequest = new ServerRequest(this);
        if(status == 0) {
            serverRequest.getAllData(this, user, new GetAllDataCallback() {
                @Override
                public void Done(String message) {
                    if(!message.equals("success"))
                        showError(message);
                }
            });
        }
        else
            serverRequest.fetchVisitPlanDataInBackground(user, new GetVisitPlanCallback() {
                @Override
                public void Done(String message) {
                    if(!message.equals("success"))
                        Toast.makeText(Dashboard.this, "Getting data from server failed because of "+message,
                                Toast.LENGTH_LONG).show();
                }
            });
    }

    private void showError(String messsage) {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setMessage(messsage);
        alert.setPositiveButton("Try Again", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                getData(0);
            }
        });
        alert.show();
    }

    public void goToMenu(View view)
    {
        Intent intent = null;
        switch (view.getId()){
            case android.trikarya.growth.R.id.dash_sv:
                intent = new Intent(this,SubmitVisit.class);
                break;
            case android.trikarya.growth.R.id.dash_vp:
                intent = new Intent(this,VisitPlan.class);
                break;
            case android.trikarya.growth.R.id.dash_ol:
                intent = new Intent(this,OutletList.class);
                break;
            case android.trikarya.growth.R.id.dash_tp:
                intent = new Intent(this,TakePhoto.class);
                break;
            case android.trikarya.growth.R.id.dash_draft:
                intent = new Intent(this,Draft.class);
                break;
            default:
                intent = null;
                databaseHandler.deleteAll();
                getData(0);
                break;
        }
        if(intent != null) {
            startActivity(intent);
            this.finish();
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(android.trikarya.growth.R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            new AlertDialog.Builder(this)
                    .setTitle("Really Exit?")
                    .setMessage("Are you sure you want to exit?")
                    .setNegativeButton(android.R.string.no, null)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface arg0, int arg1) {
                            Dashboard.super.onBackPressed();
                        }
                    }).create().show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(android.trikarya.growth.R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.trikarya.growth.R.id.nav_about) {
            startActivity(new Intent(this,About.class));
            this.finish();
        }
        else if (id == android.trikarya.growth.R.id.nav_profile) {
            startActivity(new Intent(this, ProfilUser.class));
            this.finish();
        }
        else if (id == android.trikarya.growth.R.id.nav_history) {
            startActivity(new Intent(this, History.class));
            this.finish();
        }
        else if (id == android.trikarya.growth.R.id.nav_nearby) {
            startActivity(new Intent(this,NearbyOutletActivity.class));
            this.finish();
        }
        else if (id == android.trikarya.growth.R.id.nav_logout) {
            databaseHandler.deleteAll();
            databaseHandler.deleteUser();
            this.finish();
            startActivity(new Intent(Dashboard.this, SplashScreen.class));
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(android.trikarya.growth.R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public List<Bitmap> getBitmaps() {
        return bitmaps;
    }

    public void setBitmaps(List<Bitmap> bitmaps) {
        this.bitmaps = bitmaps;
    }

    @Override
    public void onClick(View v) {

    }
}

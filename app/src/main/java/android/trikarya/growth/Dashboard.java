package android.trikarya.growth;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
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
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import Master.City;
import Master.Competitor;
import Master.DatabaseHandler;
import Master.Distributor;
import Master.GetAllDataCallback;
import Master.GetVisitPlanCallback;
import Master.Outlet;
import Master.Produk;
import Master.ServerRequest;
import Master.Tipe;
import Master.TipePhoto;
import Master.User;
import Master.VisitPlanDb;
import network.ConnectionHandler;
import network.JsonCallback;
import network.UserNetwork;

/**
 *   PT Trikarya Teknologi
 */
public class Dashboard extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

        TextView username;
        DatabaseHandler databaseHandler;
        ServerRequest serverRequest;
        private String PROJECT_NUMBER = "1095564458173";
        User user;

        List<Outlet> outletList;
        List<City> cityList;
        List<Distributor> distributorList;
        List<VisitPlanDb> visitPlanDbs;
        List<Tipe> tipeList;
        List<Produk> produkList;
        List<Competitor> competitors;
        List<TipePhoto> tipePhotos;
        UserNetwork userNetwork;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(android.trikarya.growth.R.layout.activity_dashboard);
        Toolbar toolbar = (Toolbar) findViewById(android.trikarya.growth.R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(android.trikarya.growth.R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, android.trikarya.growth.R.string.navigation_drawer_open, android.trikarya.growth.R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(android.trikarya.growth.R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        databaseHandler = new DatabaseHandler(this);
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
    }

    private void getNearbyOutlet(int status)
    {
        Toast.makeText(this, "Getting data from server, please wait",
                Toast.LENGTH_LONG).show();
        serverRequest = new ServerRequest(this);
        if(status == 0) {
            serverRequest.getAllOutlet(this, new GetAllDataCallback() {
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
            case R.id.dash_nearby:
                Toast.makeText(Dashboard.this, "Please Wait...",Toast.LENGTH_LONG).show();
                intent = new Intent(this,MapsActivity.class);
                //getNearbyOutlet(0);
                break;
            default:
                intent = null;
//                databaseHandler.deleteAll();
//                getData(0);
//                break;

                //new
                outletList = new ArrayList<Outlet>();
                cityList = new ArrayList<City>();
                distributorList = new ArrayList<Distributor>();
                visitPlanDbs = new ArrayList<VisitPlanDb>();
                tipeList = new ArrayList<Tipe>();
                produkList = new ArrayList<Produk>();
                competitors = new ArrayList<Competitor>();
                tipePhotos = new ArrayList<TipePhoto>();
                JsonCallback jsonCallback = new JsonCallback() {
                    @Override
                    public void Done(JSONObject jsonObject, String message)
                    {
                        if (message.equals(ConnectionHandler.response_message_success) && jsonObject != null) {
                            JSONObject response = null;
                            try {
                                response = new JSONObject(jsonObject.getString("data"));
                                JSONArray jsonArray;
                                if (response.has("kota")) {
                                    jsonArray = response.getJSONArray("kota");
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject jsonResponse = jsonArray.getJSONObject(i);
                                        if (jsonResponse.length() != 0) {
                                            cityList.add(new City(jsonResponse.getInt("id"),
                                                    jsonResponse.getInt("kd_area"), jsonResponse.getString("nm_kota")));
                                        }
                                    }
                                }
                                if (response.has("competitor")) {
                                    jsonArray = response.getJSONArray("competitor");
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject jsonResponse = jsonArray.getJSONObject(i);
                                        if (jsonResponse.length() != 0) {
                                            competitors.add(new Competitor(jsonResponse.getInt("id"),
                                                    jsonResponse.getInt("kd_kota"), jsonResponse.getString("nm_competitor"),
                                                    jsonResponse.getString("alamat")));
                                        }
                                    }
                                }
                                if (response.has("outlet")) {
                                    jsonArray = response.getJSONArray("outlet");
                                    Outlet outlet;
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject jsonResponse = jsonArray.getJSONObject(i);
                                        if (jsonResponse.length() != 0) {
                                            outlet = new Outlet(jsonResponse.getInt("kd_outlet"),
                                                    jsonResponse.getInt("kd_kota"), jsonResponse.getInt("kd_user"),
                                                    jsonResponse.getInt("kd_dist"), jsonResponse.getString("nm_outlet"),
                                                    jsonResponse.getString("almt_outlet"), jsonResponse.getInt("kd_tipe"),
                                                    jsonResponse.getString("rank_outlet"), jsonResponse.getString("kodepos"),
                                                    jsonResponse.getString("reg_status"), jsonResponse.getString("latitude"),
                                                    jsonResponse.getString("longitude"));
                                            outlet.setStatus_area(jsonResponse.getInt("status_area"));
                                            outletList.add(outlet);
                                        }
                                    }
                                }
                                if (response.has("distributor")) {
                                    jsonArray = response.getJSONArray("distributor");
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject jsonResponse = jsonArray.getJSONObject(i);
                                        if (jsonResponse.length() != 0) {
                                            distributorList.add(new Distributor(jsonResponse.getInt("id"),
                                                    jsonResponse.getString("kd_dist"), jsonResponse.getString("kd_tipe"),
                                                    jsonResponse.getInt("kd_kota"), jsonResponse.getString("nm_dist"),
                                                    jsonResponse.getString("almt_dist"), jsonResponse.getString("telp_dist")));
                                        }
                                    }
                                }
                                if (response.has("tipe")) {
                                    jsonArray = response.getJSONArray("tipe");
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject jsonResponse = jsonArray.getJSONObject(i);
                                        if (jsonResponse.length() != 0) {
                                            tipeList.add(new Tipe(jsonResponse.getInt("id"),
                                                    jsonResponse.getString("nm_tipe")));
                                        }
                                    }
                                }
                                if (response.has("visitplan")) {
                                    jsonArray = response.getJSONArray("visitplan");
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject jsonResponse = jsonArray.getJSONObject(i);
                                        if (jsonResponse.length() != 0) {
                                            visitPlanDbs.add(new VisitPlanDb(jsonResponse.getInt("id"),
                                                    jsonResponse.getInt("kd_outlet"), jsonResponse.getString("date_visit"),
                                                    jsonResponse.getString("date_create_visit"), jsonResponse.getInt("approve_visit"),
                                                    jsonResponse.getInt("status_visit"), jsonResponse.getString("date_visiting"),
                                                    jsonResponse.getString("skip_order_reason"), jsonResponse.getString("skip_reason")));
                                        }
                                    }
                                }
                                if (response.has("produk")) {
                                    jsonArray = response.getJSONArray("produk");
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject jsonResponse = jsonArray.getJSONObject(i);
                                        if (jsonResponse.length() != 0) {
                                            produkList.add(new Produk(jsonResponse.getInt("id"), jsonResponse.getString("kd_produk"),
                                                    jsonResponse.getString("nm_produk")));
                                        }
                                    }
                                }
                                if (response.has("tipe_photo")) {
                                    jsonArray = response.getJSONArray("tipe_photo");
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject jsonResponse = jsonArray.getJSONObject(i);
                                        if (jsonResponse.length() != 0) {
                                            tipePhotos.add(new TipePhoto(jsonResponse.getInt("id"),
                                                    jsonResponse.getString("nama_tipe")));
                                        }
                                    }
                                }
                                databaseHandler.deleteAll();
                                insertCityToDB(cityList);
                                insertDistributorToDB(distributorList);
                                insertOutletToDB(outletList);
                                insertTipeToDB(tipeList);
                                insertVisitPlanToDB(visitPlanDbs);
                                insertProdukToDB(produkList);
                                insertCompetitor(competitors);
                                insertTipePhoto(tipePhotos);
                                User user = databaseHandler.getUser();

                                //Log.d("UserData", String.valueOf(databaseHandler.getUser()));

                                user.setStatus(1);
                                databaseHandler.updateUser(user);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else
                            userNetwork.getAllData(databaseHandler.getUser().getKode(), databaseHandler.getUser().getKd_area(), this, true);
                    }
                };
                try {
                    databaseHandler.deleteAll();
                    Log.d("getAllData 3 >> ", String.valueOf("https://trikarya.growth.co.id/" + "getAll/" +databaseHandler.getUser().getKode()+"/"+databaseHandler.getUser().getKd_area()));
                    userNetwork.getAllData(databaseHandler.getUser().getKode(), databaseHandler.getUser().getKd_area(), jsonCallback, true);
                    break;
                }
                catch (Exception e){
                    e.printStackTrace();
                }

        }
        if(intent != null) {
            startActivity(intent);
            this.finish();
        }
    }

    private void insertCompetitor(List<Competitor> competitors){
        int count = competitors.size();
        for(int i = 0; i < count; i++)
            databaseHandler.createCompetitor(competitors.get(i));
    }
    private void insertVisitPlanToDB(List<VisitPlanDb> visitPlanDbs) {
        int count = visitPlanDbs.size();
        for(int i = 0; i < count; i++)
            databaseHandler.createVisitPlan(visitPlanDbs.get(i));
    }
    private void insertProdukToDB(List<Produk> produkList) {
        int count = produkList.size();
        for(int i = 0; i < count; i++)
            databaseHandler.createProduk(produkList.get(i));
    }

    private void insertTipeToDB(List<Tipe> tipeList) {
        int count = tipeList.size();
        for(int i = 0; i < count; i++)
            databaseHandler.createTipe(tipeList.get(i));
    }
    private void insertTipePhoto(List<TipePhoto> tipeList) {
        int count = tipeList.size();
        for(int i = 0; i < count; i++)
            databaseHandler.createTipePhoto(tipeList.get(i));
    }
    private void insertDistributorToDB(List<Distributor> distributorList) {
        int count = distributorList.size();
        for(int i = 0; i < count; i++)
            databaseHandler.createDistributor(distributorList.get(i));
    }

    private void insertCityToDB(List<City> cityList) {
        int count = cityList.size();
        for(int i = 0; i < count; i++)
            databaseHandler.createCity(cityList.get(i));
    }

    private void insertOutletToDB(List<Outlet> outletList) {
        int count = outletList.size();
        for(int i = 0; i < count; i++)
            databaseHandler.createOutlet(outletList.get(i));
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
}

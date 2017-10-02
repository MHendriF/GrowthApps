package android.trikarya.growth;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import master.City;
import master.Competitor;
import master.DatabaseHandler;
import master.Distributor;
import master.Outlet;
import master.Produk;
import master.Tipe;
import master.TipePhoto;
import master.User;
import master.VisitPlanDb;
import fcm.StringHelper;
import fcm.TempIDFCM;
import network.ConnectionHandler;
import network.JsonCallback;
import network.UserNetwork;

import static java.lang.Thread.sleep;

/**
 *   PT Trikarya Teknologi
 */

public class SplashScreen extends Activity {

    DatabaseHandler databaseHandler;
    Intent intent;
    UserNetwork userNetwork;
    List<Outlet> outletList;
    List<City> cityList;
    List<Distributor> distributorList;
    List<VisitPlanDb> visitPlanDbs;
    List<Tipe> tipeList;
    TempIDFCM tempIDFCM;
    List<Produk> produkList;
    List<Competitor> competitors;
    List<TipePhoto> tipePhotos;
    int toleransi = 0;
    private void waitToken(){
        try{
            sleep(1500);
            if(!StringHelper.isNotEmpty(tempIDFCM.getToken())){
                waitToken();
            }
            else {
                intent = new Intent(SplashScreen.this, Login.class);
                startActivity(intent);
            }
        }catch(InterruptedException e){
            e.printStackTrace();
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        //Fabric.with(this, new Crashlytics(), new CrashlyticsNdk());
        setContentView(R.layout.activity_splash_screen);
        databaseHandler = new DatabaseHandler(this);
        userNetwork = new UserNetwork(this);
        tempIDFCM = new TempIDFCM(getApplicationContext());
        if(databaseHandler.getUserCount() == 0) {
            FirebaseMessaging.getInstance().subscribeToTopic("allDevices");
            Thread timerThread = new Thread(){
                public void run(){
                    waitToken();
                }
            };
            timerThread.start();
        }
        else {
            if (databaseHandler.getUser().getStatus() == 0) {
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
                    public void Done(JSONObject jsonObject, String message) {
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
                                            toleransi = jsonResponse.getInt("toleransi_long");
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
                                        if (jsonResponse.length() != 0 && jsonResponse != null) {
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
                                insertCityToDB(cityList);
                                insertDistributorToDB(distributorList);
                                insertOutletToDB(outletList);
                                insertTipeToDB(tipeList);
                                insertVisitPlanToDB(visitPlanDbs);
                                insertProdukToDB(produkList);
                                insertCompetitor(competitors);
                                insertTipePhoto(tipePhotos);
                                User user = databaseHandler.getUser();
                                user.setToleransi(toleransi);
                                user.setStatus(1);
                                databaseHandler.updateUser(user);
                                intent = new Intent(SplashScreen.this, MainActivity.class);
                                startActivity(intent);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else
                            userNetwork.getAllData(databaseHandler.getUser().getKode(), databaseHandler.getUser().getKd_area(), this, false);
                    }
                };
                userNetwork.getAllData(databaseHandler.getUser().getKode(), databaseHandler.getUser().getKd_area(), jsonCallback, false);
            }
            else {
                Thread timerThread = new Thread(){
                    public void run(){
                        try{
                            sleep(1500);
                        }catch(InterruptedException e){
                            e.printStackTrace();
                        }finally{
                            intent = new Intent(SplashScreen.this, MainActivity.class);
                            startActivity(intent);
                        }

                    }
                };
                timerThread.start();
            }
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
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        finish();
    }
}

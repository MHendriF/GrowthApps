package Master;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.trikarya.growth.R;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import fcm.TempIDFCM;

/**
 *   PT Trikarya Teknologi on 3/3/2016.
 */
public class ServerRequest {
    ProgressDialog progressDialog;
    HurlStack hurlStack;
    DatabaseHandler databaseHandler;
    Calendar calendar = Calendar.getInstance();
    String tgl;
    String token;
    TempIDFCM tempIDFCM;
    GoogleMap mMap;

    public static final int CONNECTION_TIMEOUT = 1000*90;
    //public static final String SERVER_ADDRESS =  "http://localhost/growth2/growth/public/";
    public static final String TAG = "Server";
    public static final String SERVER_ADDRESS =  "http://trikarya.growth.co.id/";

    Context context;
    public ServerRequest(Context context)
    {
        tgl = String.valueOf(calendar.get(calendar.YEAR))+"-"+String.valueOf(calendar.get(calendar.MONTH)+1)+"-"+String.valueOf(calendar.get(calendar.DAY_OF_MONTH))+
                " "+String.valueOf(calendar.get(calendar.HOUR_OF_DAY))+":"+String.valueOf(calendar.get(calendar.MINUTE))+":"+String.valueOf(calendar.get(calendar.SECOND));
        databaseHandler = new DatabaseHandler(context);
        progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(false);


        this.context = context;
        progressDialog.setTitle("Sedang Memproses");
        progressDialog.setMessage("Mohon ditunggu...");
    }
    private TrustManager[] getWrappedTrustManagers(TrustManager[] trustManagers) {
        final X509TrustManager originalTrustManager = (X509TrustManager) trustManagers[0];
        return new TrustManager[]{
                new X509TrustManager() {
                    public X509Certificate[] getAcceptedIssuers() {
                        return originalTrustManager.getAcceptedIssuers();
                    }

                    public void checkClientTrusted(X509Certificate[] certs, String authType) {
                        try {
                            originalTrustManager.checkClientTrusted(certs, authType);
                        } catch (CertificateException ignored) {
                        }
                    }

                    public void checkServerTrusted(X509Certificate[] certs, String authType) {
                        try {
                            originalTrustManager.checkServerTrusted(certs, authType);
                        } catch (CertificateException ignored) {
                        }
                    }
                }
        };
    }

    private SSLSocketFactory getSSLSocketFactory()
            throws CertificateException, KeyStoreException, IOException, NoSuchAlgorithmException, KeyManagementException {
        CertificateFactory cf = CertificateFactory.getInstance("X.509");
        InputStream caInput = context.getResources().openRawResource(R.raw.cert); // this cert file stored in \app\src\main\res\raw folder path

        Certificate ca = cf.generateCertificate(caInput);
        caInput.close();

        KeyStore keyStore = KeyStore.getInstance("BKS");
        keyStore.load(null, null);
        keyStore.setCertificateEntry("ca", ca);

        String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
        TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
        tmf.init(keyStore);

        TrustManager[] wrappedTrustManagers = getWrappedTrustManagers(tmf.getTrustManagers());

        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, wrappedTrustManagers, null);

        return sslContext.getSocketFactory();
    }
    public void fetchUserDataInBackground(User user, GetUserCallback userCallback)
    {
        progressDialog.show();
        new FetchUserDataAsyncTask(user,userCallback).execute();
    }
    public class FetchUserDataAsyncTask extends AsyncTask<Void, Void, User> {
        User user;
        GetUserCallback userCallback;
        String message;

        public FetchUserDataAsyncTask(User user, GetUserCallback userCallback) {
            this.user = user;
            this.userCallback = userCallback;
        }

        @Override
        protected User doInBackground(Void... params) {
            User returnedUser = null;
            //edit
            tempIDFCM = new TempIDFCM(context.getApplicationContext());
            token = tempIDFCM.getToken();
            Log.d("Tokenku", token);

            try {
                //URL url = new URL(SERVER_ADDRESS + "login/" +user.getUsername()+"/"+user.getPassword());
                URL url = new URL(SERVER_ADDRESS + "login/" +user.getUsername()+"/"+user.getPassword()+"/"+token);
                Log.d(TAG, "goto: " + url);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(15000);
                conn.setConnectTimeout(CONNECTION_TIMEOUT);
                conn.connect();
                InputStream inputStream = conn.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                String line = "";
                StringBuilder stringBuilder = new StringBuilder();
                while((line = reader.readLine()) != null)
                {
                    stringBuilder.append(line);
                }
                String response = stringBuilder.toString();
                JSONObject jsonResponse = new JSONObject(response);
                if(jsonResponse.length()!=0)
                {
                    int kode = jsonResponse.getInt("id");
                    int kodeRole = jsonResponse.getInt("kd_role");
                    int kodeArea = jsonResponse.getInt("kd_area");
                    String NIK = jsonResponse.getString("nik");
                    String nama = jsonResponse.getString("nama");
                    String alamat = jsonResponse.getString("alamat");
                    int kd_kota = jsonResponse.getInt("kd_kota");
                    String telepon = jsonResponse.getString("telepon");
                    String path_foto = "";
                    String username = jsonResponse.getString("username");
                    String password= jsonResponse.getString("password");
                    String email = jsonResponse.getString("email_user");
                    String id_gcm = jsonResponse.getString("id_gcm");
                    returnedUser = new User(kode, kodeRole, kd_kota, kodeArea, NIK, nama,
                            alamat, telepon, path_foto, username, password, email,0,id_gcm);
                }
                reader.close();
                inputStream.close();
                conn.disconnect();
            } catch (MalformedURLException e) {
                e.printStackTrace();
                message = e.getMessage();
            } catch (IOException e) {
                e.printStackTrace();
                message = e.getMessage();
            } catch (JSONException e) {
                e.printStackTrace();
                message = e.getMessage();
            }
            return returnedUser;
        }

        @Override
        protected void onPostExecute(User returnedUser) {
            progressDialog.dismiss();
            userCallback.Done(returnedUser);
            super.onPostExecute(returnedUser);
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

    public void storeVisitDataInBackground(VisitPlanDb visitPlanDb, StoreOutletCalback storeOutletCalback)
    {
        progressDialog.show();
        new StoreVisitDataAsyncTask(visitPlanDb,storeOutletCalback).execute();
    }
    public class StoreVisitDataAsyncTask extends AsyncTask<Void, Void, String> {
        VisitPlanDb visitPlanDb;
        StoreOutletCalback storeOutletCalback;
        public StoreVisitDataAsyncTask(VisitPlanDb visitPlanDb, StoreOutletCalback storeOutletCalback) {
            this.visitPlanDb = visitPlanDb;
            this.storeOutletCalback = storeOutletCalback;
        }

        @Override
        protected String doInBackground(Void... params) {
            String response = "";
            try {
                URL url = new URL(SERVER_ADDRESS + "setVisit");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(15000);
                conn.setConnectTimeout(CONNECTION_TIMEOUT);
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);
                conn.setDoInput(true);
                conn.setRequestProperty("Content-Type", "application/json");
                conn.connect();
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("id",visitPlanDb.getKd_visit());
                jsonObject.put("kd_outlet",visitPlanDb.getKd_outlet());
                jsonObject.put("date_visit", visitPlanDb.getDate_visit());
                jsonObject.put("date_create_visit", visitPlanDb.getDate_created());
                jsonObject.put("approve_visit", visitPlanDb.getApprove_visit());
                jsonObject.put("tgl_upload",tgl);
                jsonObject.put("kd_user",databaseHandler.getUser().getKode());
                OutputStream outputStream = conn.getOutputStream();
                DataOutputStream dataOutputStream = new DataOutputStream(outputStream);
                dataOutputStream.write(jsonObject.toString().getBytes("UTF-8"));
                dataOutputStream.flush();
                dataOutputStream.close();
                InputStream inputStream = conn.getInputStream();
                int x = 9;
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                String line = "";
                StringBuilder stringBuilder = new StringBuilder();
                while((line = reader.readLine()) != null)
                {
                    stringBuilder.append(line);
                }
                response = stringBuilder.toString();
                JSONObject jsonResponse = new JSONObject(response);
                if(jsonResponse.length()!=0)
                {
                    response = jsonResponse.getString("status");
                }
                inputStream.close();
                outputStream.close();
                conn.disconnect();
            } catch (MalformedURLException e) {
                e.printStackTrace();
                response = e.getMessage();
            } catch (IOException e) {
                e.printStackTrace();
                response = e.getMessage();
            } catch (JSONException e) {
                e.printStackTrace();
                response = e.getMessage();
            }
            return response;
        }

        @Override
        protected void onPostExecute(String response) {
            progressDialog.dismiss();
            storeOutletCalback.Done(response,0,0);
            super.onPostExecute(response);
        }
    }
    public void submitVisitDataInBackground(VisitPlanDb visitPlanDb,TakeOrder takeOrder, StoreOutletCalback storeOutletCalback)
    {
        progressDialog.show();
        new SubmitVisitDataAsyncTask(visitPlanDb,takeOrder,storeOutletCalback).execute();
    }
    public class SubmitVisitDataAsyncTask extends AsyncTask<Void, Void, String> {
        VisitPlanDb visitPlanDb;
        StoreOutletCalback storeOutletCalback;
        TakeOrder takeOrder;
        public SubmitVisitDataAsyncTask(VisitPlanDb visitPlanDb, TakeOrder takeOrder, StoreOutletCalback storeOutletCalback) {
            this.visitPlanDb = visitPlanDb;
            this.storeOutletCalback = storeOutletCalback;
            this.takeOrder = takeOrder;
        }

        @Override
        protected String doInBackground(Void... params) {
            String response = "";
            try {
                URL url = new URL(SERVER_ADDRESS + "submitVisit");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(15000);
                conn.setConnectTimeout(CONNECTION_TIMEOUT);
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);
                conn.setDoInput(true);
                conn.setRequestProperty("Content-Type", "application/json");
                conn.connect();
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("kd_visit",visitPlanDb.getKd_visit());
                jsonObject.put("status_visit",visitPlanDb.getStatus_visit());
                jsonObject.put("date_visiting", visitPlanDb.getDate_visiting());
                jsonObject.put("skip_order_reason", visitPlanDb.getSkip_order_reason());
                jsonObject.put("id",takeOrder.getId());
                jsonObject.put("kd_produk",takeOrder.getKd_produk());
                jsonObject.put("qty_order", takeOrder.getQty());
                jsonObject.put("satuan",takeOrder.getSatuan());
                jsonObject.put("date_order",takeOrder.getDate_order());
                jsonObject.put("status_order",takeOrder.getStatus());
                jsonObject.put("kd_user",databaseHandler.getUser().getKode());
                OutputStream outputStream = conn.getOutputStream();
                DataOutputStream dataOutputStream = new DataOutputStream(outputStream);
                dataOutputStream.write(jsonObject.toString().getBytes("UTF-8"));
                dataOutputStream.flush();
                dataOutputStream.close();
                InputStream inputStream = conn.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                String line = "";
                StringBuilder stringBuilder = new StringBuilder();
                while((line = reader.readLine()) != null)
                {
                    stringBuilder.append(line);
                }
                response = stringBuilder.toString();
                JSONObject jsonResponse = new JSONObject(response);
                if(jsonResponse.length()!=0)
                {
                    response = jsonResponse.getString("status");
                }
                inputStream.close();
                outputStream.close();
                conn.disconnect();
            } catch (MalformedURLException e) {
                e.printStackTrace();
                response = e.getMessage();
            } catch (IOException e) {
                e.printStackTrace();
                response = e.getMessage();
            } catch (JSONException e) {
                e.printStackTrace();
                response = e.getMessage();
            }
            return response;
        }

        @Override
        protected void onPostExecute(String response) {
            progressDialog.dismiss();
            storeOutletCalback.Done(response,0,0);
            super.onPostExecute(response);
        }
    }
    public void fetchVisitPlanDataInBackground(User user,GetVisitPlanCallback getVisitPlanCallback)
    {
        progressDialog.show();
        new FetchVisitPlanDataAsyncTask(user,getVisitPlanCallback).execute();
    }
    public class FetchVisitPlanDataAsyncTask extends AsyncTask<Void, Void, List<VisitPlanDb>> {
        GetVisitPlanCallback getVisitPlanCallback;
        User user;
        String message = "success";

        public FetchVisitPlanDataAsyncTask(User user, GetVisitPlanCallback getVisitPlanCallback) {
            this.user = user;
            this.getVisitPlanCallback = getVisitPlanCallback;
        }

        @Override
        protected List<VisitPlanDb> doInBackground(Void... params) {
            List<VisitPlanDb> visitPlanDbs = new ArrayList<VisitPlanDb>();
            try {
                URL url = new URL(SERVER_ADDRESS + "visit/" + user.getKode());
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(15000);
                conn.setConnectTimeout(CONNECTION_TIMEOUT);
                conn.connect();
                InputStream inputStream = conn.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                String line = "";
                StringBuilder stringBuilder = new StringBuilder();
                while((line = reader.readLine()) != null)
                {
                    stringBuilder.append(line);
                }
                String response = stringBuilder.toString();
                if(response.length() > 2) {
                    JSONArray jsonObject = new JSONArray(response);
                    for (int i = 0; i < jsonObject.length(); i++) {
                        JSONObject jsonResponse = jsonObject.getJSONObject(i);
                        int kode = jsonResponse.getInt("id");
                        int kd_outlet = jsonResponse.getInt("kd_outlet");
                        String date_visit = jsonResponse.getString("date_visit");
                        String date_create_visit = jsonResponse.getString("date_create_visit");
                        int approve_visit = jsonResponse.getInt("approve_visit");
                        int status_visit = jsonResponse.getInt("status_visit");
                        String date_visiting= jsonResponse.getString("date_visiting");
                        String skip_order_reason = jsonResponse.getString("skip_order_reason");
                        String skip_reason = jsonResponse.getString("skip_reason");
                        visitPlanDbs.add(new VisitPlanDb(kode,kd_outlet,date_visit,date_create_visit,approve_visit,status_visit,date_visiting,skip_order_reason,skip_reason));
                    }
                }
                reader.close();
                inputStream.close();
                conn.disconnect();
            } catch (MalformedURLException e) {
                e.printStackTrace();
                message = e.getMessage();
            } catch (IOException e) {
                e.printStackTrace();
                message = e.getMessage();
            } catch (JSONException e) {
                e.printStackTrace();
                message = e.getMessage();
            }
            insertVisitPlanToDB(visitPlanDbs);
            return visitPlanDbs;
        }
        @Override
        protected void onPostExecute(List<VisitPlanDb> visitPlanDbs) {
            progressDialog.dismiss();
            getVisitPlanCallback.Done(message);
            super.onPostExecute(visitPlanDbs);
        }
    }
    public void uploadOutlet(final int take_order, final Outlet outlet,final Context context, final String bitmap, final StoreOutletCalback storeOutletCalback){
        progressDialog.setMessage("Sedang Mengunggah data...");
        progressDialog.show();
        try{
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("kd_dist",outlet.getKode_distributor());
            jsonObject.put("kd_kota",outlet.getKode_kota());
            jsonObject.put("kd_user",outlet.getKode_user());
            jsonObject.put("nm_outlet",outlet.getNama());
            jsonObject.put("almt_outlet",outlet.getAlamat());
            jsonObject.put("kd_tipe",outlet.getTipe());
            jsonObject.put("rank_outlet",outlet.getRank());
            jsonObject.put("telp_outlet",outlet.getTelpon());
            jsonObject.put("reg_status",outlet.getReg_status());
            jsonObject.put("latitude",outlet.getLatitude());
            jsonObject.put("longitude", outlet.getLongitude());
            jsonObject.put("take_order", take_order);
            jsonObject.put("toleransi_long", databaseHandler.getUser().getToleransi());
            jsonObject.put("tgl_upload",tgl);
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, SERVER_ADDRESS + "setOutlet", jsonObject, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    progressDialog.dismiss();
                    try {
                        if(response.length()!=0)
                        {
                            storeOutletCalback.Done(response.getString("status"),response.getInt("id"),response.getInt("kd_visit"));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    progressDialog.dismiss();
                    storeOutletCalback.Done(error.toString(), 0, 0);
                }
            });

            RequestQueue requestQueue = Volley.newRequestQueue(context, hurlStack);
            RetryPolicy policy = new DefaultRetryPolicy(CONNECTION_TIMEOUT, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            jsonObjectRequest.setRetryPolicy(policy);
            requestQueue.add(jsonObjectRequest);
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public void uploadFoto(final Context context, PhotoActivity photoActivity, final StoreOutletCalback storeOutletCalback) {
        progressDialog.setMessage("Mengunggah Gambar...");
        progressDialog.show();
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("kd_photo", photoActivity.getId());
            jsonObject.put("kd_user", photoActivity.getKd_user());
            jsonObject.put("kd_outlet", photoActivity.getKd_outlet());
            jsonObject.put("kd_kompetitor", photoActivity.getKd_kompetitor());
            jsonObject.put("nm_photo", photoActivity.getNama());
            jsonObject.put("jenis_photo", photoActivity.getKd_tipe());
            jsonObject.put("tgl_take", photoActivity.getTgl_take());
            jsonObject.put("alamat", photoActivity.getAlamat());
            jsonObject.put("keterangan", photoActivity.getKeterangan());
            jsonObject.put("tgl_upload", tgl);
            jsonObject.put("foto", photoActivity.getFoto());
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, SERVER_ADDRESS + "setPhoto", jsonObject, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    progressDialog.dismiss();
                    try {
                        if (response.length() != 0) {
                            storeOutletCalback.Done(response.getString("status"), 0,0);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    progressDialog.dismiss();
                    Toast.makeText(context, error.toString(), Toast.LENGTH_LONG).show();
                }
            });

            RequestQueue requestQueue = Volley.newRequestQueue(context, hurlStack);
            RetryPolicy policy = new DefaultRetryPolicy(CONNECTION_TIMEOUT, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            jsonObjectRequest.setRetryPolicy(policy);
            requestQueue.add(jsonObjectRequest);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void getAllData(final Context context, final User user, final GetAllDataCallback getAllDataCallback){
        progressDialog.setMessage("Mohon ditunggu...");
        progressDialog.show();
        final List<Outlet> outletList = new ArrayList<Outlet>();
        final List<City> cityList = new ArrayList<City>();
        final List<Distributor> distributorList = new ArrayList<Distributor>();
        final List<VisitPlanDb> visitPlanDbs = new ArrayList<VisitPlanDb>();
        final List<Tipe> tipeList = new ArrayList<Tipe>();
        final List<Produk> produkList = new ArrayList<Produk>();
        final List<Competitor> competitors = new ArrayList<Competitor>();
        final List<TipePhoto> tipePhotos = new ArrayList<TipePhoto>();
        try{
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, SERVER_ADDRESS + "getAll/"+user.getKode()
                    +"/"+user.getKd_area(),null,new Response.Listener<JSONObject>() {
                @Override
//                public void onResponse(JSONObject response) {
//                    progressDialog.dismiss();
//                    JSONObject responses = null;
//                    try {
//                        responses = new JSONObject(response.getString("data"));
//                        JSONArray jsonArray;
//                        if(response.has("kota")) {
//                             jsonArray = response.getJSONArray("kota");
//                            for (int i = 0; i < jsonArray.length(); i++) {
//                                JSONObject jsonResponse = jsonArray.getJSONObject(i);
//                                if (jsonResponse.length() != 0) {
//                                    cityList.add(new City(jsonResponse.getInt("id"),
//                                            jsonResponse.getInt("kd_area"), jsonResponse.getString("nm_kota")));
//                                }
//                            }
//                        }
//                        if(response.has("competitor")) {
//                            jsonArray = response.getJSONArray("competitor");
//                            for (int i = 0; i < jsonArray.length(); i++) {
//                                JSONObject jsonResponse = jsonArray.getJSONObject(i);
//                                if (jsonResponse.length() != 0) {
//                                    competitors.add(new Competitor(jsonResponse.getInt("id"),
//                                            jsonResponse.getInt("kd_kota"), jsonResponse.getString("nm_competitor"),
//                                            jsonResponse.getString("alamat")));
//                                }
//                            }
//                        }
//                        if(response.has("outlet")) {
//                            int toleransi = 0;
//                            jsonArray = response.getJSONArray("outlet");
//                            Outlet outlet;
//                            for (int i = 0; i < jsonArray.length(); i++) {
//                                JSONObject jsonResponse = jsonArray.getJSONObject(i);
//                                if (jsonResponse.length() != 0) {
//                                    toleransi = jsonResponse.getInt("toleransi_long");
//                                    outlet = new Outlet(jsonResponse.getInt("kd_outlet"),
//                                            jsonResponse.getInt("kd_kota"), jsonResponse.getInt("kd_user"),
//                                            jsonResponse.getInt("kd_dist"), jsonResponse.getString("nm_outlet"),
//                                            jsonResponse.getString("almt_outlet"), jsonResponse.getInt("kd_tipe"),
//                                            jsonResponse.getString("rank_outlet"), jsonResponse.getString("kodepos"),
//                                            jsonResponse.getString("reg_status"), jsonResponse.getString("latitude"),
//                                            jsonResponse.getString("longitude"));
//                                    outlet.setStatus_area(jsonResponse.getInt("status_area"));
//                                    outletList.add(outlet);
//                                }
//                            }
//                            user.setToleransi(toleransi);
//                            databaseHandler.updateUser(user);
//                        }
//                        if(response.has("distributor")) {
//                            jsonArray = response.getJSONArray("distributor");
//                            for (int i = 0; i < jsonArray.length(); i++) {
//                                JSONObject jsonResponse = jsonArray.getJSONObject(i);
//                                if (jsonResponse.length() != 0) {
//                                    distributorList.add(new Distributor(jsonResponse.getInt("id"),
//                                            jsonResponse.getString("kd_dist"), jsonResponse.getString("kd_tipe"),
//                                            jsonResponse.getInt("kd_kota"), jsonResponse.getString("nm_dist"),
//                                            jsonResponse.getString("almt_dist"), jsonResponse.getString("telp_dist")));
//                                }
//                            }
//                        }
//                        if(response.has("tipe")) {
//                            jsonArray = response.getJSONArray("tipe");
//                            for (int i = 0; i < jsonArray.length(); i++) {
//                                JSONObject jsonResponse = jsonArray.getJSONObject(i);
//                                if (jsonResponse.length() != 0) {
//                                    tipeList.add(new Tipe(jsonResponse.getInt("id"),
//                                            jsonResponse.getString("nm_tipe")));
//                                }
//                            }
//                        }
//                        if(response.has("visitplan")) {
//                            jsonArray = response.getJSONArray("visitplan");
//                            for (int i = 0; i < jsonArray.length(); i++) {
//                                JSONObject jsonResponse = jsonArray.getJSONObject(i);
//                                if (jsonResponse.length() != 0) {
//                                    if(isValid(jsonResponse))
//                                        visitPlanDbs.add(new VisitPlanDb(jsonResponse.getInt("id"),
//                                                jsonResponse.getInt("kd_outlet"), jsonResponse.getString("date_visit"),
//                                                jsonResponse.getString("date_create_visit"), jsonResponse.getInt("approve_visit"),
//                                                jsonResponse.getInt("status_visit"), jsonResponse.getString("date_visiting"),
//                                                jsonResponse.getString("skip_order_reason"), jsonResponse.getString("skip_reason")));
//                                }
//                            }
//                        }
//                        if(response.has("produk")) {
//                            jsonArray = response.getJSONArray("produk");
//                            for (int i = 0; i < jsonArray.length(); i++) {
//                                JSONObject jsonResponse = jsonArray.getJSONObject(i);
//                                if (jsonResponse.length() != 0) {
//                                    produkList.add(new Produk(jsonResponse.getInt("id"), jsonResponse.getString("kd_produk"),
//                                            jsonResponse.getString("nm_produk")));
//                                }
//                            }
//                        }
//                        if(response.has("tipe_photo")) {
//                            jsonArray = response.getJSONArray("tipe_photo");
//                            for (int i = 0; i < jsonArray.length(); i++) {
//                                JSONObject jsonResponse = jsonArray.getJSONObject(i);
//                                if (jsonResponse.length() != 0) {
//                                    tipePhotos.add(new TipePhoto(jsonResponse.getInt("id"),
//                                            jsonResponse.getString("nama_tipe")));
//                                }
//                            }
//                        }
//                        insertCityToDB(cityList);
//                        insertDistributorToDB(distributorList);
//                        insertOutletToDB(outletList);
//                        insertTipeToDB(tipeList);
//                        insertVisitPlanToDB(visitPlanDbs);
//                        insertProdukToDB(produkList);
//                        insertCompetitor(competitors);
//                        insertTipePhoto(tipePhotos);
//                        getAllDataCallback.Done("success");
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                        getAllDataCallback.Done(e.getMessage());
//                    }
//                }
                public void onResponse(JSONObject response) {
                    progressDialog.dismiss();
                    JSONObject response2 = null;
                    try {
                        response2 = new JSONObject(response.getString("data"));
                        JSONArray jsonArray;
                        if(response2.has("kota")) {
                            jsonArray = response2.getJSONArray("kota");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonResponse = jsonArray.getJSONObject(i);
                                if (jsonResponse.length() != 0) {
                                    cityList.add(new City(jsonResponse.getInt("id"),
                                            jsonResponse.getInt("kd_area"), jsonResponse.getString("nm_kota")));
                                }
                            }
                        }
                        if(response2.has("competitor")) {
                            jsonArray = response2.getJSONArray("competitor");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonResponse = jsonArray.getJSONObject(i);
                                if (jsonResponse.length() != 0) {
                                    competitors.add(new Competitor(jsonResponse.getInt("id"),
                                            jsonResponse.getInt("kd_kota"), jsonResponse.getString("nm_competitor"),
                                            jsonResponse.getString("alamat")));
                                }
                            }
                        }
                        if(response2.has("outlet")) {
                            int toleransi = 0;
                            jsonArray = response2.getJSONArray("outlet");
                            Outlet outlet;
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonResponse = jsonArray.getJSONObject(i);
                                if (jsonResponse.length() != 0) {
                                    toleransi = jsonResponse.getInt("toleransi_long");
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
                            user.setToleransi(toleransi);
                            databaseHandler.updateUser(user);
                        }
                        if(response2.has("distributor")) {
                            jsonArray = response2.getJSONArray("distributor");
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
                        if(response2.has("tipe")) {
                            jsonArray = response2.getJSONArray("tipe");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonResponse = jsonArray.getJSONObject(i);
                                if (jsonResponse.length() != 0) {
                                    tipeList.add(new Tipe(jsonResponse.getInt("id"),
                                            jsonResponse.getString("nm_tipe")));
                                }
                            }
                        }
                        if(response2.has("visitplan")) {
                            jsonArray = response2.getJSONArray("visitplan");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonResponse = jsonArray.getJSONObject(i);
                                if (jsonResponse.length() != 0) {
                                    if(isValid(jsonResponse))
                                        visitPlanDbs.add(new VisitPlanDb(jsonResponse.getInt("id"),
                                                jsonResponse.getInt("kd_outlet"), jsonResponse.getString("date_visit"),
                                                jsonResponse.getString("date_create_visit"), jsonResponse.getInt("approve_visit"),
                                                jsonResponse.getInt("status_visit"), jsonResponse.getString("date_visiting"),
                                                jsonResponse.getString("skip_order_reason"), jsonResponse.getString("skip_reason")));
                                }
                            }
                        }
                        if(response2.has("produk")) {
                            jsonArray = response2.getJSONArray("produk");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonResponse = jsonArray.getJSONObject(i);
                                if (jsonResponse.length() != 0) {
                                    produkList.add(new Produk(jsonResponse.getInt("id"), jsonResponse.getString("kd_produk"),
                                            jsonResponse.getString("nm_produk")));
                                }
                            }
                        }
                        if(response2.has("tipe_photo")) {
                            jsonArray = response2.getJSONArray("tipe_photo");
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
                        getAllDataCallback.Done("success");
                    } catch (JSONException e) {
                        e.printStackTrace();
                        getAllDataCallback.Done(e.getMessage());
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    progressDialog.dismiss();
                    getAllDataCallback.Done(error.toString());
                }
            });

            RequestQueue requestQueue = Volley.newRequestQueue(context, hurlStack);
            RetryPolicy policy = new DefaultRetryPolicy(CONNECTION_TIMEOUT, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            jsonObjectRequest.setRetryPolicy(policy);
            requestQueue.add(jsonObjectRequest);
        }
        catch (Exception e)
        {
            getAllDataCallback.Done(e.getMessage());
        }
    }

    private boolean isValid(JSONObject jsonResponse) {
        try {
            jsonResponse.getInt("id");
            jsonResponse.getInt("kd_outlet");
            jsonResponse.getString("date_visit");
            jsonResponse.getString("date_create_visit");
            jsonResponse.getInt("approve_visit");
            jsonResponse.getInt("status_visit");
            jsonResponse.getString("date_visiting");
            jsonResponse.getString("skip_order_reason");
            jsonResponse.getString("skip_reason");
            return true;
        }
        catch (Exception e) {
            return false;
        }
    }

    public void storeGCMID(final Context context, User user, final GetAllDataCallback getAllDataCallback) {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("kd_user", user.getKode());
            jsonObject.put("id_gcm", user.getGcmId());
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, SERVER_ADDRESS + "setIdGCM", jsonObject, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    progressDialog.dismiss();
                    try {
                        if (response.length() != 0) {
                            getAllDataCallback.Done(response.getString("status"));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    progressDialog.dismiss();
                    Toast.makeText(context, error.toString(), Toast.LENGTH_LONG).show();
                }
            });

            RequestQueue requestQueue = Volley.newRequestQueue(context, hurlStack);
            RetryPolicy policy = new DefaultRetryPolicy(CONNECTION_TIMEOUT, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            jsonObjectRequest.setRetryPolicy(policy);
            requestQueue.add(jsonObjectRequest);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public void resetPassword(final Context context, String email, String nik, String telepon, final GetAllDataCallback getAllDataCallback){
        progressDialog.setMessage("Sedang memproses...");
        progressDialog.show();
        try{
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, SERVER_ADDRESS + "resetPass/"+email
                    +"/"+nik+"/"+telepon,null,new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    progressDialog.dismiss();
                    try {
                        getAllDataCallback.Done(response.getString("status"));
                    } catch (JSONException e) {
                        getAllDataCallback.Done(e.getMessage());
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    progressDialog.dismiss();
                    getAllDataCallback.Done(error.toString());
                }
            });

            RequestQueue requestQueue = Volley.newRequestQueue(context, hurlStack);
            RetryPolicy policy = new DefaultRetryPolicy(CONNECTION_TIMEOUT, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            jsonObjectRequest.setRetryPolicy(policy);
            requestQueue.add(jsonObjectRequest);
        }
        catch (Exception e)
        {
            getAllDataCallback.Done(e.getMessage());
        }
    }
}
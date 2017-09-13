package network;

import android.app.ProgressDialog;
import android.content.Context;
import android.trikarya.growth.R;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

/**
 * Created by Hendry on 9/5/2017.
 */

public class ConnectionHandler {
    ProgressDialog progressDialog;
    HurlStack hurlStack;
    public static final int CONNECTION_TIMEOUT = 1000*90;

    public static final String BASE_URL =  "https://trikarya.growth.co.id/",
        response_message_success = "success",
        response_message_error = "error",
        response_data = "data",
        response_message = "message",
        response_status = "status",
        response_pagination = "pagination";;

    public static int post_method = Request.Method.POST, get_method = Request.Method.GET;
    Context context;
    public ConnectionHandler(Context context){
        this.context = context;
        progressDialog = new ProgressDialog(context);
        hurlStack = new HurlStack() {
            @Override
            protected HttpURLConnection createConnection(URL url) throws IOException {
                HttpsURLConnection httpsURLConnection = (HttpsURLConnection) super.createConnection(url);
                try {
                    httpsURLConnection.setSSLSocketFactory(getSSLSocketFactory());
                    httpsURLConnection.setHostnameVerifier(HttpsURLConnection.getDefaultHostnameVerifier());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return httpsURLConnection;
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
    public void MakeConnection(int method, String route, JSONObject prop, final JsonCallback jsonCallback, String title, String message){
        progressDialog.setCancelable(false);
        progressDialog.setTitle(title);
        progressDialog.setMessage(message);
        progressDialog.show();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(method, BASE_URL + route, prop, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                progressDialog.dismiss();
                try {
                    Log.d("Responseee", String.valueOf(response));
                    if(response.getString(response_status).equals(response_message_success)) {
                        String data = "", pagination = "";
                        if (response.has(response_pagination))
                            pagination = response.getString(response_pagination);
                        if (response.has(response_data))
                            data = response.getString(response_data);
                        JSONObject fromServer = new JSONObject();
                        fromServer.put(response_data, data);
                        fromServer.put(response_pagination, pagination);
                        jsonCallback.Done(fromServer, response.getString(response_status));
                    }
                    else
                        jsonCallback.Done(null, response.getString(response_message));
                } catch (Exception e) {
                    e.printStackTrace();
                    jsonCallback.Done(null,e.getMessage());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                error.printStackTrace();
                jsonCallback.Done(null,error.toString());
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(context, hurlStack);
        RetryPolicy policy = new DefaultRetryPolicy(CONNECTION_TIMEOUT, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        jsonObjectRequest.setRetryPolicy(policy);
        requestQueue.add(jsonObjectRequest);
    }
    public void MakeConnection(int method,String route, JSONObject prop, final JsonCallback jsonCallback){
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(method, BASE_URL + route, prop, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                progressDialog.dismiss();
                try {
                    String data = "", pagination = "";
                    if (response.has(response_pagination))
                        pagination = response.getString(response_pagination);
                    if (response.has(response_data))
                        data = response.getString(response_data);
                    JSONObject fromServer = new JSONObject();
                    fromServer.put(response_data,data);
                    fromServer.put(response_pagination,pagination);
                    jsonCallback.Done(fromServer,response.getString(response_status));
                } catch (JSONException e) {
                    e.printStackTrace();
                    jsonCallback.Done(null,e.getMessage());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                error.printStackTrace();
                jsonCallback.Done(null,error.toString());
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(context, hurlStack);
        RetryPolicy policy = new DefaultRetryPolicy(CONNECTION_TIMEOUT, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        jsonObjectRequest.setRetryPolicy(policy);
        requestQueue.add(jsonObjectRequest);
    }
}

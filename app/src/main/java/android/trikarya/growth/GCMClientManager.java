package android.trikarya.growth;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;

import java.io.IOException;

import master.DatabaseHandler;
import master.GetAllDataCallback;
import master.ServerRequest;
import master.User;

/**
 *   PT Trikarya Teknologi
 */
public class GCMClientManager {
    ProgressDialog progressDialog;
    public static final String TAG = "GCMClientManager";
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private DatabaseHandler databaseHandler;
    private User user;
    private GoogleCloudMessaging gcm;
    private String regid;
    private String projectNumber;
    private Activity activity;
    public GCMClientManager(Activity activity, String projectNumber) {
        this.activity = activity;
        this.projectNumber = projectNumber;
        this.gcm = GoogleCloudMessaging.getInstance(activity);
        databaseHandler = new DatabaseHandler(activity);
        user = databaseHandler.getUser();
    }
    private static int getAppVersion(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            throw new RuntimeException("Could not get package name: " + e);
        }
    }
    public void registerIfNeeded(final RegistrationCompletedHandler handler) {
        if (checkPlayServices()) {
            regid = getRegistrationId(getContext());
            if (regid.isEmpty()) {
                registerInBackground(handler);
            }
            else {
                Log.i(TAG, regid);
                handler.onSuccess(regid, false);
            }
        }
        else {
            Log.i(TAG, "No valid Google Play Services APK found.");
        }
    }
    private void registerInBackground(final RegistrationCompletedHandler handler) {
        progressDialog = new ProgressDialog(activity);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Storing data, please wait");
        progressDialog.show();
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                try {
                    if (gcm == null) {
                        gcm = GoogleCloudMessaging.getInstance(getContext());
                    }
                    InstanceID instanceID = InstanceID.getInstance(getContext());
                    regid = instanceID.getToken(projectNumber, GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
                    Log.i(TAG, regid);
                    storeRegistrationId(getContext(), regid);
                } catch (IOException ex) {
                    handler.onFailure("Error :" + ex.getMessage());
                }
                return regid;
            }
            @Override
            protected void onPostExecute(String regId) {
                progressDialog.dismiss();
                if (regId != null) {
                    ServerRequest serverRequest = new ServerRequest(activity);
                    serverRequest.storeGCMID(activity, user, new GetAllDataCallback() {
                        @Override
                        public void Done(String message) {
                            if (!message.equals("success")) {
                                handler.onFailure("Error :" + message);
                            }
                        }
                    });
                    handler.onSuccess(regId, true);
                }
            }
        }.execute(null, null, null);
    }
    private String getRegistrationId(Context context) {
        String registrationId = user.getGcmId();
        if (registrationId.isEmpty() || databaseHandler.getKonfigurasiCount() == 0) {
            Log.i(TAG, "Registration not found.");
            return "";
        }
        return registrationId;
    }
    private void storeRegistrationId(final Context context, String regId) {
        user.setGcmId(regId);
        databaseHandler.updateUser(user);
    }
    private void showMessage(String mes, Context context) {
        AlertDialog.Builder alert = new AlertDialog.Builder(context);
        alert.setMessage(mes);
        alert.setPositiveButton("Got It", null);
        alert.show();
    }
    private SharedPreferences getGCMPreferences(Context context) {
        return getContext().getSharedPreferences(context.getPackageName(),
                Context.MODE_PRIVATE);
    }
    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getContext());
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, getActivity(),
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Log.i(TAG, "This device is not supported.");
            }
            return false;
        }
        return true;
    }
    private Context getContext() {
        return activity;
    }
    private Activity getActivity() {
        return activity;
    }
    public static abstract class RegistrationCompletedHandler {
        public abstract void onSuccess(String registrationId, boolean isNewRegistration);
        public void onFailure(String ex) {
            Log.e(TAG, ex);
        }
    }
}

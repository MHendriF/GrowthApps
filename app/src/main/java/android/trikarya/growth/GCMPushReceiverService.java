package android.trikarya.growth;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

import com.google.android.gms.gcm.GcmListenerService;

import org.json.JSONException;
import org.json.JSONObject;

import master.DatabaseHandler;
import master.VisitPlanDb;

/**
 *   PT Trikarya Teknologi
 */
public class GCMPushReceiverService extends GcmListenerService {

    DatabaseHandler databaseHandler = new DatabaseHandler(GCMPushReceiverService.this);
    @Override
    public void onMessageReceived(String from, Bundle data) {
        String tipe = data.getString("tipe");
        String status = data.getString("status");
        if(status.equals("new")){
            JSONObject jsonResponse = null;
            try {
                jsonResponse = new JSONObject(data.getString("message"));
                if(tipe.equals("VisitPlan")){
                    databaseHandler.createVisitPlan(new VisitPlanDb(jsonResponse.getInt("id"),
                            jsonResponse.getInt("kd_outlet"), jsonResponse.getString("date_visit"),
                            jsonResponse.getString("date_create_visit"),jsonResponse.getInt("approve_visit"),
                            jsonResponse.getInt("status_visit"),jsonResponse.getString("date_visiting"),
                            jsonResponse.getString("skip_order_reason"),jsonResponse.getString("skip_reason")));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            sendNotification(tipe);
        }
    }

    private void sendNotification(String tipe) {
        String message = "Kamu mempunyai "+tipe+" baru.";
        Intent intent;
        if(tipe.equals("VisitPlan")){
            intent = new Intent(this, VisitPlan.class);
        }
        else
            intent = new Intent(this, Dashboard.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        int requestCode = 0;
        PendingIntent pendingIntent = PendingIntent.getActivity(this, requestCode, intent, PendingIntent.FLAG_ONE_SHOT);
        NotificationCompat.Builder noBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(android.trikarya.growth.R.mipmap.ic_launcher)
                .setContentText(message)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);
        NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, noBuilder.build());
    }
}

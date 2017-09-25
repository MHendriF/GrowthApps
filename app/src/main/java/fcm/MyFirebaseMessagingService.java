package fcm;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.trikarya.growth.R;
import android.trikarya.growth.VisitPlan;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONObject;

import java.util.Map;

import master.DatabaseHandler;
import master.VisitPlanDb;

/**
 * Created by Hendry on 8/28/2017.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    public static final String TAG = "FirebaseMessage";
    DatabaseHandler databaseHandler = new DatabaseHandler(MyFirebaseMessagingService.this);
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Map body = remoteMessage.getData();

        JSONObject object = new JSONObject(body);

        String title = remoteMessage.getNotification().getTitle();
        String message = remoteMessage.getNotification().getBody();
        Log.d("Body", String.valueOf(remoteMessage.getData()));

        //log data yang dikirimkan
        Log.d("dataChat",remoteMessage.getData().toString());
        try
        {
            Map<String, String> params = remoteMessage.getData();
            JSONObject objects = new JSONObject(params);
            Log.d("JSON_OBJECT", objects.toString());
        }catch (Exception e){

        }

        try {
            String tipe = (String) object.get("tipe");
            String status = (String) object.get("status");
            Log.d("Tipe", tipe);
            Log.d("Status", status);

            if(status.equals("new")) {
                JSONObject jsonResponse = null;
                try {
                    jsonResponse = new JSONObject((String) object.get("data"));
                    if(tipe.equals("VisitPlan"))
                    {
                        databaseHandler.createVisitPlan(new VisitPlanDb(jsonResponse.getInt("id"),
                                jsonResponse.getInt("kd_outlet"), jsonResponse.getString("date_visit"),
                                jsonResponse.getString("date_create_visit"),jsonResponse.getInt("approve_visit"),
                                jsonResponse.getInt("status_visit"),jsonResponse.getString("date_visiting"),
                                jsonResponse.getString("skip_order_reason"),jsonResponse.getString("skip_reason")));
                        Log.d("Go", "go");
                        sendNotification(title, message, status, tipe);
                        Log.d("Masuk", "masuk");
                    }
//                    else{
//                        Intent intent;
//                        intent = new Intent(this, Dashboard.class);
//                        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_ONE_SHOT);
//                        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
//                                .setSmallIcon(android.trikarya.growth.R.mipmap.ic_launcher)
//                                .setContentTitle(title)
//                                .setContentText(message)
//                                .setAutoCancel(true)
//                                .setContentIntent(pendingIntent);
//                        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//                        notificationManager.notify(0,notificationBuilder.build());
//                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            else if(status.equals("update")){
                sendNotification(title, message, status, tipe);
            }
            else if(status.equals("delete")){
                sendNotification(title, message, status, tipe);
            }

        }catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void sendNotification(String title, String message, String status, String tipe) {
        String notify = "Kamu mempunyai "+tipe+" baru.";
        String visit = "Visit Plan telah di approved";
        String visit_update = "Visit Plan telah di updated";
        String visit_delete = "Visit Plan telah di delete";
        Intent intent;
        if(tipe.equals("VisitPlan")){
            if (status.equals("new")){
                intent = new Intent(this, VisitPlan.class);
                //ngetas
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                PendingIntent pendingIntent = PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_ONE_SHOT);
                NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle(title)
                        .setContentText(visit)
                        .setAutoCancel(true)
                        .setContentIntent(pendingIntent);
                NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.notify(0,notificationBuilder.build());
            }
            else if (status.equals("update")){
                intent = new Intent(this, VisitPlan.class);
                //ngetas
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                PendingIntent pendingIntent = PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_ONE_SHOT);
                NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle(title)
                        .setContentText(visit_update)
                        .setAutoCancel(true)
                        .setContentIntent(pendingIntent);
                NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.notify(0,notificationBuilder.build());
            }
            else if (status.equals("delete")){
                intent = new Intent(this, VisitPlan.class);
                //ngetas
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                PendingIntent pendingIntent = PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_ONE_SHOT);
                NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle(title)
                        .setContentText(visit_delete)
                        .setAutoCancel(true)
                        .setContentIntent(pendingIntent);
                NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.notify(0,notificationBuilder.build());
            }

        }

    }
}

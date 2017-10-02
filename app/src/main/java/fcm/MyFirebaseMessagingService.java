package fcm;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.trikarya.growth.MainActivity;
import android.trikarya.growth.R;
import android.trikarya.growth.VisitPlan;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import master.DatabaseHandler;
import master.ArticleClass;
import master.Outlet;
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
        Log.d("Body", String.valueOf(remoteMessage.getData()));
        JSONObject object = new JSONObject(body);

        String title = remoteMessage.getNotification().getTitle();
        String message = remoteMessage.getNotification().getBody();
        //log data yang dikirimkan
        Log.d("dataChat",remoteMessage.getData().toString());
        try
        {
            Map<String, String> params = remoteMessage.getData();
            JSONObject objects = new JSONObject(params);
            Log.d("JSON_OBJECT", objects.toString());
        }catch (Exception e){
            e.printStackTrace();
        }

        try {
            String tipe = (String) object.get("tipe");
            String status = (String) object.get("status");
            String outlet_name = "";
            JSONObject jsonResponse = null;

            try {
                jsonResponse = new JSONObject((String) object.get("data"));
                if (tipe.equals("VisitPlan"))
                {
                    if (status.equals("new"))
                    {
                        databaseHandler.createVisitPlan(new VisitPlanDb(jsonResponse.getInt("id"),
                                jsonResponse.getInt("kd_outlet"), jsonResponse.getString("date_visit"),
                                jsonResponse.getString("date_create_visit"),jsonResponse.getInt("approve_visit"),
                                jsonResponse.getInt("status_visit"),jsonResponse.getString("date_visiting"),
                                jsonResponse.getString("skip_order_reason"),jsonResponse.getString("skip_reason")));
//                        Outlet outlet = databaseHandler.getOutlet(jsonResponse.getInt("kd_outlet"));
//                        Log.d(TAG, String.valueOf(outlet));
//                        outlet_name = outlet.getNama();
                    }
//                    else{
//                        sendNotification(title, message, status, tipe, outlet_name);
//                    }
                }
                else if (tipe.equals("Article"))
                {
                    if (status.equals("new")) {
                        databaseHandler.createArticle(new ArticleClass(jsonResponse.getInt("id"), jsonResponse.getInt("status")
                                , jsonResponse.getString("judul"), jsonResponse.getString("headline")
                                , jsonResponse.getString("content"), jsonResponse.getString("path_image")
                                , jsonResponse.getString("date_upload")));
                    } else {
                        databaseHandler.deleteArticle(jsonResponse.getInt("id"));
                    }
                }
            } catch (JSONException e){
                e.printStackTrace();
            }
            sendNotification(title, message, status, tipe, outlet_name);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendNotification(String title, String message, String status, String tipe, String name) {
        String notify = "Visit plan anda ke outlet " + name + " telah disetujui.";
        int requestCode = 0;
        if(tipe.equals("VisitPlan")){
            Intent intent = new Intent(this, VisitPlan.class);
            if (status.equals("new")){
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                PendingIntent pendingIntent = PendingIntent.getActivity(this,requestCode,intent,PendingIntent.FLAG_UPDATE_CURRENT);
                NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle(title)
                        .setContentText(message)
                        .setAutoCancel(true)
                        .setContentIntent(pendingIntent)
                        .setDefaults(Notification.DEFAULT_ALL);
                NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.notify(0,notificationBuilder.build());
            }
            else{
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                PendingIntent pendingIntent = PendingIntent.getActivity(this,requestCode,intent,PendingIntent.FLAG_UPDATE_CURRENT);
                NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle(title)
                        .setContentText(message)
                        .setAutoCancel(true)
                        .setContentIntent(pendingIntent)
                        .setDefaults(Notification.DEFAULT_ALL);
                NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.notify(0,notificationBuilder.build());
            }
        }
        else if (tipe.equals("Article"))
        {
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent pendingIntent = PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle(title)
                    .setContentText(message)
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent)
                    .setDefaults(Notification.DEFAULT_ALL);
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(0,notificationBuilder.build());
        }
    }
}

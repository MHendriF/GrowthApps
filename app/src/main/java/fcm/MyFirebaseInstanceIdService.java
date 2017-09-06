package fcm;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.google.firebase.messaging.FirebaseMessaging;

/**
 * Created by Hendry on 8/28/2017.
 */

public class MyFirebaseInstanceIdService extends FirebaseInstanceIdService {
    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        FirebaseMessaging.getInstance().subscribeToTopic("allDevices");
        // TODO: Implement this method to send any registration to your app's servers.
        try {
            TempIDFCM tempIDFCM = new TempIDFCM(getApplicationContext());
            tempIDFCM.SaveToken(refreshedToken);
            Log.d("Registration Token",refreshedToken);
        }
        catch (Exception e){

        }
    }
}

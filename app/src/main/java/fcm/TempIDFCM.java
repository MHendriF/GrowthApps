package fcm;

import android.content.Context;
import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Hendry on 8/31/2017.
 */

public class TempIDFCM {
    public static String prefName = "tokenFCM";
    private Context context;

    public TempIDFCM(Context context) {
        this.context = context;
    }

    public void SaveToken(final String token) {
        SharedPreferences.Editor editor = context.getSharedPreferences(prefName, MODE_PRIVATE).edit();
        editor.putString("token", token);
        editor.putString("isNew", "yes");
        editor.apply();
    }

    public String getToken() {
        SharedPreferences prefs = context.getSharedPreferences(prefName, MODE_PRIVATE);
        String token = prefs.getString("token", "");
        return token;
    }

}

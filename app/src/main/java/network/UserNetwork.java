package network;

import android.content.Context;
import android.util.Log;

/**
 * Created by Hendry on 9/5/2017.
 */

public class UserNetwork extends BaseNetwork{

    public static final String SERVER_ADDRESS =  "https://trikarya.growth.co.id/";

    public UserNetwork(Context context) {
        super(context);
    }
    public void login(String username,String password, String fcmId,JsonCallback jsonCallback){
      connectionHandler.MakeConnection(ConnectionHandler.get_method, "login/" +username+"/"+password+"/"+fcmId, null, jsonCallback,"Processing","Please Wait");
        Log.d("Login >> ", String.valueOf(SERVER_ADDRESS + "login/" +username+"/"+password+"/"+fcmId));
    }
    public void resetPassword(String email, String nik, String telepon,JsonCallback jsonCallback){
        connectionHandler.MakeConnection(ConnectionHandler.get_method,"resetPass/"+email
                +"/"+nik+"/"+telepon,null,jsonCallback,"Processing","Please Wait");
    }
    public void getAllData(int id, int area, JsonCallback jsonCallback, boolean prog){
        if(prog) {
            connectionHandler.MakeConnection(ConnectionHandler.get_method,"getAll/"+id+"/"+area,null,jsonCallback,"Processing","Please Wait");
            //Log.d("getAllData 1 >> ", String.valueOf(SERVER_ADDRESS + "getAll/" +id+"/"+area));
        }
        else {
            connectionHandler.MakeConnection(ConnectionHandler.get_method, "getAll/" + id + "/" + area, null, jsonCallback);
            //Log.d("getAllData 2 >> ", String.valueOf(SERVER_ADDRESS + "getAll/" +id+"/"+area));
        }
    }
}

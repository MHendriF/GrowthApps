package network;

import android.content.Context;

/**
 * Created by Hendry on 9/5/2017.
 */

public class UserNetwork extends BaseNetwork{
    public UserNetwork(Context context) {
        super(context);
    }
//    public void login(String username,String password, String fcmId,JsonCallback jsonCallback){
//      connectionHandler.MakeConnection(ConnectionHandler.get_method, "login/" +username+"/"+password+"/"+fcmId, null, jsonCallback,"Processing","Please Wait");
//    }
    public void login(String username,String password, JsonCallback jsonCallback){
        connectionHandler.MakeConnection(ConnectionHandler.get_method, "login/" +username+"/"+password+"/", null, jsonCallback,"Processing","Please Wait");
    }
    public void resetPassword(String email, String nik, String telepon,JsonCallback jsonCallback){
        connectionHandler.MakeConnection(ConnectionHandler.get_method,"resetPass/"+email
                +"/"+nik+"/"+telepon,null,jsonCallback,"Processing","Please Wait");
    }
    public void getAllData(int id, int area, JsonCallback jsonCallback, boolean prog){
        if(prog)
            connectionHandler.MakeConnection(ConnectionHandler.get_method,"getAll/"+id+"/"+area,null,jsonCallback,"Processing","Please Wait");
        else
            connectionHandler.MakeConnection(ConnectionHandler.get_method,"getAll/"+id+"/"+area,null,jsonCallback);
    }
}

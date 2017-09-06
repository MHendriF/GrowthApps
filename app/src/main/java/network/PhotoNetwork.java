package network;

import android.content.Context;

import org.json.JSONObject;

/**
 * Created by Hendry on 9/5/2017.
 */

public class PhotoNetwork extends BaseNetwork{
    public PhotoNetwork(Context context) {
        super(context);
    }
    public void uploadPhoto(JSONObject jsonObject, JsonCallback jsonCallback){
        connectionHandler.MakeConnection(ConnectionHandler.post_method, "setPhoto", jsonObject, jsonCallback,"Processing","Please Wait");
    }
    public void uploadVisitProblem(JSONObject jsonObject, JsonCallback jsonCallback){
        connectionHandler.MakeConnection(ConnectionHandler.post_method, "setVisitProblem", jsonObject, jsonCallback,"Processing","Please Wait");
    }
    public void uploadPhotoCompetitor(JSONObject jsonObject, JsonCallback jsonCallback){
        connectionHandler.MakeConnection(ConnectionHandler.post_method, "setCompAct", jsonObject, jsonCallback,"Processing","Please Wait");
    }
}

package network;

import android.content.Context;

import org.json.JSONObject;

/**
 * Created by Hendry on 9/5/2017.
 */

public class VisitPlanNetwork extends BaseNetwork{
    public VisitPlanNetwork(Context context) {
        super(context);
    }
    public void createVisit(JSONObject jsonObject, JsonCallback jsonCallback){
        connectionHandler.MakeConnection(ConnectionHandler.post_method, "setVisit", jsonObject, jsonCallback,"Processing","Please Wait");
    }
    public void submitVisit(JSONObject jsonObject, JsonCallback jsonCallback){
        connectionHandler.MakeConnection(ConnectionHandler.post_method, "submitVisit", jsonObject, jsonCallback,"Processing","Please Wait");
    }
    public void getVisitforTO(int id, JsonCallback jsonCallback){
        connectionHandler.MakeConnection(ConnectionHandler.get_method, "getTO/"+id, null, jsonCallback,"Processing","Please Wait");
    }
    public void getVisitTodayforSample(int id, JsonCallback jsonCallback){
        connectionHandler.MakeConnection(ConnectionHandler.get_method, "getSampling/"+id, null, jsonCallback,"Processing","Please Wait");
    }
    public void getVisitTodayforPop(int id, JsonCallback jsonCallback){
        connectionHandler.MakeConnection(ConnectionHandler.get_method, "getPop/"+id, null, jsonCallback,"Processing","Please Wait");
    }
    public void getVisitTodayforStok(int id, JsonCallback jsonCallback){
        connectionHandler.MakeConnection(ConnectionHandler.get_method, "getStok/"+id, null, jsonCallback,"Processing","Please Wait");
    }
    public void postTO(JSONObject jsonObject, JsonCallback jsonCallback){
        connectionHandler.MakeConnection(ConnectionHandler.post_method, "submitTO", jsonObject, jsonCallback,"Processing","Please Wait");
    }
    public void postStock(JSONObject jsonObject, JsonCallback jsonCallback){
        connectionHandler.MakeConnection(ConnectionHandler.post_method, "submitStok", jsonObject, jsonCallback,"Processing","Please Wait");
    }
    public void getPOP(JsonCallback jsonCallback){
        connectionHandler.MakeConnection(ConnectionHandler.get_method, "tpop", null, jsonCallback,"Processing","Please Wait");
    }
    public void postPOP(JSONObject jsonObject, JsonCallback jsonCallback){
        connectionHandler.MakeConnection(ConnectionHandler.post_method, "submitPop", jsonObject, jsonCallback,"Processing","Please Wait");
    }
    public void getSample(JsonCallback jsonCallback){
        connectionHandler.MakeConnection(ConnectionHandler.get_method, "sample", null, jsonCallback,"Processing","Please Wait");
    }
    public void postSample(JSONObject jsonObject, JsonCallback jsonCallback){
        connectionHandler.MakeConnection(ConnectionHandler.post_method, "submitSampling", jsonObject, jsonCallback,"Processing","Please Wait");
    }
    public void deleteVisit(JSONObject jsonObject, JsonCallback jsonCallback){
        connectionHandler.MakeConnection(ConnectionHandler.post_method, "deleteVisit", jsonObject, jsonCallback,"Processing","Please Wait");
    }
}

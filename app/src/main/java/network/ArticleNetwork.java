package network;

import android.content.Context;
import android.util.Log;

/**
 * Created by Hendry on 9/26/2017.
 */

public class ArticleNetwork extends BaseNetwork{
    public static final String SERVER_ADDRESS =  "https://trikarya.growth.co.id/";
    public ArticleNetwork(Context context) {
        super(context);
    }
    public void getList(JsonCallback jsonCallback){
        connectionHandler.MakeConnection(ConnectionHandler.get_method, "article", null, jsonCallback,"Processing","Please Wait");
        Log.d("getArticle >> ", String.valueOf(SERVER_ADDRESS + "article"));
    }
    public void getDetail(int id,JsonCallback jsonCallback){
        connectionHandler.MakeConnection(ConnectionHandler.get_method, "article/"+id, null, jsonCallback);
    }
}

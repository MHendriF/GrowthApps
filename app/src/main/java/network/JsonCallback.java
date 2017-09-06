package network;

import org.json.JSONObject;

/**
 * Created by Hendry on 9/5/2017.
 */

public interface JsonCallback {
    public void Done(JSONObject jsonObject, String message);
}

package tec.psl.demoapiconsumer.DataService;

import org.json.JSONArray;
import org.json.JSONObject;

public interface DataListener {
    public void onDataReady(JSONArray jsonArray);
    public void onDataReady(JSONObject jsonObject);
    public void onFailure(String err);
}

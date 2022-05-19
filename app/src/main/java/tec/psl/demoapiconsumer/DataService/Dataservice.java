package tec.psl.demoapiconsumer.DataService;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import tec.psl.demoapiconsumer.VolleyQueue.QueueSingleton;

public class Dataservice {
    public static final String HOST_IP = "http://10.0.2.2:8080/APIDemo/api/person";
    private Context ctx;

    public Dataservice(Context ctx) {
        this.ctx = ctx;
    }

    public void getAllPersons(DataListener listener){
        JsonArrayRequest request = new JsonArrayRequest(
                Request.Method.GET,
                HOST_IP,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        listener.onDataReady(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        listener.onFailure(error.getMessage());
                    }
                }
        );
        QueueSingleton.getInstance(ctx).addToRequestQueue(request);
    }

    public void registerPerson(JSONObject json, DataListener listener){
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                HOST_IP,
                json,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        listener.onDataReady(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        listener.onFailure(error.getMessage());
                    }
                }
        );
        QueueSingleton.getInstance(ctx).addToRequestQueue(request);
    }
    public void updatePerson(JSONObject json, DataListener listener) {
        try {
            int persId = json.getInt("persId");
            JsonObjectRequest request = new JsonObjectRequest(
                    Request.Method.PUT,
                    HOST_IP + "/" + persId,
                    json,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            listener.onDataReady(response);
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            listener.onFailure(error.getMessage());
                        }
                    }
            );
            QueueSingleton.getInstance(ctx).addToRequestQueue(request);
        }
        catch (JSONException e) {
            Log.d("PSL_LOG", e.getMessage());
        }
    }
    public void deletePerson(int persId, DataListener listener) {
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.DELETE,
                HOST_IP + "/" + persId,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        listener.onDataReady(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        listener.onFailure(error.getMessage());
                    }
                }
        );
        QueueSingleton.getInstance(ctx).addToRequestQueue(request);
    }


}

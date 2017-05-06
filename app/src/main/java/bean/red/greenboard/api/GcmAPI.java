package bean.red.greenboard.api;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.Map;

import bean.red.greenboard.network.NetworkCallbackResult;
import bean.red.greenboard.network.NetworkManager;
import bean.red.greenboard.util.Constants;

/**
 * Created by Shivam Seth on 4/30/2016.
 */
public class GcmAPI {
    public static void registerGCM(final Context mContext, final Map<String,String> parameters , final NetworkCallbackResult callbackResult){
        String requestURL = Constants.BASE_URL + "Insert" + Constants.RESOURCE_FORMAT;
        Log.d("API", ":: request url ::" + requestURL);
        StringRequest postRequest = new StringRequest(Request.Method.POST, requestURL,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.d("Response", response);
                        callbackResult.onSuccess(response);
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Log.d("Error.Response", error.toString());
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams()
            {
                return parameters;
            }
        };
        NetworkManager.getInstance(mContext).getRequestQueue().add(postRequest);
    }
    public static void updateGCM(final Context mContext, final Map<String,String> parameters , final NetworkCallbackResult callbackResult){
        String requestURL = Constants.BASE_URL + "Update" + Constants.RESOURCE_FORMAT;
        Log.d("API", ":: request url ::" + requestURL);
        StringRequest postRequest = new StringRequest(Request.Method.POST, requestURL,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.d("Response", response);
                        callbackResult.onSuccess(response);
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Log.d("Error.Response", error.toString());
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams()
            {
                return parameters;
            }
        };
        NetworkManager.getInstance(mContext).getRequestQueue().add(postRequest);
    }
}

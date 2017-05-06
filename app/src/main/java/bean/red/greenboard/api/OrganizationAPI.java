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
 * Created by Shivam Seth on 4/29/2016.
 */
public class OrganizationAPI {
    public static void checkOrganization(final Context mContext, final Map<String, String> parameters, final NetworkCallbackResult callback) {

        final String requestURL = Constants.BASE_URL + "SelectWhere" + Constants.RESOURCE_FORMAT;
        Log.d("API", ":: request url ::" + requestURL);
        StringRequest postRequest = new StringRequest(Request.Method.POST, requestURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.d("Response", response.toString());
                        callback.onSuccess(response.toString());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Log.d("Error.Response", error + "");
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {

                return parameters;
            }
        };
        NetworkManager.getInstance(mContext).getRequestQueue().add(postRequest);
    }
    public static void checkAllOrganization(final Context mContext, final Map<String, String> parameters, final NetworkCallbackResult callback) {

        final String requestURL = Constants.BASE_URL + "Select" + Constants.RESOURCE_FORMAT;
        Log.d("API", ":: request url ::" + requestURL);
        StringRequest postRequest = new StringRequest(Request.Method.POST, requestURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.d("Response", response.toString());
                        callback.onSuccess(response.toString());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Log.d("Error.Response", error + "");
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {

                return parameters;
            }
        };
        NetworkManager.getInstance(mContext).getRequestQueue().add(postRequest);
    }
    public static void updateOrganisation(final Context mContext, final Map<String,String> parameters , final NetworkCallbackResult callbackResult){
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


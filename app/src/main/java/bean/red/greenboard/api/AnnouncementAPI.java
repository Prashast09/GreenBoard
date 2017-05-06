package bean.red.greenboard.api;

import android.content.Context;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.ArrayList;
import java.util.Map;

import bean.red.greenboard.model.Announcement;
import bean.red.greenboard.network.NetworkCallback;
import bean.red.greenboard.network.NetworkCallbackResult;
import bean.red.greenboard.network.NetworkManager;
import bean.red.greenboard.parser.AnnouncementParser;
import bean.red.greenboard.util.Constants;

/**
 * Created by Shivam Seth on 4/26/2016.
 */
public class AnnouncementAPI {
    public  static ArrayList<Announcement> data;
    public static void getAllAnnouncement(final Context mContext,final Map<String,String> parameters, final NetworkCallback callback){

        String requestURL = Constants.BASE_URL + "SelectWhere" + Constants.RESOURCE_FORMAT;
        Log.d("API", ":: request url ::" + requestURL);
        StringRequest jsonRequest = new StringRequest(Request.Method.POST, requestURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    Log.d("response",response);
                    JSONObject responseJson = new JSONObject(response);
                    data = AnnouncementParser.parseGetAnnouncement(responseJson, mContext);
                    callback.onSuccess(data);
                    Log.d("data",data.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error){

            }


        }
        )
        {
        @Override
        protected Map<String, String> getParams()
        {

            return parameters;
        }

    };
        NetworkManager.getInstance(mContext).getRequestQueue().add(jsonRequest);

    }
    public static void createAnnouncement(final Context mContext, final NetworkCallbackResult callbackResult, final Map<String ,String> parameters){
        String requestURL =Constants.BASE_URL + "InsertGcm" + Constants.RESOURCE_FORMAT;
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
                        Log.d("Error.Response", error+"");
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
    public static void userData(final Context mContext, final NetworkCallbackResult callbackResult, final Map<String ,String> parameters){
        String requestURL =Constants.BASE_URL + "userinfo" + Constants.RESOURCE_FORMAT;
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
                        Log.d("Error.Response", error+"");
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

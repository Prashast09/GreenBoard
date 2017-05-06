package bean.red.greenboard.view.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.annotation.IdRes;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.facebook.FacebookSdk;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnMenuTabClickListener;
import com.squareup.otto.Subscribe;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import bean.red.greenboard.R;
import bean.red.greenboard.api.AnnouncementAPI;
import bean.red.greenboard.api.GcmAPI;
import bean.red.greenboard.events.LandingUpdateEvent;
import bean.red.greenboard.model.Announcement;
import bean.red.greenboard.network.NetworkCallback;
import bean.red.greenboard.network.NetworkCallbackResult;
import bean.red.greenboard.util.BusProvider;
import bean.red.greenboard.util.Constants;
import bean.red.greenboard.view.fragment.FragmentAnnouncement;

import bean.red.greenboard.view.fragment.FragmentIndexActivity;
import bean.red.greenboard.view.fragment.FragmentProfile;
import bean.red.greenboard.view.fragment.FragmentSubscription;


public class MainActivity extends AppCompatActivity implements FragmentIndexActivity.OnFragmentInteractionListener {
    private BottomBar mBottomBar;
    private FloatingActionButton floatingActionButton;
    private ArrayList<Announcement> announcements = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        FacebookSdk.sdkInitialize(getApplicationContext());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        floatingActionButton = (FloatingActionButton) findViewById(R.id.fab);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("GreenBoard");
        toolbar.setLogo(R.drawable.greenboard);
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        setSupportActionBar(toolbar);
        mBottomBar = BottomBar.attach(this, savedInstanceState);
        BusProvider.getInstance().register(this);
        mBottomBar.setItems(R.menu.bottombar_menu);
        mBottomBar.setOnMenuTabClickListener(new OnMenuTabClickListener() {
            @Override
            public void onMenuTabSelected(@IdRes int menuItemId) {
                if (menuItemId == R.id.bottomBarItemOne) {

                    android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
                    android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    if (announcements.size()>0) {
                        FragmentAnnouncement myFrag = new FragmentAnnouncement();
                        myFrag.setData(announcements);
                        fragmentTransaction.replace(R.id.mainActivityContainer, myFrag);
                        fragmentTransaction.commit();
                    }
                    else{
                        FragmentAnnouncement myFrag = new FragmentAnnouncement();
                        fragmentTransaction.replace(R.id.mainActivityContainer, myFrag);
                        fragmentTransaction.commit();
                        makeNetworkRequestListing();
                    }

                    // The user selected item number one.
                }
                if (menuItemId == R.id.bottomBarItemTwo) {
                    android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
                    android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    FragmentSubscription myFrag = new FragmentSubscription();
                    fragmentTransaction.replace(R.id.mainActivityContainer, myFrag);
                    fragmentTransaction.commit();

                }
                if (menuItemId == R.id.bottomBarItemThree) {
                    android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
                    android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    FragmentProfile myFrag = new FragmentProfile();
                    fragmentTransaction.replace(R.id.mainActivityContainer, myFrag);
                    fragmentTransaction.commit();

                }
            }

            @Override
            public void onMenuTabReSelected(@IdRes int menuItemId) {
                if (menuItemId == R.id.bottomBarItemOne) {
                    // The user reselected item number one, scroll your content to top.
                }
            }
        });
        mBottomBar.mapColorForTab(0, ContextCompat.getColor(this, R.color.accent));
        mBottomBar.mapColorForTab(1, ContextCompat.getColor(this, R.color.accent));
        mBottomBar.mapColorForTab(2, ContextCompat.getColor(this, R.color.accent));
        try {
            makeNetworkRequestGcmUpdate();
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }


    public void makeNetworkRequestListing() {
        SharedPreferences settings = getSharedPreferences("UserInfo", Context.MODE_PRIVATE);

        JSONArray rows = new JSONArray();
        rows.put("name");
        rows.put("login_id");
        rows.put("time");
        rows.put("email");
        rows.put("contact_no");
        rows.put("message");
        rows.put("login_provider");
        rows.put("category");

        JSONArray where = new JSONArray();
        JSONObject values = new JSONObject();
        try {
            values.put("column","organisation");
            values.put("value",settings.getString("organisation",null));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        where.put(values);


        Map<String, String> parameters = new HashMap<String, String>();
        parameters.put("table", Constants.LISTING_TABLE);
        parameters.put("rows", rows.toString());
        parameters.put("where",where.toString());
        parameters.put("sort", "ORDER BY time Desc");
        AnnouncementAPI.getAllAnnouncement(this, parameters, new NetworkCallback() {
            @Override
            public void onSuccess(ArrayList<Announcement> result) {
                Log.e("pics", result.toString());
                announcements = result;
                BusProvider.getInstance().post(new LandingUpdateEvent(result));
            }

        });
    }
    @Subscribe
    public void onAnnouncementUpdated(final LandingUpdateEvent event) {
        announcements = event.mAnnouncements;
    }




    public void createAnnouncement(View v) {
        Intent i = new Intent(this, AnnouncementsActivity.class);
        startActivity(i);

    }
    public void ReselectOrganisation(View v){
        Intent reselect = new Intent(this,SelectOrganization.class);
        startActivity(reselect);


    }

    public void Edit_email(View v){
        v.setFocusable(true);
        v.setEnabled(true);
        v.setFocusableInTouchMode(true);
        v.requestFocus();
        EditText x=(EditText) v;
        x.setSelection(x.getText().length());

    }
    @Override
    protected void onNewIntent(Intent intent) {

        super.onNewIntent(intent);


    }
    private void makeNetworkRequestGcmUpdate() throws JSONException {
        JSONArray rows = new JSONArray();
        JSONArray values = new JSONArray();
        JSONArray valuesArray = new JSONArray();
        JSONObject valuesWhere = new JSONObject();
        SharedPreferences settings = getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        try {
            valuesWhere.put("column","gcm_id");
            valuesWhere.put("value",""+settings.getString("GCM",null));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        valuesArray.put(valuesWhere);

        values.put(""+settings.getString("login_id",null));
        rows.put("login_id");

        Map<String,String> parameters = new HashMap<String, String>();
        parameters.put("table", Constants.GCM_TABLE);
        parameters.put("rows", rows.toString());
        parameters.put("values",values.toString());
        parameters.put("where",valuesArray.toString());
        GcmAPI.updateGCM(this,parameters, new NetworkCallbackResult() {
            @Override
            public void onSuccess(String result) {

            }
        });

    }


    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}

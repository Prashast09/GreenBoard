package bean.red.greenboard.view.activity;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Handler;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.ListPopupWindow;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.crashlytics.android.Crashlytics;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import bean.red.greenboard.R;
import bean.red.greenboard.api.GcmAPI;
import bean.red.greenboard.api.SubscriptionAPI;
import bean.red.greenboard.network.NetworkCallbackResult;
import bean.red.greenboard.util.Constants;
import bean.red.greenboard.view.adapter.TutorialPagerAdapter;
import bean.red.greenboard.view.fragment.OnBoardingFragment;
import io.fabric.sdk.android.Fabric;

public class OnBoarding extends AppCompatActivity implements ViewPager.OnPageChangeListener {

    ViewPager viewPager;
    LinearLayout pager_indicator;
    TutorialPagerAdapter mAdapter;
    Integer dotsCount;
    ImageView[] dots;
    LoginButton fbLogin;
    static CallbackManager callbackmanager;
    ProgressDialog dialog;
    int flagLoginCheck=0;
    ImageView popup;
    RelativeLayout phoneContainer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        Fabric.with(this,new Crashlytics());
        setContentView(R.layout.activity_on_boarding);
        initUIElements();
        fbLoginSetup();
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(flagLoginCheck == 0){
                    Glide.with(OnBoarding.this).load("http://www.getfavours.in/greenboard/loginpopup.png").into(popup);

                    popup.setVisibility(View.VISIBLE);
                    popup.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            popup.setVisibility(View.GONE);
                        }
                    });
                    popup.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                        @Override
                        public void onFocusChange(View v, boolean hasFocus) {
                            if(!hasFocus){
                                popup.setVisibility(View.GONE);
                            }
                        }
                    });

                }
                //Do something after 100ms
            }
        }, 2000);
        Bundle bundle1, bundle2, bundle3;
        Fragment fragment = new OnBoardingFragment();
        bundle1 = new Bundle();
        bundle1.putString("title", "Join your Organization");
        bundle1.putString("sub", "Get connected with people around you.");
        bundle1.putInt("img", R.drawable.request);
        fragment.setArguments(bundle1);
        mAdapter.addFragment(fragment);

        fragment = new OnBoardingFragment();
        bundle2 = new Bundle();
        bundle2.putString("title", "Add Subscription");
        bundle2.putString("sub", "Subscribe to channels that best suit your need.");
        bundle2.putInt("img", R.drawable.subscription1);
        fragment.setArguments(bundle2);

        mAdapter.addFragment(fragment);
        fragment = new OnBoardingFragment();
        bundle3 = new Bundle();
        bundle3.putString("title", "Broadcast");
        bundle3.putString("sub", "Broadcast your event and let others know about it");
        bundle3.putInt("img", R.drawable.broadcast1);
        fragment.setArguments(bundle3);
        mAdapter.addFragment(fragment);
        viewPager.setAdapter(mAdapter);
        viewPager.addOnPageChangeListener(this);
        setUiPageViewController();

    }

    private void fbLoginSetup() {
        fbLogin.setReadPermissions(Arrays.asList("email", "public_profile", "user_friends"));
        callbackmanager = CallbackManager.Factory.create();
        fbLogin.registerCallback(callbackmanager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                System.out.println("Success");
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,gender,email,friends");
                new GraphRequest(
                        loginResult.getAccessToken(),
                        "/me",
                        parameters,
                        HttpMethod.GET,
                        new GraphRequest.Callback() {
                            public void onCompleted(GraphResponse response) {
                                JSONObject d = response.getJSONObject();
                                Log.e("Facebok Response",d.toString());
                                String facebookId="";
                                try {
                                    facebookId = d.getString("id");
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                try {
                                    crashLyticsLogUser(d);
                                    makeNetworkRequestSubscriptionSetup(d);
                                    makeNetworkRequestGcmUpdate(d);
                                    dialog = new ProgressDialog(OnBoarding.this); // this = YourActivity
                                    dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                                    dialog.setTitle("Loading...");
                                    dialog.setMessage("Please wait...");
                                    dialog.setIndeterminate(true);
                                    dialog.show();


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }


            /* handle the result */
                            }
                        }
                ).executeAsync();


            }

            @Override
            public void onCancel() {
                // App code
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
            }
        });
    }

    private void crashLyticsLogUser(JSONObject d) {
            // TODO: Use the current user's information
            // You can call any combination of these three methods
        try {
            Crashlytics.setUserIdentifier(d.getString("id"));
            Crashlytics.setUserEmail(d.getString("email"));
            Crashlytics.setUserName(d.getString("name"));
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }



    private void makeNetworkRequestSubscriptionSetup(JSONObject d) {
        JSONArray rows = new JSONArray();
        JSONArray values = new JSONArray();
        try {
            values.put(""+d.getString("id"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        rows.put("login_id");
        Map<String,String> parameters = new HashMap<String, String>();
        parameters.put("table", Constants.SUBSCRIPTION_TABLE);
        parameters.put("rows", rows.toString());
        parameters.put("values",values.toString());
        SubscriptionAPI.subscriptionSetup(this,parameters, new NetworkCallbackResult() {
            @Override
            public void onSuccess(String result) {

                try {
                    JSONObject resultJSON= new JSONObject(result);
                    if(resultJSON.getString("success").equals("0"))
                    {
                        Toast.makeText(getApplicationContext(), "Subscription setup failed", Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }



    private void makeNetworkRequestGcmUpdate(final JSONObject facebookResponse) throws JSONException {

        JSONArray rows = new JSONArray();
        JSONArray values = new JSONArray();
        JSONArray valuesArray = new JSONArray();
        JSONObject valuesWhere = new JSONObject();
        try {
            SharedPreferences settings = getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
            valuesWhere.put("column","gcm_id");
            valuesWhere.put("value",""+settings.getString("GCM",null));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        valuesArray.put(valuesWhere);

        values.put(""+facebookResponse.getString("id"));
        rows.put("login_id");

        Map<String,String> parameters = new HashMap<String, String>();
        parameters.put("table", Constants.GCM_TABLE);
        parameters.put("rows", rows.toString());
        parameters.put("values",values.toString());
        parameters.put("where",valuesArray.toString());
        GcmAPI.updateGCM(this,parameters, new NetworkCallbackResult() {
            @Override
            public void onSuccess(String result) {
                dialog.dismiss();
                SharedPreferences settings = getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = settings.edit();
                try {
                    editor.putString("login_id" , facebookResponse.getString("id"));
                    editor.putString("name", facebookResponse.getString("name"));
                    editor.putString("email", facebookResponse.getString("email"));

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                editor.commit();
                Intent i = new Intent(OnBoarding.this,SelectOrganization.class);
                startActivity(i);
                finish();
            }
        });

    }


    private void initUIElements() {
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        pager_indicator = (LinearLayout) findViewById(R.id.pagerIndicator);
        fbLogin = (LoginButton) findViewById(R.id.login_button);
        mAdapter = new TutorialPagerAdapter(getSupportFragmentManager());
        popup = (ImageView)findViewById(R.id.imagecontainer);
        phoneContainer = (RelativeLayout)findViewById(R.id.phone_container);


    }

    private void setUiPageViewController() {
        dotsCount = mAdapter.getCount();
        dots = new ImageView[dotsCount];
        for (int i = 0; i < dotsCount; i++) {
            dots[i] = new ImageView(this);
            dots[i].setImageDrawable(getResources().getDrawable(R.drawable.non_selected_color));
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(4, 0, 4, 0);
            pager_indicator.addView(dots[i], params);
        }
        dots[0].setImageDrawable(getResources().getDrawable(R.drawable.selected_color));
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

        for (int i = 0; i < dotsCount; i++) {
            dots[i].setImageDrawable(getResources().getDrawable(R.drawable.non_selected_color));
        }
        dots[position].setImageDrawable(getResources().getDrawable(R.drawable.selected_color));
    }

    View.OnClickListener btnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(OnBoarding.this, SelectOrganization.class);
            startActivity(intent);
            finish();
        }
    };

    @Override
    public void onPageScrollStateChanged(int state) {
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackmanager.onActivityResult(requestCode, resultCode, data);
    }
    public void fbButtonClick(View v){
        flagLoginCheck = 1;
        System.out.print("xf");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Glide.clear(popup);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Glide.clear(popup);
    }

}

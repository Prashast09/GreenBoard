package bean.red.greenboard.view.activity;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.ndk.CrashlyticsNdk;
import com.facebook.AccessToken;
import com.facebook.FacebookSdk;

import io.fabric.sdk.android.Fabric;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import bean.red.greenboard.R;
import bean.red.greenboard.api.GcmAPI;
import bean.red.greenboard.network.NetworkCallbackResult;
import bean.red.greenboard.util.Constants;
import bean.red.greenboard.view.service.GCMRegistrationIntentService;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class SplashScreen extends AppCompatActivity {
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    View layout;
    TextView tvG;
    TextView tvB;
    boolean ready;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics(), new CrashlyticsNdk());
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_splash_screen);
        SharedPreferences sharedPref = getSharedPreferences(
                "UserInfo", Context.MODE_PRIVATE);
        String GCMReg = sharedPref.getString("GCM", "");
        if (GCMReg.length() == 0) {
            Intent itent = new Intent(SplashScreen.this, GCMRegistrationIntentService.class);
            startService(itent);
            GCMSetup();

        }
        tvG = (TextView) findViewById(R.id.tvG);
        tvB = (TextView) findViewById(R.id.tvB);
        layout = (RelativeLayout) findViewById(R.id.layoutview);
        startAnimationG(tvG, false, 1200, 400);
        startAnimationB(tvB, true, 1600, 400);


    }
    private void startAnimationG(View targetView, boolean flag, int startDelay, int duration) {
        int coordinates[] = new int[2];
        targetView.getLocationOnScreen(coordinates);
        int endY = coordinates[1];
        ObjectAnimator animator2 = ObjectAnimator.ofFloat(targetView, "translationY", endY, endY - 30, endY);
        animator2.setStartDelay(startDelay);
        animator2.setDuration(duration);
        animator2.start();
        }

    private void startAnimationB(View targetView, boolean flag, int startDelay, int duration) {
        int coordinates[] = new int[2];
        targetView.getLocationOnScreen(coordinates);
        int endY = coordinates[1];
        ObjectAnimator animator2 = ObjectAnimator.ofFloat(targetView, "translationY", endY, endY - 30, endY);
        animator2.setStartDelay(startDelay);
        animator2.setDuration(duration);
        animator2.start();
        if (flag)
            animator2.addListener(listener);
    }

    Animator.AnimatorListener listener = new Animator.AnimatorListener() {
        @Override
        public void onAnimationStart(Animator animation) {

        }

        @Override
        public void onAnimationEnd(Animator animation) {
            SharedPreferences settings = getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
            String gcm = settings.getString("GCM",null);
            if(gcm != null) {
                if (isLoggedIn()) {
                    String organisation = settings.getString("organisation",null);
                    if(organisation!=null) {
                        Intent i = new Intent(SplashScreen.this, MainActivity.class);
                        startActivity(i);
                        finish();
                    }
                    else
                    {
                        Intent i = new Intent(SplashScreen.this,SelectOrganization.class);
                        startActivity(i);
                        finish();
                    }
                } else {
                    Intent i = new Intent(SplashScreen.this, OnBoarding.class);
                    startActivity(i);
                    finish();
                }


            }
        }




        @Override
        public void onAnimationCancel(Animator animation) {

        }

        @Override
        public void onAnimationRepeat(Animator animation) {

        }
    };

    private void GCMSetup() {

        mRegistrationBroadcastReceiver = new BroadcastReceiver() {

//When the broadcast received
//We are sending the broadcast from GCMRegistrationIntentService

            @Override
            public void onReceive(Context context, Intent intent) {
                SharedPreferences sharedPref = getSharedPreferences(
                        "UserInfo", Context.MODE_PRIVATE);
                String GCMReg = sharedPref.getString("GCM", "");
                if (GCMReg.length() == 0) {
                    if (intent.getAction().equals(GCMRegistrationIntentService.REGISTRATION_SUCCESS) && ready == false) {
//Getting the registration token from the intent
                        String token = intent.getStringExtra("token");
//Displaying the token as toast
                        Log.d("GCM", token);
                        makeNetworkRequestGCMRegister(token);
//if the intent is not with success then displaying error messages
                    } else if (intent.getAction().equals(GCMRegistrationIntentService.REGISTRATION_ERROR)) {
                        Toast.makeText(getApplicationContext(), "GCM registration error!", Toast.LENGTH_LONG).show();
//GCMSetup();
                    } else {
                        Toast.makeText(getApplicationContext(), "Error occurred", Toast.LENGTH_LONG).show();
//GCMSetup();
                    }
                }
            }
        };
    }
    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(GCMRegistrationIntentService.REGISTRATION_SUCCESS));
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(GCMRegistrationIntentService.REGISTRATION_ERROR));
    }



    private void makeNetworkRequestGCMRegister(final String token) {

        JSONArray rows = new JSONArray();
        JSONArray values = new JSONArray();
        values.put(""+token);
        rows.put("gcm_id");
        Map<String,String> parameters = new HashMap<String, String>();
        parameters.put("table", Constants.GCM_TABLE);
        parameters.put("rows", rows.toString());
        parameters.put("values",values.toString());

        GcmAPI.registerGCM(this,parameters, new NetworkCallbackResult() {
            @Override
            public void onSuccess(String result) {
                System.out.print(result);
                try {
                    JSONObject response = new JSONObject(result);
                    if (response.getString("success").equals("1")) {
                        SharedPreferences settings = getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = settings.edit();
                        editor.putString("GCM", token);
                        editor.commit();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (isLoggedIn()) {
                    SharedPreferences settings = getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
                    String organisation = settings.getString("organisation",null);
                    if(organisation.length()>0) {
                        Intent i = new Intent(SplashScreen.this, MainActivity.class);
                        startActivity(i);
                        finish();
                    }
                    else {
                        Intent i= new Intent(SplashScreen.this,SelectOrganization.class);
                        startActivity(i);
                        finish();
                    }

                } else {
                    Intent i= new Intent(SplashScreen.this,OnBoarding.class);
                    startActivity(i);
                    finish();
                }


            }
        });
    }

    public boolean isLoggedIn() {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        return accessToken != null;
    }

}
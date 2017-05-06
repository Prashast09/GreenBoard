package bean.red.greenboard.view.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import bean.red.greenboard.R;
import bean.red.greenboard.api.GcmAPI;
import bean.red.greenboard.api.OrganizationAPI;
import bean.red.greenboard.network.NetworkCallbackResult;
import bean.red.greenboard.util.Constants;

public class SelectOrganization extends AppCompatActivity {
    ImageView image;
    String organization_name;
    public boolean organizationSelected = false;
    AutoCompleteTextView organisation;
    ProgressDialog dialog;
    ArrayList<String>spinner = new ArrayList<>();
    ImageView popup;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Glide.clear(popup);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Glide.clear(popup);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_organization);
        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar1);
        popup = (ImageView)findViewById(R.id.imagecontainer);
        toolbar.setTitle("Select Organisation");
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        dialog = new ProgressDialog(SelectOrganization.this); // this = YourActivity
        setSupportActionBar(toolbar);
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                    Glide.with(SelectOrganization.this).load("http://www.getfavours.in/greenboard/thankyou.png").into(popup);
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

        }, 2000);
        SharedPreferences settings = getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        if(settings.getString("login_id",null)== null){
            //makeNetworkRequestGcmUpdate(settings);
        }
        makeNetworkRequestAllOrganization();
        //   organization_name=(EditText)findViewById(R.id.input_name);

        organisation = (AutoCompleteTextView) findViewById(R.id.organisation);
    /*    organization_name.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    // do your stuff here
                   makeNetworkRequestSelectOrganization();
                }
                return false;
            }
        }); */

        organisation.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                makeNetworkRequestAllOrganization();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    organisation.setShowSoftInputOnFocus(false);
                } else {
                    try {
                        final Method method = EditText.class.getMethod(
                                "setShowSoftInputOnFocus"
                                , new Class[]{boolean.class});
                        method.setAccessible(true);
                        method.invoke(organisation, false);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                organisation.showDropDown();
                return false;
            }
        });
    }


    public void RequestOrganisation(View v){
        Intent request = new Intent(this,RequestOrganisation.class);
        startActivity(request);

    }

    private void makeNetworkRequestAllOrganization() {
        JSONArray rows = new JSONArray();
        rows.put("organisation");

        Map<String,String> parameters = new HashMap<String, String>();
        parameters.put("table", Constants.ORGANISATION_TABLE);
        parameters.put("rows", rows.toString());

        OrganizationAPI.checkAllOrganization(this,parameters, new NetworkCallbackResult() {
            @Override
            public void onSuccess(String result ) {
                dialog.dismiss();
                Log.e("pics", result.toString());
                try {
                    JSONObject resultJson = new JSONObject(result);
                    if (resultJson.getString("success").equals("1")){
                        spinner.clear();
                        JSONArray js= resultJson.getJSONArray("details");
                        for( int i=0;i<js.length();i++)
                        {

                            spinner.add(js.getJSONObject(i).getString("organisation"));
                        }
                        ArrayAdapter<String> adapter=new ArrayAdapter<String>(SelectOrganization.this,android.R.layout.simple_list_item_1, android.R.id.text1, spinner);
                        organisation.setAdapter(adapter);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });

    }



    public void submitAction(View v){
        String result =organisation.getText().toString();
        if(result.equals("")){
            Snackbar snackbar = Snackbar
                    .make(SelectOrganization.this.findViewById(R.id.container),"Select an organisation",Snackbar.LENGTH_LONG ).setAction("Got It", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        }
                    });

            snackbar.show();
        }
        else {
            makeNetworkRequestUpdateOrganization();
        }




    }

    private void makeNetworkRequestUpdateOrganization() {
        JSONArray rows = new JSONArray();
        JSONArray values = new JSONArray();
        JSONArray valuesArray = new JSONArray();
        JSONObject valuesWhere = new JSONObject();
        try {
            SharedPreferences settings = getSharedPreferences("UserInfo", Context.MODE_PRIVATE);

            valuesWhere.put("column","login_id");
            valuesWhere.put("value",""+settings.getString("login_id",null));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        valuesArray.put(valuesWhere);

        values.put(organisation.getText().toString());
        rows.put("organisation");

        Map<String,String> parameters = new HashMap<String, String>();
        parameters.put("table", Constants.SUBSCRIPTION_TABLE);
        parameters.put("rows", rows.toString());
        parameters.put("values",values.toString());
        parameters.put("where",valuesArray.toString());
        GcmAPI.updateGCM(this,parameters, new NetworkCallbackResult() {
            @Override
            public void onSuccess(String result) {
                dialog.dismiss();
                SharedPreferences settings = getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = settings.edit();
                editor.putString("organisation" , organisation.getText().toString());
                editor.commit();
                Intent i= new Intent(SelectOrganization.this,MainActivity.class);
                startActivity(i);
                finish();




            }
        });

    }



}
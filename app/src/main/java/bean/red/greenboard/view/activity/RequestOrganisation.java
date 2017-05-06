package bean.red.greenboard.view.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import bean.red.greenboard.R;
import bean.red.greenboard.api.AnnouncementAPI;
import bean.red.greenboard.network.NetworkCallbackResult;
import bean.red.greenboard.util.Constants;

public class RequestOrganisation extends AppCompatActivity {
    EditText name;
    EditText email;
    EditText contact;
    EditText message;
    Map<String ,String > params = new HashMap<String, String>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_organisation);
        Toolbar toolbar = (Toolbar)findViewById(R.id.my_toolbar);
        name =(EditText)findViewById(R.id.et_name);
        email =(EditText)findViewById(R.id.et_email);
        contact =(EditText)findViewById(R.id.et_contact);
        message =(EditText)findViewById(R.id.et_message);
        toolbar.setTitle("Request Registration");
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        final Drawable upArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_material);
        upArrow.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);


        SharedPreferences sharedPref = getSharedPreferences(
                "UserInfo", Context.MODE_PRIVATE);
        String nameUser = sharedPref.getString("name", "");
        name.setText(nameUser);
        String emailUser = sharedPref.getString("email","");
        email.setText(emailUser);

    }
    public void submitAction(View v){
        System.out.println("message");
        if(email.getText().length()==0 && contact.getText().length() == 0){
            Snackbar snackbar = Snackbar
                    .make(findViewById(R.id.main_content),"Please enter Email or Contact details.",Snackbar.LENGTH_LONG ).setAction("Got It.", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        }
                    });

            snackbar.show();
        }
        else if(!isValidEmail(email.getText().toString()) && email.getText().toString().length()>0){
            Snackbar snackbar = Snackbar
                    .make(findViewById(R.id.main_content),"Please enter valid Email to continue.",Snackbar.LENGTH_LONG ).setAction("Got It.", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        }
                    });

            snackbar.show();
        }
        else if (contact.getText().length()>0 && contact.getText().length()<7){
            Snackbar snackbar = Snackbar
                    .make(findViewById(R.id.main_content),"Please enter valid Contact details.",Snackbar.LENGTH_LONG ).setAction("Got It.", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        }
                    });

            snackbar.show();
        }
        else if (message.getText().toString().equals("")){
            Snackbar snackbar = Snackbar
                    .make(findViewById(R.id.main_content),"Please enter your Organisation to continue.",Snackbar.LENGTH_LONG ).setAction("Got It.", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        }
                    });

            snackbar.show();
        }

        else
        {
            makenetworkrequstOrganisationGenerate();
        }


    }

    private void makenetworkrequstOrganisationGenerate() {
        SharedPreferences settings = getSharedPreferences("UserInfo", 0);
        JSONArray rows = new JSONArray();
        rows.put("name");
        rows.put("login_id");
        rows.put("email");
        rows.put("contact");
        rows.put("organisation");

        JSONArray values = new JSONArray();
        values.put(name.getText()+"");
        values.put(settings.getString("login_id",null));
        values.put(email.getText()+"");
        values.put(contact.getText()+"");
        values.put(message.getText()+"");
        params.put("rows", rows.toString());
        params.put("values", values.toString());
        params.put("table", Constants.REQUEST_ORGANISATION_TABLE);
        AnnouncementAPI.createAnnouncement(this, new NetworkCallbackResult() {
            @Override
            public void onSuccess(String result ) {
                try {
                    JSONObject resultJson = new JSONObject(result);
                    if(resultJson.getString("success").equals("1")){
                        redirecttoSelectOrganisation();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.e("pics", result.toString());

            }
        },params);

    }

    private void redirecttoSelectOrganisation() {
        finish();
    }


    public final static boolean isValidEmail(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }

}

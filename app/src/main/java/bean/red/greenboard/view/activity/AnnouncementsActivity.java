package bean.red.greenboard.view.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Struct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import bean.red.greenboard.R;
import bean.red.greenboard.api.AnnouncementAPI;
import bean.red.greenboard.model.ItemData;
import bean.red.greenboard.network.NetworkCallbackResult;
import bean.red.greenboard.util.Constants;
import bean.red.greenboard.view.adapter.SpinnerAdapter;

/**
 * Created by Shivam Seth on 4/26/2016.
 */
public class AnnouncementsActivity extends AppCompatActivity{
    EditText name;
    EditText email;
    EditText contact;
    EditText message;
    Map<String ,String > params = new HashMap<String, String>();
    Spinner category;
    String selectedCategory="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_announcement);
        Toolbar toolbar = (Toolbar)findViewById(R.id.my_toolbar);
        toolbar.setTitle("Create Announcement");
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        final Drawable upArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_material);
        upArrow.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
        name =(EditText)findViewById(R.id.name_container);
        email =(EditText)findViewById(R.id.et_email);
        contact =(EditText)findViewById(R.id.et_contact);
        message =(EditText)findViewById(R.id.et_message);
        category= (Spinner)findViewById(R.id.category);

        SharedPreferences sharedPref = getSharedPreferences(
                "UserInfo", Context.MODE_PRIVATE);
        String nameUser = sharedPref.getString("name", "");
        name.setText(nameUser);
        String emailUser = sharedPref.getString("email","");
        email.setText(emailUser);
        contact.setText(sharedPref.getString("phone",""));
        ArrayList<ItemData> list=new ArrayList<>();
        list.add(new ItemData("Select Category",R.drawable.category));
        list.add(new ItemData("Events",R.drawable.events));
        list.add(new ItemData("Favours",R.drawable.favours));
        list.add(new ItemData("Sports",R.drawable.sports));
        list.add(new ItemData("Offers",R.drawable.offers));
        list.add(new ItemData("Academics",R.drawable.academics));
        list.add(new ItemData("General",R.drawable.general));
        SpinnerAdapter adapter=new SpinnerAdapter(this,
                R.layout.custom_listview,R.id.txt,list);
        category.setAdapter(adapter);

        email.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus){
                    if (!isValidEmail(email.getText().toString()) && email.getText().toString().length()>0){
                        final Snackbar snackbar = Snackbar.make(findViewById(R.id.main_content),"Invalid Email address.",Snackbar.LENGTH_INDEFINITE).setAction("Got it.", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                            }
                        });
                        snackbar.show();
                    }
                }
            }
        });
        category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TextView tv = (TextView)view.findViewById(R.id.txt);
                selectedCategory = tv.getText().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedCategory = "Select Category";
            }
        });
    }
    public void submitAction(View v){
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
        else if (message.getText().toString().trim().equals("") ){
            Snackbar snackbar = Snackbar
                    .make(findViewById(R.id.main_content),"Please enter your Announcement to continue.",Snackbar.LENGTH_LONG ).setAction("Got It.", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        }
                    });

            snackbar.show();
        }
        else if(selectedCategory.equals("Select Category")){
            Snackbar snackbar = Snackbar
                    .make(findViewById(R.id.main_content),"Please select a Category.",Snackbar.LENGTH_LONG ).setAction("Got It.", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        }
                    });

            snackbar.show();
        }

        else
        {
            makenetworkrequstAnnouncementGenerate();
        }


    }
    public void nameClick(View v){
        Snackbar snackbar = Snackbar
                .make(findViewById(R.id.main_content),"Login with different ID to change name.",Snackbar.LENGTH_SHORT).setAction("Got It.", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });

        snackbar.show();

    }

    private void makenetworkrequstAnnouncementGenerate() {
        SharedPreferences settings = getSharedPreferences("UserInfo", 0);
        final SharedPreferences.Editor editor = settings.edit();
        JSONArray rows = new JSONArray();
        rows.put("name");
        rows.put("login_id");
        rows.put("email");
        rows.put("contact_no");
        rows.put("message");
        rows.put("login_provider");
        rows.put("category");
        rows.put("organisation");
        JSONArray values = new JSONArray();
        values.put(name.getText()+"");
        values.put(settings.getString("login_id",null));
        values.put(email.getText()+"");
        values.put(contact.getText()+"");
        values.put(message.getText()+"");
        values.put("Facebook");
        values.put(selectedCategory);
        values.put(settings.getString("organisation",null));

        JSONArray where = new JSONArray();
        JSONObject whereValue = new JSONObject();
        try {
            whereValue.put("column","greenboard_user_subscription.prefference_"+selectedCategory);
            whereValue.put("value",1);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        where.put(whereValue);
        whereValue = new JSONObject();
        try {
            whereValue.put("column","greenboard_user_subscription.organisation");
            whereValue.put("value",settings.getString("organisation",null));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        where.put(whereValue);
        whereValue = new JSONObject();
        try {
            whereValue.put("column"," not greenboard_gcm.gcm_id");
            whereValue.put("value",settings.getString("GCM",null));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        where.put(whereValue);

        params.put("rows", rows.toString());
        params.put("values", values.toString());
        params.put("table", Constants.LISTING_TABLE);
        params.put("where",where.toString());
        AnnouncementAPI.createAnnouncement(this, new NetworkCallbackResult() {
            @Override
            public void onSuccess(String result ) {
                try {
                    JSONObject resultJson = new JSONObject(result);
                    if(resultJson.getString("success").equals("1")){
                        editor.putString("email",email.getText().toString()).commit();
                        editor.putString("phone",contact.getText().toString()).commit();
                        redirecttoIndex();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.e("pics", result.toString());

            }
        },params);

    }

    private void redirecttoIndex() {

        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    public final static boolean isValidEmail(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }
}

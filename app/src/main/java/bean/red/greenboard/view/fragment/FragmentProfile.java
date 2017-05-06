package bean.red.greenboard.view.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.login.widget.LoginButton;


import org.json.JSONException;

import bean.red.greenboard.MyApplication;
import bean.red.greenboard.R;
import bean.red.greenboard.view.activity.MainActivity;
import bean.red.greenboard.view.activity.OnBoarding;
import bean.red.greenboard.view.activity.SelectOrganization;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Shivam Seth on 4/28/2016.
 */
public class FragmentProfile extends Fragment {

    View v;
    Activity activity;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.activity = (MainActivity)activity;
    }

    boolean loggedIn=true;
    CircleImageView profile_pic;
    String slogin_id = "", sname = "", semail_id = "", sph_no = "";
    EditText email_id, ph_no;
    TextView name;
    SharedPreferences settings;
    SharedPreferences.Editor editor;
    LoginButton fb;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        v = inflater.inflate(R.layout.fragment_profile, container, false);

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        settings = getActivity().getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        editor = settings.edit();
        fb=(LoginButton) v.findViewById(R.id.login_button);
        profile_pic = (CircleImageView) v.findViewById(R.id.profile_image);
        name = (TextView) v.findViewById(R.id.text_name);
        email_id = (EditText) v.findViewById(R.id.email_id);
        ph_no = (EditText) v.findViewById(R.id.ph_no);
        slogin_id = settings.getString("login_id", "");
        sname = settings.getString("name", "");
        semail_id = settings.getString("email", "");
        sph_no = settings.getString("phone", "");
        name.setText(sname);
        email_id.setText(semail_id);
        ph_no.setText(sph_no);
        if(!loggedIn){
            Intent intent = new Intent( getActivity(),  bean.red.greenboard.view.activity.OnBoarding.class);
            startActivity(intent);
            //finish();

        }

        Glide.with(getActivity()).load("http://graph.facebook.com/v2.6/" + slogin_id + "/picture?height=256").into(profile_pic);

        email_id.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    if(isValidEmail(email_id.getText().toString())){
                        editor.putString("email",email_id.getText().toString()).commit();
                        Toast.makeText(getContext(),"Your Email has been updated",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        ph_no.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                        editor.putString("phone",ph_no.getText().toString()).commit();
                        Toast.makeText(getContext(),"Your Contact has been updated",Toast.LENGTH_SHORT).show();

                }
            }
        });

        AccessTokenTracker accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken accessToken, AccessToken accessToken2) {
                Log.d("XX", "onCurrentAccessTokenChanged()");
                if (accessToken == null) {
                    // Log in Logic
                    //

                } else if (accessToken2 == null) {
                    if(isAdded()) {
                        Intent onBoarding = new Intent(MyApplication.getInstance(), OnBoarding.class);
                        startActivity(onBoarding);
                        getActivity().finish();
                    }
                }
            }
        };
        return v;
    }

    public final static boolean isValidEmail(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }

}
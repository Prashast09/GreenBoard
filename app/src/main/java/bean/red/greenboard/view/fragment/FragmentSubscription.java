package bean.red.greenboard.view.fragment;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.ListPopupWindow;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import bean.red.greenboard.R;
import bean.red.greenboard.api.SubscriptionAPI;
import bean.red.greenboard.network.NetworkCallbackResult;
import bean.red.greenboard.util.Constants;


/**
 * Created by Shivam Seth on 5/2/2016.
 */
public class FragmentSubscription extends Fragment implements View.OnClickListener {
    ImageView event;
    ImageView academic;
    ImageView sports;
    ImageView general;
    ImageView favours;
    ImageView invitation;
    ImageView director;
    TextView subscription;
    TextView subscription_text;
    CoordinatorLayout coordinatorLayout;
    SharedPreferences sharedPreferences;
    PopupWindow popup;
    Point p;
    View v;
    // String[] text = {"Events[?]", "Favours[?]", "Sports[?]", "Offers[?]", "Academics[?]", "General[?]", "Directors's Desk[?]"};
    String[] text = {"Events", "Favours", "Sports", "Offers", "Academics", "General", "Directors's Desk"};

    String[] description_popup = {"Get information of all the events happening in your organization. Be it your college , school or corporation. Gang up together to thrill in exciting buzz.",
            "When you are good at something don't do it for free! Help the needy and ask for something in return. Might be calling up for Favour retutrn you something exciting. Get connected soon ",
            "Why getting bored this monsoon at home. Catch your players on ground!! Get inspired from Euro Cup to IPL, from Tennis to Badminton. Pull out shots on Pool & Snooker. Watch out for availability of centres right here right now!",
            "Exclusive Offers are waiting for you right now. Hurry Up to stamp on all the vouchers!! Get dressed at special offers from us, catch all the actions in Clubs, or end up in a restraunt with sponsored coupons",
            " Still looking for notes @ exam edge. Share all the information your notebook shouts! Call on students for group discussions and test preparations. School guys can look for mentors to dodge the nervousness. One stop platform to become an academia",
            "Can't poke your nose in any of our subscription. Add a miscelleneous post. Get started and drive the mob alone. Show your charisma the world hasn't seen!",
            "All your official announcements are made through this channel. Support your organisation by following them on GreenBoard!"};
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_subscription, container, false);
        event = (ImageView) v.findViewById(R.id.bt_events);
        academic = (ImageView) v.findViewById(R.id.bt_academics);
        sports = (ImageView) v.findViewById(R.id.bt_sports);
        general = (ImageView) v.findViewById(R.id.bt_general);
        favours = (ImageView) v.findViewById(R.id.bt_favours);
        invitation = (ImageView) v.findViewById(R.id.bt_invitation);
        director = (ImageView) v.findViewById(R.id.bt_director);
        subscription = (TextView) v.findViewById(R.id.tv_subscription);
        subscription_text = (TextView) v.findViewById(R.id.subscription_text);
        Typeface font = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Roboto-Bold.ttf");
        subscription_text.setTypeface(font);
        String fontPath = "fonts/Roboto-Regular.ttf";

        p = new Point();

        sharedPreferences = getActivity().getSharedPreferences("User Info",Context.MODE_PRIVATE);
        int e=0,ac=0,sp=0,of=0,fa=0,ge=0;
        ac= sharedPreferences.getInt("academics",1);
        sp= sharedPreferences.getInt("sports",1);
        of= sharedPreferences.getInt("offers",1);
        fa= sharedPreferences.getInt("favours",1);
        ge= sharedPreferences.getInt("general",1);
        e= sharedPreferences.getInt("events",1);

        if(e==0)showView(event,e);
        else showView(event,e);

        if(ac==0)showView(academic,ac);
        else showView(academic,ac);

        if(sp==0)showView(sports,sp);
        else showView(sports,sp);

        if(of==0)showView(invitation,of);
        else showView(invitation,of);

        if(fa==0)showView(favours,fa);
        else showView(favours,fa);

        if(ge==0)showView(general,ge);
        else showView(general,ge);


        // TODO Auto-generated method stub

        Typeface tf = Typeface.createFromAsset(getActivity().getAssets(), fontPath);
        subscription.setTypeface(tf);
        View.OnClickListener alpha = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getAlpha() == 1.0f) {
                    v.setAlpha(0.5f);
                } else {
                    v.setAlpha(1.0f);
                }
            }
        };
        event.setOnClickListener(FragmentSubscription.this);
        academic.setOnClickListener(FragmentSubscription.this);
        sports.setOnClickListener(FragmentSubscription.this);
        general.setOnClickListener(FragmentSubscription.this);
        favours.setOnClickListener(FragmentSubscription.this);
        invitation.setOnClickListener(FragmentSubscription.this);
        director.setOnClickListener(FragmentSubscription.this);


        return v;
    }


    // text view label
    // Loading Font Face

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.bt_events: {
                if (v.getAlpha() == 1.0f) {
                    makeNetworkRequestSubscribe(v,0,"prefference_Events", 0);

                } else {
                    makeNetworkRequestSubscribe(v,1,"prefference_Events",0);
                }

                subscription_text.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showPopup(getActivity(), p, 0);
                    }
                });

                break;
            }

            case R.id.bt_academics: {
                if (v.getAlpha() == 1.0f) {
                    v.setAlpha(0.5f);
                    makeNetworkRequestSubscribe(v,0,"prefference_Academics", 4);
                } else {
                    v.setAlpha(1.0f);
                    makeNetworkRequestSubscribe(v,1,"prefference_Academics", 4);
                }

                subscription_text.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showPopup(getActivity(), p, 4);
                    }
                });


                break;
            }

            case R.id.bt_sports: {
                if (v.getAlpha() == 1.0f) {
                    makeNetworkRequestSubscribe(v,0,"prefference_Sports", 2);
                } else {
                    v.setAlpha(1.0f);
                    makeNetworkRequestSubscribe(v,1,"prefference_Sports", 2);
                }

                subscription_text.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showPopup(getActivity(), p, 2);
                    }
                });

                break;
            }

            case R.id.bt_general: {
                if (v.getAlpha() == 1.0f) {
                    makeNetworkRequestSubscribe(v,0,"prefference_General", 5);
                } else {
                    v.setAlpha(1.0f);
                    makeNetworkRequestSubscribe(v,1,"prefference_General", 5);
                }

                subscription_text.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showPopup(getActivity(), p, 5);
                    }
                });

                break;
            }

            case R.id.bt_favours: {
                if (v.getAlpha() == 1.0f) {
                    makeNetworkRequestSubscribe(v,0,"prefference_Favours", 1);
                } else {
                    v.setAlpha(1.0f);
                    makeNetworkRequestSubscribe(v,1,"prefference_Favours", 1);
                }

                subscription_text.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        showPopup(getActivity(), p, 1);
                    }
                });

                break;
            }


            case R.id.bt_invitation: {
                if (v.getAlpha() == 1.0f) {
                    makeNetworkRequestSubscribe(v,0,"prefference_Offers", 3);
                } else {
                    v.setAlpha(1.0f);
                    makeNetworkRequestSubscribe(v,1,"prefference_Offers", 3);
                }

                subscription_text.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showPopup(getActivity(), p, 3);
                    }
                });

                break;
            }


            case R.id.bt_director: {
                subscription_text.setText(text[6]);
                subscription_text.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showPopup(getActivity(), p, 6);
                    }
                });
                Snackbar snackbar = Snackbar
                        .make(getActivity().findViewById(R.id.coordinatorLayout), "Sorry You cannot unsubscribe from this!!! ", Snackbar.LENGTH_LONG)
                .setAction("Know More", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showPopup(getActivity(), p, 6);
                    }
                });
                snackbar.show();

                break;
            }

        }
    }

    private void showPopup(final Activity context, Point p, int x) {
        int popupWidth = 500;
        int popupHeight = 350;

        // Inflate the popup_layout.xml
        CardView viewGroup=null;
        LayoutInflater layoutInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = layoutInflater.inflate(R.layout.popup_layout, viewGroup);

        LinearLayout ll = (LinearLayout)layout.findViewById(R.id.linear1);

        // Creating the PopupWindow
        final PopupWindow popup = new PopupWindow(context);
        popup.setContentView(layout);
        popup.setWidth(ListPopupWindow.WRAP_CONTENT);
        popup.setHeight(ListPopupWindow.WRAP_CONTENT);

        switch (x) {
            case 0:{
                ll.setBackgroundColor(getResources().getColor(R.color.events));
                break;
            }

            case 1:{
                ll.setBackgroundColor(getResources().getColor(R.color.favours));
                break;
            }

            case 2:{
                ll.setBackgroundColor(getResources().getColor(R.color.sports));
                break;
            }

            case 3:{
                ll.setBackgroundColor(getResources().getColor(R.color.offers));
                break;
            }

            case 4:{
                ll.setBackgroundColor(getResources().getColor(R.color.academics));
                break;
            }

            case 5:{
                ll.setBackgroundColor(getResources().getColor(R.color.general));
                break;
            }

            case 6:{
                ll.setBackgroundColor(getResources().getColor(R.color.desk));
                break;
            }
        }
        popup.setFocusable(true);
        popup.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                v.setAlpha(1f);
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            v.setAlpha(0.2f);
        }

        // Some offset to align the popup a bit to the right, and a bit down, relative to button's position.
        int OFFSET_X = 0;
        int OFFSET_Y = 0;

        // Clear the default translucent background
        popup.setBackgroundDrawable(new BitmapDrawable());

        // Displaying the popup at the specified location, + offsets.
        popup.showAtLocation(layout, Gravity.CENTER_HORIZONTAL, p.x + OFFSET_X, p.y + OFFSET_Y);

        TextView title = (TextView) layout.findViewById(R.id.title);
        TextView subtitle = (TextView) layout.findViewById(R.id.subtitle);
        title.setText(text[x]);
        subtitle.setText(description_popup[x]);

        // Getting a reference to Close button, and close the popup when clicked.
        Button close = (Button) layout.findViewById(R.id.ok);
        close.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                popup.dismiss();
                v.setAlpha(1f);
            }
        });
    }

    public void showNotification(final int pos)
    {
        final Snackbar snackbar = Snackbar
                .make(getActivity().findViewById(R.id.coordinatorLayout), "You have unsubscribed from "+text[pos], Snackbar.LENGTH_LONG)
                .setAction("Know More", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showPopup(getActivity(), p, pos);
                    }
                });
        snackbar.show();
    }


    private void makeNetworkRequestSubscribe(final View v, final int i, String prefference_favours , final int pos) {
        JSONArray rows = new JSONArray();
        JSONArray values = new JSONArray();
        JSONArray valuesArray = new JSONArray();
        JSONObject valuesWhere = new JSONObject();
        try {
            SharedPreferences settings = getActivity().getSharedPreferences("UserInfo", Context.MODE_PRIVATE);

            valuesWhere.put("column","login_id");
            valuesWhere.put("value",""+settings.getString("login_id",null));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        valuesArray.put(valuesWhere);

        values.put(""+i);
        rows.put(prefference_favours);

        final Map<String,String> parameters = new HashMap<String, String>();
        parameters.put("table", Constants.SUBSCRIPTION_TABLE);
        parameters.put("rows", rows.toString());
        parameters.put("values",values.toString());
        parameters.put("where",valuesArray.toString());
        SubscriptionAPI.updateSubscription(getActivity(),parameters, new NetworkCallbackResult() {
            @Override
            public void onSuccess(String result) {

                SharedPreferences sharedPref= getActivity().getSharedPreferences("User Info",Context.MODE_PRIVATE);
                SharedPreferences.Editor editor=sharedPref.edit();

                if(i == 0){
                    v.setAlpha(0.5f);
                    subscription_text.setText(text[pos]);
                    showNotification(pos);
                }
                else {
                    v.setAlpha(1.0f);
                    subscription_text.setText(text[pos]);
                    showNotification1(pos);
                }

                switch (v.getId()) {

                    case R.id.bt_events:{
                        if(i==0)editor.putInt("events",0).commit();
                        else editor.putInt("events",1).commit();
                        break;
                    }

                    case R.id.bt_academics:{
                        if(i==0)editor.putInt("academics",0).commit();
                        else editor.putInt("academics",1).commit();
                        break;
                    }

                    case R.id.bt_sports:{
                        if(i==0)editor.putInt("sports",0).commit();
                        else editor.putInt("sports",1).commit();
                        break;
                    }

                    case R.id.bt_invitation:{
                        if(i==0)editor.putInt("offers",0).commit();
                        else editor.putInt("offers",1).commit();
                        break;
                    }

                    case R.id.bt_favours:{
                        if(i==0)editor.putInt("favours",0).commit();
                        else editor.putInt("favours",1).commit();
                        break;
                    }

                    case R.id.bt_general:{
                        if(i==0)editor.putInt("general",0).commit();
                        else editor.putInt("general",1).commit();
                        break;
                    }

                }
            }
        });


    }

    private void showNotification1(final int pos) {
        Snackbar snackbar = Snackbar
                .make(getActivity().findViewById(R.id.coordinatorLayout), "You have subscribed to "+text[pos], Snackbar.LENGTH_LONG)
                .setAction("Know More", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showPopup(getActivity(),p,pos);
                    }
                });

        snackbar.show();
    }


    public void showView(View v,int value){
        if(value == 0)
            v.setAlpha(0.5f);
        else
            v.setAlpha(1.0f);
    }
}
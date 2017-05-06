package bean.red.greenboard.view.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.otto.Subscribe;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import bean.red.greenboard.R;
import bean.red.greenboard.api.AnnouncementAPI;
import bean.red.greenboard.events.LandingUpdateEvent;
import bean.red.greenboard.model.Announcement;
import bean.red.greenboard.network.NetworkCallback;
import bean.red.greenboard.util.BusProvider;
import bean.red.greenboard.util.Constants;
import bean.red.greenboard.view.activity.MainActivity;
import bean.red.greenboard.view.adapter.AnnouncementAdapter;

/**
 * Created by Shivam Seth on 4/28/2016.
 */
public class FragmentAnnouncement extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    public ArrayList<Announcement> announcements = new ArrayList<>();
    private RecyclerView.Adapter mAdapter;
    public AnnouncementAdapter announcementAdapter;
    private RecyclerView listView;
    private RecyclerView.LayoutManager mLayoutManager;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BusProvider.getInstance().register(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_announcement, container, false);
        int x =announcements.size();
//        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
//        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        listView =(RecyclerView)v.findViewById(R.id.landing_rv);
        swipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.swiperefresh);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setRefreshing(true);
//        listView.setLayoutManager(layoutManager);
        if(announcements.size()>0){
            setUIRecycler();
        }
        return v;
    }

    private void setUIRecycler() {
        mLayoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
        listView .setLayoutManager(mLayoutManager);
        mAdapter = new AnnouncementAdapter(announcements,getActivity());
        listView.setAdapter(mAdapter);
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(false);                                    }
        });
    }

    @Subscribe
    public void onAnnouncementUpdated(final LandingUpdateEvent event){
        mLayoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
        listView .setLayoutManager(mLayoutManager);
        mAdapter = new AnnouncementAdapter(event.mAnnouncements,getActivity());
        listView.setAdapter(mAdapter);
        swipeRefreshLayout.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        announcements = event.mAnnouncements;
                                        swipeRefreshLayout.setRefreshing(false);                                    }
                                }
        );

    }

    public void setData(ArrayList<Announcement> announcements){
        this.announcements = announcements;

    }

    @Override
    public void onRefresh() {
        swipeRefreshLayout.setRefreshing(true);
        makeNetworkRequestListing();

        //announcementAdapter.updateList(announcements);

    }
    public void makeNetworkRequestListing() {
        SharedPreferences settings = getActivity().getSharedPreferences("UserInfo", Context.MODE_PRIVATE);

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
        AnnouncementAPI.getAllAnnouncement(getActivity(), parameters, new NetworkCallback() {
            @Override
            public void onSuccess(ArrayList<Announcement> result) {
                Log.e("pics", result.toString());
                announcements = result;
                BusProvider.getInstance().post(new LandingUpdateEvent(result));
            }

        });
    }
}

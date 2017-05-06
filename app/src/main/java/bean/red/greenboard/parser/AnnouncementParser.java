package bean.red.greenboard.parser;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import bean.red.greenboard.model.Announcement;

/**
 * Created by Shivam Seth on 4/26/2016.
 */
public class AnnouncementParser {

    public static ArrayList<Announcement> parseGetAnnouncement(JSONObject jsonAnnouncement, Context context) {
        ArrayList<Announcement> mAnnouncement = new ArrayList<>();

        try {
            JSONArray jsonArray = jsonAnnouncement.getJSONArray("details");
            for (int i = 0; i < jsonArray.length(); i++) {
                Announcement announcement = new Announcement();
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Log.d("Parser jsonObject ", jsonObject.toString());
                announcement.name = ""+jsonObject.get("name");
                announcement.login_id = ""+jsonObject.get("login_id");
                announcement.time = ""+jsonObject.get("time");
                announcement.email = ""+jsonObject.get("email");
                announcement.contact_no = ""+jsonObject.get("contact_no");
                announcement.message = ""+jsonObject.get("message");
                announcement.login_provider = ""+jsonObject.get("login_provider");
                announcement.category = ""+ jsonObject.getString("category");
                mAnnouncement.add(announcement);

            }
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        return  mAnnouncement;
    }
}

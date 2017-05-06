package bean.red.greenboard.network;

import java.util.ArrayList;

import bean.red.greenboard.model.Announcement;

/**
 * Created by Shivam Seth on 4/26/2016.
 */
public interface NetworkCallback {
    void onSuccess(ArrayList<Announcement> result);
}

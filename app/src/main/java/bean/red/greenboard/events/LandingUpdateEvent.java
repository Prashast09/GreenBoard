package bean.red.greenboard.events;


import java.util.ArrayList;

import bean.red.greenboard.model.Announcement;

/**
 * Created by Shivam Seth on 4/26/2016.
 */
public class LandingUpdateEvent {
    public final ArrayList<Announcement> mAnnouncements;

    public LandingUpdateEvent(ArrayList<Announcement> announcements){
        this.mAnnouncements = announcements;
    }
}

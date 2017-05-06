package bean.red.greenboard.view.service;

import android.content.Intent;

import com.google.android.gms.iid.InstanceIDListenerService;

/**
 * Created by Shivam Seth on 4/30/2016.
 */
public class GCMTokenRefreshListenerService extends InstanceIDListenerService {

    //If the token is changed registering the device again
    @Override
    public void onTokenRefresh() {
        Intent intent = new Intent(this, GCMRegistrationIntentService.class);
        startService(intent);
    }
}
package com.macauto.macautoscm.Service;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;



public class ScmInstanceIDService extends FirebaseInstanceIdService {
    private static final String TAG = ScmInstanceIDService.class.getName();

    public ScmInstanceIDService() {
        Log.d(TAG, "ScmInstanceIDService");
    }

    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);

        // TODO: Implement this method to send any registration to your app's servers.
        //sendRegistrationToServer(refreshedToken);
    }

}

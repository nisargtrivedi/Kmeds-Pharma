package fcm;

import android.app.Activity;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;

import Config.BaseURL;
import util.CommonAsyTask;
import util.NameValuePair;

/**
 * Created by Rajesh on 2017-10-07.
 */

public class MyFirebaseRegister {

    Activity _context;

    public MyFirebaseRegister(Activity context) {
        this._context = context;
    }

    public void RegisterUser(String user_id) {
        // [START subscribe_topics]
        String token = FirebaseInstanceId.getInstance().getToken();
        Log.e(_context.toString(), token);
        FirebaseMessaging.getInstance().subscribeToTopic("getpills");
        // [END subscribe_topics]
        makeRegisterFCM(user_id, token);
    }

    private void makeRegisterFCM(String user_id, String token) {

        // adding post values in arraylist
        ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new NameValuePair("user_id", user_id));
        params.add(new NameValuePair("token", token));
        params.add(new NameValuePair("device", "android"));

        // CommonAsyTask class for load data from api and manage response and api
        CommonAsyTask task = new CommonAsyTask(params,
                BaseURL.REGISTER_FCM_URL, new CommonAsyTask.VJsonResponce() {
            @Override
            public void VResponce(String response) {
                Log.e(_context.toString(), response);
            }

            @Override
            public void VError(String responce) {
                Log.e(_context.toString(), responce);
            }
        }, true, _context);
        task.execute();

    }

}

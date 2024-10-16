package com.skappsstore.quizmaster;

import android.app.Application;
import android.content.Context;

import androidx.multidex.MultiDex;

import com.onesignal.OneSignal;

public class MyApplication  extends Application {

private static final String ONESIGNAL_APP_ID = "0e71adb6-1182-46b0-95eb-e77f3a82a8d1";

    @Override
    public void onCreate() {
        super.onCreate();

        // Enable verbose OneSignal logging to debug issues if needed.
        OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE);

        // OneSignal Initialization
        OneSignal.initWithContext(this);
        OneSignal.setAppId(ONESIGNAL_APP_ID);

        // promptForPushNotifications will show the native Android notification permission prompt.
        // We recommend removing the following code and instead using an In-App Message to prompt for notification permission (See step 7)
        OneSignal.promptForPushNotifications();


        }

}
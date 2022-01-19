package com.ids.qasemti.utils;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.ids.qasemti.controller.Activities.ActivitySplash;
import com.ids.qasemti.controller.MyApplication;


public class MyExceptionHandler implements Thread.UncaughtExceptionHandler {

    private Activity activity;
    public MyExceptionHandler(Activity a) {
        activity = a;
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {

       // Crashlytics.log(Log.ERROR, "Exception: ", ex.getMessage());
        //FirebaseCrash.logcat(Log.ERROR, "Exception: ", ex.getMessage());

        FirebaseCrashlytics.getInstance().recordException(ex);
        //FirebaseCrashlytics.getInstance().recordException(ex)

        Intent intent = new Intent(activity, ActivitySplash.class);
        Log.w("excCaught", ex.toString());
        intent.putExtra("crash", true);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_CLEAR_TASK
                | Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(MyApplication.instance.getBaseContext(), 0, intent, PendingIntent.FLAG_ONE_SHOT);
        AlarmManager mgr = (AlarmManager) MyApplication.instance.getBaseContext().getSystemService(Context.ALARM_SERVICE);
        mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 100, pendingIntent);
        activity.finish();
        System.exit(2);
    }
}
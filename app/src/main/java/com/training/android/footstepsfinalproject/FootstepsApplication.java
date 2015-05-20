package com.training.android.footstepsfinalproject;

import android.app.Application;
import android.content.SharedPreferences;

import com.training.android.footstepsfinalproject.data.DbHelper;

/**
 * Created by ttaila on 3/3/15.
 */
public class FootstepsApplication extends Application {

    private static SharedPreferences sharedPreferences;
    private static FootstepsApplication instance;
    private DbHelper dbHelper;

    public FootstepsApplication() {
        super();

    }


    public static SharedPreferences getSharedPreferences(){
        return sharedPreferences;
    }


    public static FootstepsApplication getInstance() {
        if(instance == null) {
            instance = new FootstepsApplication();
        }
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        LocationHelper.getInstance();
    }

//
//    public static DbHelper getDBHelper()
//    {
//        if (dbHelper == null) {
//            dbHelper = DbHelper.getInstance(instance);
//        }
//        return dbHelper;
//    }


}

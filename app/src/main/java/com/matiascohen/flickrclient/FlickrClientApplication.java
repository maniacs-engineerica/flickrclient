package com.matiascohen.flickrclient;

import android.app.Application;

/**
 * Created by fernando on 11/10/15.
 */
public class FlickrClientApplication extends Application {

    private static FlickrClientApplication instance;

    public static FlickrClientApplication getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

}

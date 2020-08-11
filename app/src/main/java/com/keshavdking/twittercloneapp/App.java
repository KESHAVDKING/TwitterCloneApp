package com.keshavdking.twittercloneapp;

import android.app.Application;

import com.parse.Parse;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("HOhWa6h8hVqB3RKvdmeuDr3LFio1Gjk6cLpIQ30f")
                // if defined
                .clientKey("pA5UgVBupygGouhqKvJIoCAmvU4CO6hbDpQ78Wpo")
                .server("https://parseapi.back4app.com/")
                .build()
        );

    }
}

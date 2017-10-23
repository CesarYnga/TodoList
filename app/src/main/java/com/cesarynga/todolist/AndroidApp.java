package com.cesarynga.todolist;

import android.app.Application;

import com.cesarynga.todolist.db.SqliteContext;

import timber.log.Timber;


public class AndroidApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        SqliteContext.init(this);

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }
    }
}

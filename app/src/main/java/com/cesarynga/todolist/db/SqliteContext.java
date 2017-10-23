package com.cesarynga.todolist.db;

import android.content.Context;


public class SqliteContext {

    private static SqliteContext instance;
    private DbHelper dbHelper;

    private SqliteContext(Context context) {
        dbHelper = new DbHelper(context);
    }

    static SqliteContext getSqliteContext() {
        if (instance == null) {
            throw new NullPointerException("SqliteContext has not been initialized properly. Call SqliteContext.init(Context) in your Application.onCreate() method and SqliteContext.terminate() in your Application.onTerminate() method.");
        }
        return instance;
    }

    public static void init(Context context) {
        instance = new SqliteContext(context);
    }

    DbHelper getDbHelper() {
        return dbHelper;
    }
}

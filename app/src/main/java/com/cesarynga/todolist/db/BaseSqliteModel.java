package com.cesarynga.todolist.db;

import android.database.sqlite.SQLiteDatabase;

import static com.cesarynga.todolist.db.SqliteContext.getSqliteContext;


public class BaseSqliteModel {

    protected static SQLiteDatabase getDb() {
        return getSqliteContext().getDbHelper().getDb();
    }
}

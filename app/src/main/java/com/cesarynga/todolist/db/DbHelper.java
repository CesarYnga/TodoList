package com.cesarynga.todolist.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import timber.log.Timber;


class DbHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "TodoList.db";

    private static final String SQL_CREATE_TODO_LIST =
            "CREATE TABLE " + TodoListContract.TodoList.TABLE_NAME + "(" +
                    TodoListContract.TodoList._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    TodoListContract.TodoList.COLUMN_NAME_TITLE + " TEXT," +
                    TodoListContract.TodoList.COLUMN_NAME_POSITION + " INTEGER)";

    private static final String SQL_CREATE_LIST_ITEM =
            "CREATE TABLE " + TodoListContract.ListItem.TABLE_NAME + "(" +
                    TodoListContract.ListItem._ID + " INTEGER PRIMARY KEY," +
                    TodoListContract.ListItem.COLUMN_NAME_DESCRIPTION + " TEXT," +
                    TodoListContract.ListItem.COLUMN_NAME_CHECKED + " INTEGER," +
                    TodoListContract.ListItem.COLUMN_NAME_POSITION + " INTEGER," +
                    TodoListContract.ListItem.COLUMN_NAME_FK_TODO_LIST + " INTEGER, " +
                    "FOREIGN KEY (" + TodoListContract.ListItem.COLUMN_NAME_FK_TODO_LIST + ") REFERENCES " +
                    TodoListContract.TodoList.TABLE_NAME + "(" + TodoListContract.ListItem._ID + ") " +
                    "ON DELETE CASCADE)";

    private static final String SQL_DELETE_TODO_LIST =
            "DROP TABLE IF EXISTS " + TodoListContract.TodoList.TABLE_NAME;

    private static final String SQL_DELETE_LIST_ITEM =
            "DROP TABLE IF EXISTS " + TodoListContract.ListItem.TABLE_NAME;

    private SQLiteDatabase db;

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Timber.d(SQL_CREATE_TODO_LIST);
        Timber.d(SQL_CREATE_LIST_ITEM);
        db.execSQL(SQL_CREATE_TODO_LIST);
        db.execSQL(SQL_CREATE_LIST_ITEM);
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_TODO_LIST);
        db.execSQL(SQL_DELETE_LIST_ITEM);
        onCreate(db);
    }

    public synchronized SQLiteDatabase getDb() {
        if (db == null) {
            db = getWritableDatabase();
        }
        return db;
    }

}

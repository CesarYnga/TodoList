package com.cesarynga.todolist.domain.model;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.cesarynga.todolist.db.BaseSqliteModel;
import com.cesarynga.todolist.db.TodoListContract;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;


public class ListItem extends BaseSqliteModel {

    public long id;
    public String description;
    public boolean checked;
    public int position;
    public TodoList todoList;

    // SQL
    public static List<ListItem> list(String where, String[] whereArgs, String orderBy) {
        Cursor cursor = getDb().query(TodoListContract.ListItem.TABLE_NAME, null, where, whereArgs, null, null, orderBy);
        List<ListItem> list = new ArrayList<>();
        while (cursor.moveToNext()) {
            list.add(cursorToQuery(cursor));
        }
        cursor.close();
        return list;
    }

    public long save() {
        ContentValues values = new ContentValues();
        if (id != 0) {
            values.put(TodoListContract.ListItem._ID, id);
        }
        values.put(TodoListContract.ListItem.COLUMN_NAME_DESCRIPTION, description);
        values.put(TodoListContract.ListItem.COLUMN_NAME_CHECKED, checked);
        values.put(TodoListContract.ListItem.COLUMN_NAME_POSITION, position);
        values.put(TodoListContract.ListItem.COLUMN_NAME_FK_TODO_LIST, todoList.id);
        id = getDb().insertWithOnConflict(
                TodoListContract.ListItem.TABLE_NAME,
                null,
                values,
                SQLiteDatabase.CONFLICT_REPLACE);
        Timber.d("Save list item with id " + id);
        return id;
    }

    public void delete() {
        getDb().delete(TodoListContract.ListItem.TABLE_NAME,
                TodoListContract.ListItem._ID + "=?",
                new String[] {String.valueOf(id)});
    }

    private static ListItem cursorToQuery(Cursor cursor) {
        ListItem listItem = new ListItem();
        listItem.id = cursor.getInt(cursor.getColumnIndex(TodoListContract.ListItem._ID));
        listItem.description = cursor.getString(cursor.getColumnIndex(TodoListContract.ListItem.COLUMN_NAME_DESCRIPTION));
        listItem.checked = cursor.getInt(cursor.getColumnIndex(TodoListContract.ListItem.COLUMN_NAME_CHECKED)) == 1;
        listItem.position = cursor.getInt(cursor.getColumnIndex(TodoListContract.ListItem.COLUMN_NAME_POSITION));
        return listItem;
    }
}

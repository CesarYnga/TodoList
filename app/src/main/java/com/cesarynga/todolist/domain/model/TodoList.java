package com.cesarynga.todolist.domain.model;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import com.cesarynga.todolist.db.BaseSqliteModel;
import com.cesarynga.todolist.db.TodoListContract;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

public class TodoList extends BaseSqliteModel implements Parcelable {

    public long id;
    public String title;
    public int position;

    public TodoList() {
        // Do nothing
    }

    protected TodoList(Parcel in) {
        id = in.readLong();
        title = in.readString();
        position = in.readInt();
    }

    public static final Creator<TodoList> CREATOR = new Creator<TodoList>() {
        @Override
        public TodoList createFromParcel(Parcel in) {
            return new TodoList(in);
        }

        @Override
        public TodoList[] newArray(int size) {
            return new TodoList[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(id);
        parcel.writeString(title);
        parcel.writeInt(position);
    }

    // SQL
    public static List<TodoList> list(String orderBy) {
        Cursor cursor = getDb().query(TodoListContract.TodoList.TABLE_NAME, null, null, null, null, null, orderBy);
        List<TodoList> list = new ArrayList<>();
        while (cursor.moveToNext()) {
            list.add(cursorToQuery(cursor));
        }
        cursor.close();
        return list;
    }

    public long save() {
        ContentValues values = new ContentValues();
        values.put(TodoListContract.TodoList.COLUMN_NAME_TITLE, title);
        values.put(TodoListContract.TodoList.COLUMN_NAME_POSITION, position);

        if (id == 0) {
            id = getDb().insert(
                    TodoListContract.TodoList.TABLE_NAME,
                    null,
                    values);
            Timber.d("Todo list with id " + id + " inserted");
        } else {
            int numRows = getDb().update(
                    TodoListContract.TodoList.TABLE_NAME,
                    values,
                    TodoListContract.TodoList._ID + "=?",
                    new String[] {String.valueOf(id)});
            if (numRows > 0) {
                Timber.d("Todo list with id " + id + " updated");
            } else {
                Timber.d("Todo list with id " + id + " could not be updated");
            }
        }
        return id;
    }

    public void delete() {
        getDb().delete(TodoListContract.TodoList.TABLE_NAME,
                TodoListContract.TodoList._ID + "=?",
                new String[] {String.valueOf(id)});
    }

    public List<ListItem> getItems(String orderBy) {
        String whereClause = TodoListContract.ListItem.COLUMN_NAME_FK_TODO_LIST + "=?";
        String[] whereArgs = new String[] {String.valueOf(id)};
        return  ListItem.list(whereClause, whereArgs, orderBy);
    }

    private static TodoList cursorToQuery(Cursor cursor) {
        TodoList todoList = new TodoList();
        todoList.id = cursor.getInt(cursor.getColumnIndex(TodoListContract.TodoList._ID));
        todoList.title = cursor.getString(cursor.getColumnIndex(TodoListContract.TodoList.COLUMN_NAME_TITLE));
        todoList.position = cursor.getInt(cursor.getColumnIndex(TodoListContract.TodoList.COLUMN_NAME_POSITION));
        return todoList;
    }
}

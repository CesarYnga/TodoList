package com.cesarynga.todolist.db;

import android.provider.BaseColumns;


public final class TodoListContract {

    private TodoListContract() {}

    public static class TodoList implements BaseColumns {
        public static final String TABLE_NAME = "TodoList";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_POSITION = "position";
    }

    public static class ListItem implements BaseColumns {
        public static final String TABLE_NAME = "ListItem";
        public static final String COLUMN_NAME_DESCRIPTION = "description";
        public static final String COLUMN_NAME_CHECKED = "checked";
        public static final String COLUMN_NAME_POSITION = "position";
        public static final String COLUMN_NAME_FK_TODO_LIST = "fk_todo_list";
    }
}

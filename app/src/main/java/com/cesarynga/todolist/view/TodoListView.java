package com.cesarynga.todolist.view;

import com.cesarynga.todolist.domain.model.TodoList;

import java.util.List;

public interface TodoListView extends LoadingView {

    void getLists(List<TodoList> list);

    void showNewList();

    void showListDetail(TodoList todoList);

}

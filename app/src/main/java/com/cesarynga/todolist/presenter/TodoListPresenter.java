package com.cesarynga.todolist.presenter;

import com.cesarynga.todolist.domain.interactor.UseCase;
import com.cesarynga.todolist.domain.model.TodoList;
import com.cesarynga.todolist.domain.interactor.TodoListUseCase;
import com.cesarynga.todolist.view.TodoListView;

import java.util.List;

import timber.log.Timber;

public class TodoListPresenter extends Presenter<TodoListView> {

    private final TodoListUseCase todoListInteractor;

    public TodoListPresenter(TodoListView view, TodoListUseCase todoListInteractor) {
        super(view);
        this.todoListInteractor = todoListInteractor;
    }

    @Override
    public void destroy() {
        view = null;
    }

    public void getLists() {
        view.showLoading();
        todoListInteractor.execute(new TodoListCallback());
    }

    public void removeItem(TodoList todoList) {
        todoList.delete();
    }

    public void sortList(List<TodoList> list) {
        int size = list.size();
        for (int i = 0; i < size; i++) {
            TodoList todoList = list.get(i);
            todoList.position = i;
            todoList.save();
        }
    }

    private final class TodoListCallback implements UseCase.UseCaseCallback<List<TodoList>> {

        @Override
        public void onSuccess(List<TodoList> todoLists) {
            view.hideLoading();
            view.getLists(todoLists);
        }

        @Override
        public void onError(Throwable exception) {
            Timber.e(exception);
            view.hideLoading();
        }
    }
}

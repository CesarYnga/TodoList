package com.cesarynga.todolist.domain.interactor;


import com.cesarynga.todolist.db.TodoListContract;
import com.cesarynga.todolist.executor.PostExecutionThread;
import com.cesarynga.todolist.executor.ThreadExecutor;
import com.cesarynga.todolist.domain.model.TodoList;

import java.util.List;

public class TodoListUseCase extends UseCase<List<TodoList>> {

    public TodoListUseCase(ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {
        super(threadExecutor, postExecutionThread);
    }

    @Override
    public void buildUseCase() {
        List<TodoList> list = TodoList.list(TodoListContract.TodoList.COLUMN_NAME_POSITION);
        notifyUseCaseSuccess(list);
    }
}

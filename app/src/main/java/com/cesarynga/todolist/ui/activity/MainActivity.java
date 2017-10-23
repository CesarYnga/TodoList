package com.cesarynga.todolist.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.ProgressBar;

import com.cesarynga.todolist.R;
import com.cesarynga.todolist.executor.JobExecutor;
import com.cesarynga.todolist.executor.UIThread;
import com.cesarynga.todolist.domain.model.TodoList;
import com.cesarynga.todolist.domain.interactor.TodoListUseCase;
import com.cesarynga.todolist.presenter.TodoListPresenter;
import com.cesarynga.todolist.ui.adapter.TodoListAdapter;
import com.cesarynga.todolist.ui.helper.SimpleItemTouchHelperCallback;
import com.cesarynga.todolist.view.TodoListView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements TodoListView, TodoListAdapter.OnItemClickListener,
        TodoListAdapter.OnItemDragListener, View.OnClickListener {

    private static final int REQUEST_CODE_ADDING = 9;
    private static final int REQUEST_CODE_SORTING = 10;

    private ProgressBar progressBar;

    private TodoListAdapter adapter;
    private List<TodoList> list = new ArrayList<>();

    private TodoListPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.app_name);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        progressBar = (ProgressBar) findViewById(R.id.progress);

        fab.setOnClickListener(this);

        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.setHasFixedSize(true);

        adapter = new TodoListAdapter(list);
        adapter.setOnItemClickListener(this);
        adapter.setOnItemDragListener(this);
        recyclerView.setAdapter(adapter);

        SimpleItemTouchHelperCallback callback = new SimpleItemTouchHelperCallback(adapter, true, true);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

        presenter = new TodoListPresenter(this,
                new TodoListUseCase(
                        new JobExecutor(), new UIThread()
                ));
        presenter.getLists();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.destroy();
    }

    @Override
    public void onItemClick(TodoList todoList) {
        showListDetail(todoList);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.fab) {
            showNewList();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            TodoList resultList = data.getParcelableExtra(TodoListDetailActivity.EXTRA_TODO_LIST);
            if (requestCode == REQUEST_CODE_SORTING) {
                list.remove(resultList.position);
                list.add(0, resultList);

                adapter.notifyItemChanged(resultList.position);
                adapter.notifyItemMoved(resultList.position, 0);
            } else if (requestCode == REQUEST_CODE_ADDING) {
                list.add(0, resultList);

                adapter.notifyItemInserted(0);
            }
            presenter.sortList(list);
        }
    }

    @Override
    public void onItemDismiss(int position) {
        presenter.removeItem(list.get(position));
        list.remove(position);
        presenter.sortList(list);
        adapter.notifyItemRemoved(position);
    }

    @Override
    public void onItemMove(int fromPosition, int toPosition) {
        TodoList toMove = list.get(fromPosition);
        list.remove(fromPosition);
        list.add(toPosition, toMove);
        presenter.sortList(list);
        adapter.notifyItemMoved(fromPosition, toPosition);
    }

    @Override
    public void getLists(List<TodoList> list) {
        this.list.addAll(list);
        adapter.notifyItemRangeInserted(0, list.size());
    }

    @Override
    public void showNewList() {
        Intent intent = new Intent(this, TodoListDetailActivity.class);
        startActivityForResult(intent, REQUEST_CODE_ADDING);
    }

    @Override
    public void showListDetail(TodoList todoList) {
        Intent intent = new Intent(this, TodoListDetailActivity.class);
        intent.putExtra(TodoListDetailActivity.EXTRA_TODO_LIST, todoList);
        startActivityForResult(intent, REQUEST_CODE_SORTING);
    }

    @Override
    public void showLoading() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        progressBar.setVisibility(View.GONE);
    }
}

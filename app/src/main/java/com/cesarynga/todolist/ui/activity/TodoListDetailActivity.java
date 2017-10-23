package com.cesarynga.todolist.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.cesarynga.todolist.R;
import com.cesarynga.todolist.db.TodoListContract;
import com.cesarynga.todolist.domain.model.ListItem;
import com.cesarynga.todolist.domain.model.TodoList;
import com.cesarynga.todolist.ui.adapter.ListItemAdapter;
import com.cesarynga.todolist.ui.helper.OnStartDragListener;
import com.cesarynga.todolist.ui.helper.SimpleItemTouchHelperCallback;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import timber.log.Timber;

public class TodoListDetailActivity extends AppCompatActivity
        implements View.OnClickListener,
        ListItemAdapter.OnItemChangeListener, OnStartDragListener {

    public static final String EXTRA_TODO_LIST = "todo_list";

    private TodoList todoList;
    private List<ListItem> listItems;

    private EditText titleView;
    private EditText itemView;

    private ListItemAdapter adapter;
    private ItemTouchHelper itemTouchHelper;

    private boolean titleChanged = false;
    private boolean itemsChanged = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_list_detail);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_nav_up);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                save();
                finish();
            }
        });

        todoList = getIntent().getParcelableExtra(EXTRA_TODO_LIST);

        titleView = (EditText) findViewById(R.id.title);
        itemView = (EditText) findViewById(R.id.new_item);
        Button btnAdd = (Button) findViewById(R.id.btn_add);
        btnAdd.setOnClickListener(this);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        if (todoList != null) {
            titleView.setText(todoList.title);
            listItems = todoList.getItems(TodoListContract.ListItem.COLUMN_NAME_POSITION);
        } else {
            todoList = new TodoList();
            listItems = new ArrayList<>();
        }

        adapter = new ListItemAdapter(listItems);
        adapter.setOnItemChangeListener(this);
        adapter.setOnStartDragListener(this);
        recyclerView.setAdapter(adapter);

        SimpleItemTouchHelperCallback callback = new SimpleItemTouchHelperCallback(adapter, false, false);
        itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    @Override
    public void onBackPressed() {
        save();
        super.onBackPressed();
    }

    private void save() {
        if (!titleView.getText().toString().isEmpty() || !listItems.isEmpty() ) {
            Timber.v("Data ready for saving");
            boolean isNew = todoList.id == 0;
            String title = titleView.getText().toString();
            if (isNew || !TextUtils.equals(title, todoList.title)) {
                Timber.v("Need to update TodoList");
                todoList.title = title;
                titleChanged = true;
                todoList.save();
            }
            if (isNew || itemsChanged) {
                Timber.v("Need to update ListItem");
                int size = listItems.size();
                for (int i = 0; i < size; i++) {
                    ListItem listItem = listItems.get(i);
                    listItem.position = i;
                    listItem.todoList = todoList;
                    listItem.save();
                }
            }
            if (isNew || titleChanged || itemsChanged) {
                Timber.v("Set result for MainActivity");
                Intent data = new Intent();
                data.putExtra(EXTRA_TODO_LIST, todoList);
                setResult(RESULT_OK, data);
            }
        }
    }

    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        itemTouchHelper.startDrag(viewHolder);
    }

    @Override
    public void onItemTextChange() {
        this.itemsChanged = true;
    }

    @Override
    public void onItemDismiss(int position) {
        listItems.get(position).delete();
        listItems.remove(position);
        adapter.notifyItemRemoved(position);
    }

    @Override
    public void onItemMove(int fromPosition, int toPosition) {
        this.itemsChanged = true;
        Collections.swap(listItems, fromPosition, toPosition);
        adapter.notifyItemMoved(fromPosition, toPosition);
    }

    @Override
    public void onClick(View v) {
        if (!itemView.getText().toString().isEmpty()) {
            ListItem listItem = new ListItem();
            listItem.description = itemView.getText().toString();
            listItem.todoList = todoList;
            listItems.add(listItem);
            adapter.notifyItemInserted(listItems.size());
            itemView.setText("");
        }
    }
}

package com.cesarynga.todolist.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cesarynga.todolist.R;
import com.cesarynga.todolist.domain.model.TodoList;
import com.cesarynga.todolist.ui.helper.ItemTouchHelperAdapter;

import java.util.List;


public class TodoListAdapter extends RecyclerView.Adapter<TodoListAdapter.ViewHolder>
        implements ItemTouchHelperAdapter {

    private List<TodoList> list;
    private OnItemClickListener onItemClickListener;
    private OnItemDragListener onItemDragListener;

    public TodoListAdapter(List<TodoList> list) {
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_todolist, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        TodoList todoList = list.get(position);
        holder.bind(todoList, onItemClickListener);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    public void setOnItemDragListener(OnItemDragListener listener) {
        this.onItemDragListener = listener;
    }

    @Override
    public void onItemMove(int fromPosition, int toPosition) {
        if (onItemDragListener != null) {
            onItemDragListener.onItemMove(fromPosition, toPosition);
        }
    }

    @Override
    public void onItemDismiss(int position) {
        if (onItemDragListener != null) {
            onItemDragListener.onItemDismiss(position);
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView title;

        public ViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.list_title);
        }

        public void bind(final TodoList todoList, final OnItemClickListener onItemClickListener) {
            title.setText(todoList.title);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClickListener != null) {
                        onItemClickListener.onItemClick(todoList);
                    }
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(TodoList todoList);
    }

    public interface OnItemDragListener {
        void onItemDismiss(int position);
        void onItemMove(int fromPosition, int toPosition);
    }
}

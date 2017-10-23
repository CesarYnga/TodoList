package com.cesarynga.todolist.ui.adapter;

import android.graphics.Paint;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;

import com.cesarynga.todolist.R;
import com.cesarynga.todolist.domain.model.ListItem;
import com.cesarynga.todolist.ui.helper.ItemTouchHelperAdapter;
import com.cesarynga.todolist.ui.helper.OnStartDragListener;

import java.util.List;

public class ListItemAdapter extends RecyclerView.Adapter<ListItemAdapter.ViewHolder>
        implements ItemTouchHelperAdapter {

    private List<ListItem> list;
    private OnStartDragListener onStartDragListener;
    private OnItemChangeListener onItemChangeListener;

    public ListItemAdapter(List<ListItem> listItems) {
        this.list = listItems;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_todo_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        ListItem listItem = this.list.get(position);
        holder.bind(listItem, onStartDragListener, onItemChangeListener);
    }

    @Override
    public int getItemCount() {
        return this.list.size();
    }

    public void setOnItemChangeListener(OnItemChangeListener listener) {
        this.onItemChangeListener = listener;
    }

    @Override
    public void onItemMove(int fromPosition, int toPosition) {
        if (onItemChangeListener != null) {
            onItemChangeListener.onItemMove(fromPosition, toPosition);
        }
    }

    @Override
    public void onItemDismiss(int position) {
        // no-op
    }

    public void setOnStartDragListener(OnStartDragListener listener) {
        this.onStartDragListener = listener;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private CheckBox checkBox;
        private EditText item;
        private ImageView remove;
        private ImageView handler;

        public ViewHolder(View itemView) {
            super(itemView);
            checkBox = (CheckBox) itemView.findViewById(R.id.checkbox);
            item = (EditText) itemView.findViewById(R.id.item);
            remove = (ImageView) itemView.findViewById(R.id.remove);
            handler = (ImageView) itemView.findViewById(R.id.handler);
        }

        public void bind(final ListItem listItem,
                         final OnStartDragListener onStartDragListener,
                         final OnItemChangeListener onItemChangeListener) {
            item.setText(listItem.description);
            checkBox.setChecked(listItem.checked);
            handler.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    if (MotionEventCompat.getActionMasked(motionEvent) == MotionEvent.ACTION_DOWN) {
                        if (onStartDragListener != null) {
                            onStartDragListener.onStartDrag(ViewHolder.this);
                        }
                    }
                    return false;
                }
            });
            item.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    // Do nothing
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    // Do nothing
                }

                @Override
                public void afterTextChanged(Editable editable) {
                    listItem.description = editable.toString();
                    if (onItemChangeListener != null) {
                        onItemChangeListener.onItemTextChange();
                    }
                }
            });
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        item.setPaintFlags(item.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    } else {
                        item.setPaintFlags(item.getPaintFlags() & (~ Paint.STRIKE_THRU_TEXT_FLAG));
                    }
                    listItem.checked = isChecked;
                }
            });
            remove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemChangeListener != null) {
                        onItemChangeListener.onItemDismiss(getAdapterPosition());
                    }
                }
            });
        }
    }

    public interface OnItemChangeListener {
        void onItemTextChange();
        void onItemDismiss(int position);
        void onItemMove(int fromPosition, int toPosition);
    }
}

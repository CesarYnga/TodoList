package com.cesarynga.todolist.presenter;


import com.cesarynga.todolist.view.BaseView;

public abstract class Presenter<V extends BaseView> {

    protected V view;

    public Presenter(V view) {
        this.view = view;
    }

    public abstract void destroy();
}

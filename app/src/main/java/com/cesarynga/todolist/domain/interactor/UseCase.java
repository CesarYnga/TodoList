package com.cesarynga.todolist.domain.interactor;

import com.cesarynga.todolist.executor.PostExecutionThread;
import com.cesarynga.todolist.executor.ThreadExecutor;

public abstract class UseCase<T> {

    private final ThreadExecutor threadExecutor;
    private final PostExecutionThread postExecutionThread;
    private UseCaseCallback useCaseCallback;

    public UseCase(ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {
        this.threadExecutor = threadExecutor;
        this.postExecutionThread = postExecutionThread;
    }

    public abstract void buildUseCase();

    public void execute(final UseCaseCallback<T> callback) {
        this.useCaseCallback = callback;
        threadExecutor.execute(new Runnable() {
            @Override
            public void run() {
                buildUseCase();
            }
        });
    }

    protected void notifyUseCaseSuccess(final T response) {
        postExecutionThread.execute(new Runnable() {
            @Override
            public void run() {
                useCaseCallback.onSuccess(response);
            }
        });
    }

    protected void notifyUseCaseError(final Throwable e) {
        postExecutionThread.execute(new Runnable() {
            @Override
            public void run() {
                useCaseCallback.onError(e);
            }
        });
    }

    protected void cancel() {

    }

    public interface UseCaseCallback<T> {

        void onSuccess(T t);

        void onError(Throwable exception);
    }
}

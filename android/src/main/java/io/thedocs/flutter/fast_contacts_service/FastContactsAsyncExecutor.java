package io.thedocs.flutter.fast_contacts_service;

import android.annotation.TargetApi;
import android.os.AsyncTask;
import android.os.Build;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class FastContactsAsyncExecutor {

    private final ExecutorService executor =
            new ThreadPoolExecutor(0, 10, 60, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(1000));


    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public <T> void execute(AsyncTask<Object, Void, T> task, Object... params) {
        task.executeOnExecutor(executor, params);
    }

}

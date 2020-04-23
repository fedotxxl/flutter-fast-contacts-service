package io.thedocs.flutter.fast_contacts_service;

import android.annotation.TargetApi;
import android.os.AsyncTask;
import android.os.Build;

import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;

@TargetApi(Build.VERSION_CODES.CUPCAKE)
public class FastContactsAsyncTask extends AsyncTask<MethodCall, Void, FastContactsTaskResult> {
    private FastContactsTask _task;
    private MethodChannel.Result _result;

    public FastContactsAsyncTask(FastContactsTask task, MethodChannel.Result result) {
        _task = task;
        _result = result;
    }

    protected FastContactsTaskResult doInBackground(MethodCall... calls) {
        try {
            Object result = _task.execute(calls[0]);
            return new FastContactsTaskResult(result);
        } catch (Exception e) {
            return new FastContactsTaskResult(e);
        }
    }

    protected void onPostExecute(FastContactsTaskResult result) {
        Exception e = result.getException();
        if (e != null) {
            if (e instanceof UnsupportedOperationException) {
                _result.notImplemented();
            } else {
                e.printStackTrace();
                _result.error("FastContactsError", e.getMessage(), null);
            }
        } else {
            _result.success(result.getResult());
        }
    }
}
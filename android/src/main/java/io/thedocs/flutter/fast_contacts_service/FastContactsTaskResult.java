package io.thedocs.flutter.fast_contacts_service;

public class FastContactsTaskResult {
    private Object _result;
    private Exception _exception;

    public FastContactsTaskResult(Object result) {
        _result = result;
    }

    public FastContactsTaskResult(Exception exception) {
        _exception = exception;
    }

    public Object getResult() {
        return _result;
    }

    public Exception getException() {
        return _exception;
    }
}
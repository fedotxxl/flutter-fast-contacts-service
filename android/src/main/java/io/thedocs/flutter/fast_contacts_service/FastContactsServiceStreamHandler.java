package io.thedocs.flutter.fast_contacts_service;

import android.os.Handler;

import java.util.HashMap;

import io.flutter.plugin.common.EventChannel;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.thedocs.flutter.fast_contacts_service.domain.ContactWithPhone;

public class FastContactsServiceStreamHandler implements EventChannel.StreamHandler {
    private final Handler handler = new Handler();
    private final FastContactsProvider provider;
    private EventChannel.EventSink eventSink;

    public FastContactsServiceStreamHandler(FastContactsProvider provider) {
        this.provider = provider;
    }

    private final Runnable runnable = new Runnable() {
        @Override
        public void run() {
            provider.streamContactsWithPhones(
                    new FastContactsProvider.OnNextCallback<ContactWithPhone>() {
                        @Override
                        public void onNext(ContactWithPhone contact) {
                            eventSink.success(contact.toMap());
                        }
                    },
                    new FastContactsProvider.OnLastCallback() {
                        @Override
                        public void onLast() {
                            eventSink.endOfStream();
                        }
                    }
            );
        }
    };


    @Override
    public void onListen(Object o, final EventChannel.EventSink eventSink) {
        this.eventSink = eventSink;
        runnable.run();
    }

    @Override
    public void onCancel(Object o) {
        handler.removeCallbacks(runnable);
    }
}
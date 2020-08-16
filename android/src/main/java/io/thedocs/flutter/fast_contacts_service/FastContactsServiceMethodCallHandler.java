package io.thedocs.flutter.fast_contacts_service;

import android.annotation.TargetApi;
import android.os.Build;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.thedocs.flutter.fast_contacts_service.domain.Contact;
import io.thedocs.flutter.fast_contacts_service.domain.FastContactsMapCastableI;

public class FastContactsServiceMethodCallHandler implements MethodChannel.MethodCallHandler, FastContactsTask {

    private final FastContactsProvider provider;

    public FastContactsServiceMethodCallHandler(FastContactsProvider provider) {
        this.provider = provider;
    }

    @TargetApi(Build.VERSION_CODES.CUPCAKE)
    @Override
    public void onMethodCall(MethodCall call, MethodChannel.Result result) {
        new FastContactsAsyncTask(this, result).execute(call);
    }

    @Override
    public Object execute(MethodCall call) throws Exception {
        switch (call.method) {
            case "listContacts": {
                return toMaps(this.listContacts((Boolean) call.argument("phones")));
            }
            default: {
                throw new UnsupportedOperationException();
            }
        }
    }

    private Collection<Contact> listContacts(Boolean phones) {
        return provider.listContacts(phones);
    }

    private List<Map<String, Object>> toMaps(Collection<? extends FastContactsMapCastableI> items) {
        List<Map<String, Object>> answer = new ArrayList<>(items.size());

        for (FastContactsMapCastableI item : items) {
            answer.add(item.toMap());
        }

        return answer;
    }
}

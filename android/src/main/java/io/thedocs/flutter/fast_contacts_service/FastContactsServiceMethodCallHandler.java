package io.thedocs.flutter.fast_contacts_service;

import android.annotation.TargetApi;
import android.os.Build;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.thedocs.flutter.fast_contacts_service.domain.ContactWithPhone;
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
            case "getContactsWithPhone": {
                return toMaps(this.getContactsWithPhone((String) call.argument("sort")));
            }
            default: {
                throw new UnsupportedOperationException();
            }
        }
    }

    private List<ContactWithPhone> getContactsWithPhone(String sort) {
        System.out.println(sort);

        final List<ContactWithPhone> answer = new ArrayList<>();

        provider.streamContactsWithPhones(
                new FastContactsProvider.OnNextCallback<ContactWithPhone>() {
                    @Override
                    public void onNext(ContactWithPhone contact) {
                        answer.add(contact);
                    }
                },
                new FastContactsProvider.OnLastCallback() {
                    @Override
                    public void onLast() {

                    }
                }
        );

        return answer;
    }

    private List<Map<String, Object>> toMaps(List<? extends FastContactsMapCastableI> items) {
        List<Map<String, Object>> answer =new ArrayList<>(items.size());

        for (FastContactsMapCastableI item : items) {
            answer.add(item.toMap());
        }

        return answer;
    }
}

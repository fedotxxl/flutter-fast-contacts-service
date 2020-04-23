package io.thedocs.flutter.fast_contacts_service;

import android.annotation.TargetApi;
import android.content.ContentResolver;
import android.database.Cursor;
import android.os.Build;
import android.provider.ContactsContract;

import io.flutter.plugin.common.PluginRegistry;
import io.thedocs.flutter.fast_contacts_service.domain.ContactWithPhone;

public class FastContactsProvider {

    private PluginRegistry.Registrar registrar;

    public FastContactsProvider(PluginRegistry.Registrar registrar) {
        this.registrar = registrar;
    }

    @TargetApi(Build.VERSION_CODES.ECLAIR)
    public void streamContactsWithPhones(OnNextCallback<ContactWithPhone> onNext, OnLastCallback onLast) {
        Cursor c = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                new String[]{ContactsContract.Contacts._ID,
                        ContactsContract.Contacts.DISPLAY_NAME,
                        ContactsContract.CommonDataKinds.Phone.NUMBER,
                        ContactsContract.RawContacts.ACCOUNT_TYPE},
                ContactsContract.RawContacts.ACCOUNT_TYPE + " <> 'google' ",
                null, "upper("+ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + ") ASC");

        if (c.getCount() <= 0) {
            onLast.onLast();
        } else {
            while (c.moveToNext()) {
                String phoneNumber = c.getString(c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                String displayName = c.getString(c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                String accountType = c.getString(c.getColumnIndex(ContactsContract.RawContacts.ACCOUNT_TYPE));

                onNext.onNext(
                        new ContactWithPhone(
                                displayName, phoneNumber, accountType
                        )
                );
            }

            onLast.onLast();
        }
    }

    private ContentResolver getContentResolver() {
        return registrar.context().getContentResolver();
    }


    public interface OnNextCallback<T> {

        void onNext(T value);

    }

    public interface OnLastCallback {

        void onLast();

    }
}

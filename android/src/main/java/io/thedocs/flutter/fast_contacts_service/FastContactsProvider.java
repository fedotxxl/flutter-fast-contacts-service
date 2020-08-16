package io.thedocs.flutter.fast_contacts_service;

import android.annotation.TargetApi;
import android.content.ContentResolver;
import android.content.res.Resources;
import android.database.Cursor;
import android.os.Build;
import android.provider.ContactsContract;

import io.flutter.plugin.common.PluginRegistry;
import io.thedocs.flutter.fast_contacts_service.domain.Contact;

import java.util.*;

public class FastContactsProvider {

    private PluginRegistry.Registrar registrar;

    public FastContactsProvider(PluginRegistry.Registrar registrar) {
        this.registrar = registrar;
    }

    @TargetApi(Build.VERSION_CODES.ECLAIR)
    public Collection<Contact> listContacts(Boolean queryPhones) {
        PhoneTypeProvider phoneTypeProvider = new PhoneTypeProvider();

        Cursor c = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                getQueryData(queryPhones),
                ContactsContract.RawContacts.ACCOUNT_TYPE + " <> 'google' ",
                null, "upper(" + ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + ") ASC");

        if (c.getCount() <= 0) {
            return new ArrayList<>();
        } else {
            LinkedHashMap<String, Contact> contacts = new LinkedHashMap<>();

            while (c.moveToNext()) {
                String contactId = c.getString(c.getColumnIndex(ContactsContract.Data.CONTACT_ID));
                String displayName = c.getString(c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                String accountType = c.getString(c.getColumnIndex(ContactsContract.RawContacts.ACCOUNT_TYPE));

                Contact contact = contacts.get(contactId);

                if (contact == null) {
                    contact = new Contact(contactId, displayName, accountType);
                    contacts.put(contactId, contact);
                }

                if (queryPhones) {
                    String phoneNumber = c.getString(c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    Contact.PhoneType phoneType = phoneTypeProvider.getPhoneType(c.getInt(c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE)));

                    if (phoneNumber != null && !phoneNumber.equals("")) {
                        contact.addPhone(new Contact.Phone(phoneNumber, phoneType));
                    }
                }
            }

            return contacts.values();
        }
    }

    private String[] getQueryData(Boolean queryPhones) {
        ArrayList<String> answer = new ArrayList<>();

        answer.add(ContactsContract.Data.CONTACT_ID);
        answer.add(ContactsContract.Contacts.DISPLAY_NAME);
        answer.add(ContactsContract.RawContacts.ACCOUNT_TYPE);

        if (queryPhones) {
            answer.add(ContactsContract.CommonDataKinds.Phone.NUMBER);
            answer.add(ContactsContract.CommonDataKinds.Phone.TYPE);
        }

        return answer.toArray(new String[0]);
    }

    private ContentResolver getContentResolver() {
        return registrar.context().getContentResolver();
    }

    private static class PhoneTypeProvider {

        private static Map<Integer, Contact.PhoneType> PHONE_TYPES_BY_ID;

        static {
            PHONE_TYPES_BY_ID = new HashMap<>();
            PHONE_TYPES_BY_ID.put(ContactsContract.CommonDataKinds.Phone.TYPE_HOME, Contact.PhoneType.HOME);
            PHONE_TYPES_BY_ID.put(ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE, Contact.PhoneType.MOBILE);
            PHONE_TYPES_BY_ID.put(ContactsContract.CommonDataKinds.Phone.TYPE_WORK, Contact.PhoneType.WORK);
        }

        public PhoneTypeProvider() {
        }

        public Contact.PhoneType getPhoneType(int labelType) {
            Contact.PhoneType phoneType = PhoneTypeProvider.PHONE_TYPES_BY_ID.get(labelType);

            if (phoneType != null) {
                return phoneType;
            } else {
                return Contact.PhoneType.OTHER;
            }
        }
    }
}

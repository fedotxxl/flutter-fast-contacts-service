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
    public Collection<Contact> listContacts(Boolean queryPhones, Boolean queryEmails) {
        PhoneTypeProvider phoneTypeProvider = new PhoneTypeProvider(registrar.context().getResources());

        Cursor c = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                new String[]{ContactsContract.Data.CONTACT_ID,
                        ContactsContract.Contacts.DISPLAY_NAME,
                        ContactsContract.CommonDataKinds.Phone.NUMBER,
                        ContactsContract.CommonDataKinds.Phone.TYPE,
                        ContactsContract.RawContacts.ACCOUNT_TYPE},
                ContactsContract.RawContacts.ACCOUNT_TYPE + " <> 'google' ",
                null, "upper("+ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + ") ASC");

        if (c.getCount() <= 0) {
            return new ArrayList<>();
        } else {
            LinkedHashMap<String, Contact> contacts = new LinkedHashMap<>();

            while (c.moveToNext()) {
                String contactId = c.getString(c.getColumnIndex(ContactsContract.Data.CONTACT_ID));
                String displayName = c.getString(c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                String accountType = c.getString(c.getColumnIndex(ContactsContract.RawContacts.ACCOUNT_TYPE));
                String phoneNumber = c.getString(c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                Contact.PhoneType phoneType = phoneTypeProvider.getPhoneType(c.getInt(c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE)));

                Contact contact = contacts.get(contactId);

                if (contact == null) {
                    contact = new Contact(contactId, displayName, accountType);
                    contacts.put(contactId, contact);
                }

                contact.addPhone(new Contact.Phone(phoneNumber, phoneType));
            }

            return contacts.values();
        }
    }

    private ContentResolver getContentResolver() {
        return registrar.context().getContentResolver();
    }

    private static class PhoneTypeProvider {

        private static Map<String, Contact.PhoneType> PHONE_TYPES_BY_LABEL;

        private Resources resources;
        private Map<Integer, Contact.PhoneType> phoneTypes;

        static {
            PHONE_TYPES_BY_LABEL = new HashMap<>();
            PHONE_TYPES_BY_LABEL.put("home", Contact.PhoneType.HOME);
            PHONE_TYPES_BY_LABEL.put("mobile", Contact.PhoneType.MOBILE);
            PHONE_TYPES_BY_LABEL.put("work", Contact.PhoneType.WORK);
        }

        public PhoneTypeProvider(Resources resources) {
            this.resources = resources;
            this.phoneTypes = new HashMap<>();
        }

        public Contact.PhoneType getPhoneType(int labelType) {
            Contact.PhoneType phoneType = this.phoneTypes.get(labelType);

            if (phoneType == null) {
                phoneType = doGetPhoneType(labelType);
                this.phoneTypes.put(labelType, phoneType);
            }

            return phoneType;
        }

        private Contact.PhoneType doGetPhoneType(int labelType) {
            String phoneLabel = ContactsContract.CommonDataKinds.Phone.getTypeLabel(resources, labelType, "other").toString().toLowerCase();
            Contact.PhoneType phoneType = PHONE_TYPES_BY_LABEL.get(phoneLabel);

            if (phoneType != null) {
                return phoneType;
            } else {
                return Contact.PhoneType.OTHER;
            }
        }
    }
}

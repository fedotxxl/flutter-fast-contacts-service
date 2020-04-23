package io.thedocs.flutter.fast_contacts_service.domain;

import java.util.List;

public class ContactWithPhones {
    private String displayName;
    private List<ContactItem> phones;

    public ContactWithPhones(String displayName, List<ContactItem> phones) {
        this.displayName = displayName;
        this.phones = phones;
    }

    public String getDisplayName() {
        return displayName;
    }

    public List<ContactItem> getPhones() {
        return phones;
    }
}

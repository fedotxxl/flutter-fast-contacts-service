package io.thedocs.flutter.fast_contacts_service.domain;

public class ContactItem {
    private String label;
    private String value;

    public ContactItem(String label, String value) {
        this.label = label;
        this.value = value;
    }

    public String getLabel() {
        return label;
    }

    public String getValue() {
        return value;
    }
}

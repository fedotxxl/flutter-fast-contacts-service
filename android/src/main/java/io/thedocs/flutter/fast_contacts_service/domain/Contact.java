package io.thedocs.flutter.fast_contacts_service.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Contact implements FastContactsMapCastableI {
    private String id;
    private String displayName;
    private String accountType;
    private List<Phone> phones;

    public Contact(String id, String displayName, String accountType) {
        this.id = id;
        this.displayName = displayName;
        this.accountType = accountType;
        this.phones = new ArrayList<>();
    }

    public void addPhone(Phone phone) {
        this.phones.add(phone);
    }

    @Override
    public Map<String, Object> toMap() {
        HashMap<String, Object> answer = new HashMap<>();
        List<Map<String, Object>> phonesTransformed = new ArrayList<>();

        for (Phone phone : this.phones) {
            phonesTransformed.add(phone.toMap());
        }

        answer.put("id", this.id);
        answer.put("displayName", this.displayName);
        answer.put("accountType", this.accountType);
        answer.put("phones", phonesTransformed);

        return answer;
    }

    public static class Phone implements FastContactsMapCastableI {
        private String number;
        private PhoneType type;

        public Phone(String number, PhoneType type) {
            this.number = number;
            this.type = type;
        }

        public String getNumber() {
            return number;
        }

        public PhoneType getType() {
            return type;
        }

        @Override
        public Map<String, Object> toMap() {
            HashMap<String, Object> answer = new HashMap<>();

            answer.put("number", number);
            answer.put("type", type.id);

            return answer;
        }
    }

    public enum PhoneType {
        HOME(1), MOBILE(2), WORK(3), OTHER(-1);

        private int id;

        PhoneType(int id) {
            this.id = id;
        }
    }
}

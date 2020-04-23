package io.thedocs.flutter.fast_contacts_service.domain;

import java.util.HashMap;
import java.util.Map;

public class ContactWithPhone implements FastContactsMapCastableI {
    private String displayName;
    private String phoneNumber;
    private String accountType;

    public ContactWithPhone(String displayName, String phoneNumber, String accountType) {
        this.displayName = displayName;
        this.phoneNumber = phoneNumber;
        this.accountType = accountType;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getAccountType() {
        return accountType;
    }

    @Override
    public Map<String, Object> toMap() {
        HashMap<String, Object> answer = new HashMap<>();

        answer.put("phoneNumber", phoneNumber);
        answer.put("displayName", displayName);
        answer.put("accountType", accountType);

        return answer;
    }
}

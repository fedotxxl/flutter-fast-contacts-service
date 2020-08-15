class FastContact {
  String id;
  String displayName;
  String accountType;
  List<FastContactPhone> phones;

  FastContact(this.id, this.displayName, this.accountType, this.phones);
}

class FastContactPhone {
  String number;
  FastContactPhoneType type;
}

enum FastContactPhoneType {
  HOME, MOBILE, WORK, OTHER
}

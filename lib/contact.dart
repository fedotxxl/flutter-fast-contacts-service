import 'package:equatable/equatable.dart';

class ContactWithPhone {
  final String displayName;
  final String phoneNumber;
  final String accountType;

  ContactWithPhone(this.displayName, this.phoneNumber, this.accountType);

}

class ContactWithPhones {
  final String displayName;
  final List<Item> phones;

  ContactWithPhones(this.displayName, this.phones);
}

/// Item class used for contact fields which only have a [label] and
/// a [value], such as emails and phone numbers
// ignore: must_be_immutable
class Item extends Equatable {
  Item({this.label, this.value});

  String label, value;

  Item.fromMap(final dyn) {
    if (dyn is Map) {
      value = dyn["value"] as String;
      label = dyn["label"] as String;
    }
  }

  String get equalsValue => value;

  @override
  List get props => [equalsValue];
}

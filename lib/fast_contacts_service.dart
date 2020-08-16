import 'dart:async';

import 'package:fast_contacts_service/fast_contact.dart';
import 'package:flutter/services.dart';
import 'package:streams_channel/streams_channel.dart';

import 'contact.dart';

class FastContactsService {
  static const MethodChannel _methodChannel = const MethodChannel('github.com/fedotxxl/flutter_fast_contacts_provider');

  static Future<List<dynamic>> listContacts({phones = true}) async {
    List<dynamic> result = await _methodChannel.invokeMethod('listContacts', <String, dynamic>{"phones": phones});

    return result;
  }

  static FastContact _toContact(Map<dynamic, dynamic> contact) {
    return null;
//    return new ContactWithPhone(contact["displayName"], contact["phoneNumber"], contact["accountType"]);
  }
}

import 'dart:async';

import 'package:flutter/services.dart';
import 'package:streams_channel/streams_channel.dart';

import 'contact.dart';

class FastContactsService {
  static const MethodChannel _methodChannel = const MethodChannel('fast_contacts_service_method_channel');
  static final StreamsChannel _streamsChannel = new StreamsChannel('fast_contacts_service_streams_channel');

  static Future<String> get platformVersion async {
    final String version = await _methodChannel.invokeMethod('getPlatformVersion');
    return version;
  }

  static Stream<ContactWithPhones> streamContactsWithPhones() {
    return _streamsChannel.receiveBroadcastStream('some args').map((v) {
      print(v);

      return new ContactWithPhones("name", []);
    });
  }

  static Future<List<ContactWithPhones>> getContactsWithPhone(String sort) async {
    print(DateTime.now());

    var answer = await _methodChannel.invokeMethod('getContactsWithPhone', <String, dynamic>{"sort": sort});
    print(answer);
    print(DateTime.now());

    return [];
  }

}

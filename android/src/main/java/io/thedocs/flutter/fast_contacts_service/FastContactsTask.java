package io.thedocs.flutter.fast_contacts_service;

import io.flutter.plugin.common.MethodCall;

public interface FastContactsTask {
    Object execute(MethodCall call) throws Exception;
}

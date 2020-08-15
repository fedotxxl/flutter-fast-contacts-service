package io.thedocs.flutter.fast_contacts_service;

import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.PluginRegistry;

public class FastContactsServicePlugin {

    final MethodChannel methodChannel;
    final FastContactsProvider provider;
    final FastContactsServiceMethodCallHandler methodCallHandler;

    public FastContactsServicePlugin(PluginRegistry.Registrar registrar) {
        this.provider = new FastContactsProvider(registrar);
        this.methodChannel = new MethodChannel(registrar.messenger(), "github.com/fedotxxl/flutter_fast_contacts_provider");
        this.methodCallHandler = new FastContactsServiceMethodCallHandler(this.provider);
    }

    public void init() {
        methodChannel.setMethodCallHandler(this.methodCallHandler);
    }

    public static void registerWith(PluginRegistry.Registrar registrar) {
       new FastContactsServicePlugin(registrar).init();
    }

}

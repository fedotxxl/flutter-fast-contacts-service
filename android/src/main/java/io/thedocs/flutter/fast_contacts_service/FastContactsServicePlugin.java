package io.thedocs.flutter.fast_contacts_service;

import app.loup.streams_channel.StreamsChannel;
import io.flutter.plugin.common.EventChannel;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.PluginRegistry;

public class FastContactsServicePlugin {

    final MethodChannel methodChannel;
    final StreamsChannel streamsChannel;
    final FastContactsProvider provider;
    final FastContactsServiceMethodCallHandler methodCallHandler;
    final FastContactsServiceStreamHandler streamHandler;

    public FastContactsServicePlugin(PluginRegistry.Registrar registrar) {
        this.provider = new FastContactsProvider(registrar);
        this.streamsChannel = new StreamsChannel(registrar.messenger(), "fast_contacts_service_streams_channel");
        this.methodChannel = new MethodChannel(registrar.messenger(), "fast_contacts_service_method_channel");
        this.streamHandler = new FastContactsServiceStreamHandler(this.provider);
        this.methodCallHandler = new FastContactsServiceMethodCallHandler(this.provider);
    }

    public void init() {
        methodChannel.setMethodCallHandler(this.methodCallHandler);
        streamsChannel.setStreamHandlerFactory(new StreamsChannel.StreamHandlerFactory() {
            @Override
            public EventChannel.StreamHandler create(Object arguments) {
                return streamHandler;
            }
        });
    }

    public static void registerWith(PluginRegistry.Registrar registrar) {
       new FastContactsServicePlugin(registrar).init();
    }

}

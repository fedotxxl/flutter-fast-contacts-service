#import "FastContactsServicePlugin.h"
#if __has_include(<fast_contacts_service/fast_contacts_service-Swift.h>)
#import <fast_contacts_service/fast_contacts_service-Swift.h>
#else
// Support project import fallback if the generated compatibility header
// is not copied when this plugin is created as a library.
// https://forums.swift.org/t/swift-static-libraries-dont-copy-generated-objective-c-header/19816
#import "fast_contacts_service-Swift.h"
#endif

@implementation FastContactsServicePlugin
+ (void)registerWithRegistrar:(NSObject<FlutterPluginRegistrar>*)registrar {
  [SwiftContactsServicePlugin registerWithRegistrar:registrar];
}
@end

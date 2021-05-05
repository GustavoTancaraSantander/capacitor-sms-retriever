#import <Foundation/Foundation.h>
#import <Capacitor/Capacitor.h>

// Define the plugin using the CAP_PLUGIN Macro, and
// each method the plugin supports using the CAP_PLUGIN_METHOD macro.
CAP_PLUGIN(SmsRetrieverPlugin, "SmsRetriever",
           CAP_PLUGIN_METHOD(getAppSignature, CAPPluginReturnNone);
           CAP_PLUGIN_METHOD(requestPhoneNumber, CAPPluginReturnNone);
           CAP_PLUGIN_METHOD(startSmsReceiver, CAPPluginReturnNone);
           CAP_PLUGIN_METHOD(removeSmsReceiver, CAPPluginReturnNone);

)

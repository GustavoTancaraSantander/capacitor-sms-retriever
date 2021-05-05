import Foundation
import Capacitor

/**
 * Please read the Capacitor iOS Plugin Development Guide
 * here: https://capacitorjs.com/docs/plugins/ios
 */
@objc(SmsRetrieverPlugin)
public class SmsRetrieverPlugin: CAPPlugin {
    
    @objc func getAppSignature(_ call: CAPPluginCall) {
        call.unimplemented()
    }
    
    @objc func requestPhoneNumber(_ call: CAPPluginCall) {
        call.unimplemented()
    }
    
    @objc func startSmsReceiver(_ call: CAPPluginCall) {
        call.unimplemented()
    }
    
    @objc func removeSmsReceiver(_ call: CAPPluginCall) {
        call.unimplemented()
    }
}

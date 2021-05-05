package com.dwlrathod.plugins;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;

import com.getcapacitor.JSObject;
import com.getcapacitor.NativePlugin;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.google.android.gms.auth.api.credentials.Credential;
import com.google.android.gms.auth.api.credentials.Credentials;
import com.google.android.gms.auth.api.credentials.CredentialsApi;
import com.google.android.gms.auth.api.credentials.HintRequest;
import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.auth.api.phone.SmsRetrieverClient;
import com.google.android.gms.tasks.Task;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static com.dwlrathod.plugins.SMSReceiver.ERROR_KEY;
import static com.dwlrathod.plugins.SMSReceiver.MESSAGE_KEY;
import static com.dwlrathod.plugins.SmsRetrieverPlugin.REQUEST_PHONE_NUMBER_REQUEST_CODE;

@NativePlugin(name = "SmsRetriever", requestCodes = REQUEST_PHONE_NUMBER_REQUEST_CODE)
public class SmsRetrieverPlugin extends Plugin implements
        SMSReceiver.OTPReceiveListener {

    public static final int REQUEST_PHONE_NUMBER_REQUEST_CODE = 23;
    static final String SOMETHING_WENT_WRONG = "Something went wrong!";
    static final String PHONE_HINT_USER_CANCELLED = "User cancelled!";
    static final String PHONE_HINT_NO_HINTS_AVAILABLE = "No hits available!";

    @PluginMethod
    public void getAppSignature(PluginCall call) {
        AppSignatureHelper signatureHelper = new AppSignatureHelper(getContext());
        JSObject ret = new JSObject();
        ret.put("signature", signatureHelper.getAppSignatures().get(0));
        call.resolve(ret);
    }

    @PluginMethod
    public void requestPhoneNumber(PluginCall call) {

        if (!GooglePlayServicesHelper.isAvailable(getContext())) {
            call.error(GooglePlayServicesHelper.UNAVAILABLE_ERROR_MESSAGE);
            return;
        }

        if (!GooglePlayServicesHelper.hasSupportedVersion(getContext())) {
            call.error(GooglePlayServicesHelper.UNSUPPORTED_VERSION_ERROR_MESSAGE);
            return;
        }

        saveCall(call);

        // Construct hint request dialog
        HintRequest hintRequest = new HintRequest.Builder().setPhoneNumberIdentifierSupported(true).build();
        PendingIntent intent = Credentials.getClient(getContext()).getHintPickerIntent(hintRequest);

        try {
            // Initiate hint request dialog
            getActivity().startIntentSenderForResult(intent.getIntentSender(), REQUEST_PHONE_NUMBER_REQUEST_CODE, null, 0, 0, 0);
        } catch (IntentSender.SendIntentException e) {
            e.printStackTrace();
        }
    }

    // Handle result for hint request
    @Override
    protected void handleOnActivityResult(int requestCode, int resultCode, Intent data) {
        super.handleOnActivityResult(requestCode, resultCode, data);

        PluginCall savedCall = getSavedCall();
        if (savedCall == null) {
            return;
        }

        if (requestCode == REQUEST_PHONE_NUMBER_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Credential credential = data.getParcelableExtra(Credential.EXTRA_KEY);
                if (credential != null) {
                    JSObject info = new JSObject();
                    info.put("phoneNumber", credential.getId());
                    savedCall.resolve(info);
                } else {
                    savedCall.error(SOMETHING_WENT_WRONG);
                }
            } else if (resultCode == RESULT_CANCELED) {
                savedCall.error(PHONE_HINT_USER_CANCELLED);
            } else if (resultCode == CredentialsApi.ACTIVITY_RESULT_NO_HINTS_AVAILABLE) {
                savedCall.error(PHONE_HINT_NO_HINTS_AVAILABLE);
            } else {
                savedCall.error(SOMETHING_WENT_WRONG);
            }
        }
    }

    private SMSReceiver smsReceiver;

    @PluginMethod
    public void startSmsReceiver(PluginCall call) {

        if (!GooglePlayServicesHelper.isAvailable(getContext())) {
            call.error(GooglePlayServicesHelper.UNAVAILABLE_ERROR_MESSAGE);
            return;
        }

        if (!GooglePlayServicesHelper.hasSupportedVersion(getContext())) {
            call.error(GooglePlayServicesHelper.UNSUPPORTED_VERSION_ERROR_MESSAGE);
            return;
        }

        saveCall(call);

        // Get an instance of SmsRetrieverClient, used to start listening for a matching
        // SMS message.
        SmsRetrieverClient client = SmsRetriever.getClient(getContext());

        // Starts SmsRetriever, which waits for ONE matching SMS message until timeout
        // (5 minutes). The matching SMS message will be sent via a Broadcast Intent with
        // action SmsRetriever#SMS_RETRIEVED_ACTION.
        Task<Void> task = client.startSmsRetriever();


        // Listen for success/failure of the start Task. If in a background thread, this
        // can be made blocking using Tasks.await(task, [timeout]);

        task.addOnSuccessListener(aVoid -> {
            // Successfully started retriever, expect broadcast intent
            PluginCall savedCall = getSavedCall();
            final boolean registered = registerReceiver();
            JSObject info = new JSObject();
            info.put("isRegistered", registered);
            savedCall.resolve(info);
        });

        task.addOnFailureListener(e -> {
            // Failed to start retriever, inspect Exception for more details
            PluginCall savedCall = getSavedCall();
            savedCall.error(e.getLocalizedMessage());
        });
    }

    private boolean registerReceiver() {

        smsReceiver = new SMSReceiver();
        smsReceiver.setOTPListener(this);

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(SmsRetriever.SMS_RETRIEVED_ACTION);

        try {
            // Registers receiver to listen for SMS
            getContext().registerReceiver(smsReceiver, intentFilter);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    private void unregisterReceiver() {
        if (smsReceiver == null) {
            return;
        }

        try {
            getContext().unregisterReceiver(smsReceiver);
            smsReceiver = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @PluginMethod
    public void removeSmsReceiver(PluginCall call) {
        unregisterReceiver();
        call.resolve();
    }

    @Override
    protected void handleOnDestroy() {
        super.handleOnDestroy();
        unregisterReceiver();
    }

    @Override
    public void onOTPReceived(final String message) {
        JSObject ret = new JSObject();
        ret.put(MESSAGE_KEY, message);
        notifyListeners("onSmsReceive", ret);
    }

    @Override
    public void onOTPReceivedError(final String error) {
        JSObject ret = new JSObject();
        ret.put(ERROR_KEY, error);
        notifyListeners("onSmsReceive", ret);
    }
}

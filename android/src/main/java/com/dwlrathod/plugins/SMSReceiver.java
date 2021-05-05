package com.dwlrathod.plugins;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.common.api.Status;


/**
 * BroadcastReceiver to wait for SMS messages. This can be registered either
 * in the AndroidManifest or at runtime.  Should filter Intents on
 * SmsRetriever.SMS_RETRIEVED_ACTION.
 */
public class SMSReceiver extends BroadcastReceiver {

    static final String MESSAGE_KEY = "message";
    static final String ERROR_KEY = "error";

    private static final String EXTRAS_NULL_ERROR_MESSAGE = "Extras is null.";
    private static final String STATUS_NULL_ERROR_MESSAGE = "Status is null.";
    private static final String TIMEOUT_ERROR_MESSAGE = "Timeout error.";

    private OTPReceiveListener otpListener;

    public void setOTPListener(OTPReceiveListener otpListener) {
        this.otpListener = otpListener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        if (SmsRetriever.SMS_RETRIEVED_ACTION.equals(intent.getAction())) {

            Bundle extras = intent.getExtras();
            if (extras == null) {
                if (otpListener != null) {
                    otpListener.onOTPReceivedError(EXTRAS_NULL_ERROR_MESSAGE);
                }
                return;
            }

            Status status = (Status) extras.get(SmsRetriever.EXTRA_STATUS);
            if (status == null) {
                if (otpListener != null) {
                    otpListener.onOTPReceivedError(STATUS_NULL_ERROR_MESSAGE);
                }
                return;
            }

            switch (status.getStatusCode()) {
                case CommonStatusCodes.SUCCESS:
                    String message = (String) extras.get(SmsRetriever.EXTRA_SMS_MESSAGE);
                    if (otpListener != null) {
                        otpListener.onOTPReceived(message);
                    }
                    break;
                case CommonStatusCodes.TIMEOUT:
                    if (otpListener != null) {
                        otpListener.onOTPReceivedError(TIMEOUT_ERROR_MESSAGE);
                    }
                    break;

            }
        }
    }

    public interface OTPReceiveListener {

        void onOTPReceived(final String message);

        void onOTPReceivedError(final String error);
    }
}
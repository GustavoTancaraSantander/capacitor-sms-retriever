import { PluginListenerHandle } from "@capacitor/core";

export interface SmsRetrieverPlugin {

  /**
   * This is a helper method to generate your message hash to be included in your SMS message.
   * Without the correct hash, your app won't receive the message callback. This only needs to be generated once per app and stored.
   * 
   * Note: Do not use hash strings dynamically computed on the client in your verification messages.
   * 
   * @since 0.1.0
   */
  getAppSignature(): Promise<AppSignature>;

  /**
   * Initiate hint picker to prompt the user to choose from the phone numbers stored on the device.
   * 
   * @since 0.1.0
   */
  requestPhoneNumber(): Promise<HintPhoneNumber>;

  /**
   * Start to listen for SMS messages.
   * 
   * @since 0.1.0
   */
  startSmsReceiver(): Promise<RegisterReceiver>;

  /**
   * Stop to listen for SMS messages.
   * 
   * @since 0.1.0
   */
  removeSmsReceiver(): Promise<void>;


  /**
   * Listen for when SMS is coming
   *
   * @since 0.1.0
   */
  addListener(
    eventName: 'onSmsReceive',
    listenerFunc: (receivedMessage: ReceivedMessage) => void,
  ): Promise<PluginListenerHandle> & PluginListenerHandle;
}

export interface AppSignature {

  /**
   * App signature.
   * 
   * @since 0.1.0
   */
  signature: string;
}
export interface HintPhoneNumber {

  /**
   * Phone number user selected
   * 
   * @since 0.1.0
   */
  phoneNumber: string;
}

export interface RegisterReceiver {

  /**
   * Returns if register receiver started or not.
   * 
   * @since 0.1.0
   */
  isRegistered: boolean;
}

export interface ReceivedMessage {

  /**
   * Received message with hash
   * 
   * @since 0.1.0
   */
  message?: string;

  /**
   * In case of any error
   * 
   * @since 0.1.0
   */
  error?: string;
}
import { WebPlugin } from '@capacitor/core';

import { AppSignature, HintPhoneNumber, RegisterReceiver, SmsRetrieverPlugin } from './definitions';

export class SmsRetrieverWeb extends WebPlugin implements SmsRetrieverPlugin {

  getAppSignature(): Promise<AppSignature> {
    this.throwUnsupportedError();
  }

  requestPhoneNumber(): Promise<HintPhoneNumber> {
    this.throwUnsupportedError();
  }

  startSmsReceiver(): Promise<RegisterReceiver> {
    this.throwUnsupportedError();
  }

  removeSmsReceiver(): Promise<void> {
    this.throwUnsupportedError();
  }

  private throwUnsupportedError(): never {
    throw this.unavailable(
      'SMS retriever API not available in this browser.',
    );
  }
}

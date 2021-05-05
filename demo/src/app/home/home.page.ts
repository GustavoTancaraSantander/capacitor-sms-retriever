import { Component } from '@angular/core';
import { PluginListenerHandle, Plugins } from '@capacitor/core';
const { SmsRetriever } = Plugins;
import { AppSignature, HintPhoneNumber, ReceivedMessage, RegisterReceiver } from 'capacitor-sms-retriever';

@Component({
  selector: 'app-home',
  templateUrl: 'home.page.html',
  styleUrls: ['home.page.scss'],
})
export class HomePage {

  public appSignature: string = "";
  public phoneNumber: string = "";
  public message: string = "";
  public smsRetrieverListener: PluginListenerHandle;

  constructor() { }

  async generateAppSignature() {
    let data: AppSignature = await SmsRetriever.getAppSignature();
    this.appSignature = data.signature;
  }

  copySignature() {
    Plugins.Clipboard.write({ string: this.appSignature });
  }

  async requestPhoneNumber() {
    try {
      let data: HintPhoneNumber = await SmsRetriever.requestPhoneNumber();
      this.phoneNumber = data.phoneNumber;
    } catch (error) {
      this.phoneNumber = "Error: " + error;
    }
  }

  async startWatching() {
    let data: RegisterReceiver = await SmsRetriever.startSmsReceiver();
    if (data.isRegistered) {
      this.smsRetrieverListener =
        SmsRetriever.addListener(
          'onSmsReceive', async (receivedMessage: ReceivedMessage) => {
            if (receivedMessage.message) {
              this.message = receivedMessage.message;
            } else {
              this.message = "Error: " + receivedMessage.error;
            }
            alert(this.message);
            this.smsRetrieverListener.remove();
            await SmsRetriever.removeSmsReceiver();
          }
        );
    }
  }

  async stopWatching() {
    await SmsRetriever.removeSmsReceiver();
  }


}

# capacitor-sms-retriever

Android SMS Retriever API for Capacitor.

<img src="https://img.shields.io/npm/v/capacitor-sms-retriever" />
<img src="https://img.shields.io/bower/l/capacitor-sms-retriever" />
<img src="https://img.shields.io/amo/dw/capacitor-sms-retriever" />

## Install

```bash
npm install capacitor-sms-retriever
npx cap sync
```

## API

<docgen-index>

* [`getAppSignature()`](#getappsignature)
* [`requestPhoneNumber()`](#requestphonenumber)
* [`startSmsReceiver()`](#startsmsreceiver)
* [`removeSmsReceiver()`](#removesmsreceiver)
* [`addListener(...)`](#addlistener)
* [Interfaces](#interfaces)

</docgen-index>

<docgen-api>
<!--Update the source file JSDoc comments and rerun docgen to update the docs below-->

### getAppSignature()

```typescript
getAppSignature() => any
```

This is a helper method to generate your message hash to be included in your SMS message.
Without the correct hash, your app won't receive the message callback. This only needs to be generated once per app and stored.

Note: Do not use hash strings dynamically computed on the client in your verification messages.

**Returns:** <code>any</code>

**Since:** 0.1.0

--------------------


### requestPhoneNumber()

```typescript
requestPhoneNumber() => any
```

Initiate hint picker to prompt the user to choose from the phone numbers stored on the device.

**Returns:** <code>any</code>

**Since:** 0.1.0

--------------------


### startSmsReceiver()

```typescript
startSmsReceiver() => any
```

Start to listen for SMS messages.

**Returns:** <code>any</code>

**Since:** 0.1.0

--------------------


### removeSmsReceiver()

```typescript
removeSmsReceiver() => any
```

Stop to listen for SMS messages.

**Returns:** <code>any</code>

**Since:** 0.1.0

--------------------


### addListener(...)

```typescript
addListener(eventName: 'onSmsReceive', listenerFunc: (receivedMessage: ReceivedMessage) => void) => Promise<PluginListenerHandle> & PluginListenerHandle
```

Listen for when SMS is coming

| Param              | Type                                                                                      |
| ------------------ | ----------------------------------------------------------------------------------------- |
| **`eventName`**    | <code>"onSmsReceive"</code>                                                               |
| **`listenerFunc`** | <code>(receivedMessage: <a href="#receivedmessage">ReceivedMessage</a>) =&gt; void</code> |

**Returns:** <code>any</code>

**Since:** 0.1.0

--------------------


### Interfaces


#### AppSignature

| Prop            | Type                | Description    | Since |
| --------------- | ------------------- | -------------- | ----- |
| **`signature`** | <code>string</code> | App signature. | 0.1.0 |


#### HintPhoneNumber

| Prop              | Type                | Description                | Since |
| ----------------- | ------------------- | -------------------------- | ----- |
| **`phoneNumber`** | <code>string</code> | Phone number user selected | 0.1.0 |


#### RegisterReceiver

| Prop               | Type                 | Description                                  | Since |
| ------------------ | -------------------- | -------------------------------------------- | ----- |
| **`isRegistered`** | <code>boolean</code> | Returns if register receiver started or not. | 0.1.0 |


#### ReceivedMessage

| Prop          | Type                | Description                | Since |
| ------------- | ------------------- | -------------------------- | ----- |
| **`message`** | <code>string</code> | Received message with hash | 0.1.0 |
| **`error`**   | <code>string</code> | In case of any error       | 0.1.0 |


#### PluginListenerHandle

| Prop         | Type                      |
| ------------ | ------------------------- |
| **`remove`** | <code>() =&gt; any</code> |

</docgen-api>

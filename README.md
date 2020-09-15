# SmartPesa Android starter example

This guide will teach you how you can use SmartPesa SDK in order to perform a transaction from an Android device.

# Installation Via Gradle

### Declare SmartPesa hosted repository

In build.gradle file [app/build.gradle line-23](https://github.com/smartpesa/mpos-android-starter-example/blob/master/app/build.gradle#L23) add custom repository to point to SmartPesa’s SDK Maven repository.
SmartPesa will provide the repository URL to customers and their 3rd parties to retrieve .aar upon request

### Add SmartPesa SDK dependency

Add SmartPesa dependency in the build.gradle file.
Please check with SmartPesa representative for the latest SDK version to use.

```
implementation ('com.smartpesa:sdk:4.0.0.80-BETA') {
        transitive = true
        changing = true
    }
```

# Service Manager
Service Manager is the main class which you will use to communicate with SmartPesa SDK to invoke SmartPesa’s APIs.

### Initializing Service Manager

Initialize the SmartPesa SDK as shown in [SplashActivity.java line-4](https://github.com/smartpesa/mpos-android-starter-example/blob/363f1a03a2f3d927485f99607722956ef2cc02c2/app/src/main/java/com/smartpesa/smartpesademo/activities/SplashActivity.java#L44).
Client specific host endpoint will be provided by SmartPesa on request.

# Authentication

Authentication is a process for verifying a merchant’s identity. In its simplest form, authentication is the act of verifying a merchant’s claim on his or her identity and is usually implemented through a combination of merchantCode, operatorCode and operatorPin and it allows the merchant to log in into the SmartPesa and perform the transactions and other features offered by SmartPesa. Authentication is a two step process

### Establishing Session

SmartPesa SDK uses session management for security and to validate access to the platform. SmartPesa SDK uses session management for security and to validate access to the platform. Session must be established before you are able to access functions of SmartPesa SDK. This session will expire after 24 hours of inactivity. To instantiate/ renew the session. You need to invoke getVersion method of the ServiceManager class.

To initiate the session, you can follow code example in [SplashActivity.java line-71](https://github.com/smartpesa/mpos-android-starter-example/blob/363f1a03a2f3d927485f99607722956ef2cc02c2/app/src/main/java/com/smartpesa/smartpesademo/activities/SplashActivity.java#L71)

### Logging In

MerchantCode, OperatorCode and OperatorPin combinations are matched against the server. If the three parameters match with the server's data, then the Merchant is allowed to perform transactions and other features of SmartPesa SDK.
SmartPesa will provide MerchantCode, OperatorCode and OperatorPin for your host upon request.

To initiate the login using SDK, you can follow code example in [LoginActivity.java line-64](https://github.com/smartpesa/mpos-android-starter-example/blob/363f1a03a2f3d927485f99607722956ef2cc02c2/app/src/main/java/com/smartpesa/smartpesademo/activities/LoginActivity.java#L64)

# SoftPOS

In order to use SoftPOS device, you will have to run this application on an Android device which has NFC.
To transact with a SoftPOS device, you will have to register and activate your device once.

### Register SoftPOS

To register device, you can follow code example in [SaleFragment.java line-82](https://github.com/smartpesa/mpos-android-starter-example/blob/363f1a03a2f3d927485f99607722956ef2cc02c2/app/src/main/java/com/smartpesa/smartpesademo/fragments/SaleFragment.java#L82)

### Activate SoftPOS

Upon registering SoftPOS, you will receive an OTP to the registered mobile number of the operator you are using to login to the application. You will need to pass the OTP into the activate method to activate the SoftPOS device.

To activate device, you can follow code example in [SaleFragment.java line-103](https://github.com/smartpesa/mpos-android-starter-example/blob/363f1a03a2f3d927485f99607722956ef2cc02c2/app/src/main/java/com/smartpesa/smartpesademo/fragments/SaleFragment.java#L103)

# Transaction

### Scan Terminal

Before starting a transaction you should start to scan for the available SoftPOS and bluetooth mPOS devices.
The scanTerminal() method will list both available Bluetooth mPOS devices and NFC devices to start the transaction. 
If you need to directly start a SoftPOS transaction, you can skip using the scanTerminal() method of the SmartPesa SDK and directly pass SpNFCDevice.getInstance() as the terminal into the TransactionBuilder.

To perform Scan Terminal, you can follow code example in [PaymentProgressActivity.java line-60](https://github.com/smartpesa/mpos-android-starter-example/blob/363f1a03a2f3d927485f99607722956ef2cc02c2/app/src/main/java/com/smartpesa/smartpesademo/activities/PaymentProgressActivity.java#L60)

### Initializing Transaction

After the device is selected, you need to setup the Transaction parameters. This could be done by using SmartPesa.TranscationParam.Builder class. This class provides a logical way to provide the parameters to your transaction and ensures that all the required parameters are set and validated.

To initialise transaction, you can follow code example in [PaymentProgressActivity.java line-101](https://github.com/smartpesa/mpos-android-starter-example/blob/50e9b88dfa0be31843aaa9b6525c88a71cc240e5/app/src/main/java/com/smartpesa/smartpesademo/activities/PaymentProgressActivity.java#L101)

### Perform Transaction

In order to start a transaction, you need to call ServiceManager.performTransaction(TransactionParam param, TransactionCallback callback)

To perform transaction, you can follow code example in [PaymentProgressActivity.java line-114](https://github.com/smartpesa/mpos-android-starter-example/blob/50e9b88dfa0be31843aaa9b6525c88a71cc240e5/app/src/main/java/com/smartpesa/smartpesademo/activities/PaymentProgressActivity.java#L114)

Please follow the callbacks and implement the user interface for your application as required.

Please follow our confluence documentation for a detailed documentation.
https://smartpesa.atlassian.net/wiki/spaces/SPD/pages/51090535/Getting+Started+With+Android

We also have a demo app which covers most functions of SmartPesa SDK such as perform transaction, print receipt, send notification and much more. 
Please check it here https://github.com/smartpesa/mpos-smartpesa-demo-app

You can email your request for login credentials or any queries to support@smartpesa.com


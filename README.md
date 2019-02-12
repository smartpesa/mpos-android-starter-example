# SmartPesa Android starter example

This guide will teach you how you can use SmartPesa SDK in order to perform a transaction from an Android device.

# Installation Via Gradle

In build.gradle file inside your_project/app folder

Add custom repository in your android {...} section to point to SmartPesa’s SDK Maven repository:
```
android {
    ...
    repositories {
        maven { url "https://<smartpesa hosted repository>" }
    }
    ...
}
```
SmartPesa will provide the repository URL to customers and their 3rd parties to retrieve .aar upon request

Add the following dependency to your dependencies {...} section.
```
compile 'com.smartpesa:sdk:3.2.9'
```

# Service Manager
Service Manager is the main class which you will use to communicate with SmartPesa SDK to invoke SmartPesa’s APIs.

# Initializing Service Manager
In order to initialize ServiceManager class. We are going to utilize ServiceManagerConfig class to setup all the options.

To do that, extend Application class and override it's onCreate method:

```
@Override
public void onCreate(){
  super.onCreate();
  ServiceManagerConfig config = ServiceManagerConfig.newBuilder(getApplicationContext())
            .endPoint("demo.smartpesa.com")
            .withoutSsl()
            .build();
 
  ServiceManager.init(config);
 
  //The rest of your code...
}
```

or when you have an SSL setup:

```
@Override
public void onCreate(){
  super.onCreate();
  ServiceManagerConfig config = ServiceManagerConfig.newBuilder(getApplicationContext())
            .endPoint("secure.smartpesa.com")
            .withSsl()
            .build();
 
  ServiceManager.init(config);
 
  //The rest of your code...
}
```

Please follow our confluence documentation for the process of authentication, logging in and performing transaction.
https://smartpesa.atlassian.net/wiki/spaces/SPD/pages/51090535/Getting+Started+With+Android

You can email your request for login credentials or any queries to support@smartpesa.com


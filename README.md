# FileShare

Now implement LAN file sharing feature in your Android App using FileShare library with ease!

## Adding FileShare to your Android project
The library is published in the jCenter repository. So you just add this in your App's `build.gradle` file, in `dependencies` :

```gradle
compile 'io.github.karuppiah7890:fileshare:0.1.0'
```

## API Usage
See the [API Usage](https://github.com/Android-File-Share/FileShare/wiki/API-Usage) Wiki page

## Demo App

FileSharer is a Demo App that is built using FileShare library. You can see the source code in the [FileSharerDemoApp](https://github.com/Android-File-Share/FileShare/tree/master/FileSharerDemoApp) folder. The APK for the Demo App is available in the releases.

I am yet to do some automation. These are the steps to use the App and to see how the Demo App works based on FileShare

Install the App in two Android devices.

In Receiver device : 

1. Create WiFi hotspot
2. Open App
3. click "Get File"
4. The app should show a code on top of the button. And say "Listening" as a Toast

In Sender device :

1. Connect to hotspot created by receiver
2. Open App
3. Type the code you saw in Receiver device
4. Click "Pick File and Send"
5. Pick a file (only docs can be picked) and select Done on top right

The file should get sent and you will see Toast messages accordingly in between for "Connected" , "Sending file", "Receiving file" etc in the two devices and finally the Receiver device will also show the location of the stored file as a Toast



## License
The library is available under [MIT License](https://github.com/Android-File-Share/FileShare/blob/master/LICENSE.md)

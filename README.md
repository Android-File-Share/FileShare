# FileShare

Now implement LAN file sharing feature in your Android App using FileShare module with ease!

## Adding FileShare to your Android project
The module is yet to be published in jCenter repository. So, for now, to use the module, you have to include my maven repository in your project's `build.gradle` file in `allprojects`. After adding it will look something like this :

```
allprojects {
    repositories {
        jcenter()
        maven {
            url  "https://dl.bintray.com/karuppiah7890/maven"
        }
    }
}
```

And then in your App's `build.gradle` file, put this in `dependencies` :

`compile 'io.github.karuppiah7890:fileshare:0.1.0'`

After adding, it will look something like this :

```
dependencies {
    compile fileTree(dir: 'libs', include: ['*.jar'])
    androidTestCompile('com.android.support.test.espresso:espresso-core:2.2.2', {
        exclude group: 'com.android.support', module: 'support-annotations'
    })
    compile 'io.github.karuppiah7890:fileshare:0.1.0'
    compile 'com.android.support:appcompat-v7:25.0.0'
    testCompile 'junit:junit:4.12'
}
```

# Demo App

FileSharer is a Demo App that is built using FileShare library. You can see the source code in the FileSharerDemoApp folder. The APK for the Demo App is available in the releases.

I am yet to do some automation. These are the steps to use the App and to see how the Demo App works based on FileShare module

Install the App in two Android devices.

In Receiver device
1. Create WiFi hotspot
2. Open App
3. click "Get File"
4. The app should show a code on top of the button. And say "Listening" as a Toast

In Sender device
1. Connect to hotspot created by receiver
2. Open App
3. Type the code you saw in Receiver device
4. Click "Pick File and Send"
5. Pick a file (only docs can be picked) and select Done on top right

The file should get sent and you will see Toast messages accordingly in between for "Connected" , "Sending file", "Receiving file" etc in the two devices and finally the Receiver device will also show the location of the stored file as a Toast

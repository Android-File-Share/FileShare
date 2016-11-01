package io.github.karuppiah7890.fileshare;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Messenger;

import java.io.File;
import java.net.InetAddress;

public class SenderService extends Service {

    private final String PORT = "PORT";
    private final String MESSENGER = "MESSENGER";
    private final String FILE = "FILE";
    private final String RECEIVER_IP = "RECEIVER_IP";

    private InetAddress receiverIP;
    private int port;
    private Messenger messenger;
    private File file;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Bundle b = intent.getExtras();

        receiverIP = (InetAddress) b.get(RECEIVER_IP);
        port = (int) b.get(PORT);
        messenger = (Messenger) b.get(MESSENGER);
        file = (File) b.get(FILE);

        SenderThread senderThread = new SenderThread(receiverIP,port,file,messenger);

        senderThread.start();

        return START_REDELIVER_INTENT;
    }


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}

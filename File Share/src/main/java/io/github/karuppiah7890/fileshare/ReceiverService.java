package io.github.karuppiah7890.fileshare;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Messenger;

public class ReceiverService extends Service {

    private final String PORT = "PORT";
    private final String MESSENGER = "MESSENGER";

    private int port;
    private Messenger messenger;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Bundle b = intent.getExtras();

        port = (int) b.get(PORT);
        messenger = (Messenger) b.get(MESSENGER);

        ReceiverThread receiverThread = new ReceiverThread(port,messenger);

        receiverThread.start();

        return START_REDELIVER_INTENT;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}

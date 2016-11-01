package io.github.karuppiah7890.fileshare;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Messenger;
import android.util.Log;

import java.io.IOException;
import java.net.ServerSocket;

public class FileReceiver {

    private final boolean mDebug = true;

    private final int MIN_PORT_NUMBER = 1024;
    private final int MAX_PORT_NUMBER = 65536;
    private final String PORT = "PORT";
    private final String MESSENGER = "MESSENGER";

    // Constants start from 2001
    public static final int CODE = 2001;
    public static final int LISTENING = 2002;
    public static final int CONNECTED = 2003;
    public static final int RECEIVING_FILE = 2004;
    public static final int FILE_RECEIVED = 2005;
    public static final int RECEIVE_ERROR = 2006;

    private Context context;
    private Handler mHandler;

    private Intent i;

    public FileReceiver(Context context, Handler mHandler) {
        this.context = context;
        this.mHandler = mHandler;
    }

    private boolean isPortAvailable(int port) {

        boolean available;

        if (port < MIN_PORT_NUMBER || port > MAX_PORT_NUMBER) {
            throw new IllegalArgumentException("Invalid port: " + port);
        }

        ServerSocket ss = null;

        try {
            ss = new ServerSocket(port);
            ss.setReuseAddress(true);
            available = true;
        } catch (IOException e) {
            available = false;
        } finally {

            if (ss != null) {
                try {
                    ss.close();
                } catch (IOException e) {

                }
            }
        }

        return available;
    }

    private int getRandomPort() {

        int port;

        do{
            port = (int) (MIN_PORT_NUMBER + 1 + Math.floor(Math.random()*(MAX_PORT_NUMBER - MIN_PORT_NUMBER-1)));

            if(mDebug)
            Log.i("FileReceiver","Trying port : " + port);

        }while(!isPortAvailable(port));

        return port;

    }

    public void getFile(){

        int port = getRandomPort();

        if(mDebug)
        Log.i("FileReceiver","Port : " + port);

        i = new Intent(context,ReceiverService.class);

        i.putExtra(PORT,port);
        i.putExtra(MESSENGER,new Messenger(mHandler));

        context.startService(i);

    }

    public void close() {
        context.stopService(i);
    }
}

package io.github.karuppiah7890.fileshare;

import android.content.Context;
import android.content.Intent;
import android.net.DhcpInfo;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.Messenger;
import android.text.format.Formatter;
import android.util.Log;

import java.io.File;
import java.net.InetAddress;

public class FileSender {

    private final boolean mDebug = true;

    private final String PORT = "PORT";
    private final String MESSENGER = "MESSENGER";
    private final String FILE = "FILE";
    private final String RECEIVER_IP = "RECEIVER_IP";

    // Constants start from 1001
    public static final int CONNECTING = 1001;
    public static final int CONNECTED = 1002;
    public static final int SENDING_FILE = 1003;
    public static final int FILE_SENT = 1004;
    public static final int SEND_ERROR = 1005;

    private Context context;
    private int port;
    private Handler mHandler;

    private final WifiManager manager;
    private final DhcpInfo dhcp;

    Intent i;

    public FileSender(Context context, Handler mHandler) {
        this.context = context;
        this.mHandler = mHandler;
        manager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        dhcp = manager.getDhcpInfo();
    }

    private InetAddress getMyIP() {
        final String address = Formatter.formatIpAddress(dhcp.ipAddress); // ipAddress - IP address of my device, assigned through dhcp
        InetAddress myIP = null;

        try {

            myIP = InetAddress.getByName(address);
            if(mDebug)
            Log.i("FileSender","My IP : " + myIP.toString());

        } catch (Exception e) {
            if(mDebug)
            Log.e("FileSender","Cannot find my own IP. Error : " + e.toString());
        }

        return myIP;
    }

    private InetAddress getReceiverIP() {
        final String address = Formatter.formatIpAddress(dhcp.gateway); // gateway - default gateway IP address
        InetAddress receiverIP = null;

        try {

            receiverIP = InetAddress.getByName(address);

            if(mDebug)
            Log.i("FileSender","Receiver IP : " + receiverIP.toString());

        } catch (Exception e) {
            if(mDebug)
            Log.e("FileSender","Cannot find receiver's IP. Error : " + e.toString());
        }

        return receiverIP;
    }

    public void sendFile(File file,int code) {

        if(!file.exists()){
            mHandler.obtainMessage(SEND_ERROR,"File " + file.getName() + " doesn't exist").sendToTarget();
            return;
        }

        if(!file.isFile()){
            mHandler.obtainMessage(SEND_ERROR,file.getName() + " is a folder, not file").sendToTarget();
            return;
        }

        this.port = code;

        InetAddress receiverIP = getReceiverIP();

        i = new Intent(context,SenderService.class);

        i.putExtra(RECEIVER_IP,receiverIP);
        i.putExtra(PORT,port);
        i.putExtra(MESSENGER,new Messenger(mHandler));
        i.putExtra(FILE,file);


        context.startService(i);
    }

    public void close() {
        if(context!=null && i!=null)
            context.stopService(i);
    }
}

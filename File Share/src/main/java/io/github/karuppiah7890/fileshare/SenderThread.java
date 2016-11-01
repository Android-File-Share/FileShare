package io.github.karuppiah7890.fileshare;

import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

public class SenderThread extends Thread {

    private InetAddress receiverIP;
    private int port;
    private File fileToSend;
    private Messenger messenger;
    private Socket senderSocket;
    private int PKT_SIZE = 60*1024;

    public SenderThread(InetAddress receiverIP, int port, File fileToSend, Messenger messenger){
        this.receiverIP = receiverIP;
        this.port = port;
        this.messenger = messenger;
        this.fileToSend = fileToSend;
    }

    @Override
    public void run() {
        Message message;

        try {

            message = Message.obtain();
            message.what = FileSender.CONNECTING;
            message.obj = "";
            messenger.send(message);

            senderSocket = new Socket(receiverIP,port);

            message = Message.obtain();
            message.what = FileSender.CONNECTED;
            message.obj = "";
            messenger.send(message);

            DataOutputStream out = new DataOutputStream(senderSocket.getOutputStream());

            message = Message.obtain();
            message.what = FileSender.SENDING_FILE;
            message.obj = "";
            messenger.send(message);

            // Send File Name
            out.writeUTF(fileToSend.getName());

            DataInputStream din = new DataInputStream(new FileInputStream(fileToSend));

            // Send File Size
            long fileSize = fileToSend.length();
            out.writeLong(fileSize);

            int totalLength = 0;
            int length = 0;
            byte[] sendData = new byte[PKT_SIZE];

            long startTime = System.currentTimeMillis();

            // Send the file data
            while ((length = din.read(sendData)) != -1) {

                out.write(sendData, 0, length);

                totalLength += length;

            }

            long stopTime = System.currentTimeMillis();

            din.close();

            double time = (stopTime - startTime) / 1000.0;

            double speed = (totalLength / time) / 1048576.0;

            message = Message.obtain();
            message.what = FileSender.FILE_SENT;
            message.obj = "";

            messenger.send(message);

        } catch (Exception e){

            e.printStackTrace();

            message = Message.obtain();
            message.what = FileSender.SEND_ERROR;
            message.obj = e.toString();

            try {
                messenger.send(message);
            } catch (RemoteException re) {
                Log.e("SenderThread","Error in sending an error message! Error : " + re.toString());
                re.printStackTrace();
            }

        } finally {

            try {

                if(senderSocket!=null)
                    senderSocket.close();

            } catch (IOException ioe) {
                Log.e("SenderThread","Error in closing sockets. Error : " + ioe.toString());
                ioe.printStackTrace();
            }
        }
    }
}

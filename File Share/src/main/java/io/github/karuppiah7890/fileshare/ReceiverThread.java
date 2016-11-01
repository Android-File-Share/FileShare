package io.github.karuppiah7890.fileshare;

import android.os.Environment;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ReceiverThread extends Thread {

    private int port;
    private Messenger messenger;
    private ServerSocket listenerSocket;
    private Socket communicationSocket;
    private int PKT_SIZE = 60*1024;

    public ReceiverThread(int port,Messenger messenger){
        this.port = port;
        this.messenger = messenger;
        File folder = new File(Environment.getExternalStorageDirectory() + "/FileSharer/");

        folder.mkdirs();
    }

    @Override
    public void run() {

        Message message;

        try {
            listenerSocket = new ServerSocket(port);

            message = Message.obtain();
            message.what = FileReceiver.CODE;
            message.obj = port;
            messenger.send(message);

            message = Message.obtain();
            message.what = FileReceiver.LISTENING;
            message.obj = "";
            messenger.send(message);

            communicationSocket = listenerSocket.accept();

            message = Message.obtain();
            message.what = FileReceiver.CONNECTED;
            message.obj = "";
            messenger.send(message);

            DataInputStream in = new DataInputStream(communicationSocket.getInputStream());

            message = Message.obtain();
            message.what = FileReceiver.RECEIVING_FILE;
            message.obj = "";
            messenger.send(message);

            // Read File Name and create Output Stream
            String fileName = in.readUTF();

            File receiveFile = new File(Environment.getExternalStorageDirectory() + "/FileSharer/" + fileName);

            DataOutputStream dout = new DataOutputStream(new FileOutputStream(receiveFile,true));

            // Read File Size
            long fileSize = in.readLong();

            int totalLength = 0;
            int length = 0;
            byte[] receiveData = new byte[PKT_SIZE];

            long startTime = System.currentTimeMillis();

            // Get the file data
            while( fileSize>0 && ( ( length = in.read( receiveData,0,(int) Math.min(receiveData.length,fileSize) ))!= -1) )
            {
                dout.write(receiveData, 0, length);

                totalLength += length;

                fileSize -= length;
            }

            long stopTime = System.currentTimeMillis();

            dout.close();

            double time = (stopTime - startTime) / 1000.0;

            double speed = (totalLength / time) / 1048576.0;

            message = Message.obtain();
            message.what = FileReceiver.FILE_RECEIVED;
            message.obj = receiveFile;

            messenger.send(message);

        } catch (Exception e){

            e.printStackTrace();

            message = Message.obtain();
            message.what = FileReceiver.RECEIVE_ERROR;
            message.obj = e.toString();

            try {
                messenger.send(message);
            } catch (RemoteException re) {
                Log.e("ReceiverThread","Error in sending an error message! Error : " + re.toString());
                re.printStackTrace();
            }

        } finally {

                try {

                    if(communicationSocket!=null)
                    communicationSocket.close();

                    if(listenerSocket!=null)
                    listenerSocket.close();

                } catch (IOException ioe) {
                    Log.e("ReceiverThread","Error in closing sockets. Error : " + ioe.toString());
                    ioe.printStackTrace();
                }
        }

    }
}

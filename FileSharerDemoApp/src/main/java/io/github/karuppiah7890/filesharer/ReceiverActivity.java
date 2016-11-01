package io.github.karuppiah7890.filesharer;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

import io.github.karuppiah7890.fileshare.FileReceiver;

public class ReceiverActivity extends AppCompatActivity {

    FileReceiver fileReceiver;
    TextView tvCode;

    private final Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case FileReceiver.CODE :
                    tvCode.setText((int)msg.obj + "");
                    break;

                case FileReceiver.LISTENING :
                    Toast.makeText(ReceiverActivity.this,"Listening...",Toast.LENGTH_SHORT).show();
                    break;

                case FileReceiver.CONNECTED:
                    Toast.makeText(ReceiverActivity.this,"Connected!",Toast.LENGTH_SHORT).show();
                    break;

                case FileReceiver.RECEIVING_FILE :
                    Toast.makeText(ReceiverActivity.this,"Receiving File!",Toast.LENGTH_SHORT).show();
                    break;

                case FileReceiver.FILE_RECEIVED :
                    File file = (File) msg.obj;
                    Toast.makeText(ReceiverActivity.this,file.getName() + " Received!",Toast.LENGTH_SHORT).show();
                    Toast.makeText(ReceiverActivity.this,"Stored in " + file.getAbsolutePath(),Toast.LENGTH_SHORT).show();
                    fileReceiver.close();
                    break;

                case FileReceiver.RECEIVE_ERROR :
                    Toast.makeText(ReceiverActivity.this,"Error occured : " + (String)msg.obj,Toast.LENGTH_SHORT).show();
                    fileReceiver.close();
                    break;
            }
        }
    };

    public void getFile(View view) {

        fileReceiver = new FileReceiver(this,mHandler);

        fileReceiver.getFile();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receiver);

        tvCode = (TextView) findViewById(R.id.tvCode);
    }
}

package io.github.karuppiah7890.filesharer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

import droidninja.filepicker.FilePickerBuilder;
import droidninja.filepicker.FilePickerConst;
import io.github.karuppiah7890.fileshare.FileSender;

public class SenderActivity extends AppCompatActivity {

    private ArrayList<String> docPaths;
    private EditText etCode;
    private FileSender fileSender;
    Button bPickFile;

    private final Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case FileSender.CONNECTING :
                    Toast.makeText(SenderActivity.this,"Connecting...",Toast.LENGTH_SHORT).show();
                    break;

                case FileSender.CONNECTED :
                    Toast.makeText(SenderActivity.this,"Connected!",Toast.LENGTH_SHORT).show();
                    break;

                case FileSender.SENDING_FILE :
                    Toast.makeText(SenderActivity.this,"Sending File!",Toast.LENGTH_SHORT).show();
                    break;

                case FileSender.FILE_SENT :
                    Toast.makeText(SenderActivity.this,"File Sent!",Toast.LENGTH_SHORT).show();
                    fileSender.close();
                    bPickFile.setEnabled(true);
                    break;

                case FileSender.SEND_ERROR :
                    Toast.makeText(SenderActivity.this,"Error occured : " + (String)msg.obj,Toast.LENGTH_SHORT).show();
                    fileSender.close();
                    bPickFile.setEnabled(true);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sender);

        etCode = (EditText) findViewById(R.id.etCode);
        bPickFile = (Button) findViewById(R.id.bPickFile);
    }

    private void sendMyFile(String filePath) {

        Log.i("SenderActivity","File Path : " + filePath);
        Toast.makeText(this,filePath,Toast.LENGTH_SHORT).show();

        File file = new File(filePath);

        fileSender = new FileSender(this,mHandler);

        if(!etCode.getText().toString().equals("")){
            int code = Integer.parseInt(etCode.getText().toString());
            fileSender.sendFile(file,code);
        }


    }

    public void pickAFile(View view){
        FilePickerBuilder.getInstance().setMaxCount(1)
                .setActivityTheme(R.style.AppTheme)
                .pickDocument(this);

        view.setEnabled(false);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode)
        {
            case FilePickerConst.REQUEST_CODE_DOC:
                if(resultCode== Activity.RESULT_OK && data!=null)
                {
                    docPaths = new ArrayList<>();
                    docPaths.addAll(data.getStringArrayListExtra(FilePickerConst.KEY_SELECTED_DOCS));
                    sendMyFile(docPaths.get(0));
                }
                break;
        }
    }
}

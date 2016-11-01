package io.github.karuppiah7890.filesharer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class DashBoard extends AppCompatActivity implements View.OnClickListener {

    Button bSend,bReceive;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);

        bSend = (Button) findViewById(R.id.bSend);
        bReceive = (Button) findViewById(R.id.bReceive);

        bSend.setOnClickListener(this);
        bReceive.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        switch (id){
            case R.id.bSend :
                                    startActivity(new Intent(DashBoard.this,SenderActivity.class));
                                    break;

            case R.id.bReceive :
                                    startActivity(new Intent(DashBoard.this,ReceiverActivity.class));
                                    break;
        }
    }
}

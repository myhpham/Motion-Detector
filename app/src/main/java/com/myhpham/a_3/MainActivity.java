package com.myhpham.a_3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private final String TAG = "MainActivity";

    TextView status_textView;
    Button reset_button, exit_button;

    LocalService localService;
    private boolean bound;
    boolean moved;
    boolean movedResult;

    AccStatus status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        status_textView = findViewById(R.id.status_textView);
        reset_button = findViewById(R.id.reset_button);
        exit_button = findViewById(R.id.exit_button);

        //reset button listener
        reset_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetAccelerometer();
            }
        });

        //exit button listener
        exit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exit();
            }
        });
    }

    @Override
    public void onResume(){
        Log.d(TAG, "onResume");

        super.onResume();

        //start service
        Intent it = new Intent(MainActivity.this, LocalService.class);
        startService(it);

        //calls serivce binder
        bindToLocalService();
    }

    //service binder
    private void bindToLocalService() {
        Log.d(TAG, "Start service binding...");
        Intent it = new Intent(MainActivity.this, LocalService.class);
        bindService(it, connection, Context.BIND_AUTO_CREATE);
    }

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            LocalService.LocalBinder binder = (LocalService.LocalBinder) service;
            localService = binder.getService();
            bound = true;

            Log.d(TAG, "Successful bind");

            movedResult = localService.didItMove();
            showResult(movedResult);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.d(TAG, "Unbound");
            bound = false;
        }
    };

    @Override
    public void onPause(){
        Log.d(TAG, "onPause");
        //unbinds service but does not exit
        if(bound) {
            unbindService(connection);
            bound = false;
        }
        super.onPause();
    }

    //exit button clicked
    //stops movement service and closes activity
    public void exit(){
        unbindService(connection);
        bound = false;
        Intent it = new Intent(MainActivity.this, LocalService.class);
        stopService(it);
        finish();
    }

    //reset button clicked
    public void resetAccelerometer(){
        movedResult = localService.resetAccelerometer();
        showResult(movedResult);
    }

    public void showResult(Boolean m){
        Log.d(TAG, "showResult");

        if (m){
            status_textView.setText(R.string.active_string);
        }
        else {
            status_textView.setText(R.string.inactive_string);
        }
    }
}
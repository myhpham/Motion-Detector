package com.myhpham.a_3;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class LocalService extends Service {

    private final String TAG = "LocalService";
    private final IBinder binder = new LocalBinder();
    WakeLock wakeLock;
    MovementTask movementTask;
    Thread thread;

    public class LocalBinder extends Binder {
        LocalService getService() {
            return LocalService.this;
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "Binded");
        return binder;
    }

    @Override
    public void onCreate() {
        Log.d(TAG, "LocalService onCreate");

        movementTask = new MovementTask(getApplicationContext());
        thread = new Thread(movementTask);
        thread.start();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //Toast.makeText(this, "onStartCommand", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "onStartCommand");

        PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);
        wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
                "MyApp::MyWakelockTag");
        wakeLock.acquire();
        //thread.start();

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        wakeLock.release();
        movementTask.stopRunning();
        thread.interrupt();

        Log.d(TAG, "onDestroy");
    }

    public boolean didItMove(){
        Log.d(TAG, "Start didItMove thread...");
        //get from thread
        boolean moved;
        moved = movementTask.didItMove();
        return moved;
    }

    public boolean resetAccelerometer(){
        //reset from thread
        boolean m;
        m = movementTask.resetAccelerometer();
        return m;
    }
}
package com.myhpham.a_3;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;
import android.widget.Toast;

import java.util.Date;

import static android.content.Context.SENSOR_SERVICE;

public class MovementTask implements SensorEventListener, Runnable {

    private final String TAG = "MovementTask";

    SensorManager sensorManager;
    Sensor accelerometer;
    Context ct;

    float accX, accY;

    Date startTime = null;
    Date d;
    Date firstTimeMoved;
    private boolean isRunning = false;
    boolean moved = false;
    final Object lock = new Object();

    AccStatus status;

    //movementTask constructor
    public MovementTask(Context context){
        ct = context;
        if(startTime == null) {
            startTime = new Date();
        }
        Log.d(TAG, "Start time: " + startTime.getTime());

        status = new AccStatus(moved, accX, accX);

        sensorManager = (SensorManager) ct.getSystemService(SENSOR_SERVICE);

        if(sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)!= null){
            accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            Log.d(TAG, "Sensor made");
            //Toast.makeText(context, "sensor made", Toast.LENGTH_SHORT).show();
            sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
            Log.d(TAG, "Sensor registered");
        }
    }

    @Override
    public void run() {
        isRunning = true;
        if (isRunning) {
            try{
                //something
            } catch (Exception e){
                //something?
            }
        }
    }
    
    @Override
    public void onSensorChanged(SensorEvent event) {
        Log.d(TAG, "onSensorChanged");

        float x_threshold = (float) 0.5;
        float y_threshold = (float) 9.81;

        //get movement differences
        float x_diff = (event.values[0] - x_threshold);
        float y_diff = (event.values[1] - y_threshold);

        //test time moved and start time
        if(firstTimeMoved == null){
            if(x_diff > 1 || y_diff > 1){
                firstTimeMoved = new Date();

                Log.d(TAG, "First time moved: " + firstTimeMoved.getTime());

                int seconds = (int) ((firstTimeMoved.getTime() - startTime.getTime()) / 1000);
                if(seconds > 15) {
                    accX = event.values[0];
                    accY = event.values[1];

                    Log.d(TAG, "Time moved passed");
                }
                else {
                    Log.d(TAG, "Time moved not passed");
                }
            }
        }
        else {
            x_diff = 0;
            y_diff = 0;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        //do nothing
    }

    public boolean didItMove() {
        Log.d(TAG, "didItMove");
        //ct = context;

        Date d = new Date();
        if(firstTimeMoved != null) {
            synchronized (lock) {
                int seconds = (int) ((d.getTime() - firstTimeMoved.getTime()) / 1000);
                Log.d(TAG, "sec: " + seconds);

                if (firstTimeMoved != null && seconds > 15) {
                    moved = true;
                    Log.d(TAG, "Time reopened passed");
                }
                else{
                    Log.d(TAG, "Time reopened not passed");
                }
            }
        }
        return moved;
    }

    public boolean resetAccelerometer(){
        Log.d(TAG, "resetAccelerometer");

        startTime = new Date();
        firstTimeMoved = null;
        moved = false;

        Log.d(TAG, "Time reset: " + startTime.getTime());
        //Toast.makeText(ct, "X: " + accX + " Y: " + accY, Toast.LENGTH_SHORT).show();

        return moved;
    }

    public void stopRunning(){
        Log.d(TAG, "Stopped");
        isRunning = false;
    }
}

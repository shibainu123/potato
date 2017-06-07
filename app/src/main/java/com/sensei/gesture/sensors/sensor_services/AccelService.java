package com.sensei.gesture.sensors.sensor_services;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;
import android.os.IBinder;

import com.sensei.gesture.sensors.BinderSub;

public class AccelService extends SensorService {

    private final IBinder accelBinder = new MyLocalBinder();
    private SensorManager sensorManager;
    private Sensor accelerometer;

    public AccelService() {
        //sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        //accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        //accelBinder = new MyLocalBinder();
    }

    public void init (Context context, String configuration) {}


    public void register (){
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public IBinder onBind(Intent intent) {
        register ();
        return accelBinder; //Return the communication channel to the service.
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        //Do something here if the sensor accuracy changes
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        // alpha is calculated as t / (t + dT)
        // with t, the low-pass filter's time-constant
        // and dT, the event delivery rate

        float gravity [] = new float [3];
        float linear_accel[] = new float [3];

        final float alpha = (float) 0.8;

        gravity[0] = alpha * gravity[0] + (1 - alpha) * event.values[0];
        gravity[1] = alpha * gravity[1] + (1 - alpha) * event.values[1];
        gravity[2] = alpha * gravity[2] + (1 - alpha) * event.values[2];

        linear_accel[0] = event.values[0] - gravity[0];
        linear_accel[1] = event.values[1] - gravity[1];
        linear_accel[2] = event.values[2] - gravity[2];
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        sensorManager.unregisterListener(this);
    }

    class MyLocalBinder extends BinderSub {
        public AccelService getService(){
            return AccelService.this;
        }
    }
}

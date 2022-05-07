package com.example.project1.objects;

import android.hardware.Sensor;
import android.hardware.SensorManager;

public class MySensors {

    public MySensors() {
    }

    private SensorManager sensorManager;
    private Sensor accSensor;

    public SensorManager getSensorManager() {
        return sensorManager;
    }

    public void setSensorManager(SensorManager sensorManager) {
        this.sensorManager = sensorManager;
    }

    public Sensor getAccSensor() {
        return accSensor;
    }

    public void setAccSensor(Sensor accSensor) {
        this.accSensor = accSensor;
    }


    public void initSensor() {
        accSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    }
}

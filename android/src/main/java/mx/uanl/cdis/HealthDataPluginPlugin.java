package mx.uanl.cdis;

import com.getcapacitor.JSObject;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.annotation.CapacitorPlugin;

import androidx.appcompat.app.AppCompatActivity;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.content.Context;

@CapacitorPlugin(name = "HealthDataPlugin")
public class HealthDataPluginPlugin extends Plugin implements SensorEventListener {

    private AppCompatActivity activity;
    private SensorManager sensorManager;
    private Sensor stepCounterSensor;


    private HealthDataPlugin implementation = new HealthDataPlugin();

    @Override
    public void load() {
        this.activity = getActivity();
        initSensors(this.activity);
    }

    private void initSensors(AppCompatActivity activity) {
        sensorManager = (SensorManager) getContext().getSystemService(Context.SENSOR_SERVICE);
        stepCounterSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
    }

    private void registerAvailableSensors() {
        if (stepCounterSensor != null) {
            sensorManager.registerListener(this, stepCounterSensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        JSObject ret = getJSObjectForSensorData(sensorEvent);
        System.out.println("onSensorChanged: activity detected");
        if (sensorEvent.sensor.getType() == Sensor.TYPE_STEP_COUNTER) {
            notifyListeners("count", ret);
            ret.put("count", sensorEvent.values[0]);
            System.out.println(sensorEvent.values[0]);
        }
    }

    private JSObject getJSObjectForSensorData(SensorEvent event) {
        JSObject res = new JSObject();
        res.put("count", event.values[0]);
        System.out.println(event.values[0]);
        return res;
    }

    private boolean isSensorAvailable() {

        if(sensorManager==null){
            sensorManager = (SensorManager) this.getContext().getSystemService(Context.SENSOR_SERVICE);
        }

        if (sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER) != null) {
            return true;
        } else {
            return false; // Failure! No sensor.
        }
    }

    protected void onResume(){
        sensorManager.registerListener(this, stepCounterSensor, sensorManager.SENSOR_DELAY_FASTEST);
    }

    protected void onPause(){
        sensorManager.unregisterListener(this);

    }

    @PluginMethod
    public void getSteps(PluginCall call){
        if(isSensorAvailable()){
            this.load();
            this.registerAvailableSensors();
            onResume();

            JSObject res = new JSObject();

            res.put("name", stepCounterSensor.getName());


            call.resolve(res);
        } else {
            call.reject("Light sensor not available cannot get info");
        }

    }



    @PluginMethod
    public void echo(PluginCall call) {
        String value = call.getString("value");

        JSObject ret = new JSObject();
        ret.put("value", implementation.echo(value));
        call.resolve(ret);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }


}

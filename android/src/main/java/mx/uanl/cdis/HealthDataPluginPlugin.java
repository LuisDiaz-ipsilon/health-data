package mx.uanl.cdis;

import com.getcapacitor.JSObject;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.annotation.CapacitorPlugin;
import com.getcapacitor.annotation.Permission;
import com.getcapacitor.JSArray;
import com.getcapacitor.PermissionState;
import com.getcapacitor.annotation.ActivityCallback;
import com.getcapacitor.annotation.Permission;
import com.getcapacitor.annotation.PermissionCallback;

import androidx.appcompat.app.AppCompatActivity;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.content.Context;
import android.Manifest;
import android.os.Build;
import android.content.SharedPreferences;
import android.util.Log;
import android.provider.Settings;
import android.content.Intent;
import android.net.Uri;

import static android.content.Context.MODE_PRIVATE;


@CapacitorPlugin(name = "HealthDataPlugin", permissions = { @Permission(strings = { Manifest.permission.ACTIVITY_RECOGNITION }, alias = HealthDataPluginPlugin.PERMISSION_ALIAS_ACTIVITY_RECOGNITION) })
public class HealthDataPluginPlugin extends Plugin implements SensorEventListener {

    public static final String PERMISSION_ALIAS_ACTIVITY_RECOGNITION = "stepCounterPermission";

    private AppCompatActivity activity;
    private SensorManager sensorManager;
    private Sensor stepCounterSensor;
    private float stepCount = 0;
    private HealthDataPlugin implementation = new HealthDataPlugin();

    @Override
    public void load() {
        //When starting the application this method will be executed first.
        this.activity = getActivity();
        initSensors(this.activity);
    }

    private void initSensors(AppCompatActivity activity) {
        //We set the required sensors.
        sensorManager = (SensorManager) getContext().getSystemService(Context.SENSOR_SERVICE);
        stepCounterSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
    }

    private void registerAvailableSensors() {
        //Refresh rate is set.
        if (stepCounterSensor != null) {
            sensorManager.registerListener(this, stepCounterSensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        //Whenever the sensor value changes this method will be executed. It is necessary to have the permission granted and that the device has the sensor.
        JSObject ret = getJSObjectForSensorData(sensorEvent);
        if (sensorEvent.sensor.getType() == Sensor.TYPE_STEP_COUNTER) {
            notifyListeners("count", ret);
            this.stepCount = sensorEvent.values[0];
            ret.put("count", sensorEvent.values[0]);
        }
    }

    private JSObject getJSObjectForSensorData(SensorEvent event) {
        JSObject res = new JSObject();
        res.put("count", event.values[0]);
        return res;
    }

    private boolean isSensorAvailable() {
        //validate sensor.
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
        //Resets the sensor with the refresh time values.
        sensorManager.registerListener(this, stepCounterSensor, sensorManager.SENSOR_DELAY_FASTEST);
    }

    protected void onPause(){
        //Pause the update of the value of the steps.
        sensorManager.unregisterListener(this);
    }

    @PluginMethod
    public void getSteps(PluginCall call){
        //
        if(isSensorAvailable()){
            if (getPermissionState(PERMISSION_ALIAS_ACTIVITY_RECOGNITION) == PermissionState.GRANTED) {
                this.load();
                this.registerAvailableSensors();

                JSObject res = new JSObject();

                res.put("name", stepCounterSensor.getName());
                res.put("count", this.stepCount);
                call.resolve(res);
            } else{
                // Permission denied, request permission
                PluginCall permissionCall = call;
                _checkPermission(permissionCall, true);
            }
        } else {
            call.reject("Step counter sensor not available cannot get info");
        }

    }

    @PluginMethod
    public void echo(PluginCall call) {
        //Main method, in case the permission has not been granted,
        // it will return null, in the solution of the call returned two values,
        // string: sensor name, float: number of steps.
        String value = call.getString("value");

        JSObject ret = new JSObject();
        ret.put("value", implementation.echo(value));
        call.resolve(ret);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    private static final String TAG_PERMISSION = "permission";
    private static final String GRANTED = "granted";
    private static final String DENIED = "denied";
    private static final String ASKED = "asked";
    private static final String NEVER_ASKED = "neverAsked";

    private static final String PERMISSION_NAME = Manifest.permission.ACTIVITY_RECOGNITION;

    private JSObject savedReturnObject;

    void _checkPermission(PluginCall call, boolean force) {
        this.savedReturnObject = new JSObject();

        if (getPermissionState(PERMISSION_ALIAS_ACTIVITY_RECOGNITION) == PermissionState.GRANTED) {
            // permission GRANTED
            this.savedReturnObject.put(GRANTED, true);
        } else {
            // permission NOT YET GRANTED

            // check if asked before
            boolean neverAsked = isPermissionFirstTimeAsking(PERMISSION_NAME);
            if (neverAsked) {
                this.savedReturnObject.put(NEVER_ASKED, true);
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                // from version Android M on,
                // on runtime,
                // each permission can be temporarily denied,
                // or be denied forever
                if (neverAsked || getActivity().shouldShowRequestPermissionRationale(PERMISSION_NAME)) {
                    // permission never asked before
                    // OR
                    // permission DENIED, BUT not for always
                    // So
                    // can be asked (again)
                    if (force) {
                        // request permission
                        // so a callback can be made from the handleRequestPermissionsResult
                        requestPermissionForAlias(PERMISSION_ALIAS_ACTIVITY_RECOGNITION, call, "stepsPermsCallback");
                        return;
                    }
                } else {
                    // permission DENIED
                    // user ALSO checked "NEVER ASK AGAIN"
                    this.savedReturnObject.put(DENIED, true);
                }
            } else {
                // below android M
                // no runtime permissions exist
                // so always
                // permission GRANTED
                this.savedReturnObject.put(GRANTED, true);
            }
        }
        call.resolve(this.savedReturnObject);
    }

    private boolean isPermissionFirstTimeAsking(String permission) {
        return getActivity().getSharedPreferences(PREFS_PERMISSION_FIRST_TIME_ASKING, MODE_PRIVATE).getBoolean(permission, true);
    }

    private static final String PREFS_PERMISSION_FIRST_TIME_ASKING = "PREFS_PERMISSION_FIRST_TIME_ASKING";

    private void setPermissionFirstTimeAsking(String permission, boolean isFirstTime) {
        SharedPreferences sharedPreference = getActivity().getSharedPreferences(PREFS_PERMISSION_FIRST_TIME_ASKING, MODE_PRIVATE);
        sharedPreference.edit().putBoolean(permission, isFirstTime).apply();
    }


    @PermissionCallback
    private void stepsPermsCallback(PluginCall call) {
        if (this.savedReturnObject == null) {
            // No stored plugin call for permissions request result
            return;
        }

        // the user was apparently requested this permission
        // update the preferences to reflect this
        setPermissionFirstTimeAsking(PERMISSION_NAME, false);

        boolean granted = false;
        if (getPermissionState(PERMISSION_ALIAS_ACTIVITY_RECOGNITION) == PermissionState.GRANTED) {
            granted = true;
        }

        // indicate that the user has been asked to accept this permission
        this.savedReturnObject.put(ASKED, true);

        if (granted) {
            // permission GRANTED
            Log.d(TAG_PERMISSION, "Asked. Granted");
            this.savedReturnObject.put(GRANTED, true);
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (getActivity().shouldShowRequestPermissionRationale(PERMISSION_NAME)) {
                    // permission DENIED
                    // BUT not for always
                    Log.d(TAG_PERMISSION, "Asked. Denied For Now");
                } else {
                    // permission DENIED
                    // user ALSO checked "NEVER ASK AGAIN"
                    Log.d(TAG_PERMISSION, "Asked. Denied");
                    this.savedReturnObject.put(DENIED, true);
                }
            } else {
                // below android M
                // no runtime permissions exist
                // so always
                // permission GRANTED
                Log.d(TAG_PERMISSION, "Asked. Granted");
                this.savedReturnObject.put(GRANTED, true);
            }
        }
        // resolve saved call
        call.resolve(this.savedReturnObject);
        // release saved
        this.savedReturnObject = null;
    }

    @PluginMethod
    public void checkPermission(PluginCall call) {
        Boolean force = call.getBoolean("force", false);

        if (force != null && force) {
            _checkPermission(call, true);
        } else {
            _checkPermission(call, false);
        }
    }

    @PluginMethod
    public void openAppSettings(PluginCall call) {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.fromParts("package", getAppId(), null));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivityForResult(call, intent, "openSettingsResult");
    }

}

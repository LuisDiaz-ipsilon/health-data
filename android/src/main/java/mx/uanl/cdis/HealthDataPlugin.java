package mx.uanl.cdis;

import android.util.Log;

public class HealthDataPlugin {

    public String echo(String value) {
        Log.i("Echo", value);
        return value;
    }
}

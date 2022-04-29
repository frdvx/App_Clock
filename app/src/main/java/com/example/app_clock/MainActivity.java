package com.example.app_clock;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.app_clock.databinding.ActivityMainBinding;

public class MainActivity extends Activity {
    SensorManager sysmanager;
    private Sensor sensor;
    private ImageView img;
    private TextView txt;
    private SensorEventListener sv;
    private TextView mTextView;
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        txt = findViewById(R.id.txt);
        img = findViewById(R.id.img);


        sysmanager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        if (sysmanager != null)

            sensor = sysmanager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);

        sv = new SensorEventListener() {

            @Override

            public void onSensorChanged(SensorEvent event) {

                float[] rotationMatrix = new float[16];

                SensorManager.getRotationMatrixFromVector(

                        rotationMatrix, event.values);

                float[] remappedRotationMatrix = new float[16];

                SensorManager.remapCoordinateSystem(rotationMatrix,

                        SensorManager.AXIS_X,

                        SensorManager.AXIS_Z,

                        remappedRotationMatrix);

                float[] orientations = new float[3];

                SensorManager.getOrientation(remappedRotationMatrix, orientations);

                for (int i = 0; i < 3; i++) {

                    orientations[i] = (float) (Math.toDegrees(orientations[i]));

                    txt.setText(String.valueOf((int) orientations[2]));

                    img.setRotation(-orientations[2]);

                }

            }

            @Override

            public void onAccuracyChanged(Sensor sensor, int i) {

            }

        };

    }

    @Override

    protected void onResume () {

        super.onResume();

        sysmanager.registerListener(sv, sensor, SensorManager.SENSOR_DELAY_FASTEST);

    }

    @Override

    protected void onPause () {

        super.onPause();

        sysmanager.unregisterListener(sv);
    }
}
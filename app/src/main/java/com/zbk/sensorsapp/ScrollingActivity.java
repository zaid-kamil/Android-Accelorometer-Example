package com.zbk.sensorsapp;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

public class ScrollingActivity extends AppCompatActivity implements SensorEventListener {

    SensorManager manager;
    private boolean isSensorAvailable = false;
    private Sensor sensor;
    private TextView textY;
    private TextView textX;
    private TextView textZ;
    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        CollapsingToolbarLayout toolBarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        toolBarLayout.setTitle(getTitle());

        textX = findViewById(R.id.textX);
        textY = findViewById(R.id.textZ);
        textZ = findViewById(R.id.textY);

        manager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensor = manager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        if (sensor == null) {
            Snackbar.make(textX, "Your Phone does not have Accelerometer, throw it", Snackbar.LENGTH_INDEFINITE).setAction("EXIT", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish(); // close activity and then app
                }
            }).show();
        } else {
            isSensorAvailable = true;
        }
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (isSensorAvailable) {
            manager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_UI);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (isSensorAvailable) {
            manager.unregisterListener(this);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_scrolling, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        float X = event.values[0];
        float Y = event.values[1];
        float Z = event.values[2];
        textX.setText(String.valueOf(X));
        textY.setText(String.valueOf(Y));
        textZ.setText(String.valueOf(Z));

        PropertyValuesHolder xVal = PropertyValuesHolder.ofFloat(View.TRANSLATION_X, -X*155);
        PropertyValuesHolder yVal = PropertyValuesHolder.ofFloat(View.TRANSLATION_Y, Y*155);
        PropertyValuesHolder zVal = PropertyValuesHolder.ofFloat(View.TRANSLATION_Z, Z*3);
        ObjectAnimator animator = ObjectAnimator.ofPropertyValuesHolder(fab, xVal, yVal, zVal);
        animator.setDuration(200);
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        animator.start();

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}

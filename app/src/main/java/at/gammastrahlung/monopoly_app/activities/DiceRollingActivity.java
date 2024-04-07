package at.gammastrahlung.monopoly_app.activities;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;

import at.gammastrahlung.monopoly_app.R;

public class DiceRollingActivity extends AppCompatActivity implements SensorEventListener {

    private ImageView img1, img2;
    float last_x, last_y, last_z;
    long lastUpdateTime, diffTime;
    public static final int THRESHOLD = 1000; // pressure
    SensorManager sensorManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dice_rolling);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        Button btnRollDice = findViewById(R.id.rollDices);
        img1 = findViewById(R.id.imageView1);
        img2 = findViewById(R.id.imageView5);

        rollDice();
        btnRollDice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rollDice();
            }
        });
    }

    public void rollDice() {
        int value1 = new Random().nextInt(6) + 1;
        int value2 = new Random().nextInt(6) + 1;

        int res1 = getResources().getIdentifier("die_" + value1, "drawable", "at.gammastrahlung.monopoly_app");
        int res2 = getResources().getIdentifier("die_" + value2, "drawable", "at.gammastrahlung.monopoly_app");

        img1.setImageResource(res1);
        img2.setImageResource(res2);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];

            long currentTime = System.currentTimeMillis();
            if (currentTime - lastUpdateTime > 100) {
                diffTime = currentTime - lastUpdateTime;
                float speed = Math.abs(x + y + z - last_x - last_y - last_z);
                if(speed > THRESHOLD){
                    rollDice();
                }
                lastUpdateTime = currentTime;
                last_x = x;
                last_y = y;
                last_z = z;
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    @Override
    protected void onResume(){     // who's the listener
        super.onResume();
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause(){
        super.onPause();
        sensorManager.unregisterListener(this);
    }
}
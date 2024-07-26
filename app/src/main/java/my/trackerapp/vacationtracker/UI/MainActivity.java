package my.trackerapp.vacationtracker.UI;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import my.trackerapp.vacationtracker.R;

public class MainActivity extends AppCompatActivity {
    public static int numAlert;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = getSharedPreferences("app_prefs", Context.MODE_PRIVATE);

        if (!isPinSet()) {
            startPinActivity();
        } else {
            initializeMainActivity();
        }
    }

    private boolean isPinSet() {
        String pin = sharedPreferences.getString("user_pin", null);
        return pin != null;
    }

    private void startPinActivity() {
        Intent intent = new Intent(MainActivity.this, PinActivity.class);
        startActivity(intent);
        finish();
    }

    private void initializeMainActivity() {
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Set up entry button
        Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, VacationList.class);
                startActivity(intent);
            }
        });
    }
}
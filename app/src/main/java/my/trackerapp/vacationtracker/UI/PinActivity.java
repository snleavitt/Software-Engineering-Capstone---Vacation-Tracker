package my.trackerapp.vacationtracker.UI;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import my.trackerapp.vacationtracker.R;

public class PinActivity extends AppCompatActivity {

    private EditText etPin;
    private Button btnSubmitPin;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pin);

        etPin = findViewById(R.id.etPin);
        btnSubmitPin = findViewById(R.id.btnSubmitPin);
        sharedPreferences = getSharedPreferences("app_prefs", Context.MODE_PRIVATE);

        btnSubmitPin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pin = etPin.getText().toString();
                if (pin.length() == 4) {
                    if (isPinSet()) {
                        verifyPin(pin);
                    } else {
                        savePin(pin);
                        Toast.makeText(PinActivity.this, "PIN saved successfully", Toast.LENGTH_SHORT).show();
                        startMainActivity();
                    }
                } else {
                    Toast.makeText(PinActivity.this, "Please enter a 4-digit PIN", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void savePin(String pin) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("user_pin", pin);
        editor.apply();
    }

    private boolean isPinSet() {
        String pin = sharedPreferences.getString("user_pin", null);
        return pin != null;
    }

    private void verifyPin(String inputPin) {
        String savedPin = sharedPreferences.getString("user_pin", "");
        if (savedPin.equals(inputPin)) {
            startMainActivity();
        } else {
            Toast.makeText(PinActivity.this, "Incorrect PIN", Toast.LENGTH_SHORT).show();
        }
    }

    private void startMainActivity() {
        Intent intent = new Intent(PinActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}

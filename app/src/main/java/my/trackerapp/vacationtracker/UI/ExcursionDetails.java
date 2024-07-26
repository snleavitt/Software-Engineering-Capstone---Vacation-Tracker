package my.trackerapp.vacationtracker.UI;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import my.trackerapp.vacationtracker.R;
import my.trackerapp.vacationtracker.database.Repository;
import my.trackerapp.vacationtracker.entities.Excursion;
import my.trackerapp.vacationtracker.entities.Vacation;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

//Excurison Details Activity
public class ExcursionDetails extends AppCompatActivity {


    //private static final String TAG = ExcursionDetails.class.getSimpleName();

    //Current Excursion
    private Excursion currentExcursion;

    //Excurion variables
    private String name;
    private int excursionID;
    private int vacationID;
    private String additionalInformation;
    private DatePickerDialog.OnDateSetListener startDate;

    //Excursion Details Input Containers
    private EditText editName;
    private TextView editDate;
    private Repository repository;
    private TextView editStartTime;
    private TextView editAdditionalInformation;
    private final Calendar myCalendarStart = Calendar.getInstance();

    //Array of vacations for spinner
    private List<Vacation> vacationArrayList = new ArrayList<>();

    //Date Format
    private static final String DATE_FORMAT = "MM/dd/yyyy";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_excursion_details);

        repository = new Repository(getApplication());

        //Get and Set Name
        name = getIntent().getStringExtra("name");
        editName = findViewById(R.id.excursionName);
        editName.setText(name);

        //Get and Set Start date
        editDate = findViewById(R.id.exdate);
        String startDateStr = getIntent().getStringExtra("Start Date");
        if (startDateStr != null && !startDateStr.isEmpty()) {
            editDate.setText(startDateStr);
        }

        //Get and Set ExcurionID
        excursionID = getIntent().getIntExtra("id", -1);

        //Get and Set associated VacationID
        vacationID = getIntent().getIntExtra("vacationID", -1);

        //Get and Set Additional Information
        additionalInformation = getIntent().getStringExtra("Additional Information");
        editAdditionalInformation = findViewById(R.id.informationTextBox);
        editAdditionalInformation.setText(additionalInformation);


        //Log.d("ExcursionDetails", "Excursion ID" + excursionID);

        //Get and Set Start Time
        String StartTimeStr = getIntent().getStringExtra("Start Time");
        editStartTime = findViewById(R.id.starttime);
        if (StartTimeStr != null && !StartTimeStr.isEmpty()) {
            editStartTime.setText(StartTimeStr);
        }

        setupVacationSpinner();
        setupDatePicker();
        setupTimePicker();
    }

    //Set up Vacation choice spinner
    private void setupVacationSpinner() {
        vacationArrayList.addAll(repository.getAllVacations());
        ArrayList<String> vacationList = new ArrayList<>();
        for (Vacation vacation : vacationArrayList) {
            vacationList.add(vacation.getVacationName());
        }
        ArrayAdapter<String> vacationAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, vacationList);
        Spinner spinner = findViewById(R.id.spinner);
        spinner.setAdapter(vacationAdapter);

        for (int i = 0; i < vacationArrayList.size(); i++) {
            if (vacationArrayList.get(i).getVacationID() == vacationID) {
                spinner.setSelection(i);
                break;
            }
        }

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                vacationID = vacationArrayList.get(position).getVacationID();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    //Set up time picker for Start Time
    private void setupTimePicker() {

        String startTimeStr = editStartTime.getText().toString();
        SimpleDateFormat sdfTime = new SimpleDateFormat("HH:mm", Locale.US);

        //set time format
        try {
            Date startTime = sdfTime.parse(startTimeStr);
            if (startTime != null) {
                myCalendarStart.setTime(startTime);
                editStartTime.setText(startTimeStr);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        editStartTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog();
            }
        });

    }

    //for time picker for Start Time
    private void showTimePickerDialog() {

        final Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        String amPm;
                        if (hourOfDay >= 12) {
                            amPm = "PM";
                            hourOfDay = (hourOfDay > 12) ? hourOfDay - 12 : hourOfDay;
                        } else {
                            amPm = "AM";
                            hourOfDay = (hourOfDay == 0) ? 12 : hourOfDay;
                        }
                        editStartTime.setText(String.format("%02d:%02d", hourOfDay, minute));
                    }
                }, hour, minute, true);
        timePickerDialog.show();
    }

    //Set up date picker for Start Date
    private void setupDatePicker() {
        String myFormat = DATE_FORMAT;
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        startDate = (view, year, monthOfYear, dayOfMonth) -> {
            myCalendarStart.set(Calendar.YEAR, year);
            myCalendarStart.set(Calendar.MONTH, monthOfYear);
            myCalendarStart.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabelStart();
        };

        editDate.setOnClickListener(v -> {
            String info = editDate.getText().toString();
            if (info.equals("")) {
                Calendar currentDate = Calendar.getInstance();
                info = sdf.format(currentDate.getTime());
            }
            try {
                myCalendarStart.setTime(sdf.parse(info));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            new DatePickerDialog(ExcursionDetails.this, startDate, myCalendarStart
                    .get(Calendar.YEAR), myCalendarStart.get(Calendar.MONTH),
                    myCalendarStart.get(Calendar.DAY_OF_MONTH)).show();
        });
    }

    //Update Start Date
    private void updateLabelStart() {
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT, Locale.US);
        editDate.setText(sdf.format(myCalendarStart.getTime()));
    }

    //Validates that excursion date is within associated vacation
    private boolean isValidExcursionDate(String excursionDateStr) {
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT, Locale.US);
        try {
            Date excursionDate = sdf.parse(excursionDateStr);

            Vacation vacation = repository.getVacationById(vacationID);
            if (vacation != null) {
                Date vacationStartDate = sdf.parse(vacation.getStartDate());
                Date vacationEndDate = sdf.parse(vacation.getEndDate());
                return !excursionDate.before(vacationStartDate) && !excursionDate.after(vacationEndDate);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }

    //Creates Excursion Details Menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_excursion_details, menu);
        return true;
    }

    //Option set up
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }

        if (item.getItemId() == R.id.excursionsave) {

            saveExcursion();
            return true;
        }

        if (item.getItemId() == R.id.exnotify) {
            String dateStr = editDate.getText().toString();
            String startTimeStr = editStartTime.getText().toString();

            if (dateStr.isEmpty() || startTimeStr.isEmpty()) {
                Toast.makeText(this, "Please fill in excursion date and time.", Toast.LENGTH_SHORT).show();
            } else {
                scheduleStartAlarm(dateStr, startTimeStr, excursionID, editName.getText().toString());
                Toast.makeText(this, "Alarm set successfully for your excursion", Toast.LENGTH_LONG).show();
            }
            return true;
        }

        if (item.getItemId() == R.id.excursiondelete) {
            deleteExcursion();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //Save excursion to Table
    private void saveExcursion() {
        String excursionName = editName.getText().toString();
        String startDate = editDate.getText().toString();
        String startTime = editStartTime.getText().toString();
        String additionalInformation = editAdditionalInformation.getText().toString();

        if (excursionName.isEmpty() || startDate.isEmpty()) {
            Toast.makeText(this, "Excursion name and start date cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!isValidExcursionDate(startDate)) {
            Toast.makeText(this, "Excursion date must be within the associated vacation's dates", Toast.LENGTH_SHORT).show();
            return;
        }

        Spinner spinner = findViewById(R.id.spinner);
        String selectedVacationName = (String) spinner.getSelectedItem();

        Vacation selectedVacation = findVacationByName(selectedVacationName);
        if (selectedVacation == null) {
            Toast.makeText(this, "Please select a valid vacation", Toast.LENGTH_SHORT).show();
            return;
        }

        vacationID = selectedVacation.getVacationID();

        repository.databaseExecutor.execute(() -> {
            if (excursionID == -1) {
                Excursion newExcursion = new Excursion(excursionID, excursionName, vacationID, startDate, startTime, additionalInformation);
                repository.insert(newExcursion);
            } else {
                Excursion updatedExcursion = new Excursion(excursionID, excursionName, vacationID, startDate, startTime, additionalInformation);
                repository.update(updatedExcursion);
            }

            runOnUiThread(() -> {
                Toast.makeText(ExcursionDetails.this, "Excursion saved successfully", Toast.LENGTH_SHORT).show();
                Intent resultIntent = new Intent();
                setResult(RESULT_OK, resultIntent);
                finish();
            });
        });
    }


    //Schedules Alarm for start of excursion
    private void scheduleStartAlarm(String dateStr, String timeStr, int excursionID, String excursionName) {
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT + " HH:mm", Locale.US);
        Date date = null;
        try {
            date = sdf.parse(dateStr + " " + timeStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (date != null) {
            long triggerTime = date.getTime();
            Intent intent = new Intent(ExcursionDetails.this, MyReceiver.class);
            intent.putExtra("excursionName", excursionName);
            intent.putExtra("notificationType", "startExcursion");
            PendingIntent sender = PendingIntent.getBroadcast(ExcursionDetails.this, excursionID + "startExcursion".hashCode(), intent, PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_IMMUTABLE);
            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            alarmManager.set(AlarmManager.RTC_WAKEUP, triggerTime, sender);
        }
    }

    //Delete excursion from Table
    private void deleteExcursion() {
        repository.databaseExecutor.execute(() -> {
            currentExcursion = repository.getExcursionById(excursionID);
            if (currentExcursion != null) {
                repository.delete(currentExcursion);
                runOnUiThread(() -> {
                    Toast.makeText(ExcursionDetails.this, currentExcursion.getExcursionName() + " was deleted successfully", Toast.LENGTH_LONG).show();

                    Intent resultIntent = new Intent();
                    setResult(RESULT_OK, resultIntent);
                    finish();
                });
            } else {
                //Log.e(TAG, "Failed to delete: currentExcursion is null");
                runOnUiThread(() -> Toast.makeText(this, "Failed to delete the excursion: excursion not found", Toast.LENGTH_LONG).show());
            }
        });
    }

    //Find vacation for saving excursion
    private Vacation findVacationByName(String vacationName) {
        for (Vacation vacation : vacationArrayList) {
            if (vacation.getVacationName().equals(vacationName)) {
                return vacation;
            }
        }
        return null;
    }


}



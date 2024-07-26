package my.trackerapp.vacationtracker.UI;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import my.trackerapp.vacationtracker.R;
import my.trackerapp.vacationtracker.database.Repository;
import my.trackerapp.vacationtracker.entities.Excursion;
import my.trackerapp.vacationtracker.entities.Vacation;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class VacationDetails extends AppCompatActivity {

    //Vacation Variables
    String name;
    String lodgingName;
    String transportation;
    String additionalDetails;
    int vacationID;
    DatePickerDialog.OnDateSetListener planeDepartureDate;
    DatePickerDialog.OnDateSetListener startDate;
    DatePickerDialog.OnDateSetListener endDate;
    Vacation currentVacation;
    Repository repository;
    int numExc;
    List<String> transportationArrayList = new ArrayList<>();


    //Edit variables
    EditText editName;
    EditText editLodgingName;
    TextView editStartDate;
    TextView editEndDate;
    TextView editDepartureDate;
    TextView editDepartureTime;
    EditText editAdditionalDetails;
    LinearLayout planeDetailsLayout;


    final Calendar myCalendarStart = Calendar.getInstance();
    final Calendar myCalendarEnd = Calendar.getInstance();

    private ExcursionAdapter excursionAdapter;
    private static final String DATE_FORMAT = "MM/dd/yyyy";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vacation_details);
        repository = new Repository(getApplication());

        ScrollView scrollView = findViewById(R.id.scrollView);
        scrollView.setVerticalScrollBarEnabled(true);

        vacationID = getIntent().getIntExtra("ID", -1);

        //Set name
        name = getIntent().getStringExtra("Name");
        editName = findViewById(R.id.vacationName);
        editName.setText(name);

        //Set lodging
        lodgingName = getIntent().getStringExtra("Lodging");
        editLodgingName = findViewById(R.id.lodgingName);
        editLodgingName.setText(lodgingName);

        //Set start and end date
        String startDateStr = getIntent().getStringExtra("Start Date");
        String endDateStr = getIntent().getStringExtra("End Date");
        editStartDate = findViewById(R.id.startDate);
        editEndDate = findViewById(R.id.endDate);

        //set transportation
        transportation = getIntent().getStringExtra("Transportation");

        //set additional details
        additionalDetails = getIntent().getStringExtra("Additional Details");
        editAdditionalDetails = findViewById(R.id.additionalTextBox);
        editAdditionalDetails.setText(additionalDetails);

        //optional plane details
        planeDetailsLayout = findViewById(R.id.planeDetailsLayout);

        //set Departure Date
        String planeDepartureDateStr = getIntent().getStringExtra("Departure Date");
        editDepartureDate = findViewById(R.id.depatureDate);


        //set departure time
        String planeDepartureTimeStr = getIntent().getStringExtra("Departure Time");
        editDepartureTime = findViewById(R.id.departureTime);


        if (planeDepartureTimeStr != null && !planeDepartureTimeStr.isEmpty()) {
            editDepartureTime.setText(planeDepartureTimeStr);
        }

//        Log.d("VacationDetails", "Vacation Name: " + transportation);
//        Log.d("VacationDetails", "Hotel Name: " + lodgingName);
//        Log.d("VacationDetails", "Vacation ID" + vacationID);
        excursionAdapter = new ExcursionAdapter(this);

        planeDepartureTimeStr = editDepartureTime.getText().toString();
        SimpleDateFormat sdfTime = new SimpleDateFormat("HH:mm", Locale.US);

        //set time format
        try {
            Date planeDepartureTime = sdfTime.parse(planeDepartureTimeStr);
            if (planeDepartureTime != null) {
                myCalendarStart.setTime(planeDepartureTime);
                editDepartureTime.setText(planeDepartureTimeStr);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        //set date format
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT, Locale.US);
        try {
            if (startDateStr != null) {
                Date startDate = sdf.parse(startDateStr);
                if (startDate != null) {
                    myCalendarStart.setTime(startDate);
                    editStartDate.setText(startDateStr);
                }
            }
            if (endDateStr != null) {
                Date endDate = sdf.parse(endDateStr);
                if (endDate != null) {
                    myCalendarEnd.setTime(endDate);
                    editEndDate.setText(endDateStr);
                }
            }

            if (planeDepartureDateStr != null) {
                Date planeDepartureDate = sdf.parse(planeDepartureDateStr);
                if (planeDepartureDate != null) {
                    myCalendarEnd.setTime(planeDepartureDate);
                    editDepartureDate.setText(planeDepartureDateStr);
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error parsing date", Toast.LENGTH_LONG).show();
        }


        setupRecyclerView();
        setupTransportationSpinner();
        setUpFAB();


        //start date format on click
        startDate = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                myCalendarStart.set(Calendar.YEAR, year);
                myCalendarStart.set(Calendar.MONTH, monthOfYear);
                myCalendarStart.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabelStart();
            }
        };


        //end date format on click
        endDate = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                myCalendarEnd.set(Calendar.YEAR, year);
                myCalendarEnd.set(Calendar.MONTH, monthOfYear);
                myCalendarEnd.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabelEnd();
            }
        };

        //departure time format
        editDepartureTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog();
            }
        });

        //plane departure date on click
        planeDepartureDate = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                myCalendarStart.set(Calendar.YEAR, year);
                myCalendarStart.set(Calendar.MONTH, monthOfYear);
                myCalendarStart.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabelDeparture();
            }
        };

        //edit Start date
        editStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String info = editStartDate.getText().toString();
                if (info.equals("")) {
                    Calendar currentDate = Calendar.getInstance();
                    info = sdf.format(currentDate.getTime());
                }
                try {
                    myCalendarStart.setTime(sdf.parse(info));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                new DatePickerDialog(VacationDetails.this, startDate, myCalendarStart
                        .get(Calendar.YEAR), myCalendarStart.get(Calendar.MONTH),
                        myCalendarStart.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        //edit End date
        editEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String info = editEndDate.getText().toString();
                if (info.equals("")) {
                    Calendar currentDate = Calendar.getInstance();
                    info = sdf.format(currentDate.getTime());
                }
                try {
                    myCalendarEnd.setTime(sdf.parse(info));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                new DatePickerDialog(VacationDetails.this, endDate, myCalendarEnd
                        .get(Calendar.YEAR), myCalendarEnd.get(Calendar.MONTH),
                        myCalendarEnd.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        //edit Departure Date
        editDepartureDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String info = editDepartureDate.getText().toString();
                if (info.equals("")) {
                    Calendar currentDate = Calendar.getInstance();
                    info = sdf.format(currentDate.getTime());
                }
                try {
                    myCalendarStart.setTime(sdf.parse(info));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                new DatePickerDialog(VacationDetails.this, planeDepartureDate, myCalendarStart
                        .get(Calendar.YEAR), myCalendarStart.get(Calendar.MONTH),
                        myCalendarStart.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }

    private void setUpFAB() {
        FloatingActionButton fab = findViewById(R.id.excursionfab);
        fab.setImageResource(R.drawable.add);
        fab.setColorFilter(ContextCompat.getColor(this, R.color.white), PorterDuff.Mode.SRC_IN);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (vacationID < 1) {
                    Toast.makeText(VacationDetails.this, "Please save vacation before adding excursion", Toast.LENGTH_LONG).show();
                } else {
                    Intent intent = new Intent(VacationDetails.this, ExcursionDetails.class);
                    intent.putExtra("vacationID", vacationID);
                    startActivity(intent);
                }
            }
        });
    }

    //departure time
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
                        editDepartureTime.setText(String.format("%02d:%02d", hourOfDay, minute));
                    }
                }, hour, minute, true);
        timePickerDialog.show();
    }

    //Set up Transportation spinner and options
    private void setupTransportationSpinner() {
        transportationArrayList.clear();

        String[] allTransportations = getResources().getStringArray(R.array.trans_options);
        transportationArrayList.addAll(Arrays.asList(allTransportations));


        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, transportationArrayList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        Spinner tranSpinner = findViewById(R.id.transportationSpinner);
        tranSpinner.setAdapter(adapter);

        for (int i = 0; i < transportationArrayList.size(); i++) {
            if (transportationArrayList.get(i).equals(transportation)) {
                tranSpinner.setSelection(i);
                break;
            }
        }
        //Log.d("VacationDetails", "Vacation Name: " + transportation);

        tranSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                transportation = parent.getItemAtPosition(position).toString();

                if (transportation.equals("Plane")) {
                    planeDetailsLayout.setVisibility(View.VISIBLE);


                } else {
                    planeDetailsLayout.setVisibility(View.GONE);
                }

                invalidateOptionsMenu();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    //validation for date format
    private boolean isValidDateFormat(String dateStr) {
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT, Locale.US);
        sdf.setLenient(false);
        try {
            sdf.parse(dateStr);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    //validation for end date after start
    public boolean isEndDateAfterStartDate(String startDateStr, String endDateStr) {
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT, Locale.US);
        try {
            Date startDate = sdf.parse(startDateStr);
            Date endDate = sdf.parse(endDateStr);
            return endDate != null && endDate.after(startDate);
        } catch (ParseException e) {
            return false;
        }
    }

    //update start format
    private void updateLabelStart() {
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT, Locale.US);
        editStartDate.setText(sdf.format(myCalendarStart.getTime()));
    }

    //update end format
    private void updateLabelEnd() {
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT, Locale.US);
        editEndDate.setText(sdf.format(myCalendarEnd.getTime()));
    }

    private void updateLabelDeparture() {
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT, Locale.US);
        editDepartureDate.setText(sdf.format(myCalendarStart.getTime()));
    }


    //create options menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_vacation_details, menu);
        return true;
    }

    //show plane alarm if plane is selected
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem setPlaneAlarmItem = menu.findItem(R.id.departurenotify);
        if ("Plane".equals(transportation)) {
            setPlaneAlarmItem.setVisible(true);
        } else {
            setPlaneAlarmItem.setVisible(false);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    //menu options
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        //home button
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }

        //save vacation
        if (item.getItemId() == R.id.vacationsave) {

            saveVacation();
            return true;
        }

        //delete vacation
        if (item.getItemId() == R.id.vactiondelete) {
            deleteVacation();
            return true;
        }


        //set vacation notification
        if (item.getItemId() == R.id.vacationnotify) {
            String startDateStr = editStartDate.getText().toString();
            String endDateStr = editEndDate.getText().toString();

            scheduleVacationNotification(startDateStr, "start", vacationID, editName.getText().toString());
            scheduleVacationNotification(endDateStr, "end", vacationID, editName.getText().toString());

            Toast.makeText(this, "Alarm set successfully for vacation start and end dates", Toast.LENGTH_LONG).show();
            return true;
        }

        //set plane departure notification
        if (item.getItemId() == R.id.departurenotify) {
            String departureDateStr = editDepartureDate.getText().toString();
            String departureTimeStr = editDepartureTime.getText().toString();

            if (departureDateStr.isEmpty() || departureTimeStr.isEmpty()) {
                Toast.makeText(this, "Please fill in plane departure date and time.", Toast.LENGTH_SHORT).show();
            } else {
                schedulePlaneDepartureAlarm(departureDateStr, departureTimeStr, vacationID, editName.getText().toString());
                Toast.makeText(this, "Alarm set successfully for plane departure", Toast.LENGTH_LONG).show();
            }
            return true;
        }
        //share vacation details
        if (item.getItemId() == R.id.share) {
                String vacationDetails;
                if (Objects.equals(transportation, "Plane")) {
                    vacationDetails = "Vacation Details:\n" +
                            "Name: " + editName.getText().toString() + "\n" +
                            "Lodging: " + editLodgingName.getText().toString() + "\n" +
                            "Transportation: " + transportation + "\n" +
                            "Departure Date: " + editDepartureDate.getText().toString() + "\n" +
                            "Departure Time: " + editDepartureTime.getText().toString() + "\n" +
                            "Start Date: " + editStartDate.getText().toString() + "\n" +
                            "End Date: " + editEndDate.getText().toString() + "\n" +
                            "Additional Details: " + editAdditionalDetails.getText().toString();
                } else {
                    vacationDetails = "Vacation Details:\n" +
                            "Name: " + editName.getText().toString() + "\n" +
                            "Lodging: " + editLodgingName.getText().toString() + "\n" +
                            "Transportation: " + transportation + "\n" +
                            "Start Date: " + editStartDate.getText().toString() + "\n" +
                            "End Date: " + editEndDate.getText().toString() + "\n" +
                            "Additional Details: " + editAdditionalDetails.getText().toString();
                }

                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, vacationDetails);
                sendIntent.putExtra(Intent.EXTRA_SUBJECT, "Vacation Details");
                sendIntent.setType("text/plain");

                Intent shareIntent = Intent.createChooser(sendIntent, null);
                startActivity(shareIntent);
                return true;
            }
            return super.onOptionsItemSelected(item);
        }

        //schedule vacation alarm
        private void scheduleVacationNotification (String dateStr, String type,
        int vacationID, String vacationName){
            String myFormat = DATE_FORMAT;
            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
            Date date = null;
            try {
                date = sdf.parse(dateStr);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if (date != null) {
                long triggerTime = date.getTime();
                Intent intent = new Intent(VacationDetails.this, MyReceiver.class);
                intent.putExtra("vacationName", vacationName);
                intent.putExtra("notificationType", type);
                PendingIntent sender = PendingIntent.getBroadcast(VacationDetails.this, vacationID + type.hashCode(), intent, PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_IMMUTABLE);
                AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                alarmManager.set(AlarmManager.RTC_WAKEUP, triggerTime, sender);
            }
        }

        //schedule plane departure alarm
        private void schedulePlaneDepartureAlarm (String dateStr, String timeStr,
        int vacationID, String vacationName){
            SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT + " HH:mm", Locale.US);
            Date date = null;
            try {
                date = sdf.parse(dateStr + " " + timeStr);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if (date != null) {
                long triggerTime = date.getTime();
                Intent intent = new Intent(VacationDetails.this, MyReceiver.class);
                intent.putExtra("vacationName", vacationName);
                intent.putExtra("notificationType", "planeDeparture");
                PendingIntent sender = PendingIntent.getBroadcast(VacationDetails.this, vacationID + "planeDeparture".hashCode(), intent, PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_IMMUTABLE);
                AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                alarmManager.set(AlarmManager.RTC_WAKEUP, triggerTime, sender);
            }
        }


    //resume on page
    protected void onResume() {
        super.onResume();
        updateRecyclerView();
    }

    //set up recycler view
    private void setupRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.excursionRecyclerView);
        excursionAdapter = new ExcursionAdapter(this);
        recyclerView.setAdapter(excursionAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        updateRecyclerView();
    }

    //update recycler view
    private void updateRecyclerView() {
        List<Excursion> filteredExcursions = new ArrayList<>();
        if (excursionAdapter != null) {
            for (Excursion e : repository.getAllExcursions()) {
                if (e.getVacationID() == vacationID) {
                    filteredExcursions.add(e);
                }
            }
            excursionAdapter.setExcursions(filteredExcursions);
            excursionAdapter.notifyDataSetChanged();
        } else {
            //Log.e("VacationDetails", "excursionAdapter is null in updateRecyclerView()");
        }
    }

    //Save vacation
    private void saveVacation() {
        String startDateStr = editStartDate.getText().toString();
        String endDateStr = editEndDate.getText().toString();
        String planeDepartureDateStr = editEndDate.getText().toString();
        Vacation vacation;

        //validate date format
        if (!isValidDateFormat(startDateStr) || !isValidDateFormat(endDateStr) || !isValidDateFormat(planeDepartureDateStr)) {
            Toast.makeText(this, "Please enter dates in the correct format: MM/dd/yyyy", Toast.LENGTH_LONG).show();
            return;
        }

        //validate end date after start date
        if (!isEndDateAfterStartDate(startDateStr, endDateStr)) {
            Toast.makeText(this, "End date must be after start date", Toast.LENGTH_LONG).show();
            return;
        }
        if (transportation.equals("Plane")) {
            String planeDepartureDate = editDepartureDate.getText().toString();
            String planeDepartureTime = editDepartureTime.getText().toString();

            if (planeDepartureDate.isEmpty() || planeDepartureTime.isEmpty()) {
                Toast.makeText(this, "Please fill in plane departure date and time.", Toast.LENGTH_SHORT).show();
                return;
            }
            vacation = new Vacation(vacationID, editName.getText().toString(), editLodgingName.getText().toString(), transportation, editStartDate.getText().toString(), editEndDate.getText().toString(), editAdditionalDetails.getText().toString(), editDepartureDate.getText().toString(), editDepartureTime.getText().toString());
        } else {
            vacation = new Vacation(vacationID, editName.getText().toString(), editLodgingName.getText().toString(), transportation, editStartDate.getText().toString(), editEndDate.getText().toString(), editAdditionalDetails.getText().toString(), null, null);

        }
        //set and save vacation in database
        if (vacationID == -1) {
            if (repository.getAllVacations().isEmpty()) {
                vacationID = 1;
            } else {
                vacationID = repository.getAllVacations().get(repository.getAllVacations().size() - 1).getVacationID() + 1;
            }
            vacation.setVacationID(vacationID);
            repository.insert(vacation);
            Toast.makeText(VacationDetails.this, "Vacation saved successfully", Toast.LENGTH_SHORT).show();
        } else {
            repository.update(vacation);
            Toast.makeText(VacationDetails.this, "Vacation saved successfully", Toast.LENGTH_SHORT).show();
        }
    }

    //delete vacation
    private void deleteVacation(){
        for (Vacation vacation : repository.getAllVacations()) {
            if (vacation.getVacationID() == vacationID) currentVacation = vacation;
        }

        //validation for vacation with no excursions
        numExc = 0;
        for (Excursion excursion : repository.getAllExcursions()) {
            if (excursion.getExcursionID() == vacationID) ++numExc;
        }

        if (numExc == 0) {
            repository.delete(currentVacation);
            Toast.makeText(VacationDetails.this, currentVacation.getVacationName() + " was deleted successfully", Toast.LENGTH_LONG).show();
            VacationDetails.this.finish();
        } else {
            Toast.makeText(VacationDetails.this, "Can't delete a vacation with associated Excursions", Toast.LENGTH_LONG).show();
        }
    }


}








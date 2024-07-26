package my.trackerapp.vacationtracker.UI;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import my.trackerapp.vacationtracker.R;
import my.trackerapp.vacationtracker.dao.ExcursionDAO;
import my.trackerapp.vacationtracker.dao.VacationDAO;
import my.trackerapp.vacationtracker.database.VacationDatabaseBuilder;
import my.trackerapp.vacationtracker.entities.Excursion;
import my.trackerapp.vacationtracker.entities.Vacation;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

//Report Generator Activity
public class ReportGenerator extends AppCompatActivity {

    private VacationDatabaseBuilder vacationDatabaseBuilder;

    //Report Generator variables
    private String tableSelection;
    private String beforeOrAfter;
    private String editTextDatestr;

    //Report Generator Input Containers
    private Spinner spinnerTables;
    private Button buttonGenerateReport;
    private EditText editTextDate;
    private Spinner beforeOrAfterSpinner;
    private Spinner spinnerFields;
    private TextView selectedFieldsview;

    //Date format
    private static final String DATE_FORMAT = "MM/dd/yyyy";
    private static final String SQLITE_DATE_FORMAT = "yyyy-MM-dd";

    //Array for selected fields
    private ArrayList<String> selectedFields = new ArrayList<>();
    //private List<Map<String, String>> formattedData = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        //set table spinner
        spinnerTables = findViewById(R.id.spinnerTables);

        //set fields spinner
        spinnerFields = findViewById(R.id.spinnerFields);

        //set date field
        editTextDate = findViewById(R.id.editTextDate);

        //button generator
        buttonGenerateReport = findViewById(R.id.buttonGenerateReport);

        //Before or After spinner
        beforeOrAfterSpinner = findViewById(R.id.spinnerBeforeAfter);

        //Select fields spinner
        selectedFieldsview = findViewById(R.id.selectedFieldsview);


        vacationDatabaseBuilder = VacationDatabaseBuilder.getDatabase(getApplicationContext());

        selectedFields.clear();
        updateSelectedFieldsTextView();

        setupTableSpinner();
        setupFieldSpinner();
        setupButton();
        setupBeforeorAfterSpinner();
    }

    //Show selected fields
    private void updateSelectedFieldsTextView() {
        if (selectedFields.isEmpty()) {
            selectedFieldsview.setVisibility(View.GONE); // Hide TextView if no fields are selected
        } else {
            selectedFieldsview.setVisibility(View.VISIBLE); // Show TextView if fields are selected
            String selectedFieldsText = "Selected Fields:\n";
            selectedFieldsText += TextUtils.join(", ", selectedFields);
            selectedFieldsview.setText(selectedFieldsText);
        }
    }

    //Set up button
    private void setupButton() {
        buttonGenerateReport.setOnClickListener(v -> {
            editTextDatestr = editTextDate.getText().toString();
            Log.d("ReportGenerator", "Date: " + editTextDatestr);
            if (!isValidDateFormat(editTextDatestr)) {
                Toast.makeText(this, "Please enter dates in the correct format: MM/dd/yyyy", Toast.LENGTH_LONG).show();
            } else if (Objects.equals(beforeOrAfter, "")) {
                Toast.makeText(this, "Please choose Before or After", Toast.LENGTH_LONG).show();
            } else {
                generateReport(editTextDatestr);
            }

        });

    }

    //Set up table spinner
    private void setupTableSpinner() {
        String[] tables = getResources().getStringArray(R.array.table_options);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, tables);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTables.setAdapter(adapter);
        updateSelectedFieldsTextView();

        spinnerTables.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                tableSelection = parent.getItemAtPosition(position).toString();
                setupFieldSpinner();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                tableSelection = null;
            }
        });
    }

    //Set up before or after spinner
    private void setupBeforeorAfterSpinner() {
        String[] option = getResources().getStringArray(R.array.b_or_a);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, option);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        beforeOrAfterSpinner.setAdapter(adapter);

        beforeOrAfterSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                beforeOrAfter = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                tableSelection = null;
            }
        });
    }


    //Set up fields spinner
    private void setupFieldSpinner() {
        selectedFields.clear();
        updateSelectedFieldsTextView();
        if (Objects.equals(tableSelection, "Vacation")) {
            String[] fields = getResources().getStringArray(R.array.field_options_vacation);
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, fields);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerFields.setAdapter(adapter);
            spinnerFields.setSelection(-1);

        } else if (Objects.equals(tableSelection, "Excursion")) {
            String[] fields = getResources().getStringArray(R.array.field_options_excursion);
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, fields);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerFields.setAdapter(adapter);
            spinnerFields.setSelection(-1);

        } else {
            String[] fields = {""};
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, fields);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerFields.setAdapter(adapter);
            spinnerFields.setSelection(-1);
        }

        spinnerFields.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    if (Objects.equals(tableSelection, "Vacation")) {
                        String selectedField = parent.getItemAtPosition(position).toString();
                        if (!TextUtils.isEmpty(selectedField)) {

                            selectedFields.add(selectedField);
                            updateSelectedFieldsTextView();
                        }
                    }
                    else if (Objects.equals(tableSelection, "Excursion")) {
                        String selectedField = parent.getItemAtPosition(position).toString();
                        if (!TextUtils.isEmpty(selectedField)) {

                            selectedFields.add(selectedField);
                            updateSelectedFieldsTextView();
                        }
                    }
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //selectedFields.clear();
            }
        });
    }

    //Validation for date format
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

    //Date to time stamp
    private long dateToTimestamp(String dateStr) {
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
        try {
            Date date = sdf.parse(dateStr);
            return date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }
    }

    //Generate report
    private void generateReport(String dateConstraint) {
        AsyncTask.execute(() -> {
            Date dateConstraintSQL = formatDate(dateConstraint);

            if (Objects.equals(tableSelection, "Vacation")) {
                tableSelection = "vacation";
            } else if (Objects.equals(tableSelection, "Excursion")) {
                tableSelection = "excursion";
            }
            //Log.d("report", "table: " + tableSelection);

            if (tableSelection != null) {
                String query = "SELECT * FROM " + tableSelection;

                if (Objects.equals(beforeOrAfter, "Before")) {
                    query += " WHERE startDateSQL <= '" + dateConstraintSQL + "'";
                } else if (Objects.equals(beforeOrAfter, "After")) {
                    query += " WHERE startDateSQL >= '" + dateConstraintSQL + "'";
                }
                //Log.d("report", "Query ReportGenerator: " + query);
                List<?> results = executeQuery(query, dateConstraint);
                //Log.d("report", "results: " + results);

                runOnUiThread(() -> {
                    if (results.isEmpty()) {
                        Toast.makeText(this, "No results found", Toast.LENGTH_LONG).show();
                    } else {
                        List<Map<String, String>> formattedData = new ArrayList<>();
                        for (Object result : results) {
                            Map<String, String> row = new HashMap<>();

                            if (result instanceof Vacation) {
                                Vacation vacation = (Vacation) result;
                                for (String field : selectedFields) {
                                    switch (field) {
                                        case "Vacation Name":
                                            row.put(field, vacation.getVacationName());
                                            break;
                                        case "Lodging Name":
                                            row.put(field, vacation.getLodgingName());
                                            break;
                                        case "Transportation":
                                            row.put(field, vacation.getTransportation());
                                            break;
                                        case "Start Date":
                                            row.put(field, vacation.getStartDate());
                                            break;
                                        case "End Date":
                                            row.put(field, vacation.getEndDate());
                                            break;
                                        case "Additional Details":
                                            row.put(field, vacation.getAdditionalDetails());
                                            break;
                                        case "Plane Departure Date":
                                            row.put(field, vacation.getPlaneDepartureDate());
                                            break;
                                        case "Plane Departure Time":
                                            row.put(field, vacation.getPlaneDepartureTime());
                                            break;
                                        default:
                                            break;
                                    }
                                }
                            } else if (result instanceof Excursion) {
                                Excursion excursion = (Excursion) result;
                                for (String field : selectedFields) {
                                    switch (field) {
                                        case "Excursion Name":
                                            row.put(field, excursion.getExcursionName());
                                            break;
                                        case "Start Date":
                                            row.put(field, excursion.getStartDate());
                                            break;
                                        case "Start Time":
                                            row.put(field, excursion.getStartTime());
                                            break;
                                        case "Additional Information":
                                            row.put(field, excursion.getAdditionalInformation());
                                            break;
                                        default:
                                            break;
                                    }
                                }
                            }
                            formattedData.add(row);

                            //Log.d("ReportGenerator", "Formatted Data Row: " + row.toString());
                        }

                        runOnUiThread(() -> {
                            editTextDatestr = editTextDate.getText().toString();
                            Intent intent = new Intent(ReportGenerator.this, ReportDisplay.class);
                            intent.putExtra("Formatted Data", (ArrayList<Map<String, String>>) formattedData);
                            intent.putStringArrayListExtra("Selected Fields", selectedFields);
                            intent.putExtra("Date", editTextDatestr);
                            intent.putExtra("Before or After", beforeOrAfter);
                            intent.putExtra("Table Selection", tableSelection);
                            startActivity(intent);
                        });
                    }
                });
            }
        });
    }


    //Excecute query
    private List<?> executeQuery(String query, String dateConstraint) {
        if (Objects.equals(tableSelection, "vacation")) {
            VacationDAO vacationDAO = vacationDatabaseBuilder.vacationDAO();
            if (Objects.equals(beforeOrAfter, "Before")) {
                return vacationDAO.getVacationsBefore(dateToTimestamp(dateConstraint));
            } else if (Objects.equals(beforeOrAfter, "After")) {
                return vacationDAO.getVacationsAfter(dateToTimestamp(dateConstraint));
            }
        } else if (Objects.equals(tableSelection, "excursion")) {
            ExcursionDAO excursionDAO = vacationDatabaseBuilder.excursionDAO();
            if (Objects.equals(beforeOrAfter, "Before")) {
                return excursionDAO.getExcursionsBefore(dateToTimestamp(dateConstraint));
            } else if (Objects.equals(beforeOrAfter, "After")) {
                return excursionDAO.getExcursionsAfter(dateToTimestamp(dateConstraint));
            }
        }
        return new ArrayList<>();
    }

    //Format date for sql comparison
    private Date formatDate(String dateStr) {
        SimpleDateFormat sdf = new SimpleDateFormat(SQLITE_DATE_FORMAT, Locale.US);
        try {
            Date date = new SimpleDateFormat(DATE_FORMAT, Locale.US).parse(dateStr);
            return sdf.parse(sdf.format(date));
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }
}



package my.trackerapp.vacationtracker.UI;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import my.trackerapp.vacationtracker.R;
import my.trackerapp.vacationtracker.test.TitleFormat;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

//Report Display Activity
public class ReportDisplay extends AppCompatActivity {

    //ReportDisplay variables
    private String beforeOrAfter;
    private String tableSelection;
    private String date;
    private List<Map<String, String>> formattedData = new ArrayList<>();
    private ArrayList<String> selectedFields = new ArrayList<>();

    //Report Display containers
    private RecyclerView recyclerViewReport;
    private ReportAdapter reportAdapter;
    private TextView title;
    private TextView subtitle;



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_display);

        //Get variables
        formattedData = (List<Map<String, String>>) getIntent().getSerializableExtra("Formatted Data");
        selectedFields = getIntent().getStringArrayListExtra("Selected Fields");
        beforeOrAfter = getIntent().getStringExtra("Before or After");
        date = getIntent().getStringExtra("Date");
        tableSelection = getIntent().getStringExtra("Table Selection");

        //Set RecyclerView
        recyclerViewReport = findViewById(R.id.recyclerViewReport);

        //Set title
        title = findViewById(R.id.reportTitle);
        title.setText(TitleFormat.reportTitle(tableSelection, beforeOrAfter, date));

        //Set subtitle
        subtitle = findViewById(R.id.subtitle);
        subtitle.setText(TitleFormat.formatFields(selectedFields));


        //Fill in table
        recyclerViewReport.setLayoutManager(new GridLayoutManager(this, selectedFields.size()));
        reportAdapter = new ReportAdapter(formattedData, selectedFields);


        //Set grid lines
        int dividerHeightDp = 1;
        int dividerHeight = (int) (dividerHeightDp * getResources().getDisplayMetrics().density);
        int dividerColor = ContextCompat.getColor(this, R.color.black);

        DividerItemDecoration itemDecoration = new DividerItemDecoration(this, dividerColor, dividerHeight);
        recyclerViewReport.addItemDecoration(itemDecoration);

        recyclerViewReport.setAdapter(reportAdapter);
        reportAdapter.notifyDataSetChanged();
        //Log.d("ReportGenerator", "Adapter set with data: " + formattedData.toString());

    }


}

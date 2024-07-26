package my.trackerapp.vacationtracker.UI;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import my.trackerapp.vacationtracker.R;
import my.trackerapp.vacationtracker.database.Repository;
import my.trackerapp.vacationtracker.entities.Vacation;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import my.trackerapp.vacationtracker.test.SampleData;

import java.util.List;

public class VacationList extends AppCompatActivity {
    private Repository repository;
    private VacationAdapter vacationAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vacation_list);

        //Set up search view
        SearchView searchView = findViewById(R.id.searchView);
        searchView.setQueryHint("Search by Vacation Name, Lodging Name, or Transportation");
        TextView searchTextView = searchView.findViewById(androidx.appcompat.R.id.search_src_text);
        searchTextView.setTextSize(13);

        searchView.setIconifiedByDefault(false);

        //set up FAB
        FloatingActionButton fab = findViewById(R.id.floatingActionButton);
        fab.setImageResource(R.drawable.add);
        fab.setColorFilter(ContextCompat.getColor(this, R.color.white), PorterDuff.Mode.SRC_IN);

        fab.setOnClickListener(view -> {
            Intent intent = new Intent(VacationList.this, VacationDetails.class);
            startActivity(intent);
        });

        repository = new Repository(getApplication());
        SampleData sampleData = new SampleData(repository);

        sampleData.addSampleVacations();
        sampleData.addSampleExcursions();

        List<Vacation> allVacations = repository.getAllVacations();
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        vacationAdapter = new VacationAdapter(this);
        recyclerView.setAdapter(vacationAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        vacationAdapter.setVacations(allVacations);



//        setupSearchView();
//        loadVacations();
    }

    //set up searchview
    private void setupSearchView() {
        SearchView searchView = findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                performSearch(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                performSearch(newText);
                return true;
            }
        });
    }

    //Execute view
    private void performSearch(String query) {
        List<Vacation> searchResults = repository.searchVacations("%" + query + "%");
        if (searchResults != null && !searchResults.isEmpty()) {
            vacationAdapter.setVacations(searchResults);
        } else {
            Toast.makeText(VacationList.this, "No results found", Toast.LENGTH_SHORT).show();
        }
    }

    //Set up menu options
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_vacation_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }

        //Generate report options
        if (item.getItemId() == R.id.generateReport) {
            Intent intent = new Intent(VacationList.this, ReportGenerator.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //Display vacations
    private void loadVacations() {
        List<Vacation> allVacations = repository.getAllVacations();
        vacationAdapter.setVacations(allVacations);
    }

    @Override
    protected void onResume() {
        super.onResume();
        List<Vacation> allVacations = repository.getAllVacations();
        vacationAdapter.setVacations(allVacations);
    }
}
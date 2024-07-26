package my.trackerapp.vacationtracker.test;

import androidx.appcompat.app.AppCompatActivity;

import my.trackerapp.vacationtracker.database.Repository;
import my.trackerapp.vacationtracker.entities.Excursion;
import my.trackerapp.vacationtracker.entities.Vacation;

import java.util.concurrent.Executors;

public class SampleData extends AppCompatActivity {

    private Repository repo;

    //Constructing Repository
    public SampleData(Repository repo) {
        this.repo = repo;
    }

    //Add sample data for Vacation Table
    public void addSampleVacations() {
        Executors.newSingleThreadExecutor().execute(() -> {
            if (repo.isVacationsTableEmpty()) {

                Vacation vacation = new Vacation(1, "London", "AirBNB", "Tour Bus", "07/19/2024", "07/25/2024", "Tour Bus: London Experience", null, null);
                repo.insert(vacation);
                vacation = new Vacation(2, "Rio", "Hostel", "Plane", "08/01/2024", "08/25/2024", "Airline: Delta", "08/02/2024", "13:15");
                repo.insert(vacation);
                vacation = new Vacation(3, "Denver", "Best Western", "Car", "09/02/2024", "09/06/2024", "", null, null);
                repo.insert(vacation);
                vacation = new Vacation(4, "Seattle", "AirBNB", "Boat", "10/08/2024", "10/18/2024", "Confirmation #: 1248-96528", null, null);
                repo.insert(vacation);
                vacation = new Vacation(5, "Saint Petersburg", "The Grand Russian", "Plane", "11/23/2024", "11/28/2024", "", "11/23/2024", "08:30");
                repo.insert(vacation);
                vacation = new Vacation(6, "Paris", "Hôtel Georgette", "Cruise", "12/20/2024", "12/31/2024", "Cruise Line: Disney", null, null);
                repo.insert(vacation);
                vacation = new Vacation(7, "New York City", "Holiday Inn", "Walking", "01/10/2025", "01/16/2025", "", null, null);
                repo.insert(vacation);
                vacation = new Vacation(8, "Los Angeles", "AirBNB", "Tour Bus", "02/23/2025", "02/28/2025", "Tour Bus: Hollywood Stars", null, null);
                repo.insert(vacation);
                vacation = new Vacation(9, "Miami", "Hostel", "Car", "03/19/2025", "03/26/2025", "Stay Away From Locals", null, null);
                repo.insert(vacation);
                vacation = new Vacation(10, "Portland", "Home", "Walking", "04/20/2025", "04/25/2025", "", null, null);
                repo.insert(vacation);
            }

        });
    }

    //Add sample data for Excurion Table
    public void addSampleExcursions() {
        Executors.newSingleThreadExecutor().execute(() -> {
            if (repo.isExcursionsTableEmpty()) {

                Excursion excursion = new Excursion(1, "Sherlock Holmes Experience", 1, "07/20/2024", "09:30", "Address: 221 B Baker Street");
                repo.insert(excursion);
                excursion = new Excursion(2, "Carnival", 2, "08/05/2024", "17:30", "");
                repo.insert(excursion);
                excursion = new Excursion(3, "Hiking", 3, "09/04/2024", "10:00", "Total Miles: 25 miles round trip");
                repo.insert(excursion);
                excursion = new Excursion(4, "Ferry Ride", 4, "10/09/2024", "15:15", "Ticket Number: 587948-5631458");
                repo.insert(excursion);
                excursion = new Excursion(5, "State Hermitage Museum", 5, "11/24/2024", "10:00", "");
                repo.insert(excursion);
                excursion = new Excursion(6, "Wine Tasting", 6, "12/26/2024", "19:00", "Remember Drivers Licence");
                repo.insert(excursion);
                excursion = new Excursion(7, "Empire State Building", 7, "01/14/2025", "08:20", "");
                repo.insert(excursion);
                excursion = new Excursion(8, "Walk of Fame", 8, "02/24/2025", "16:00", "");
                repo.insert(excursion);
                excursion = new Excursion(9, "Miami Beach", 9, "03/20/2025", "07:30", "");
                repo.insert(excursion);
                excursion = new Excursion(10, "Portland Underground", 10, "04/21/2025", "12:00", "Stop at VooDoo Doughnuts before the tour");
                repo.insert(excursion);

            }
        });


    }
}

package my.trackerapp.vacationtracker.database;

import android.app.Application;

import my.trackerapp.vacationtracker.dao.ExcursionDAO;
import my.trackerapp.vacationtracker.dao.VacationDAO;
import my.trackerapp.vacationtracker.entities.Excursion;
import my.trackerapp.vacationtracker.entities.Vacation;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Repository {
    private ExcursionDAO excursionDAO;
    private VacationDAO vacationDAO;
    private List<Vacation> allVacations;
    private List<Excursion> allExcursions;

    private static int NUMBER_OF_THREADS=4;
    public static final ExecutorService databaseExecutor= Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    //Constructor
    public Repository(Application application){
        VacationDatabaseBuilder db=VacationDatabaseBuilder.getDatabase(application);
        excursionDAO=db.excursionDAO();
        vacationDAO=db.vacationDAO();
    }

    //Get all vacations
    public List<Vacation> getAllVacations() {
        Future<List<Vacation>> future = databaseExecutor.submit(new Callable<List<Vacation>>() {
            @Override
            public List<Vacation> call() {
                return vacationDAO.getAllVacation();
            }
        });
        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

    //insert into Vacation Table
    public void insert(Vacation vacation){
        databaseExecutor.execute(()->{
            vacationDAO.insert(vacation);
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    //Update Vacation Table
    public void update(Vacation vacation){
        databaseExecutor.execute(()->{
            vacationDAO.update(vacation);
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    //Delete from Vacation Table
    public void delete(Vacation vacation){
        databaseExecutor.execute(()->{
            vacationDAO.delete(vacation);
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    //Get all excursions
    public List<Excursion>getAllExcursions(){
        databaseExecutor.execute(()->{
            allExcursions=excursionDAO.getAllExcursions();
        });

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return allExcursions;
    }

    //Insert into Excursion Table
    public void insert(Excursion excursion){
        databaseExecutor.execute(()->{
            excursionDAO.insert(excursion);
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    //Update Vacation Table
    public void update(Excursion excursion){
        databaseExecutor.execute(()->{
            excursionDAO.update(excursion);
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    //Delete from Excursion Table
    public void delete(Excursion excursion){
        databaseExecutor.execute(()->{
            excursionDAO.delete(excursion);
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    //Get Vacation by ID
    public Vacation getVacationById(int vacationID) {
        Future<Vacation> future = databaseExecutor.submit(new Callable<Vacation>() {
            @Override
            public Vacation call() {
                return vacationDAO.getVacationById(vacationID);
            }
        });
        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

    //Get Excursion by ID
    public Excursion getExcursionById(int excursionID) {
        Future<Excursion> future = databaseExecutor.submit(new Callable<Excursion>() {
            @Override
            public Excursion call() {
                return excursionDAO.getExcursionById(excursionID);
            }
        });
        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

    //Get all Transportations from Vacation Table
    public List<String> getAllTransportations() {
        Future<List<String>> future = databaseExecutor.submit(new Callable<List<String>>() {
            @Override
            public List<String> call() {
                return vacationDAO.getAllTransportations();
            }
        });
        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

    //Search Vacation Table based on query
    public List<Vacation> searchVacations(String query) {
        Future<List<Vacation>> future = databaseExecutor.submit(new Callable<List<Vacation>>() {
            @Override
            public List<Vacation> call() {
                return vacationDAO.searchVacations(query);
            }
        });
        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

    //Checks to see if Vacation Table is Empty
    public boolean isVacationsTableEmpty() {
        return vacationDAO.getVacationsCount() == 0;
    }

    //Checks to see if Excursions Table is Empty
    public boolean isExcursionsTableEmpty() {
        return excursionDAO.getExcursionCount() == 0;
    }
}



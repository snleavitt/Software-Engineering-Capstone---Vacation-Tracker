package my.trackerapp.vacationtracker.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import my.trackerapp.vacationtracker.entities.Vacation;

import java.util.List;

@Dao
public interface VacationDAO {

    //insert into Vacation Table
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Vacation vacation);

    //Update Vacation Table
    @Update
    void update(Vacation vacation);

    //Delete from Vacation Table
    @Delete
    void delete(Vacation vacation);

    //Count vacations
    @Query("SELECT COUNT(*) FROM vacation")
    int getVacationsCount();

    //Get vacations before user-inputed date
    @Query("SELECT * FROM vacation WHERE startDateSQL <= :dateConstraint")
    List<Vacation> getVacationsBefore(long dateConstraint);

    //Get vacations after user-inputed date
    @Query("SELECT * FROM vacation WHERE startDateSQL >= :dateConstraint")
    List<Vacation> getVacationsAfter(long dateConstraint);

    //Get all vacations
    @Query("SELECT * FROM VACATION ORDER BY vacationId ASC")
    List<Vacation> getAllVacation();

    //Get all transportations
    @Query("SELECT transportation FROM VACATION ORDER BY transportation ASC")
    List<String> getAllTransportations();

    //Get vacation by vacationID
    @Query("SELECT * FROM vacation WHERE vacationID = :vacationID")
    Vacation getVacationById(int vacationID);

    //Search vacation table by vacation name, lodging name, transportation
    @Query("SELECT * FROM vacation WHERE vacationName LIKE '%' || :query || '%' OR lodgingName LIKE '%' || :query || '%' OR transportation LIKE '%' || :query || '%'")
    List<Vacation> searchVacations(String query);

}

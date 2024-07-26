package my.trackerapp.vacationtracker.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import my.trackerapp.vacationtracker.entities.Excursion;

import java.util.List;

@Dao
public interface ExcursionDAO {

    //insert into Excursion Table
    @Insert(onConflict= OnConflictStrategy.IGNORE)
    void insert(Excursion excursion);

    //Update Excursion Table
    @Update
    void update(Excursion excursion);

    //Delete from Excursion Table
    @Delete
    void delete(Excursion excursion);

    //Count excursions
    @Query("SELECT COUNT(*) FROM excursion")
    int getExcursionCount();

    //Get excursions before user-inputed date
    @Query("SELECT * FROM excursion WHERE startDateSQL <= :dateConstraint")
    List<Excursion> getExcursionsBefore(long dateConstraint);

    //Get excursions after user-inputed date
    @Query("SELECT * FROM excursion WHERE startDateSQL >= :dateConstraint")
    List<Excursion> getExcursionsAfter(long dateConstraint);

    //Get all excursions
    @Query("SELECT * FROM EXCURSION ORDER BY excursionID ASC")
    List<Excursion> getAllExcursions();

    //Get all excursions for associated vacation
    @Query("SELECT * FROM EXCURSION WHERE vacationId=:vacation ORDER BY excursionID ASC")
    List<Excursion> getAssociatedExcursions(int vacation);

    //Get excursion by excursionID
    @Query("SELECT * FROM excursion WHERE excursionID = :excursionID")
    Excursion getExcursionById(int excursionID);
}

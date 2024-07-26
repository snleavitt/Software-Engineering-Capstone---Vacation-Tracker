package my.trackerapp.vacationtracker.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import my.trackerapp.vacationtracker.dao.ExcursionDAO;
import my.trackerapp.vacationtracker.dao.VacationDAO;
import my.trackerapp.vacationtracker.entities.Excursion;
import my.trackerapp.vacationtracker.entities.Vacation;

@Database(entities = {Vacation.class, Excursion.class}, version = 30,exportSchema = false)
@TypeConverters({Converters.class})
public abstract class VacationDatabaseBuilder extends RoomDatabase {
    public abstract ExcursionDAO excursionDAO();
    public abstract VacationDAO vacationDAO();

    private static volatile VacationDatabaseBuilder INSTANCE;

    public static VacationDatabaseBuilder getDatabase(final Context context){
        if(INSTANCE==null){
            synchronized (VacationDatabaseBuilder.class){
                if(INSTANCE==null){
                    INSTANCE= Room.databaseBuilder(context.getApplicationContext(),VacationDatabaseBuilder.class,"VacationDatabase.db")
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }


}

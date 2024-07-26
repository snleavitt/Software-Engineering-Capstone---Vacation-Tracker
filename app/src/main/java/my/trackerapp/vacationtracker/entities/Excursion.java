package my.trackerapp.vacationtracker.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

@Entity(tableName = "excursion")
public class Excursion {

    //Setting Table Columns
    @PrimaryKey(autoGenerate = true)
    private int excursionID;
    private String excursionName;
    private int vacationID;
    private String startDate;
    private String startTime;
    private String additionalInformation;
    @ColumnInfo(name = "startDateSQL")
    private Date startDateSQL;

    //Date Formats
    private static final String SQLITE_DATE_FORMAT = "yyyy-MM-dd";
    private static final String DATE_FORMAT = "MM/dd/yyyy";

    //Format Date for SQL comparison
    public Date formatDate(String dateStr) {
        SimpleDateFormat sdf = new SimpleDateFormat(SQLITE_DATE_FORMAT, Locale.US);
        try {
            Date date = new SimpleDateFormat(DATE_FORMAT, Locale.US).parse(dateStr);
            return sdf.parse(sdf.format(date));
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    //Constructor
    public Excursion(int excursionID, String excursionName, int vacationID, String startDate, String startTime, String additionalInformation) {
        this.excursionID = excursionID;
        this.excursionName = excursionName;
        this.vacationID = vacationID;
        this.startDate = startDate;
        this.startTime = startTime;
        this.additionalInformation = additionalInformation;
        this.startDateSQL = formatDate(startDate);
    }

    //Getters and Setters
    public int getExcursionID() {
        return excursionID;
    }

    public void setExcursionID(int excursionID) {
        this.excursionID = excursionID;
    }

    public String getExcursionName() {
        return excursionName;
    }

    public void setExcursionName(String excursionName) {
        this.excursionName = excursionName;
    }

    public int getVacationID() {
        return vacationID;
    }

    public void setVacationID(int vacationID) {
        this.vacationID = vacationID;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getAdditionalInformation() {
        return additionalInformation;
    }

    public void setAdditionalInformation(String additionalInformation) {
        this.additionalInformation = additionalInformation;
    }
    public Date getStartDateSQL() {
        return startDateSQL;
    }

    public void setStartDateSQL(Date startDateSQL) {
        this.startDateSQL = startDateSQL;
    }
}

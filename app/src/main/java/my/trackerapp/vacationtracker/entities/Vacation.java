package my.trackerapp.vacationtracker.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

@Entity(tableName = "vacation")
public class Vacation {

    //Setting Table Columns
    @PrimaryKey(autoGenerate = true)
    private int vacationID;
    private String vacationName;
    private String lodgingName;
    private String transportation;
    private String startDate;
    private String endDate;
    private String additionalDetails;
    @ColumnInfo(name = "startDateSQL")
    private Date startDateSQL;

    //optional plane details
    private String planeDepartureDate;
    private String planeDepartureTime;

    //Date Formats
    private static final String SQLITE_DATE_FORMAT = "yyyy-MM-dd";
    private static final String DATE_FORMAT = "MM/dd/yyyy";

    //Format Date for SQL comparion
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

    //Constructors
    public Vacation(int vacationID, String vacationName, String lodgingName, String transportation, String startDate, String endDate, String additionalDetails, String planeDepartureDate, String planeDepartureTime) {
        this.vacationID = vacationID;
        this.vacationName = vacationName;
        this.lodgingName = lodgingName;
        this.transportation = transportation;
        this.startDate= startDate;
        this.endDate = endDate;
        this.additionalDetails = additionalDetails;
        this.planeDepartureDate = planeDepartureDate;
        this.planeDepartureTime = planeDepartureTime;
        this.startDateSQL = formatDate(startDate);

    }

    //Getters and Setters
    public int getVacationID() {
        return vacationID;
    }

    public void setVacationID(int vacationID) {
        this.vacationID = vacationID;
    }

    public String getVacationName() {
        return vacationName;
    }

    public void setVacationName(String vacationName) {
        this.vacationName = vacationName;
    }

    public String getLodgingName() {
        return lodgingName;
    }

    public void setLodgingName(String lodgingName) {
        this.lodgingName = lodgingName;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getTransportation() {
        return transportation;
    }

    public void setTransportation(String transportation) {
        this.transportation = transportation;
    }

    public String getPlaneDepartureDate() {
        return planeDepartureDate;
    }

    public void setPlaneDepartureDate(String planeDepartureDate) {
        this.planeDepartureDate = planeDepartureDate;
    }

    public String getPlaneDepartureTime() {
        return planeDepartureTime;
    }

    public void setPlaneDepartureTime(String planeDepartureTime) {
        this.planeDepartureTime = planeDepartureTime;
    }

    public String getAdditionalDetails() {
        return additionalDetails;
    }

    public void setAdditionalDetails(String additionalDetails) {
        this.additionalDetails = additionalDetails;
    }

    public Date getStartDateSQL() {
        return startDateSQL;
    }

    public void setStartDateSQL(Date startDateSQL) {
        this.startDateSQL = startDateSQL;
    }
}

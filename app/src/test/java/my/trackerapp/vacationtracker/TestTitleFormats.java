package my.trackerapp.vacationtracker;


import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import my.trackerapp.vacationtracker.test.TitleFormat;

import org.junit.Test;

import java.util.*;


public class TestTitleFormats {

    private ArrayList<String> selectedFields = new ArrayList<String>();
    private String beforeOrAfter = "Before";
    private String tableSelection = "vacation";
    private String date = "12/31/2025";
    private String title = "Vacations: Before 12/31/2025";
    private String subtitle = "Selected Fields: Lodging Name, Transportation, Start Date";

    ;

    //Set array
    public void setSelectedFields() {
        selectedFields.add("Lodging Name");
        selectedFields.add("Transportation");
        selectedFields.add("Start Date");
    }

    //test title method
    @Test
    public void test_Title() {
        TitleFormat titleFormat = new TitleFormat();
        String testTitle = titleFormat.reportTitle(tableSelection, beforeOrAfter, date);
        boolean titleMatches = false;

        if (Objects.equals(testTitle, title)) {
            titleMatches = true;
        }

        assertTrue("Title should match expected title", titleMatches);

    }

    //test subtitle method
    @Test
    public void test_Subtitle() {
        TitleFormat titleFormat = new TitleFormat();
        setSelectedFields();
        String testSubtitle = titleFormat.formatFields(selectedFields);
        boolean titleMatches = false;

        if (Objects.equals(testSubtitle, subtitle)) {
            titleMatches = true;
        }

        assertTrue("Subtitle should match expected subtitle", titleMatches);

    }
}

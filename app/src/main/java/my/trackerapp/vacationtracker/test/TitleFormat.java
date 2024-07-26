package my.trackerapp.vacationtracker.test;

import java.util.ArrayList;
import java.util.Objects;

public class TitleFormat {


    //format report title
    public static String reportTitle(String tableSelection, String beforeOrAfter, String date) {
        String title;
        if (Objects.equals(tableSelection, "vacation")) {
            tableSelection = "Vacation";
        } else if (Objects.equals(tableSelection, "excursion")) {
            tableSelection = "Excursion";
        }

        title = tableSelection + "s: " + beforeOrAfter + " " + date;
        return title;
    }


    //format subtitle
    public static String formatFields(ArrayList<String> selectedFields) {
        StringBuilder selectedFieldsStr = new StringBuilder("Selected Fields: ");

        for (int i = 0; i < selectedFields.size(); i++) {
            selectedFieldsStr.append(selectedFields.get(i));
            if (i < selectedFields.size() - 1) {
                selectedFieldsStr.append(", ");
            }
        }

        return selectedFieldsStr.toString();
    }
}


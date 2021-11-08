package fr.b4.apps.expenses.util;

import lombok.experimental.UtilityClass;

import java.time.LocalDate;
import java.time.temporal.TemporalField;
import java.time.temporal.WeekFields;
import java.util.Locale;

@UtilityClass
public class ExpenseUtils {

    public static LocalDate getFistDayOfCurrentWeek() {
        LocalDate now = LocalDate.now();
        TemporalField fieldISO = WeekFields.of(Locale.FRANCE).dayOfWeek();
        return now.with(fieldISO, 1);
    }

    public static LocalDate getFistDayOfCurrentMonth() {
        LocalDate now = LocalDate.now();
        return LocalDate.of(now.getYear(), now.getMonth(), 1);
    }
}

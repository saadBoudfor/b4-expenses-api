package fr.b4.apps.expenses.util;

import lombok.experimental.UtilityClass;

import java.time.LocalDate;

@UtilityClass
public class ExpenseUtils {
    public static LocalDate getCurrentTargetDate() {
        LocalDate today = LocalDate.now();
        return LocalDate.of(today.getYear(), today.getMonth(), 1);
    }
}

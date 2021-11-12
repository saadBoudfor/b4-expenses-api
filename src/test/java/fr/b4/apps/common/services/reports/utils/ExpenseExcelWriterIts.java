package fr.b4.apps.common.services.reports.utils;

import fr.b4.apps.common.repositories.DataGenerator;
import fr.b4.apps.expenses.entities.Expense;
import fr.b4.apps.expenses.util.converters.ExpenseConverter;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;


import java.util.List;


public class ExpenseExcelWriterIts {
    @Test
    public void shouldWriteExpenseSuccess() {
        List<Expense> expenseList = DataGenerator.generateExpenses(10);
        expenseList.forEach(expense -> expense.setExpenseLines(DataGenerator.generateExpenseLines(3)));

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("test");
        sheet.setColumnWidth(0, 6000);
        sheet.setColumnWidth(1, 4000);


        ExpenseExcelWriter.writeExpenses(ExpenseConverter.toDTO(expenseList), workbook, sheet);


        Assertions.assertNotNull(workbook);

        for (int i = 0; i < expenseList.size(); i++) {
            Assertions.assertEquals(expenseList.get(i).getName(), sheet.getRow(i + 1).getCell(0).toString());
        }

    }

    @Test
    public void shouldDefineExpenseReportHeaderSuccess() {

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("test");
        sheet.setColumnWidth(0, 6000);
        sheet.setColumnWidth(1, 4000);


        ExpenseExcelWriter.defineHeaders(workbook, sheet);


        Assertions.assertNotNull(workbook);

        final String[] headersNames = {"Name", "Date", "auteur", "commentaire",
                "Magasin/Restaurant", "Produit", "Prix", "Quantité"};

        for (int i = 0; i < headersNames.length; i++) {
            Assertions.assertEquals(headersNames[i], sheet.getRow(0).getCell(i).toString());
        }

    }
}

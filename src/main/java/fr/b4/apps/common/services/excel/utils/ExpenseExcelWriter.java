package fr.b4.apps.common.services.excel.utils;

import fr.b4.apps.expenses.dto.ExpenseDTO;
import fr.b4.apps.expenses.dto.ExpenseLineDTO;
import lombok.experimental.UtilityClass;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.List;

@UtilityClass
public class ExpenseExcelWriter {


    /**
     * Save expense list as Excel File.
     *
     * @param expenses expenses list to save
     * @param workbook workbook used to store expenses information
     * @param sheet    sheet used to store expenses information
     */
    public static void writeExpenses(List<ExpenseDTO> expenses, Workbook workbook, Sheet sheet) {
        int rowIndex = 1; //$rowIndex 0 used for header
        for (ExpenseDTO expense : expenses) {
            if (!ObjectUtils.isEmpty(expense)) {
                for (ExpenseLineDTO expenseLine : expense.getExpenseLines()) {
                    writeOneExpenseLine(expense, expenseLine, workbook, sheet, rowIndex);
                }
            }
            rowIndex++;
        }

    }

    /**
     * Write one expense line in one row in expense report
     *
     * @param expense     some information on expense line are stored in expense level
     * @param expenseLine expenseLine to save
     * @param workbook    excel report file
     * @param sheet       Excel sheet used for expense report
     * @param rowIndex   index of row
     */
    private static void writeOneExpenseLine(ExpenseDTO expense, ExpenseLineDTO expenseLine, Workbook workbook, Sheet sheet, int rowIndex) {
        CellStyle style = workbook.createCellStyle();
        style.setWrapText(true);
        Row row = sheet.createRow(rowIndex);

        final String[] expenseRow = {
                StringUtils.hasLength(expense.getName()) ? expense.getName() : "",
                ObjectUtils.isEmpty(expense.getDate()) ? "" : expense.getDate().toString(),
                ObjectUtils.isEmpty(expense.getAuthor()) ? "" : expense.getAuthor().getName() + " " + expense.getAuthor().getLastname(), // full name
                StringUtils.hasLength(expense.getComment()) ? expense.getComment() : "",
                ObjectUtils.isEmpty(expense.getPlace()) ? "" : expense.getPlace().getName(),
                ObjectUtils.isEmpty(expenseLine.getProduct()) ? "" : expenseLine.getProduct().getName(),
                ObjectUtils.isEmpty(expenseLine.getPrice()) ? "" : expenseLine.getPrice().toString(),
                ObjectUtils.isEmpty(expenseLine.getQuantity()) ? "" : expenseLine.getQuantity().toString()
        };

        Cell cell;
        for (int i = 0; i < expenseRow.length; i++) {
            cell = row.createCell(i);
            cell.setCellValue(expenseRow[i]);
            cell.setCellStyle(style);
        }
    }

    public static void defineHeaders(Workbook workbook, Sheet sheet) {
        CellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFillForegroundColor(IndexedColors.BROWN.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        XSSFFont font = ((XSSFWorkbook) workbook).createFont();
        font.setFontName("Arial");
        font.setFontHeightInPoints((short) 12);
        font.setBold(true);
        font.setColor(IndexedColors.WHITE.index);
        headerStyle.setFont(font);

        final String[] headersNames = {"Name", "Date", "auteur", "commentaire", "Magasin/Restaurant", "Produit", "Prix", "Quantité"};
        Row header = sheet.createRow(0);

        Cell headerCell;
        for (int i = 0; i < headersNames.length; i++) {
            headerCell = header.createCell(i);
            headerCell.setCellValue(headersNames[i]);
            headerCell.setCellStyle(headerStyle);
        }
    }

}

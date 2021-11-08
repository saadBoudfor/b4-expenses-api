package fr.b4.apps.common.services.excel;

import fr.b4.apps.expenses.dto.ExpenseDTO;
import fr.b4.apps.expenses.dto.ExpenseLineDTO;
import fr.b4.apps.expenses.entities.Expense;
import fr.b4.apps.expenses.entities.ExpenseLine;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.List;

public class ExpenseExcelWriter {
    static void writeExpenses(List<ExpenseDTO> expenses, Workbook workbook, Sheet sheet) {
        int $rowIndex = 1;
        for (ExpenseDTO expense : expenses) {
            for (ExpenseLineDTO expenseLine : expense.getExpenseLines()) {
                CellStyle style = workbook.createCellStyle();
                style.setWrapText(true);
                Row row = sheet.createRow($rowIndex);
                Cell cell = row.createCell(0);
                if (StringUtils.hasLength(expense.getName())) {
                    cell.setCellValue(expense.getName());
                    cell.setCellStyle(style);
                }

                if (!ObjectUtils.isEmpty(expense.getDate())) {
                    cell = row.createCell(1);
                    cell.setCellValue(expense.getDate().toString());
                    cell.setCellStyle(style);
                }

                if (!ObjectUtils.isEmpty(expense.getAuthor())) {
                    cell = row.createCell(2);
                    cell.setCellValue(expense.getAuthor().getName() + " " + expense.getAuthor().getLastname());
                    cell.setCellStyle(style);
                }

                if (StringUtils.hasLength(expense.getComment())) {
                    cell = row.createCell(3);
                    cell.setCellValue(expense.getComment());
                    cell.setCellStyle(style);
                }

                if (!ObjectUtils.isEmpty(expense.getPlace())) {
                    cell = row.createCell(4);
                    cell.setCellValue(expense.getPlace().getName());
                    cell.setCellStyle(style);
                }

                if (!ObjectUtils.isEmpty(expenseLine.getProduct())) {
                    cell = row.createCell(5);
                    cell.setCellValue(expenseLine.getProduct().getName());
                    cell.setCellStyle(style);
                }

                if (!ObjectUtils.isEmpty(expenseLine.getPrice())) {
                    cell = row.createCell(6);
                    cell.setCellValue(expenseLine.getPrice());
                    cell.setCellStyle(style);
                }

                if (!ObjectUtils.isEmpty(expenseLine.getQuantity())) {
                    cell = row.createCell(7);
                    cell.setCellValue(expenseLine.getQuantity());
                    cell.setCellStyle(style);
                }

                $rowIndex++;
            }
        }

    }

    static void defineHeaders(Workbook workbook, Sheet sheet) {
        CellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFillForegroundColor(IndexedColors.BROWN.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        XSSFFont font = ((XSSFWorkbook) workbook).createFont();
        font.setFontName("Arial");
        font.setFontHeightInPoints((short) 12);
        font.setBold(true);
        font.setColor(IndexedColors.WHITE.index);
        headerStyle.setFont(font);

        Row header = sheet.createRow(0);
        Cell headerCell = header.createCell(0);
        headerCell.setCellValue("Name");
        headerCell.setCellStyle(headerStyle);

        headerCell = header.createCell(1);
        headerCell.setCellValue("Date");
        headerCell.setCellStyle(headerStyle);

        headerCell = header.createCell(2);
        headerCell.setCellValue("auteur");
        headerCell.setCellStyle(headerStyle);

        headerCell = header.createCell(3);
        headerCell.setCellValue("commentaire");
        headerCell.setCellStyle(headerStyle);

        headerCell = header.createCell(4);
        headerCell.setCellValue("Magasin/Restaurant");
        headerCell.setCellStyle(headerStyle);

        headerCell = header.createCell(5);
        headerCell.setCellValue("Produit");
        headerCell.setCellStyle(headerStyle);

        headerCell = header.createCell(6);
        headerCell.setCellValue("Prix");
        headerCell.setCellStyle(headerStyle);

        headerCell = header.createCell(7);
        headerCell.setCellValue("Quantité");
        headerCell.setCellStyle(headerStyle);
    }

}

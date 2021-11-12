package fr.b4.apps.expenses.process;

import fr.b4.apps.expenses.dto.ExpenseDTO;
import fr.b4.apps.expenses.dto.ExpenseBasicStatsDTO;
import fr.b4.apps.expenses.entities.Expense;
import fr.b4.apps.expenses.services.BudgetService;
import fr.b4.apps.expenses.services.ExpenseService;
import fr.b4.apps.expenses.util.converters.ExpenseConverter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;

@Component
public class ExpenseProcess {
    @Value("${working.dir}")
    String workingDir;

    @Value("${expenses.photos.dir}")
    String expenseBillDir;

    private final BudgetService budgetService;
    private final ExpenseService expenseService;

    public ExpenseProcess(BudgetService budgetService, ExpenseService expenseService) {
        this.budgetService = budgetService;
        this.expenseService = expenseService;
    }

    public ExpenseDTO addBill(String expenseStr, MultipartFile bill) throws IOException {
        ExpenseDTO expense = ExpenseConverter.valueOf(expenseStr);
        if (!ObjectUtils.isEmpty(bill)) {
            String photoURL = expenseBillDir + bill.getOriginalFilename();
            bill.transferTo(Path.of(photoURL));
            expense.setBill(bill.getOriginalFilename());
        }
        return ExpenseConverter.toDTO(expenseService.save(expense));
    }

    public ExpenseDTO addBill(Long expenseID, MultipartFile bill) throws IOException {
        String photoURL = expenseBillDir + bill.getOriginalFilename();
        bill.transferTo(Path.of(photoURL));
        return expenseService.updateException(expenseID, bill.getOriginalFilename());
    }

    public ExpenseBasicStatsDTO getBasicStats(Long userID) {
        ExpenseBasicStatsDTO expenseBasicStatsDTO = expenseService.getExpenseStats(userID);
        expenseBasicStatsDTO.setTarget(budgetService.getTarget(userID));
        return expenseBasicStatsDTO;
    }

}

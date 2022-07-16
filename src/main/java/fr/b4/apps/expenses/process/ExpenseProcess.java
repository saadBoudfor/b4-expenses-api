package fr.b4.apps.expenses.process;

import fr.b4.apps.expenses.dto.ExpenseDTO;
import fr.b4.apps.expenses.dto.ExpenseBasicStatsDTO;
import fr.b4.apps.expenses.services.BudgetService;
import fr.b4.apps.expenses.services.ExpenseService;
import fr.b4.apps.expenses.util.converters.ExpenseConverter;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;

@Slf4j
@Component
public class ExpenseProcess {

    @Value("${expenses.photos.dir.bill}")
    private String expenseBillDir = "";

    private final BudgetService budgetService;
    private final ExpenseService expenseService;

    public ExpenseProcess(BudgetService budgetService, ExpenseService expenseService) {
        this.budgetService = budgetService;
        this.expenseService = expenseService;
    }

    /**
     * Add new expense
     *
     * @param expense new expense
     * @param bill    (file)
     * @return saved expense
     */
    public ExpenseDTO save(@NonNull ExpenseDTO expense, MultipartFile bill) {
        if (!ObjectUtils.isEmpty(bill)) {
            try {
                String photoURL = expenseBillDir + bill.getOriginalFilename();
                bill.transferTo(Path.of(photoURL));
                expense.setBill(bill.getOriginalFilename());
            } catch (IOException exception) {
                log.error("failed to save bill for expense: {}", expense);
            }
        }
        return ExpenseConverter.toDTO(expenseService.save(expense));
    }

    /**
     * Add bill to existing expense
     *
     * @param expenseID expense to upodate
     * @param bill      given bill
     * @return updated expense
     * @throws IOException when
     */
    public ExpenseDTO addBill(Long expenseID, MultipartFile bill) throws IOException {
        if (ObjectUtils.isEmpty(bill)) {
            throw new IllegalArgumentException("bill must be not null");
        }
        if (ObjectUtils.isEmpty(expenseID) || expenseID <= 0) {
            throw new IllegalArgumentException("bill must be not null");
        }
        String photoURL = expenseBillDir + bill.getOriginalFilename();
        bill.transferTo(Path.of(photoURL));
        return expenseService.updateExpenseBill(expenseID, bill.getOriginalFilename());
    }

    /**
     * Get Stats about expenses for given user
     */
    public ExpenseBasicStatsDTO getBasicStats(Long userID) {
        ExpenseBasicStatsDTO expenseBasicStatsDTO = expenseService.getExpenseStats(userID);
        expenseBasicStatsDTO.setTarget(budgetService.getTarget(userID));
        return expenseBasicStatsDTO;
    }

}

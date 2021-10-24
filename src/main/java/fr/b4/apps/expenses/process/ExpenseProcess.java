package fr.b4.apps.expenses.process;

import fr.b4.apps.clients.entities.User;
import fr.b4.apps.clients.repositories.UserRepository;
import fr.b4.apps.expenses.dto.ExpenseDTO;
import fr.b4.apps.expenses.dto.ExpenseInfoDTO;
import fr.b4.apps.expenses.entities.Expense;
import fr.b4.apps.expenses.services.BudgetService;
import fr.b4.apps.expenses.services.ExpenseService;
import fr.b4.apps.expenses.util.converters.ExpenseConverter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

@Component
public class ExpenseProcess {
    @Value("${working.dir}")
    String workingDir;

    @Value("${expenses.photos.dir}")
    String expenseBillDir;


    private final UserRepository userRepository;
    private final BudgetService budgetService;
    private final ExpenseService expenseService;

    public ExpenseProcess(UserRepository userRepository, BudgetService budgetService, ExpenseService expenseService) {
        this.userRepository = userRepository;
        this.budgetService = budgetService;
        this.expenseService = expenseService;
    }

    public Expense save(String expenseStr, MultipartFile file) throws IOException {
        Expense expense = ExpenseConverter.valueOf(expenseStr);
        if (!ObjectUtils.isEmpty(file)) {
            String photoURL = workingDir + expenseBillDir + file.getOriginalFilename();
            file.transferTo(Path.of(photoURL));
            expense.setBill(file.getOriginalFilename());
        }
        return expenseService.save(expense);
    }

    private User getUser(String userID) {
        if (!StringUtils.hasLength(userID))
            return null;
        Optional<User> userOptional = userRepository.findById(Long.valueOf(userID));
        return userOptional.orElse(null);
    }

    public ExpenseInfoDTO getInfo(String userID) {
        User authenticated = getUser(userID);
        if (ObjectUtils.isEmpty(authenticated))
            return null;

        ExpenseInfoDTO expenseInfoDTO = new ExpenseInfoDTO();
        expenseInfoDTO.setTarget(budgetService.getTarget(authenticated));
        expenseInfoDTO.setTotal(expenseService.getTotal(Long.valueOf(userID)));
        expenseInfoDTO.setCountRestaurant(expenseService.getRestaurantCount(Long.valueOf(userID)));
        expenseInfoDTO.setCountStore(expenseService.getStoreCount(Long.valueOf(userID)));
        expenseInfoDTO.setTotalRestaurant(expenseService.getTotalRestaurant(Long.valueOf(userID)));
        expenseInfoDTO.setTotalStore(expenseService.getTotalStore(Long.valueOf(userID)));
        return expenseInfoDTO;
    }

    public List<ExpenseDTO> findByUserID(String userID, Integer page, Integer size) {
        User authenticated = getUser(userID);
        if (ObjectUtils.isEmpty(authenticated))
            return null;
        return ExpenseConverter.toDTO(expenseService.findByUser(authenticated, page, size));
    }
}

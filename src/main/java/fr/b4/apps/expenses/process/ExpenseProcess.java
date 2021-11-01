package fr.b4.apps.expenses.process;

import fr.b4.apps.clients.entities.User;
import fr.b4.apps.clients.repositories.UserRepository;
import fr.b4.apps.common.entities.Place;
import fr.b4.apps.common.services.PlaceService;
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
    private final PlaceService placeService;

    public ExpenseProcess(UserRepository userRepository, BudgetService budgetService, ExpenseService expenseService, PlaceService placeService) {
        this.userRepository = userRepository;
        this.budgetService = budgetService;
        this.expenseService = expenseService;
        this.placeService = placeService;
    }

    public Expense save(String expenseStr, MultipartFile file) throws IOException {
        Expense expense = ExpenseConverter.valueOf(expenseStr);
        if (!ObjectUtils.isEmpty(file)) {
            String photoURL = expenseBillDir + file.getOriginalFilename();
            file.transferTo(Path.of(photoURL));
            expense.setBill(file.getOriginalFilename());
        }
        return expenseService.save(expense);
    }

    public ExpenseDTO save(Long expenseID, MultipartFile file) throws IOException {
        Expense expense = expenseService.findByID(expenseID);
        if (!ObjectUtils.isEmpty(file)) {
            String photoURL = expenseBillDir + file.getOriginalFilename();
            file.transferTo(Path.of(photoURL));
            expense.setBill(file.getOriginalFilename());
        }
        return ExpenseConverter.toDTO(expenseService.save(expense));
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
        expenseInfoDTO.setWeekCount(expenseService.getCurrentWeekTotal(Long.valueOf(userID)));
        return expenseInfoDTO;
    }

    public List<ExpenseDTO> findByUserID(String userID, Integer page, Integer size) {
        User authenticated = getUser(userID);
        if (ObjectUtils.isEmpty(authenticated))
            return null;
        return ExpenseConverter.toDTO(expenseService.findByUser(authenticated, page, size));
    }

    public List<ExpenseDTO> findByPlaceID(String userID, String placeID) {
        User authenticated = getUser(userID);
        Place place = placeService.get(Long.valueOf(placeID));
        if (ObjectUtils.isEmpty(authenticated))
            return null;
        return ExpenseConverter.toDTO(expenseService.findByPlaceID(authenticated, place));
    }

    public ExpenseDTO find(Long id) {
        return ExpenseConverter.toDTO(expenseService.findByID(id));
    }

    public void delete(Long expenseID) {
        expenseService.delete(expenseID);
    }
}

package fr.b4.apps.expenses.web;

import com.fasterxml.jackson.core.JsonProcessingException;
import fr.b4.apps.common.entities.PlaceType;
import fr.b4.apps.common.exceptions.BadRequestException;
import fr.b4.apps.common.exceptions.ResourceUpdateFailedException;
import fr.b4.apps.expenses.dto.ExpenseDTO;
import fr.b4.apps.expenses.dto.ExpenseBasicStatsDTO;
import fr.b4.apps.expenses.dto.NutrientStatRecapDTO;
import fr.b4.apps.expenses.process.ExpenseProcess;
import fr.b4.apps.expenses.services.ExpenseService;
import fr.b4.apps.expenses.util.converters.ExpenseConverter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Slf4j
@RequestMapping("expenses")
@RestController
public class ExpenseController {

    private final ExpenseProcess expenseProcess;
    private final ExpenseService expenseService;

    public ExpenseController(ExpenseProcess expenseProcess,
                             ExpenseService expenseService) {
        this.expenseProcess = expenseProcess;
        this.expenseService = expenseService;
    }

    @GetMapping("/basic-stats")
    public ExpenseBasicStatsDTO getBasicStats(@RequestHeader("access-token") String accessToken) {
        return expenseProcess.getBasicStats(Long.valueOf(accessToken));
    }

    @GetMapping("/basic-stats/restaurants")
    public ExpenseBasicStatsDTO getRestaurantBasicStats(@RequestHeader("access-token") String accessToken) {
        return expenseService.getExpenseStats(Long.valueOf(accessToken), PlaceType.RESTAURANT);
    }

    @GetMapping("/basic-stats/stores")
    public ExpenseBasicStatsDTO getStoresBasicStats(@RequestHeader("access-token") String accessToken) {
        return expenseService.getExpenseStats(Long.valueOf(accessToken), PlaceType.STORE);
    }

    @GetMapping("/basic-stats/nutrients")
    public NutrientStatRecapDTO getNutrientStats() {
        return expenseService.getNutrientStats();
    }

    @GetMapping
    public List<ExpenseDTO> getAll(@RequestHeader("access-token") String userID,
                                   @RequestParam(value = "page", required = false) Integer page,
                                   @RequestParam(value = "size", required = false) Integer size) {
        return expenseService.find(Long.valueOf(userID), page, size);
    }

    @GetMapping("/last")
    List<ExpenseDTO> findTop5ByUser(@RequestHeader("access-token") String userID) {
        return expenseService.findTop5ByUser(Long.valueOf(userID));
    }

    @GetMapping("/{id}")
    public ExpenseDTO getByID(@PathVariable("id") String id) {
        return expenseService.findDTOByID(Long.valueOf(id));
    }

    @GetMapping("/place/{placeID}")
    public List<ExpenseDTO> getByPlace(@RequestHeader("access-token") String userID,
                                       @PathVariable("placeID") String placeID) {
        return expenseService.findByPlaceID(Long.valueOf(userID), Long.valueOf(placeID));
    }

    @PostMapping
    public ExpenseDTO save(@RequestParam(value = "file", required = false) MultipartFile file,
                           @RequestParam(value = "expense") String expenseStr) {
        try {
            ExpenseDTO expense = ExpenseConverter.valueOf(expenseStr);
            return expenseProcess.save(expense, file);
        } catch (JsonProcessingException | NullPointerException exception) {
            log.error("Error add new expense. Given expense is invalid");
            exception.printStackTrace();
            throw new BadRequestException("given expense is invalid");
        }
    }

    @DeleteMapping("/{expenseID}")
    public void delete(@PathVariable("expenseID") Long expenseID) {
        expenseService.delete(expenseID);
    }

    @PutMapping("/{expenseID}")
    public ExpenseDTO addBill(@PathVariable(value = "expenseID") Long expenseID,
                              @RequestParam(value = "file") MultipartFile expenseBill) {
        try {
            return expenseProcess.addBill(expenseID, expenseBill);
        } catch (IOException exception) {
            log.error("add new Bill to expense {} failed", expenseID);
            exception.printStackTrace();
            throw new ResourceUpdateFailedException("add new Bill to expense " + expenseID + " failed");
        } catch (IllegalArgumentException illegalArgumentException) {
            throw new BadRequestException(illegalArgumentException.getMessage());
        }
    }


}

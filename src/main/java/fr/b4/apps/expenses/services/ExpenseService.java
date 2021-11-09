package fr.b4.apps.expenses.services;

import fr.b4.apps.common.entities.Address;
import fr.b4.apps.common.entities.Place;
import fr.b4.apps.common.entities.Product;
import fr.b4.apps.common.repositories.AddressRepository;
import fr.b4.apps.common.repositories.PlaceRepository;
import fr.b4.apps.common.repositories.ProductRepository;
import fr.b4.apps.expenses.dto.ExpenseBasicStatsDTO;
import fr.b4.apps.expenses.dto.ExpenseDTO;
import fr.b4.apps.expenses.entities.Expense;
import fr.b4.apps.expenses.entities.ExpenseLine;
import fr.b4.apps.common.entities.PlaceType;
import fr.b4.apps.expenses.repositories.ExpenseLineRepository;
import fr.b4.apps.expenses.repositories.ExpenseRepository;
import fr.b4.apps.expenses.util.converters.ExpenseConverter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.time.LocalDate;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static fr.b4.apps.expenses.util.ExpenseUtils.getFistDayOfCurrentMonth;
import static fr.b4.apps.expenses.util.ExpenseUtils.getFistDayOfCurrentWeek;

@Slf4j
@Component
public class ExpenseService {
    private final ExpenseRepository expenseRepository;
    private final ExpenseLineRepository expenseLineRepository;
    private final PlaceRepository placeRepository;
    private final AddressRepository addressRepository;
    private final ProductRepository productRepository;

    public ExpenseService(ExpenseRepository expenseRepository,
                          ExpenseLineRepository expenseLineRepository,
                          PlaceRepository placeRepository,
                          AddressRepository addressRepository,
                          ProductRepository productRepository) {
        this.expenseRepository = expenseRepository;
        this.expenseLineRepository = expenseLineRepository;
        this.placeRepository = placeRepository;
        this.addressRepository = addressRepository;
        this.productRepository = productRepository;
    }

    public Expense save(ExpenseDTO dto) {
        Expense expense = ExpenseConverter.toExpense(dto);
        if (!ObjectUtils.isEmpty(expense.getPlace()) && ObjectUtils.isEmpty(expense.getPlace().getId())) {
            // create place first
            Address saved = addressRepository.save(expense.getPlace().getAddress());
            expense.getPlace().setAddress(saved);

            Place savedPlace = placeRepository.save(expense.getPlace());
            expense.setPlace(savedPlace);
        }
        for (ExpenseLine expenseLine : expense.getExpenseLines()) {
            expenseLine.setExpense(expense);
        }

        expense.getExpenseLines()
                .stream()
                .filter(this::isProductValid)
                .forEach(this::checkProduct);

        expense = expenseRepository.save(expense);
        expenseLineRepository.saveAll(expense.getExpenseLines());
        return expenseRepository.save(expense);
    }

    private boolean isProductValid(ExpenseLine expenseLine) {
        return !ObjectUtils.isEmpty(expenseLine) && !ObjectUtils.isEmpty(expenseLine.getProduct());
    }

    private void checkProduct(ExpenseLine expenseLine) {
        Product found = productRepository.findFirstByName(expenseLine.getProduct().getName());
        if (!ObjectUtils.isEmpty(found)) {
            // to prevent update product stored in db
            expenseLine.setProduct(found);
        } else {
            Product saved = productRepository.save(expenseLine.getProduct());
            expenseLine.setProduct(saved);
        }
    }

    public List<ExpenseDTO> findByUser(Long userID, Integer page, Integer size) {
        if (ObjectUtils.isEmpty(size))
            return expenseRepository.findByUserIdOrderByDateDesc(userID).stream().map(ExpenseConverter::toDTO).collect(Collectors.toList());
        Pageable pageable = Pageable.unpaged();
        if (!ObjectUtils.isEmpty(page) && !ObjectUtils.isEmpty(size)) {
            pageable = PageRequest.of(page, size);
        }
        List<Expense> results = expenseRepository.findByUserIdOrderByDateDesc(userID, pageable);
        return CollectionUtils.isEmpty(results) ? new ArrayList<>() : results.stream().map(ExpenseConverter::toDTO).collect(Collectors.toList());
    }

    public List<ExpenseDTO> findTop5ByUser(Long userID) {
        return expenseRepository.findTop5ByUserIdOrderByDateDesc(userID).stream().map(ExpenseConverter::toDTO).collect(Collectors.toList());
    }

    public List<ExpenseDTO> findByPlaceID(Long userID, Long placeId) {
        List<Expense> results = expenseRepository.findByUserIdAndPlaceId(userID, placeId);
        return CollectionUtils.isEmpty(results) ? new ArrayList<>() : results.stream().map(ExpenseConverter::toDTO).collect(Collectors.toList());
    }

    public Expense findByID(Long id) {
        return expenseRepository.findById(id).orElse(null);
    }

    public ExpenseDTO findDTOByID(Long id) {
        return ExpenseConverter.toDTO(expenseRepository.findById(id).orElse(null));
    }


    public List<Expense> findAll() {
        return expenseRepository.findAll();
    }

    @Transactional
    public void delete(Long expenseID) {
        expenseLineRepository.deleteAllByExpenseId(expenseID);
        expenseRepository.deleteById(expenseID);
    }

    public ExpenseBasicStatsDTO getExpenseStats(Long userID) {
        final LocalDate firstDayOfCurrentWeek = getFistDayOfCurrentWeek();
        final LocalDate firstDayOfCurrentMonth = getFistDayOfCurrentMonth();
        final ExpenseBasicStatsDTO basicStatsDTO = new ExpenseBasicStatsDTO();
        setStatsForAllExpenses(basicStatsDTO, userID, firstDayOfCurrentMonth, firstDayOfCurrentWeek);
        return basicStatsDTO;
    }

    public ExpenseBasicStatsDTO getExpenseStatsByPlace(Long userID, PlaceType placeType) {
        final LocalDate firstDayOfCurrentWeek = getFistDayOfCurrentWeek();
        final LocalDate firstDayOfCurrentMonth = getFistDayOfCurrentMonth();
        final ExpenseBasicStatsDTO basicStatsDTO = new ExpenseBasicStatsDTO();
        setStatsForAllExpensesByPlace(basicStatsDTO, userID, firstDayOfCurrentMonth, firstDayOfCurrentWeek, placeType);
        return basicStatsDTO;
    }

    private void setStatsForAllExpenses(ExpenseBasicStatsDTO basicStatsDTO,
                                        Long userID,
                                        LocalDate firstDayOfCurrentMonth,
                                        LocalDate firstDayOfCurrentWeek) {
        Float total = expenseRepository.sumAllExpenses(userID, firstDayOfCurrentMonth);
        Integer count = expenseRepository.countAllExpenses(userID, firstDayOfCurrentMonth);
        Integer countForCurrentWeek = expenseRepository.countAllExpenses(userID, firstDayOfCurrentWeek);
        Float totalForCurrentWeek = expenseRepository.sumAllExpenses(userID, firstDayOfCurrentWeek);
        basicStatsDTO.setTotal(ObjectUtils.isEmpty(total) ? 0 : total);
        basicStatsDTO.setCount(ObjectUtils.isEmpty(count) ? 0 : count);
        basicStatsDTO.setTotalForCurrentWeek(ObjectUtils.isEmpty(totalForCurrentWeek) ? 0 : totalForCurrentWeek);
        basicStatsDTO.setCountForCurrentWeek(ObjectUtils.isEmpty(countForCurrentWeek) ? 0 : countForCurrentWeek);
    }

    private void setStatsForAllExpensesByPlace(ExpenseBasicStatsDTO basicStatsDTO,
                                               Long userID,
                                               LocalDate firstDayOfCurrentMonth,
                                               LocalDate firstDayOfCurrentWeek,
                                               PlaceType placeType) {
        Float total = expenseRepository.sumAllExpenses(userID, firstDayOfCurrentMonth, placeType.toString());
        Integer count = expenseRepository.countAllExpenses(userID, firstDayOfCurrentMonth, placeType.toString());
        Integer countForCurrentWeek = expenseRepository.countAllExpenses(userID, firstDayOfCurrentWeek, placeType.toString());
        Float totalForCurrentWeek = expenseRepository.sumAllExpenses(userID, firstDayOfCurrentWeek, placeType.toString());
        basicStatsDTO.setTotal(ObjectUtils.isEmpty(total) ? 0 : total);
        basicStatsDTO.setCount(ObjectUtils.isEmpty(count) ? 0 : count);
        basicStatsDTO.setTotalForCurrentWeek(ObjectUtils.isEmpty(totalForCurrentWeek) ? 0 : totalForCurrentWeek);
        basicStatsDTO.setCountForCurrentWeek(ObjectUtils.isEmpty(countForCurrentWeek) ? 0 : countForCurrentWeek);
    }

}

package fr.b4.apps.expenses.web;

import fr.b4.apps.expenses.dto.ExpenseDTO;
import fr.b4.apps.expenses.dto.ExpenseInfoDTO;
import fr.b4.apps.expenses.process.ExpenseProcess;
import fr.b4.apps.expenses.web.interfaces.IExpenseController;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

import static fr.b4.apps.expenses.util.converters.ExpenseConverter.toDTO;


@CrossOrigin("*")
@RequestMapping("expenses")
@RestController
public class ExpenseController implements IExpenseController {

    private final ExpenseProcess expenseProcess;

    public ExpenseController(ExpenseProcess expenseProcess) {
        this.expenseProcess = expenseProcess;
    }

    @PostMapping
    public ExpenseDTO save(@RequestParam(value = "file", required = false) MultipartFile file,
                           @RequestParam(value = "expense", required = true) String expenseStr) throws IOException {
        return toDTO(expenseProcess.save(expenseStr, file));
    }

    @GetMapping("/info")
    @Override
    public ExpenseInfoDTO get(@RequestHeader("access-token") String accessToken) {
        return expenseProcess.getInfo(accessToken);
    }

    @GetMapping
    public List<ExpenseDTO> getAll(@RequestHeader("access-token") String accessToken,
                                   @RequestParam(value = "page", required = false) Integer page,
                                   @RequestParam(value = "size", required = false) Integer size) {
        return expenseProcess.findByUserID(accessToken, page, size);
    }


    @GetMapping("/{id}")
    public ExpenseDTO getByID(@PathVariable("id") String id) {
        return expenseProcess.find(Long.valueOf(id));
    }


    @GetMapping("/place/{placeID}")
    public List<ExpenseDTO> getByPlace(@RequestHeader("access-token") String accessToken,
                                       @PathVariable("placeID") String placeID) {
        return expenseProcess.findByPlaceID(accessToken, placeID);
    }

    @DeleteMapping("/{expenseID}")
    public void delete(@PathVariable("expenseID") Long expenseID) {
        expenseProcess.delete(expenseID);
    }

}

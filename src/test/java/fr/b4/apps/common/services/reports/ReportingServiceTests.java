package fr.b4.apps.common.services.reports;

import fr.b4.apps.DataGenerator;
import fr.b4.apps.clients.entities.User;
import fr.b4.apps.clients.repositories.UserRepository;
import fr.b4.apps.expenses.entities.Expense;
import fr.b4.apps.expenses.process.ExpenseProcess;
import fr.b4.apps.expenses.services.ExpenseService;
import fr.b4.apps.expenses.util.converters.ExpenseConverter;
import org.apache.poi.ss.usermodel.Workbook;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.TestPropertySource;

import javax.mail.Session;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.io.IOException;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;


@RunWith(MockitoJUnitRunner.class)
@TestPropertySource(locations = "classpath:application.properties")
public class ReportingServiceTests {

    @Mock
    private ExpenseProcess expenseProcess;

    @Mock
    private ExpenseService expenseService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private JavaMailSender emailSender;

    @Before
    public void setUp() {
        MimeMessage mimeMessage = new MimeMessage((Session)null);
        emailSender = Mockito.mock(JavaMailSender.class);
        Mockito.when(emailSender.createMimeMessage()).thenReturn(mimeMessage);
        Mockito.when(expenseProcess.getBasicStats(any())).thenReturn(DataGenerator.generateBasicStats());
    }

    @Test
    public void shouldExportExcelFile() throws IOException {
        List<User> users = DataGenerator.generateUser(1);
        List<Expense> expenses = DataGenerator.generateExpenses(1);
        expenses.get(0).setExpenseLines(DataGenerator.generateExpenseLines(1));

        String reportPath = "" + "report_" + users.get(0).getId() + ".xlsx";
        File reportFile = new File(reportPath);
        reportFile.createNewFile();

        Mockito.when(userRepository.findAll()).thenReturn(users);
        Mockito.when(expenseService.findByUser(any(), any(), any())).thenReturn(ExpenseConverter.toDTO(expenses));
        Mockito.doNothing().when(emailSender).send(any(MimeMessage.class));
        ReportingService reportingService = new ReportingService(expenseProcess, expenseService, userRepository, emailSender);

        reportingService.exportExcel();

        Mockito.verify(expenseService).findByUser(users.get(0).getId(), 0, null);
        Mockito.verify(expenseProcess).getBasicStats(any());
        Mockito.verify(emailSender).createMimeMessage();
        Assertions.assertDoesNotThrow(reportingService::exportExcel);
        reportFile.delete();
    }


    @Test
    public void shouldHandleReportSendErrorSuccess() {

        List<User> users = DataGenerator.generateUser(1);
        List<Expense> expenses = DataGenerator.generateExpenses(1);
        expenses.get(0).setExpenseLines(DataGenerator.generateExpenseLines(1));
        Mockito.when(userRepository.findAll()).thenReturn(users);
        Mockito.when(expenseService.findByUser(any(), any(), any())).thenReturn(ExpenseConverter.toDTO(expenses));
        Mockito.doThrow(new MailSendException("ok")).when(emailSender).send(any(MimeMessage.class));
        ReportingService reportingService = new ReportingService(expenseProcess, expenseService, userRepository, emailSender);
        Assertions.assertDoesNotThrow(reportingService::exportExcel);
        Mockito.verify(expenseService).findByUser(users.get(0).getId(), 0, null);
    }

    @Test
    public void shouldHandleReportWriteErrorSuccess() throws IOException {
        List<User> users = DataGenerator.generateUser(1);
        List<Expense> expenses = DataGenerator.generateExpenses(1);
        expenses.get(0).setExpenseLines(DataGenerator.generateExpenseLines(1));
        Workbook workbook = Mockito.mock(Workbook.class);
        Mockito.when(userRepository.findAll()).thenReturn(users);
        Mockito.when(expenseService.findByUser(any(), any(), any())).thenReturn(ExpenseConverter.toDTO(expenses));
        Mockito.doNothing().when(emailSender).send(any(MimeMessage.class));

        ReportingService reportingService = new ReportingService(expenseProcess, expenseService, userRepository, emailSender);
        Assertions.assertDoesNotThrow(reportingService::exportExcel);
        Mockito.verify(expenseService).findByUser(users.get(0).getId(), 0, null);
    }
}

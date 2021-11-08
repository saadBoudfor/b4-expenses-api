package fr.b4.apps.common.services.excel;

import fr.b4.apps.clients.repositories.UserRepository;
import fr.b4.apps.common.services.email.GmailService;
import fr.b4.apps.common.services.email.TemplateProvider;
import fr.b4.apps.expenses.dto.ExpenseBasicStatsDTO;
import fr.b4.apps.expenses.dto.ExpenseDTO;
import fr.b4.apps.expenses.entities.Expense;
import fr.b4.apps.expenses.process.ExpenseProcess;
import fr.b4.apps.expenses.services.ExpenseService;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailSendException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import javax.mail.MessagingException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import static fr.b4.apps.common.services.excel.ExpenseExcelWriter.defineHeaders;
import static fr.b4.apps.common.services.excel.ExpenseExcelWriter.writeExpenses;

@Slf4j
@Component
public class ExcelService {

    @Value("${working.dir}")
    private String workingDir;

    private final ExpenseProcess expenseProcess;
    private final ExpenseService expenseService;
    private final GmailService gmailService;
    private final UserRepository userRepository;

    public ExcelService(ExpenseProcess expenseProcess, ExpenseService expenseService, GmailService gmailService, UserRepository userRepository) {
        this.expenseProcess = expenseProcess;
        this.expenseService = expenseService;
        this.gmailService = gmailService;
        this.userRepository = userRepository;
    }

//    @PostConstruct
//    @Scheduled(cron = "0 0 6 * * *")
    public void exportExcel() {

        userRepository.findAll().forEach(user -> {
            List<ExpenseDTO> expenses = expenseService.findByUser(user.getId(), 0, null);
            if (!CollectionUtils.isEmpty(expenses)) {
                Workbook workbook = new XSSFWorkbook();
                Sheet sheet = workbook.createSheet("Dépenses");
                sheet.setColumnWidth(0, 6000);
                sheet.setColumnWidth(1, 4000);

                defineHeaders(workbook, sheet);
                writeExpenses(expenses, workbook, sheet);

                try {
                    log.info("saving report for user {}", user.getId());
                    String reportPath = workingDir + "/report_" + user.getId() + "_" + LocalDate.now().toString().replaceAll("/", "_") + ".xlsx";
                    FileOutputStream outputStream = new FileOutputStream(reportPath);
                    workbook.write(outputStream);
                    workbook.close();

                    ExpenseBasicStatsDTO info = expenseProcess.getBasicStats(user.getId());
                    log.info("sending report for: {}", user.getEmail());
                    gmailService.sendMailWithAttachment(user.getEmail(),
                            "Rapport dépense " + LocalDate.now(),
                            TemplateProvider.getReportMailTemplate(user.getName(), info.getTarget().toString(), info.getTotal().toString(), LocalDate.now().toString()),
                            "report.xlsx",
                            reportPath);

                } catch (IOException e) {
                    log.error("failed to save excel report file for user {}, error {}", user.getId(), e.getMessage());
                } catch (MailSendException | MessagingException e) {
                    log.error("failed to send report email for user {}, error : {} ", user.getId(), e.getMessage());
                }
            }

        });


    }


}

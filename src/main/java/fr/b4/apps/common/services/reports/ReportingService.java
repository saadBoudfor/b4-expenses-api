package fr.b4.apps.common.services.reports;

import fr.b4.apps.clients.entities.User;
import fr.b4.apps.clients.repositories.UserRepository;
import fr.b4.apps.common.services.reports.utils.TemplateProvider;
import fr.b4.apps.expenses.dto.ExpenseBasicStatsDTO;
import fr.b4.apps.expenses.dto.ExpenseDTO;
import fr.b4.apps.expenses.process.ExpenseProcess;
import fr.b4.apps.expenses.services.ExpenseService;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import static fr.b4.apps.common.services.reports.utils.ExpenseExcelWriter.*;

@Slf4j
@Component
public class ReportingService {

    @Value("${working.dir}")
    private String workingDir = "";

    private final ExpenseProcess expenseProcess;
    private final ExpenseService expenseService;
    private final UserRepository userRepository;
    private final JavaMailSender emailSender;

    public ReportingService(ExpenseProcess expenseProcess,
                            ExpenseService expenseService,
                            UserRepository userRepository,
                            JavaMailSender emailSender) {
        this.expenseProcess = expenseProcess;
        this.expenseService = expenseService;
        this.userRepository = userRepository;
        this.emailSender = emailSender;
    }


    @Scheduled(cron = "0 0 6 * * *")
    public void exportExcel() {

        userRepository.findAll().forEach(user -> {
            List<ExpenseDTO> expenses = expenseService.find(user.getId(), 0, null);
            if (!CollectionUtils.isEmpty(expenses)) {
                Workbook workbook = new XSSFWorkbook();
                Sheet sheet = workbook.createSheet("Dépenses");
                sheet.setColumnWidth(0, 6000);
                sheet.setColumnWidth(1, 4000);

                defineHeaders(workbook, sheet);
                writeExpenses(expenses, workbook, sheet);

                try {
                    log.info("saving report for user {}", user.getId());
                    String reportPath = workingDir + "report_" + user.getId() + ".xlsx";
                    FileOutputStream outputStream = new FileOutputStream(reportPath);
                    workbook.write(outputStream);
                    workbook.close();
                    MimeMessage mimeMessage = getExpenseReportMail(user, reportPath);
                    emailSender.send(mimeMessage);
                } catch (IOException e) {
                    log.error("failed to save excel report file for user {}, error {}", user.getId(), e.getMessage());
                } catch (MailSendException | MessagingException e) {
                    log.error("failed to send report email for user {}, error : {} ", user.getId(), e.getMessage());
                }
            }
        });

    }

    private MimeMessage getExpenseReportMail(User user, String pathToAttachment) throws MessagingException, IOException {
        ExpenseBasicStatsDTO info = expenseProcess.getBasicStats(user.getId());
        log.info("sending report for: {}", user.getEmail());
        String mailBody = TemplateProvider.getReportMailTemplate(user.getName(),
                info.getTarget().toString(),
                info.getTotal().toString(),
                LocalDate.now().toString());

        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setFrom("noreplay@b4expenses.com");
        helper.setTo(user.getEmail());
        helper.setSubject("Rapport dépense " + LocalDate.now());
        helper.setText(mailBody, true);

        FileSystemResource file = new FileSystemResource(new File(pathToAttachment));
        helper.addAttachment("report.xlsx", file);

        return message;
    }


}

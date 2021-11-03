package fr.b4.apps.expenses.fr.b4.apps.common.services.email;

import fr.b4.apps.common.services.email.TemplateProvider;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.LocalDate;

public class TemplateProviderTest {


    @Test
    @DisplayName("test template generation")
    void shouldReturnMailTemplate() throws IOException {
        String today =  LocalDate.now().toString();
        String mail = TemplateProvider.getReportMailTemplate("saad", "32.32", "15.60", today);
        Assertions.assertTrue(mail.contains("saad"));
        Assertions.assertTrue(mail.contains("32.32"));
        Assertions.assertTrue(mail.contains("15.60"));
        Assertions.assertTrue(mail.contains(today));
    }
}

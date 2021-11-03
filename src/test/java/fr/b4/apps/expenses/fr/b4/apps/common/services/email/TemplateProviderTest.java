package fr.b4.apps.expenses.fr.b4.apps.common.services.email;

import fr.b4.apps.common.services.email.TemplateProvider;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.LocalDate;

public class TemplateProviderTest {


    @Test
    void shouldReturnMailTemplate() throws IOException {
        String mail = TemplateProvider.getReportMailTemplate("saad", "32.32", "15.60", LocalDate.now().toString());
        Assertions.assertTrue(mail.contains("saad"));
        Assertions.assertTrue(mail.contains("32.32"));
        Assertions.assertTrue(mail.contains("15.60"));
    }
}

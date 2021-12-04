package fr.b4.apps.common.services.reports.utils;

import org.junit.Test;
import org.junit.jupiter.api.Assertions;

import java.io.IOException;

public class TemplateProviderIts {

    @Test
    public void shouldBuildHTMLReportSuccess() throws IOException {
        // Given - when:
        Assertions.assertNotNull(TemplateProvider.getReportMailTemplate("boudfor", "25.3", "52", "2018-01-01"));
        Assertions.assertEquals("<h1>boudfor 25.3 52 2018-01-01</h1>",
                TemplateProvider.getReportMailTemplate("boudfor", "25.3", "52", "2018-01-01"));
    }

    @Test
    public void shouldThrowExceptionIFInputsIllegals() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> TemplateProvider.getReportMailTemplate(null, null, null, ""));
    }
}

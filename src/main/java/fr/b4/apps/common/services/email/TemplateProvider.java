package fr.b4.apps.common.services.email;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class TemplateProvider {
    public static String getReportMailTemplate(String name, String target, String total, String date) throws IOException {

        Resource resource = new ClassPathResource("static/report.html");
        File file = resource.getFile();
        String report = new String(
                Files.readAllBytes(file.toPath()));
        return report.replace("name", name)
                .replace("target", target)
                .replace("total", total)
                .replace("date", date);
    }
}

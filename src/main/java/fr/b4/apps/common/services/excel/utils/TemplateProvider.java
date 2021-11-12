package fr.b4.apps.common.services.excel.utils;

import lombok.experimental.UtilityClass;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.io.InputStream;

@UtilityClass
public class TemplateProvider {
    public static String getReportMailTemplate(String name, String target, String total, String date) throws IOException {

        Resource resource = new ClassPathResource("static/report.html");
        InputStream inputStream = resource.getInputStream();
        String report = new String(inputStream.readAllBytes());
        return report.replace("name", name)
                .replace("target", target)
                .replace("total", total)
                .replace("date", date);
    }
}

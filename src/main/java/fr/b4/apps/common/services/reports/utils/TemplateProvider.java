package fr.b4.apps.common.services.reports.utils;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.io.InputStream;

@Component
public class TemplateProvider {
    public static String getReportMailTemplate(String name, String target, String total, String date) throws IOException, IllegalArgumentException {
        if (StringUtils.hasLength(name)
                && StringUtils.hasLength(target)
                && StringUtils.hasLength(total)
                && StringUtils.hasLength(date)) {
            final Resource resource = new ClassPathResource("static/report.html");
            final InputStream inputStream = resource.getInputStream();
            final String report = new String(inputStream.readAllBytes());
            return report.replace("name", name)
                    .replace("target", target)
                    .replace("total", total)
                    .replace("date", date);
        } else {
            throw new IllegalArgumentException("all inputs are required and cannot be null or empty");
        }
    }
}

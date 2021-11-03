package fr.b4.apps;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import fr.b4.apps.openfoodfact.apis.OpenFoodFactClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import javax.annotation.PostConstruct;
import javax.mail.MessagingException;
import java.io.File;
import java.io.IOException;
import java.util.Properties;
import java.util.function.Predicate;

@Slf4j
@SpringBootApplication
public class ExpensesApplication {

    @Autowired
    private OpenFoodFactClient openFoodFactClient;

    @Value("${products.photos.dir}")
    private String productPhotoDir;

    @Value("${expenses.photos.dir}")
    private String expensePhotoDir;

    @Autowired
    public ObjectMapper objectMapper;

    @Value("${enableCategoryUpdate}")
    public boolean enableCategoryUpdate;

    @Value("${app.version}")
    private String appVersion;

    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**").allowedMethods("PUT", "GET", "DELETE", "OPTIONS", "PATCH", "POST");
    }

    @Bean
    public JavaMailSender getJavaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost("smtp.gmail.com");
        mailSender.setPort(587);

        mailSender.setUsername("b4.expenses@gmail.com");
        mailSender.setPassword("Fifa2018*");

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.debug", "true");

        return mailSender;
    }

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .paths(Predicate.not(PathSelectors.regex("/error.*")))
                .build();
    }

    @PostConstruct
    public void setUp() throws IOException, MessagingException {
        log.info("B4 expense API version: " + appVersion);
        if (enableCategoryUpdate) {
            openFoodFactClient.updateProductCategories();
        }
        objectMapper.registerModule(new JavaTimeModule());
        createRequirementAssetsFolders();
    }

    private void createRequirementAssetsFolders() {
        createFolder(productPhotoDir);
        createFolder(expensePhotoDir);
    }

    private static void createFolder(String path) {
        File file = new File(path);
        if (!file.exists()) {
            boolean created = file.mkdirs();
            if (created) {
                log.info("create required folder: {}", path);
            }
        }
    }

    public static void main(String[] args) {
        SpringApplication.run(ExpensesApplication.class, args);
    }
}

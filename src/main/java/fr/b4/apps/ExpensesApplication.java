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
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import javax.annotation.PostConstruct;
import java.io.File;
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

    @Value("${app.version}")
    private String appVersion;

    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**").allowedMethods("PUT", "GET", "DELETE", "OPTIONS", "PATCH", "POST");
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
    public void setUp() {
        log.info("B4 expense API version: " + appVersion);
        openFoodFactClient.updateProductCategories();
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

package fr.b4.apps;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import fr.b4.apps.openfoodfact.apis.OpenFoodFactClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import javax.annotation.PostConstruct;
import java.util.function.Predicate;

@SpringBootApplication
public class ExpensesApplication {

	@Autowired
	private OpenFoodFactClient openFoodFactClient;

	@Autowired
	public ObjectMapper objectMapper;

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
//		openFoodFactClient.updateProductCategories();
		objectMapper.registerModule(new JavaTimeModule());
	}
	public static void main(String[] args) {
		SpringApplication.run(ExpensesApplication.class, args);
	}
}

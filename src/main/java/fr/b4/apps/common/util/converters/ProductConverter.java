package fr.b4.apps.common.util.converters;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.b4.apps.common.entities.Product;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ProductConverter {
    public static Product valueOf(String str) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(str, Product.class);
    }
}

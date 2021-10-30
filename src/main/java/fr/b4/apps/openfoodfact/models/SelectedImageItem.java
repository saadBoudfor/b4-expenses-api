package fr.b4.apps.openfoodfact.models;

import lombok.Data;
import org.springframework.util.ObjectUtils;

@Data
public class SelectedImageItem {

    private String en;

    private String fr;

    private String pl;

    public String getUrl() {
        return fr;
    }
}

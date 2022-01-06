package fr.b4.apps.expenses.util;

import fr.b4.apps.expenses.dto.NutrientStatDTO;
import fr.b4.apps.expenses.dto.NutrientStatRecapDTO;
import lombok.experimental.UtilityClass;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.math.BigInteger;
import java.time.LocalDate;
import java.time.temporal.TemporalField;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@UtilityClass
public class ExpenseUtils {

    public static LocalDate getFistDayOfCurrentWeek() {
        LocalDate now = LocalDate.now();
        TemporalField fieldISO = WeekFields.of(Locale.FRANCE).dayOfWeek();
        return now.with(fieldISO, 1);
    }

    public static LocalDate getFistDayOfCurrentMonth() {
        LocalDate now = LocalDate.now();
        return LocalDate.of(now.getYear(), now.getMonth(), 1);
    }

    public static NutrientStatRecapDTO extractStats(List<Object[]> data) {
        NutrientStatRecapDTO nutrientStatRecapDTO = new NutrientStatRecapDTO();
        List<NutrientStatDTO> list = new ArrayList<>();
        if (CollectionUtils.isEmpty(data)) {
            return nutrientStatRecapDTO;
        }
        for (Object[] item : data) {
            NutrientStatDTO dto = new NutrientStatDTO();
            dto.setLabel((String) item[0]);
            dto.setCount(((BigInteger) item[1]).toString());
            list.add(dto);
        }
        nutrientStatRecapDTO.setStats(list);
        return nutrientStatRecapDTO;
    }
}

package fr.b4.apps;

import com.github.javafaker.Faker;
import com.github.javafaker.Name;
import fr.b4.apps.clients.entities.User;
import fr.b4.apps.common.entities.*;
import fr.b4.apps.expenses.dto.ExpenseBasicStatsDTO;
import fr.b4.apps.expenses.dto.ExpenseDTO;
import fr.b4.apps.expenses.dto.ExpensePlaceDTO;
import fr.b4.apps.expenses.entities.Budget;
import fr.b4.apps.expenses.entities.Expense;
import fr.b4.apps.expenses.entities.ExpenseLine;
import fr.b4.apps.openfoodfact.models.LanguagesCodes;
import fr.b4.apps.openfoodfact.models.OFCategory;
import fr.b4.apps.openfoodfact.models.OFProduct;
import fr.b4.apps.stores.dto.BucketDTO;
import fr.b4.apps.stores.dto.ItemDTO;
import fr.b4.apps.stores.dto.StoreDTO;
import fr.b4.apps.stores.entities.Bucket;
import fr.b4.apps.stores.entities.Item;
import fr.b4.apps.stores.entities.Store;
import lombok.experimental.UtilityClass;

import java.math.BigInteger;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@UtilityClass
public class DataGenerator {
    private static final Faker faker = new Faker();


    public List<Category> generateCategories(int num) throws IllegalArgumentException {
        if (num <= 0) {
            throw new IllegalArgumentException("num must be > 0");
        }
        List<Category> categories = new ArrayList<>();
        for (int i = 0; i < num; i++) {
            categories.add(generateOneCategory(i));
        }
        return categories;
    }

    public Category generateOneCategory(int id) {
        Category category = new Category();
        category.setFr(faker.cat().name());
        category.setId(id + "");
        return category;
    }


    public OFCategory generateOneOFCategory(int id) {
        OFCategory ofCategory = new OFCategory();
        LanguagesCodes languagesCodes = new LanguagesCodes();
        languagesCodes.setFr(faker.cat().name());
        ofCategory.setName(languagesCodes);
        return ofCategory;
    }

    public ExpenseBasicStatsDTO generateBasicStats() {
        ExpenseBasicStatsDTO statsDTO = new ExpenseBasicStatsDTO();
        statsDTO.setCountForCurrentWeek(2);
        statsDTO.setTotal(5f);
        statsDTO.setTarget(6f);
        statsDTO.setTotalForCurrentWeek(8f);
        return statsDTO;
    }

    public Address generateOneAddress() {
        Address address = new Address();
        address.setStreet(faker.address().streetAddress());
        address.setCountry(faker.address().country());
        address.setZipCode(faker.address().zipCode());
        address.setCity(faker.address().city());
        return address;
    }

    public Place generateOnePlace(PlaceType type) {
        Place place = new Place();
        place.setAddress(generateOneAddress());
        place.setType(type);
        place.setName(faker.commerce().productName());
        return place;
    }

    public List<Place> generatePlaces(PlaceType placeType, int num) {
        List<Place> places = new ArrayList<>();
        for (int i = 0; i < num; i++) {
            places.add(generateOnePlace(placeType));
        }
        return places;
    }

    public ExpensePlaceDTO generateExpensePlace(PlaceType placeType) {
        ExpensePlaceDTO expensePlaceDTO = new ExpensePlaceDTO();
        expensePlaceDTO.setPlace(generateOnePlace(placeType));
        expensePlaceDTO.setCount(faker.number().numberBetween(1L, 9023456L));
        expensePlaceDTO.setCount(faker.number().numberBetween(1L, 6978546L));
        return expensePlaceDTO;
    }

    public List<ExpensePlaceDTO> generateExpensePlaces(PlaceType placeType, int num) {
        List<ExpensePlaceDTO> expensePlaceDTOS = new ArrayList<>();
        for (int i = 0; i < num; i++) {
            expensePlaceDTOS.add(generateExpensePlace(placeType));
        }
        return expensePlaceDTOS;
    }

    public Expense generateExpense() {
        Expense expense = new Expense();
        expense.setPlace(generateOnePlace(PlaceType.RESTAURANT));
        expense.setName("shopping with " + faker.artist().name());
        expense.setDate(convertToLocalDateViaInstant(faker.date().birthday(5, 80)));
        return expense;
    }

    public ExpenseDTO generateExpenseDTO(boolean withId) {
        ExpenseDTO expense = new ExpenseDTO();
        if (withId) {
            expense.setId((long) faker.number().numberBetween(1, 6));
        }
        expense.setPlace(generateOnePlace(PlaceType.RESTAURANT));
        expense.setName("shopping with " + faker.artist().name());
        expense.setDate(convertToLocalDateViaInstant(faker.date().birthday(5, 80)));
        return expense;
    }

    public List<Expense> generateExpenses(int num) {
        List<Expense> expenses = new ArrayList<>();
        for (int i = 0; i < num; i++) {
            expenses.add(generateExpense());
        }
        return expenses;
    }

    public List<Expense> generateExpensesForPlace(Place place, int num) {
        List<Expense> expenses = generateExpenses(num);
        expenses.forEach(expense -> {
            expense.setPlace(place);
            expense.setExpenseLines(generateExpenseLines(faker.number().numberBetween(1, 12)));
        });
        return expenses;
    }

    public OFProduct generateOneOFProduct() {
        OFProduct ofProduct = new OFProduct();
        ofProduct.setProductName(faker.food().ingredient());
        ofProduct.setImageFrontUrl(faker.letterify("one of product"));
        ofProduct.setCode(faker.code().asin());
        ofProduct.setProductQuantity(faker.number().numberBetween(1, 85) + "");
        ofProduct.setQuantity(faker.number().numberBetween(1, 85) + "L");
        ofProduct.setBrands(faker.commerce().productName());
        ofProduct.setNutritionDataPer((faker.number().numberBetween(1, 85) + " cal/100g"));
        ofProduct.setNutritionGrades("E");
        return ofProduct;
    }

    public List<OFProduct> generateOFProducts(int num) {
        List<OFProduct> products = new ArrayList<>();
        for (int i = 0; i < num; i++) {
            products.add(generateOneOFProduct());
        }
        return products;
    }

    public List<Object[]> generateExpensePlaceRawData(int num) {
        List<Object[]> objects = new ArrayList<>();
        for (int i = 0; i < num; i++) {
            Object[] data = new Object[3];
            data[0] = new BigInteger(faker.number().numberBetween(1, 789645L) + "");
            data[1] = Double.valueOf(faker.number().numberBetween(1, 789645L) + "");
            data[2] = new BigInteger(faker.number().numberBetween(1, 989645L) + "");
            objects.add(data);
        }
        return objects;
    }

    public static List<ExpenseLine> generateExpenseLines(int num) {
        List<ExpenseLine> expenseLines = new ArrayList<>();
        for (int i = 0; i < num; i++) {
            ExpenseLine expenseLine = new ExpenseLine();
            expenseLine.setProduct(generateProduct());
            expenseLine.setQuantity((float) faker.number().numberBetween(1, 6));
            expenseLine.setPrice((float) faker.number().numberBetween(1, 6));
            expenseLines.add(expenseLine);
        }
        return expenseLines;
    }

    public static Product generateProduct() {
        Product product = new Product();
        product.setQuantity(1f);
        product.setDataPer("100g");
        product.setQrCode(faker.code().ean8());
        product.setName(faker.food().ingredient());
        product.setCalories(faker.number().numberBetween(0, 1000));
        return product;
    }

    public static LocalDate convertToLocalDateViaInstant(Date dateToConvert) {
        return dateToConvert.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
    }

    public static List<Product> generateProducts(int num) {
        List<Product> products = new ArrayList<>();
        for (int i = 0; i < num; i++) {
            products.add(generateProduct());
        }
        return products;
    }

    public static User generateUser() {
        User user = new User();
        Name fakeUser = faker.name();
        user.setId((long) faker.number().numberBetween(1, 6));
        user.setLastname(fakeUser.lastName());
        user.setName(fakeUser.firstName());
        user.setUsername(fakeUser.username());
        user.setEmail(fakeUser.username() + "@gmail.com");
        return user;
    }

    public static List<User> generateUser(int number) {
        List<User> users = new ArrayList<>();
        for (int i = 0; i < number; i++) {
            users.add(generateUser());
        }
        return users;
    }

    public static List<String> generateStrings(int num) {
        List<String> names = new ArrayList<>();
        for (int i = 0; i < num; i++) {
            names.add(faker.cat().name());
        }
        return names;
    }

    public static NutrientLevels generateNutrientLevels() {
        NutrientLevels nutrientLevels = new NutrientLevels();
        nutrientLevels.setSugars(faker.number().numberBetween(1, 4568) + "");
        nutrientLevels.setSalt(faker.number().numberBetween(1, 4568) + "");
        nutrientLevels.setSaturatedFat(faker.number().numberBetween(1, 4568) + "");
        nutrientLevels.setFat(faker.number().numberBetween(1, 4568) + "");
        return nutrientLevels;
    }

    public static Budget generateBudget() {
        Budget budget = new Budget();
        budget.setUser(generateUser());
        budget.setTarget((float) faker.number().numberBetween(1, 6));
        budget.setDate(LocalDate.now());
        return budget;
    }

    public static List<Budget> generateBudgets(int num) {
        List<Budget> budgets = new ArrayList<>();
        for (int i = 0; i < num; i++) {
            budgets.add(generateBudget());
        }
        return budgets;
    }

    public static StoreDTO generateStoreDTO() {
        StoreDTO store = new StoreDTO();
        store.setId((long) faker.number().numberBetween(1, 6));
        store.setName(faker.pokemon().name());
        store.setOwner(generateUser());
        return store;
    }

    public static List<StoreDTO> generateStoreDTO(int num) {
        List<StoreDTO> budgets = new ArrayList<>();
        for (int i = 0; i < num; i++) {
            budgets.add(generateStoreDTO());
        }
        return budgets;
    }

    public static Store generateStore() {
        Store store = new Store();
        store.setId((long) faker.number().numberBetween(1, 6));
        store.setName(faker.pokemon().name());
        store.setOwner(generateUser());
        return store;
    }

    public static List<Store> generateStore(int num) {
        List<Store> stores = new ArrayList<>();
        for (int i = 0; i < num; i++) {
            stores.add(generateStore());
        }
        return stores;
    }

    public static BucketDTO generateBucketDTO() {
        BucketDTO dto = new BucketDTO();
        dto.setId((long) faker.number().numberBetween(1, 6));
        dto.setName(faker.pokemon().name());
        dto.setStore(generateStoreDTO());
        dto.setOwner(generateUser());
        return dto;
    }

    public static List<BucketDTO> generateBucketDTO(int num) {
        List<BucketDTO> buckets = new ArrayList<>();
        for (int i = 0; i < num; i++) {
            buckets.add(generateBucketDTO());
        }
        return buckets;
    }


    public static Bucket generateBucket() {
        Bucket bucket = new Bucket();
        bucket.setId((long) faker.number().numberBetween(1, 6));
        bucket.setName(faker.pokemon().name());
        bucket.setStore(generateStore());
        bucket.setOwner(generateUser());
        return bucket;
    }

    public static List<Bucket> generateBucket(int num) {
        List<Bucket> buckets = new ArrayList<>();
        for (int i = 0; i < num; i++) {
            buckets.add(generateBucket());
        }
        return buckets;
    }

    public static ItemDTO generateItemDTO(boolean withId) {
        ItemDTO item = new ItemDTO();
        if (withId) {
            item.setId((long) faker.number().numberBetween(1, 6));
        }
        item.setAuthor(generateUser());
        item.setExpense(generateExpenseDTO(true));
        item.setLocation(generateBucketDTO());
        item.setQuantity(faker.number().numberBetween(100, 200) + 0f);
        item.setRemaining(faker.number().numberBetween(1, 100) + 0f);
        return item;
    }

    public static List<ItemDTO> generateItemDTO(int num) {
        List<ItemDTO> itemDTOS = new ArrayList<>();
        for (int i = 0; i < num; i++) {
            itemDTOS.add(generateItemDTO(true));
        }
        return itemDTOS;
    }

    public static Item generateItem(boolean withId) {
        Item item = new Item();
        if (withId) {
            item.setId((long) faker.number().numberBetween(1, 6));
        }
        item.setAuthor(generateUser());
        item.setExpense(generateExpense());
        item.setLocation(generateBucket());
        item.setQuantity(faker.number().numberBetween(100, 200) + 0f);
        item.setRemaining(faker.number().numberBetween(1, 100) + 0f);
        return item;
    }

    public static List<Item> generateItem(int num) {
        List<Item> itemDTOS = new ArrayList<>();
        for (int i = 0; i < num; i++) {
            itemDTOS.add(generateItem(true));
        }
        return itemDTOS;
    }

}

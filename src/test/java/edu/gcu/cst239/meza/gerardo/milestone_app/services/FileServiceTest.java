package edu.gcu.cst239.meza.gerardo.milestone_app.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import com.fasterxml.jackson.databind.ObjectMapper;

import edu.gcu.cst239.meza.gerardo.milestone_app.dtos.ProductDto;
import edu.gcu.cst239.meza.gerardo.milestone_app.models.InventoryItem;
import edu.gcu.cst239.meza.gerardo.milestone_app.models.Product;

class FileServiceTest {

    @TempDir
    Path tempDirectory;

    private Path productsPath;
    private Path categoriesPath;

    private final FileService fileService = new FileService();

    @BeforeEach
    void setUp() throws IOException {
        productsPath = tempDirectory.resolve("products.json");
        categoriesPath = tempDirectory.resolve("categories.json");

        Files.writeString(categoriesPath, """
                [
                  {
                    "category": "tires",
                    "description": "Racing tires"
                  }
                ]
                """);
    }

    @Test
    void loadsJsonIntoDtoAndDomainProduct() throws IOException {
        Files.writeString(productsPath, validProductJson());

        ProductDto[] dtos = new ObjectMapper().readValue(
                productsPath.toFile(),
                ProductDto[].class);

        assertEquals(1, dtos.length);
        assertEquals("Soft Tires", dtos[0].name());
        assertEquals("2026-08-10", dtos[0].dateOfManufacture());

        List<InventoryItem> items = fileService.loadInventory(
                productsPath, categoriesPath);

        assertEquals(1, items.size());

        Product product = items.get(0).getProduct();

        assertEquals(1, product.getId());
        assertEquals("Soft Tires", product.getName());
        assertEquals("Four racing tires", product.getDescription());
        assertEquals(
                LocalDate.of(2026, 8, 10),
                product.getDateOfManufacture());
        assertEquals(2400.00, product.getPrice(), 0.001);
        assertEquals("tires", product.getCategory());
        assertEquals(10, items.get(0).getQuantityInStock());
    }

    @Test
    void loadsExpectedNumberOfControlledRecords() throws IOException {
        Files.writeString(productsPath, """
                [
                  {
                    "id": 1,
                    "name": "Soft Tires",
                    "description": "Maximum grip",
                    "dateOfManufacture": "2026-08-10",
                    "price": 2400.00,
                    "category": "tires"
                  },
                  {
                    "id": 2,
                    "name": "Hard Tires",
                    "description": "Long race stints",
                    "dateOfManufacture": "2026-08-12",
                    "price": 2200.00,
                    "category": "tires"
                  }
                ]
                """);

        List<InventoryItem> items = fileService.loadInventory(
                productsPath, categoriesPath);

        assertEquals(2, items.size());
        assertEquals(1, items.get(0).getProduct().getId());
        assertEquals(2, items.get(1).getProduct().getId());
    }

    @Test
    void preservesAllProductValuesDuringRoundTrip() throws IOException {
        Product original = Product.builder()
                .id(25)
                .name("Updated Medium Tires")
                .description("Updated racing tire description")
                .dateOfManufacture(LocalDate.of(2026, 7, 15))
                .price(2350.50)
                .category("tires")
                .build();

        List<InventoryItem> inventory = List.of(
                new InventoryItem(original, 4));

        fileService.saveInventory(
                inventory, productsPath, categoriesPath);

        List<InventoryItem> loaded = fileService.loadInventory(
                productsPath, categoriesPath);

        assertEquals(1, loaded.size());

        Product restored = loaded.get(0).getProduct();

        assertEquals(original.getId(), restored.getId());
        assertEquals(original.getName(), restored.getName());
        assertEquals(
                original.getDescription(),
                restored.getDescription());
        assertEquals(
                original.getDateOfManufacture(),
                restored.getDateOfManufacture());
        assertEquals(
                original.getPrice(),
                restored.getPrice(),
                0.001);
        assertEquals(original.getCategory(), restored.getCategory());

        // Stock is outside the product JSON contract.
        assertEquals(10, loaded.get(0).getQuantityInStock());
    }

    @Test
    void rejectsMalformedJson() throws IOException {
        Files.writeString(
                productsPath,
                "[ { \"id\": 1, broken JSON } ]");

        assertThrows(
                IOException.class,
                () -> fileService.loadInventory(
                        productsPath, categoriesPath));
    }

    @Test
    void rejectsUnknownCategory() throws IOException {
        Files.writeString(
                productsPath,
                validProductJson().replace(
                        "\"tires\"", "\"unknown\""));

        assertThrows(
                IllegalArgumentException.class,
                () -> fileService.loadInventory(
                        productsPath, categoriesPath));
    }

    @Test
    void rejectsDuplicateProductIds() throws IOException {
        String product = """
                {
                  "id": 1,
                  "name": "Soft Tires",
                  "description": "Four racing tires",
                  "dateOfManufacture": "2026-08-10",
                  "price": 2400.00,
                  "category": "tires"
                }
                """;

        Files.writeString(
                productsPath,
                "[" + product + "," + product + "]");

        assertThrows(
                IllegalArgumentException.class,
                () -> fileService.loadInventory(
                        productsPath, categoriesPath));
    }

    @Test
    void reportsMissingProductsFile() {
        assertThrows(
                IOException.class,
                () -> fileService.loadInventory(
                        productsPath, categoriesPath));
    }

    @Test
    void invalidSavePreservesExistingFile() throws IOException {
        String originalJson = validProductJson();
        Files.writeString(productsPath, originalJson);

        Product invalidProduct = Product.builder()
                .id(2)
                .name("Unknown Part")
                .description("Part with an unknown category")
                .dateOfManufacture(LocalDate.of(2026, 8, 10))
                .price(100.00)
                .category("unknown")
                .build();

        List<InventoryItem> inventory = List.of(
                new InventoryItem(invalidProduct, 5));

        assertThrows(
                IllegalArgumentException.class,
                () -> fileService.saveInventory(
                        inventory, productsPath, categoriesPath));

        assertEquals(
                originalJson,
                Files.readString(productsPath));
    }

    @Test
    void productionResourceContainsExactly100ValidProducts()
            throws IOException {

        Path productionProducts = Path.of(
                "src", "main", "resources", "products.json");

        Path productionCategories = Path.of(
                "src", "main", "resources", "categories.json");

        List<InventoryItem> items = fileService.loadInventory(
                productionProducts, productionCategories);

        assertEquals(100, items.size());
    }

    private String validProductJson() {
        return """
                [
                  {
                    "id": 1,
                    "name": "Soft Tires",
                    "description": "Four racing tires",
                    "dateOfManufacture": "2026-08-10",
                    "price": 2400.00,
                    "category": "tires"
                  }
                ]
                """;
    }
}
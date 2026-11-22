package edu.gcu.cst239.meza.gerardo.milestone_app.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import edu.gcu.cst239.meza.gerardo.milestone_app.models.InventoryItem;
import edu.gcu.cst239.meza.gerardo.milestone_app.models.Product;

class ProductCommandServiceTest {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private InventoryService inventoryService;
    private ProductCommandService commandService;

    @BeforeEach
    void setUp() {
        inventoryService = new InventoryManager(List.of(
                createItem(1, "Front Wing Assembly", 150_000.00, 4),
                createItem(2, "Brake Duct", 850.00, 8),
                createItem(3, "Soft Tire", 300.00, 12)));

        commandService = new ProductCommandService(inventoryService);
    }

    @Test
    void allReturnsEveryInventoryItem() throws Exception {
        JsonNode result = execute("ALL");

        assertTrue(result.isArray());
        assertEquals(3, result.size());
        assertEquals(1, result.get(0).path("product").path("id").asInt());
        assertEquals(2, result.get(1).path("product").path("id").asInt());
        assertEquals(3, result.get(2).path("product").path("id").asInt());
    }

    @Test
    void getReturnsRequestedItemWithStockAndManufactureDate() throws Exception {
        JsonNode result = execute("GET 1");

        assertTrue(result.isArray());
        assertEquals(1, result.size());

        JsonNode item = result.get(0);
        JsonNode product = item.path("product");

        assertEquals(1, product.path("id").asInt());
        assertEquals("Front Wing Assembly", product.path("name").asText());
        assertEquals("2026-08-10", product.path("dateOfManufacture").asText());
        assertEquals(4, item.path("quantityInStock").asInt());
    }

    @Test
    void getReturnsEmptyArrayForMissingId() throws Exception {
        JsonNode result = execute("GET 999");

        assertTrue(result.isArray());
        assertEquals(0, result.size());
    }

    @Test
    void findMatchesPartialProductNameIgnoringCase() throws Exception {
        JsonNode result = execute("FIND fRoNt WiNg");

        assertTrue(result.isArray());
        assertEquals(1, result.size());
        assertEquals("Front Wing Assembly",
                result.get(0).path("product").path("name").asText());
    }

    @Test
    void deleteRemovesItemAndReturnsUpdatedInventory() throws Exception {
        JsonNode result = execute("DELETE 2");

        assertTrue(result.isArray());
        assertEquals(2, result.size());
        assertEquals(1, result.get(0).path("product").path("id").asInt());
        assertEquals(3, result.get(1).path("product").path("id").asInt());
        assertNull(inventoryService.getInventoryItemByProductId(2));
    }

    @Test
    void sortNameReturnsItemsInAscendingNameOrder() throws Exception {
        JsonNode result = execute("SORT NAME");

        assertTrue(result.isArray());
        assertEquals(3, result.size());
        assertEquals(2, result.get(0).path("product").path("id").asInt());
        assertEquals(1, result.get(1).path("product").path("id").asInt());
        assertEquals(3, result.get(2).path("product").path("id").asInt());
    }

    @Test
    void sortPriceReturnsItemsInAscendingPriceOrder() throws Exception {
        JsonNode result = execute("SORT PRICE");

        assertTrue(result.isArray());
        assertEquals(3, result.size());
        assertEquals(3, result.get(0).path("product").path("id").asInt());
        assertEquals(2, result.get(1).path("product").path("id").asInt());
        assertEquals(1, result.get(2).path("product").path("id").asInt());
    }

    @Test
    void unknownCommandReturnsJsonError() throws Exception {
        JsonNode result = execute("BOGUS");

        assertTrue(result.isObject());
        assertEquals("Unknown command", result.path("error").asText());
    }

    @Test
    void malformedCommandReturnsJsonError() throws Exception {
        JsonNode result = execute("GET abc");

        assertTrue(result.isObject());
        assertTrue(result.path("error").isTextual());
        assertFalse(result.path("error").asText().isBlank());
    }

    private JsonNode execute(String command) throws Exception {
        return objectMapper.readTree(commandService.processMessage(command));
    }

    private InventoryItem createItem(int id, String name, double price, int quantity) {
        Product product = Product.builder()
                .id(id)
                .name(name)
                .dateOfManufacture(LocalDate.of(2026, 8, 10))
                .price(price)
                .build();

        return new InventoryItem(product, quantity);
    }
}
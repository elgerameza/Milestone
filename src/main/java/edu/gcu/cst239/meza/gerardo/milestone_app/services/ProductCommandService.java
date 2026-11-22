package edu.gcu.cst239.meza.gerardo.milestone_app.services;

import java.time.LocalDate;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import edu.gcu.cst239.meza.gerardo.milestone_app.models.InventoryItem;

/**
 * Processes text commands for the Product Store and returns JSON responses.
 */
public class ProductCommandService {
    private final InventoryService inventoryService;
    private final ObjectMapper objectMapper;

    /**
     * Creates a command service backed by the supplied inventory service.
     *
     * @param inventoryService the service that manages the store inventory
     */
    public ProductCommandService(InventoryService inventoryService) {
        this.inventoryService = Objects.requireNonNull(inventoryService);

        SimpleModule dateModule = new SimpleModule();
        dateModule.addSerializer(LocalDate.class, ToStringSerializer.instance);
        this.objectMapper = new ObjectMapper().registerModule(dateModule);
    }

    /**
     * Executes an inventory command and serializes its result.
     *
     * @param message the command and any required argument
     * @return a JSON array of inventory items or a JSON error object
     */
    public String processMessage(String message) {
        if (message == null || message.isBlank()) {
            return error("Command is required");
        }

        String[] parts = message.strip().split("\\s+", 2);
        String command = parts[0].toUpperCase(Locale.ROOT);
        String argument = parts.length == 2 ? parts[1] : "";

        try {
            return switch (command) {
                case "ALL" -> processAll(argument);
                case "GET" -> processGet(argument);
                case "FIND" -> processFind(argument);
                case "DELETE" -> processDelete(argument);
                case "SORT" -> processSort(argument);
                default -> error("Unknown command");
            };
        } catch (NumberFormatException ex) {
            return error("Product ID must be a valid integer");
        }
    }

    private String processAll(String argument) {
        if (!argument.isEmpty()) {
            return error("Usage: ALL");
        }

        return json(inventoryService.getAllInventoryItems());
    }

    private String processGet(String argument) {
        if (argument.isEmpty()) {
            return error("Usage: GET id");
        }

        int productId = Integer.parseInt(argument);
        InventoryItem item = inventoryService.getInventoryItemByProductId(productId);
        return json(item == null ? List.of() : List.of(item));
    }

    private String processFind(String argument) {
        if (argument.isEmpty()) {
            return error("Usage: FIND text");
        }

        return json(inventoryService.searchProductsByName(argument));
    }

    private String processDelete(String argument) {
        if (argument.isEmpty()) {
            return error("Usage: DELETE id");
        }

        int productId = Integer.parseInt(argument);
        inventoryService.removeProductById(productId);
        return json(inventoryService.getAllInventoryItems());
    }

    private String processSort(String argument) {
        return switch (argument.toUpperCase(Locale.ROOT)) {
            case "NAME" -> json(inventoryService.getInventoryItemsSortedByName());
            case "PRICE" -> json(inventoryService.getInventoryItemsSortedByPrice());
            default -> error("Usage: SORT NAME or SORT PRICE");
        };
    }

    private String error(String message) {
        return json(Map.of("error", message));
    }

    private String json(Object value) {
        try {
            return objectMapper.writeValueAsString(value);
        } catch (JsonProcessingException ex) {
            return "{\"error\":\"Unable to create JSON\"}";
        }
    }
}
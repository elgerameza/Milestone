package edu.gcu.cst239.meza.gerardo.milestone_app.services;

import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import edu.gcu.cst239.meza.gerardo.milestone_app.dtos.ProductCategoryDto;
import edu.gcu.cst239.meza.gerardo.milestone_app.dtos.ProductDto;
import edu.gcu.cst239.meza.gerardo.milestone_app.mappers.ProductMapper;
import edu.gcu.cst239.meza.gerardo.milestone_app.models.InventoryItem;

public class FileService {

    private final ObjectMapper objectMapper;
    private final ProductMapper productMapper;

    public FileService() {
        objectMapper = new ObjectMapper();

        objectMapper.enable(
                DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES);
        objectMapper.enable(
                DeserializationFeature.FAIL_ON_MISSING_CREATOR_PROPERTIES);
        objectMapper.enable(
                DeserializationFeature.FAIL_ON_TRAILING_TOKENS);

        productMapper = new ProductMapper();
    }

    public List<InventoryItem> loadInventory(
            Path productsPath,
            Path categoriesPath) throws IOException {

        Set<String> categories = loadCategories(categoriesPath);

        ProductDto[] products = objectMapper.readValue(
                productsPath.toFile(),
                ProductDto[].class);

        if (products == null) {
            throw new IllegalArgumentException(
                    "Products JSON must contain an array.");
        }

        List<InventoryItem> inventory = new ArrayList<>();
        Set<Integer> productIds = new HashSet<>();

        for (ProductDto dto : products) {
            validateProduct(dto, categories);

            if (!productIds.add(dto.id())) {
                throw new IllegalArgumentException(
                        "Duplicate product ID: " + dto.id());
            }

            InventoryItem item = new InventoryItem(
                    productMapper.toProduct(dto),
                    10);

            inventory.add(item);
        }

        return inventory;
    }

    private Set<String> loadCategories(
            Path categoriesPath) throws IOException {

        ProductCategoryDto[] definitions = objectMapper.readValue(
                categoriesPath.toFile(),
                ProductCategoryDto[].class);

        if (definitions == null || definitions.length == 0) {
            throw new IllegalArgumentException(
                    "At least one category definition is required.");
        }

        Set<String> categories = new HashSet<>();

        for (ProductCategoryDto dto : definitions) {
            if (dto == null
                    || dto.category() == null
                    || dto.category().isBlank()
                    || dto.description() == null
                    || dto.description().isBlank()) {

                throw new IllegalArgumentException(
                        "Each category needs a key and description.");
            }

            if (!categories.add(dto.category())) {
                throw new IllegalArgumentException(
                        "Duplicate category: " + dto.category());
            }
        }

        return categories;
    }

    private void validateProduct(
            ProductDto dto,
            Set<String> categories) {

        if (dto == null) {
            throw new IllegalArgumentException(
                    "Product entries cannot be null.");
        }

        if (dto.id() <= 0) {
            throw new IllegalArgumentException(
                    "Product ID must be positive.");
        }

        if (dto.name() == null || dto.name().isBlank()) {
            throw new IllegalArgumentException(
                    "Product name is required for ID " + dto.id());
        }

        if (dto.description() == null) {
            throw new IllegalArgumentException(
                    "Product description is required for ID " + dto.id());
        }

        if (!Double.isFinite(dto.price()) || dto.price() <= 0) {
            throw new IllegalArgumentException(
                    "Product price must be positive for ID " + dto.id());
        }

        if (dto.category() == null
                || !categories.contains(dto.category())) {

            throw new IllegalArgumentException(
                    "Unknown category for product ID " + dto.id()
                            + ": " + dto.category());
        }

        if (dto.dateOfManufacture() == null
                || !dto.dateOfManufacture()
                        .matches("\\d{4}-\\d{2}-\\d{2}")) {

            throw new IllegalArgumentException(
                    "Date must use yyyy-MM-dd for product ID "
                            + dto.id());
        }

        try {
            LocalDate.parse(dto.dateOfManufacture());
        } catch (DateTimeParseException exception) {
            throw new IllegalArgumentException(
                    "Invalid manufacture date for product ID "
                            + dto.id(),
                    exception);
        }
    }
}
package edu.gcu.cst239.meza.gerardo.milestone_app.services;

import java.io.IOException;
import java.nio.file.Path;
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
import edu.gcu.cst239.meza.gerardo.milestone_app.models.Product;

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
        ProductValidator validator = new ProductValidator(categories);

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
            validator.validate(dto);

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

    public void saveInventory(
            List<InventoryItem> inventory,
            Path productsPath,
            Path categoriesPath) throws IOException {

        if (inventory == null) {
            throw new IllegalArgumentException(
                    "Inventory is required.");
        }

        Set<String> categories = loadCategories(categoriesPath);
        ProductValidator validator = new ProductValidator(categories);

        List<ProductDto> products = new ArrayList<>();
        Set<Integer> productIds = new HashSet<>();

        for (InventoryItem item : inventory) {
            if (item == null) {
                throw new IllegalArgumentException(
                        "Inventory items cannot be null.");
            }

            Product product = item.getProduct();
            ProductDto dto = productMapper.toDto(product);

            validator.validate(dto);

            if (!productIds.add(dto.id())) {
                throw new IllegalArgumentException(
                        "Duplicate product ID: " + dto.id());
            }

            products.add(dto);
        }

        objectMapper.writerWithDefaultPrettyPrinter()
                .writeValue(productsPath.toFile(), products);
    }
}
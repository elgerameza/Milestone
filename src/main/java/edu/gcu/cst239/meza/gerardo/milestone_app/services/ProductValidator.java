package edu.gcu.cst239.meza.gerardo.milestone_app.services;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Set;

import edu.gcu.cst239.meza.gerardo.milestone_app.dtos.ProductDto;

public class ProductValidator {

    private final Set<String> categories;

    public ProductValidator(Set<String> categories) {
        if (categories == null || categories.isEmpty()) {
            throw new IllegalArgumentException(
                    "Known categories are required.");
        }

        this.categories = Set.copyOf(categories);
    }

    public void validate(ProductDto dto) {
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
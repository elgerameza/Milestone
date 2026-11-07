package edu.gcu.cst239.meza.gerardo.milestone_app.services;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;
import java.util.Set;

import org.junit.jupiter.api.Test;

import edu.gcu.cst239.meza.gerardo.milestone_app.dtos.ProductDto;
import edu.gcu.cst239.meza.gerardo.milestone_app.mappers.ProductMapper;
import edu.gcu.cst239.meza.gerardo.milestone_app.models.Product;

class ProductValidatorTest {

    private final ProductValidator validator = new ProductValidator(
            Set.of("tires", "power-unit", "aerodynamics"));

    @Test
    void acceptsKnownCategoryAndMapsProduct() {
        ProductDto dto = createDto(
                "2026-08-10", 2400.00, "tires");

        validator.validate(dto);
        Product product = new ProductMapper().toProduct(dto);

        assertEquals("tires", product.getCategory());
        assertEquals(
                LocalDate.of(2026, 8, 10),
                product.getDateOfManufacture());
    }

    @Test
    void rejectsUnknownCategory() {
        ProductDto dto = createDto(
                "2026-08-10", 2400.00, "unknown");

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> validator.validate(dto));

        assertEquals(
                "Unknown category for product ID 1: unknown",
                exception.getMessage());
    }

    @Test
    void rejectsMissingCategory() {
        ProductDto dto = createDto(
                "2026-08-10", 2400.00, null);

        assertThrows(
                IllegalArgumentException.class,
                () -> validator.validate(dto));
    }

    @Test
    void rejectsImpossibleDate() {
        ProductDto dto = createDto(
                "2026-02-30", 2400.00, "tires");

        assertThrows(
                IllegalArgumentException.class,
                () -> validator.validate(dto));
    }

    @Test
    void rejectsIncorrectDateFormat() {
        ProductDto dto = createDto(
                "08/10/2026", 2400.00, "tires");

        assertThrows(
                IllegalArgumentException.class,
                () -> validator.validate(dto));
    }

    @Test
    void acceptsValidLeapDay() {
        ProductDto dto = createDto(
                "2024-02-29", 2400.00, "tires");

        assertDoesNotThrow(() -> validator.validate(dto));
    }

    @Test
    void rejectsZeroPrice() {
        ProductDto dto = createDto(
                "2026-08-10", 0.00, "tires");

        assertThrows(
                IllegalArgumentException.class,
                () -> validator.validate(dto));
    }

    @Test
    void rejectsNegativePrice() {
        ProductDto dto = createDto(
                "2026-08-10", -100.00, "tires");

        assertThrows(
                IllegalArgumentException.class,
                () -> validator.validate(dto));
    }

    @Test
    void rejectsNonFinitePrices() {
        double[] prices = {
                Double.NaN,
                Double.POSITIVE_INFINITY,
                Double.NEGATIVE_INFINITY
        };

        for (double price : prices) {
            ProductDto dto = createDto(
                    "2026-08-10", price, "tires");

            assertThrows(
                    IllegalArgumentException.class,
                    () -> validator.validate(dto));
        }
    }

    @Test
    void rejectsNullProduct() {
        assertThrows(
                IllegalArgumentException.class,
                () -> validator.validate(null));
    }

    private ProductDto createDto(
            String date,
            double price,
            String category) {

        return new ProductDto(
                1,
                "Soft Compound Tire Set",
                "Four soft-compound racing tires",
                date,
                price,
                category);
    }
}
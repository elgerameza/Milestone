package edu.gcu.cst239.meza.gerardo.milestone_app.mappers;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

import edu.gcu.cst239.meza.gerardo.milestone_app.dtos.ProductDto;
import edu.gcu.cst239.meza.gerardo.milestone_app.models.Product;

class ProductMapperTest {

    private final ProductMapper mapper = new ProductMapper();

    @Test
    void convertsDtoToProduct() {
        ProductDto dto = new ProductDto(
                1,
                "Front Wing Assembly",
                "Adjustable racing front wing",
                "2026-08-10",
                150000.00,
                "aerodynamics");

        Product product = mapper.toProduct(dto);

        assertEquals(dto.id(), product.getId());
        assertEquals(dto.name(), product.getName());
        assertEquals(dto.description(), product.getDescription());
        assertEquals(
                LocalDate.of(2026, 8, 10),
                product.getDateOfManufacture());
        assertEquals(dto.price(), product.getPrice(), 0.001);
        assertEquals(dto.category(), product.getCategory());
    }

    @Test
    void convertsProductToDto() {
        Product product = Product.builder()
                .id(2)
                .name("Medium Compound Tire Set")
                .description("Four medium-compound racing tires")
                .dateOfManufacture(LocalDate.of(2026, 7, 15))
                .price(2300.00)
                .category("tires")
                .build();

        ProductDto dto = mapper.toDto(product);

        assertEquals(product.getId(), dto.id());
        assertEquals(product.getName(), dto.name());
        assertEquals(product.getDescription(), dto.description());
        assertEquals("2026-07-15", dto.dateOfManufacture());
        assertEquals(product.getPrice(), dto.price(), 0.001);
        assertEquals(product.getCategory(), dto.category());
    }
}
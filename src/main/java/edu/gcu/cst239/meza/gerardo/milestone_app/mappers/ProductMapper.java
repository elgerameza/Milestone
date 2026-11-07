package edu.gcu.cst239.meza.gerardo.milestone_app.mappers;

import java.time.LocalDate;

import edu.gcu.cst239.meza.gerardo.milestone_app.dtos.ProductDto;
import edu.gcu.cst239.meza.gerardo.milestone_app.models.Product;

public class ProductMapper {

    public Product toProduct(ProductDto dto) {
        if (dto == null) {
            throw new IllegalArgumentException("Product DTO is required.");
        }

        return Product.builder()
                .id(dto.id())
                .name(dto.name())
                .description(dto.description())
                .dateOfManufacture(
                        LocalDate.parse(dto.dateOfManufacture()))
                .price(dto.price())
                .category(dto.category())
                .build();
    }

    public ProductDto toDto(Product product) {
        if (product == null) {
            throw new IllegalArgumentException("Product is required.");
        }

        return new ProductDto(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getDateOfManufacture().toString(),
                product.getPrice(),
                product.getCategory());
    }
}
package edu.gcu.cst239.meza.gerardo.milestone_app.dtos;

public record ProductDto(
        int id,
        String name,
        String description,
        String dateOfManufacture,
        double price,
        String category) {
}
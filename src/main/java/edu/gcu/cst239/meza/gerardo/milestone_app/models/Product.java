package edu.gcu.cst239.meza.gerardo.milestone_app.models;


import java.time.LocalDate;
import java.util.Objects;

public class Product {
 
    private final int id;
    private final String name;
    private final String description;
    private final LocalDate dateOfManufacture;
    private final double price;
    private final String category;
 
    private Product(Builder builder) {
        id = builder.id;
        name = builder.name;
        description = builder.description;
        dateOfManufacture = builder.dateOfManufacture;
        price = builder.price;
        category = builder.category;
    }
 
    public static Builder builder() {
        return new Builder();
    }
 
    public int getId() {
        return id;
    }
 
    public String getName() {
        return name;
    }
 
    public String getDescription() {
        return description;
    }
 
    public LocalDate getDateOfManufacture() {
        return dateOfManufacture;
    }
 
    public double getPrice() {
        return price;
    }
 
    public String getCategory() {
        return category;
    }
 
    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", dateOfManufacture=" + dateOfManufacture +
                ", price=" + price +
                ", category='" + category + '\'' +
                '}';
    }
    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof Product product)) {
            return false;
        }
 
        return id == product.id;
    }
 
    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }
 
    public static class Builder {
 
        private int id;
        private String name;
        private String description;
        private LocalDate dateOfManufacture;
        private double price;
        private String category = "no-category";
 
        private Builder() {
        }
 
        public Builder id(int id) {
            this.id = id;
            return this;
        }
 
        public Builder name(String name) {
            this.name = name;
            return this;
        }
 
        public Builder description(String description) {
            this.description = description;
            return this;
        }
 
        public Builder dateOfManufacture(
                LocalDate dateOfManufacture) {
 
            this.dateOfManufacture = dateOfManufacture;
            return this;
        }
 
        public Builder price(double price) {
            this.price = price;
            return this;
        }
 
        public Builder category(String category) {
            this.category = category;
            return this;
        }
 
        public Product build() {
            validate();
            return new Product(this);
        }
 
        private void validate() {
            if (id < 1) {
                throw new IllegalStateException(
                        "Product id must be positive.");
            }
            if (name == null || name.isBlank()) {
                throw new IllegalStateException(
                        "Product name is required.");
            }
 
            if (description == null) {
                description = "";
            }
 
            Objects.requireNonNull(
                    dateOfManufacture,
                    "Date of manufacture is required.");
 
            if (price < 0) {
                throw new IllegalStateException(
                        "Product price cannot be negative.");
            }
 
            if (category == null || category.isBlank()) {
                category = "no-category";
            }
        }
    }
}


    
 



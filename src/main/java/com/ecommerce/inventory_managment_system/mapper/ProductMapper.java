package com.ecommerce.inventory_managment_system.mapper;

import com.ecommerce.inventory_managment_system.entities.Category;
import com.ecommerce.inventory_managment_system.entities.Product;
import com.ecommerce.inventory_managment_system.requests.ProductRequest;
import com.ecommerce.inventory_managment_system.responses.ProductResponse;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {


    public Product toEntity(final ProductRequest request) {
        return Product.builder()
                .name(request.getName())
                .reference(request.getReference())
                .description(request.getDescription())
                .price(request.getPrice())
                .alertThreshold(request.getAlertThreshold())
                .category(Category.builder()
                        .id(request.getCategoryId())
                        .build())
                .deleted(false)
                .build();
    }

    public ProductResponse toResponse(final Product product) {
        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .reference(product.getReference())
                .description(product.getDescription())
                .price(product.getPrice())
                .alertThreshold(product.getAlertThreshold())
                .categoryId(product.getCategory()
                        .getId())
                .categoryName(product.getCategory()
                        .getName())
                // .availableQuantity() to be later implemented
                .build();
    }
}

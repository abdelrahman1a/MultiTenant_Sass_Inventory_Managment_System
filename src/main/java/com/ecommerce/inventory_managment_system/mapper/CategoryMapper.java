package com.ecommerce.inventory_managment_system.mapper;

import com.ecommerce.inventory_managment_system.entities.Category;
import com.ecommerce.inventory_managment_system.requests.CategoryRequest;
import com.ecommerce.inventory_managment_system.responses.CategoryResponse;
import org.springframework.stereotype.Component;

@Component
public class CategoryMapper {

    public Category toEntity(final CategoryRequest request) {
        return Category.builder()
                .name(request.getName())
                .description(request.getDescription())
                .deleted(false)
                .build();
    }

    public CategoryResponse toResponse(final Category entity) {
        final int nbProduct = 0;// entity.getProducts() == null ? 0 : entity.getProducts().size();
        return CategoryResponse.builder()
                .id(entity.getId())
                .name(entity.getName())
                .description(entity.getDescription())
                .nbProducts(nbProduct)
                .build();
    }
}

package com.ecommerce.inventory_managment_system.services.impl;

import com.ecommerce.inventory_managment_system.common.PageResponse;
import com.ecommerce.inventory_managment_system.entities.Category;
import com.ecommerce.inventory_managment_system.exceptions.DuplicateResourceException;
import com.ecommerce.inventory_managment_system.mapper.CategoryMapper;
import com.ecommerce.inventory_managment_system.repositories.CategoryRepository;
import com.ecommerce.inventory_managment_system.requests.CategoryRequest;
import com.ecommerce.inventory_managment_system.responses.CategoryResponse;
import com.ecommerce.inventory_managment_system.services.CategoryService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    public void create(final CategoryRequest request) {

        // check if category already exists
        checkIfCategoryAlreadyExistsByName(request.getName());

        final Category entity = this.categoryMapper.toEntity(request);
        this.categoryRepository.save(entity);


    }
    private void checkIfCategoryAlreadyExistsByName(final String categoryName)
    {
        Optional<Category>  category = this.categoryRepository.findByNameIgnoreCase(categoryName);

        if(category.isPresent())
        {
            log.debug("Category already exists");
            throw new DuplicateResourceException("Category already exists");
        }
    }

    @Override
    public void update(final String id,final  CategoryRequest request) {
        // check if category already exists by ID
        final Optional<Category> existingCategory = this.categoryRepository.findById(id);
        if (existingCategory.isEmpty()) {
            log.debug("Category does not exist");
            throw new EntityNotFoundException("Category does Not exist");
        }

        // check if category already exists by name
        if(!existingCategory.get().getName().equalsIgnoreCase(request.getName())){
            checkIfCategoryAlreadyExistsByName(request.getName());
        }

        final Category categoryToUpdate = this.categoryMapper.toEntity(request);
        categoryToUpdate.setId(id);
        this.categoryRepository.save(categoryToUpdate);


    }

    @Override
    public PageResponse<CategoryResponse> findAll(final int page, final int size) {

        final PageRequest pageRequest = PageRequest.of(page , size);
        final Page<Category> categories = this.categoryRepository.findAll(pageRequest);
        final Page<CategoryResponse> categoryResponses = categories.map(this.categoryMapper::toResponse);
        return PageResponse.of(categoryResponses);

    }

    @Override
    public CategoryResponse findById(final String id) {
        return this.categoryRepository.findById(id)
                .map(this.categoryMapper::toResponse)
                .orElseThrow(() -> new EntityNotFoundException("Category does not exist"));
    }



    @Override
    public void delete(final String id) {
        final Category category = this.categoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Category does not exist"));
        this.categoryRepository.delete(category);
    }
}

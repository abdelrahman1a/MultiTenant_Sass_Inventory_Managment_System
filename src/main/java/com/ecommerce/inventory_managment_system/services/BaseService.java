package com.ecommerce.inventory_managment_system.services;

import com.ecommerce.inventory_managment_system.common.PageResponse;

public interface BaseService<I , O> {

    void create(final I request);

    void update(final String id  ,final I request);

    PageResponse<O> findAll(final int page , final int size);
    O findById(final String id);
    void delete(final String id);

}

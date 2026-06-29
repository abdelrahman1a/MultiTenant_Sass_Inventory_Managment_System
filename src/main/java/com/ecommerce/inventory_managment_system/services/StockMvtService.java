package com.ecommerce.inventory_managment_system.services;

import com.ecommerce.inventory_managment_system.common.PageResponse;
import com.ecommerce.inventory_managment_system.requests.StockMvtRequest;
import com.ecommerce.inventory_managment_system.responses.StockMvtResponse;

public interface StockMvtService extends BaseService<StockMvtRequest , StockMvtResponse>{

    PageResponse<StockMvtResponse> findAllByProductId(final String productId , final int page , final int size);
}

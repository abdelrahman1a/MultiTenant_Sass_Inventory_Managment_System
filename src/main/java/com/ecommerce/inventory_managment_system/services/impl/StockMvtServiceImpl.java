package com.ecommerce.inventory_managment_system.services.impl;

import com.ecommerce.inventory_managment_system.common.PageResponse;
import com.ecommerce.inventory_managment_system.entities.Product;
import com.ecommerce.inventory_managment_system.entities.StockMvt;
import com.ecommerce.inventory_managment_system.mapper.StockMvtMapper;
import com.ecommerce.inventory_managment_system.repositories.ProductRepository;
import com.ecommerce.inventory_managment_system.repositories.StockMvtRepository;
import com.ecommerce.inventory_managment_system.requests.StockMvtRequest;
import com.ecommerce.inventory_managment_system.responses.StockMvtResponse;
import com.ecommerce.inventory_managment_system.services.StockMvtService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class StockMvtServiceImpl implements StockMvtService {

    private final StockMvtRepository stockMvtRepository;
    private final ProductRepository productRepository;
    private final StockMvtMapper stockMvtMapper;


    @Override
    public PageResponse<StockMvtResponse> findAllByProductId(final String productId,final int page,final int size) {
        final PageRequest pageRequest = PageRequest.of(page, size);
        final Page<StockMvt> stockMvts = this.stockMvtRepository.findAllByProductId(productId, pageRequest);
        final Page<StockMvtResponse> stockMvtResponses = stockMvts.map(this.stockMvtMapper::toResponse);
        return PageResponse.of(stockMvtResponses);
    }

    @Override
    public void create(final StockMvtRequest request) {

        // check if product exists by id
        checkIfProductExistsById(request.getProductId());

        final StockMvt entity = this.stockMvtMapper.toEntity(request);
        entity.setDateMvt(LocalDate.now());
        this.stockMvtRepository.save(entity);

    }


    private void checkIfProductExistsById(final String productId) {
        final Optional<Product> product = this.productRepository.findById(productId);
        if (product.isEmpty()) {
            log.debug("Product does not exist");
            throw new EntityNotFoundException("Product does not exist");
        }
    }

    @Override
    public void update(final String id,final StockMvtRequest request) {
        final Optional<StockMvt> stockMvt = this.stockMvtRepository.findById(id);
        if(stockMvt.isEmpty())
        {
            log.debug("StockMvt does not exist");
            throw new EntityNotFoundException("StockMvt does not exist");
        }

        // check if product exists
        checkIfProductExistsById(request.getProductId());

        final StockMvt stockMvtToUpdate = this.stockMvtMapper.toEntity(request);
        stockMvtToUpdate.setDateMvt(LocalDate.now());
        stockMvtToUpdate.setId(id); // to tell hibernate it is the same id update not insert
        this.stockMvtRepository.save(stockMvtToUpdate);

    }

    @Override
    public PageResponse<StockMvtResponse> findAll(final int page,final  int size) {
        final PageRequest pageRequest = PageRequest.of(page, size);
        final Page<StockMvt> stockMvts = this.stockMvtRepository.findAll(pageRequest);
        final Page<StockMvtResponse> stockMvtResponses = stockMvts.map(this.stockMvtMapper::toResponse);
        return PageResponse.of(stockMvtResponses);
    }

    @Override
    public StockMvtResponse findById(final String id) {
        return this.stockMvtRepository.findById(id)
                .map(this.stockMvtMapper::toResponse)
                .orElseThrow(() -> new EntityNotFoundException("StockMvt does not exist"));
    }

    @Override
    public void delete(final String id) {
        final StockMvt stockMvt = this.stockMvtRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("StockMvt does not exist"));
        this.stockMvtRepository.delete(stockMvt);
    }
}

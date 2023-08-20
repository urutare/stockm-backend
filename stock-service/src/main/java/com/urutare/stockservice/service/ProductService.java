package com.urutare.stockservice.service;

import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsMutation;
import com.netflix.graphql.dgs.DgsQuery;
import com.netflix.graphql.dgs.InputArgument;
import com.urutare.stockservice.entities.Product;
import com.urutare.stockservice.exception.NotFoundException;
import com.urutare.stockservice.models.input.ProductInput;
import com.urutare.stockservice.models.response.PaginatedResponseDTO;
import com.urutare.stockservice.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.UUID;

@DgsComponent
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;

    @DgsQuery
    public Product getProduct(@InputArgument UUID id) {
        return productRepository.findById(id).orElseThrow(() -> new NotFoundException("Product not found"));
    }

    @DgsQuery
    public PaginatedResponseDTO<Product> getProducts(@InputArgument String keyword, @InputArgument Integer pageNumber, @InputArgument Integer pageSize, @InputArgument String sortBy, @InputArgument String sortDirection) {
        Sort.Direction direction = Sort.Direction.fromString(sortDirection);
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(direction, sortBy));
        Page<Product> productPage;

        if (keyword != null) {
            productPage = productRepository.searchByName(keyword, pageable);
        } else {
            productPage = productRepository.findAll(pageable);
        }
        return new PaginatedResponseDTO<>(productPage);
    }

    @DgsMutation
    public Product createProduct(@InputArgument ProductInput input) {
        Product product = new Product(input);
        return productRepository.save(product);
    }
}

package com.urutare.stockservice.controller;

import com.urutare.stockservice.aspect.RequiresRole;
import com.urutare.stockservice.entities.Destination;
import com.urutare.stockservice.exception.NotFoundException;
import com.urutare.stockservice.models.enums.UserRole;
import com.urutare.stockservice.models.response.PaginatedResponseDTO;
import com.urutare.stockservice.repository.DestinationRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class DestinationController {
    private final DestinationRepository destinationRepository;
    private final HttpServletRequest request;

    @QueryMapping
    public Destination getDestinationById(@Argument UUID id) {
        return destinationRepository.findById(id).orElseThrow(() -> new NotFoundException("Destination not found"));
    }

    @QueryMapping
    public PaginatedResponseDTO<Destination> getAllDestinations(@Argument String keyword,
                                                                @Argument Integer pageNumber,
                                                                @Argument Integer pageSize,
                                                                @Argument String sortBy,
                                                                @Argument String sortDirection) {
        Sort.Direction direction = Sort.Direction.fromString(sortDirection);
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(direction, sortBy));
        Page<Destination> destinationPage;

        if (keyword != null) {
            destinationPage = destinationRepository.searchByName(keyword, pageable);
        } else {
            destinationPage = destinationRepository.findAll(pageable);
        }
        return new PaginatedResponseDTO<>(destinationPage);
    }

    @MutationMapping
//    @RequiresRole(UserRole.ADMIN)
    public Destination createDestination(@Argument String name) {
//        UUID userId = UUID.fromString(request.getHeader("userId"));
        Destination destination = new Destination();
        destination.setName(name);
//        destination.setCreatedBy(userId);
        return destinationRepository.save(destination);
    }

    @MutationMapping
//    @RequiresRole(UserRole.ADMIN)
    public Destination updateDestination(@Argument UUID id, @Argument String name) {
//        UUID userId = UUID.fromString(request.getHeader("userId"));
        Destination destination = destinationRepository.findById(id).orElseThrow(() -> new NotFoundException("Destination not found"));
        destination.setName(name);
//        destination.setUpdatedBy(userId);
        return destinationRepository.save(destination);
    }

}

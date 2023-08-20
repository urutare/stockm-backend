package com.urutare.stockservice.service;

import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsMutation;
import com.netflix.graphql.dgs.DgsQuery;
import com.netflix.graphql.dgs.InputArgument;
import com.urutare.stockservice.entities.Destination;
import com.urutare.stockservice.exception.NotFoundException;
import com.urutare.stockservice.models.response.PaginatedResponseDTO;
import com.urutare.stockservice.repository.DestinationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.UUID;

@DgsComponent
@RequiredArgsConstructor
public class DestinationService {
    private final DestinationRepository destinationRepository;

    @DgsQuery
    public Destination getDestinationById(@InputArgument UUID id) {
        return destinationRepository.findById(id).orElseThrow(() -> new NotFoundException("Destination not found"));
    }

    @DgsQuery
    public PaginatedResponseDTO<Destination> getAllDestinations(@InputArgument String keyword, @InputArgument Integer pageNumber, @InputArgument Integer pageSize, @InputArgument String sortBy, @InputArgument String sortDirection) {
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

    @DgsMutation
//    @RequiresRole(UserRole.ADMIN)
    public Destination createDestination(@InputArgument String name, @RequestHeader UUID userId) {
        Destination destination = new Destination();
        destination.setName(name);
        destination.setCreatedBy(userId);
        return destinationRepository.save(destination);
    }

    @DgsMutation
//    @RequiresRole(UserRole.ADMIN)
    public Destination updateDestination(@InputArgument UUID id, @InputArgument String name, @RequestHeader UUID userId) {
        Destination destination = destinationRepository.findById(id).orElseThrow(() -> new NotFoundException("Destination not found"));
        destination.setName(name);
        destination.setUpdatedBy(userId);
        return destinationRepository.save(destination);
    }

    @DgsMutation
//    @RequiresRole(UserRole.ADMIN)
    public Destination deleteDestination(@InputArgument UUID id) {
        Destination destination = destinationRepository.findById(id).orElseThrow(() -> new NotFoundException("Destination not found"));
        destinationRepository.delete(destination);
        return destination;
    }

}

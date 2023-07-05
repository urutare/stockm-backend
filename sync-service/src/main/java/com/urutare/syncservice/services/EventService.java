package com.urutare.syncservice.services;

import com.urutare.syncservice.entities.Event;
import com.urutare.syncservice.repositories.EventRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepository;

    public void processEvent(Event event) {
        // Implement your logic for processing the event,
        // including OT transformation and conflict resolution

        // Example: Save the event to the database
        eventRepository.save(event);
    }

    // Add other service methods as per your requirements
}

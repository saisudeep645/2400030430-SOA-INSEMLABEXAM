
package com.example.tourist.service;

import com.example.tourist.model.Tourist;
import com.example.tourist.repository.TouristRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TouristService {

    private final TouristRepository repository;

    public TouristService(TouristRepository repository) {
        this.repository = repository;
    }

    public Tourist createTourist(Tourist tourist) {
        return repository.save(tourist);
    }

    public List<Tourist> getAllTourists() {
        return repository.findAll();
    }

    public Tourist updateTourist(
            Long id, Tourist updatedDetails) {

        Tourist existingTourist = repository.findById(id)
                .orElseThrow(
                    () -> new TouristNotFoundException(id));

        existingTourist.setName(
                updatedDetails.getName());

        existingTourist.setDestination(
                updatedDetails.getDestination());

        existingTourist.setContact(
                updatedDetails.getContact());

        return repository.save(existingTourist);
    }
}

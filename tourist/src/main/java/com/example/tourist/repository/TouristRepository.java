
package com.example.tourist.repository;

import com.example.tourist.model.Tourist;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TouristRepository
        extends JpaRepository<Tourist, Long> {
}

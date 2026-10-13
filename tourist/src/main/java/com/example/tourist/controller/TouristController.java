package com.example.tourist.controller;

import com.example.tourist.model.Tourist;
import com.example.tourist.service.TouristService;
import com.example.tourist.service.TouristNotFoundException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/tourists")
public class TouristController {

    private final TouristService service;

    @Value("${app.security.token}")
    private String validToken;

    // Constructor
    public TouristController(TouristService service) {
        this.service = service;
    }

    // 1. Create a tourist
    @PostMapping
    public ResponseEntity<Tourist> createTourist(
            @RequestBody Tourist tourist) {

        Tourist saved = service.createTourist(tourist);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(saved);
    }

    // 2. Get all tourists
    @GetMapping
    public List<Tourist> getAllTourists() {
        return service.getAllTourists();
    }

    // 3. Update a tourist after validating the token
    @PutMapping("/{id}")
    public ResponseEntity<?> updateTourist(
            @PathVariable Long id,
            @RequestHeader(
                    value = "Token_value",
                    required = false
            ) String token,
            @RequestBody Tourist tourist) {

        // Step 1: Validate the token
        if (token == null || !validToken.equals(token)) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of(
                            "message",
                            "Invalid or missing token"
                    ));
        }

        // Step 2: Temporarily simulate HTTP 500 for testing
        // Use ID -999 only when testing the error handler.
        if (id == -999L) {
            throw new RuntimeException("Testing HTTP 500");
        }

        try {
            // Step 3: Update the existing tourist
            Tourist updated = service.updateTourist(id, tourist);

            return ResponseEntity.ok(Map.of(
                    "message", "Tourist Updated Successfully",
                    "tourist", updated
            ));

        } catch (TouristNotFoundException e) {
            // Step 4: Tourist ID does not exist
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(Map.of(
                            "message",
                            e.getMessage()
                    ));

        } catch (Exception e) {
            // Step 5: Handle unexpected errors
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of(
                            "message",
                            "Internal Server Error"
                    ));
        }
    }

    // 4. Handle unexpected errors from controller endpoints
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleServerError(
            Exception e) {

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of(
                        "message",
                        "Internal Server Error"
                ));
    }
}
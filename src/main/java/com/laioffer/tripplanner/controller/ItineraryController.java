package com.laioffer.tripplanner.controller;

import com.laioffer.tripplanner.entity.ItineraryEntity;
import com.laioffer.tripplanner.model.*;
import com.laioffer.tripplanner.repository.UserRepository;
import com.laioffer.tripplanner.service.ItineraryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/itineraries")
public class ItineraryController {

    private final ItineraryService itineraryService;
    private final UserRepository userRepository;

    public ItineraryController(ItineraryService itineraryService, UserRepository userRepository) {
        this.itineraryService = itineraryService;
        this.userRepository = userRepository;
    }

    private Long getCurrentUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = (String) auth.getPrincipal();
        return userRepository.findByEmail(email).getId();
    }

    @PostMapping
    public ResponseEntity<CreateItineraryResponse> createItinerary(@RequestBody CreateItineraryRequest request) {
        try {
            Long userId = getCurrentUserId();

            ItineraryEntity itinerary = itineraryService.createItinerary(
                    userId,
                    request.cityId(),
                    request.title(),
                    request.durationDays()
            );

            CreateItineraryData data = new CreateItineraryData(
                    itinerary.id(),
                    itinerary.currentVersionId()
            );

            CreateItineraryResponse response = new CreateItineraryResponse(true, data, null);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            CreateItineraryResponse response = new CreateItineraryResponse(false, null, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping
    public ResponseEntity<ItineraryListResponse> getMyItineraries() {
        try {
            Long userId = getCurrentUserId();

            List<ItineraryEntity> itineraries = itineraryService.getMyItineraries(userId);

            List<ItineraryData> data = itineraries.stream()
                    .map(it -> new ItineraryData(
                            it.id(),
                            it.title(),
                            it.durationDays(),
                            it.cityId()
                    ))
                    .collect(Collectors.toList());

            ItineraryListResponse response = new ItineraryListResponse(true, data, null);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            ItineraryListResponse response = new ItineraryListResponse(false, null, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PutMapping("/{id}/duration")
    public ResponseEntity<UpdateDurationResponse> updateDuration(
            @PathVariable Long id,
            @RequestBody UpdateDurationRequest request) {
        try {
            Long userId = getCurrentUserId();

            boolean success = itineraryService.updateDuration(id, userId, request.durationDays());

            if (success) {
                UpdateDurationResponse response = new UpdateDurationResponse(true, "Duration updated", null);
                return ResponseEntity.ok(response);
            } else {
                UpdateDurationResponse response = new UpdateDurationResponse(false, null, "Itinerary not found or unauthorized");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }

        } catch (Exception e) {
            UpdateDurationResponse response = new UpdateDurationResponse(false, null, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
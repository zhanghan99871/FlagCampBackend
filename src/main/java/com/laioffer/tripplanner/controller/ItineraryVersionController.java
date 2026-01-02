package com.laioffer.tripplanner.controller;

import com.laioffer.tripplanner.model.ItineraryContentResponse;
import com.laioffer.tripplanner.model.UpdateItineraryContentRequest;
import com.laioffer.tripplanner.model.UpdateItineraryContentResponse;
import com.laioffer.tripplanner.service.ItineraryVersionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/itineraries")
public class ItineraryVersionController {

    private final ItineraryVersionService itineraryVersionService;

    public ItineraryVersionController(ItineraryVersionService itineraryVersionService) {
        this.itineraryVersionService = itineraryVersionService;
    }

    @GetMapping("/{id}/content")
    public ItineraryContentResponse getCurrentContent(@PathVariable Long id) {
        return new ItineraryContentResponse(
                true,
                itineraryVersionService.getCurrentContent(id),
                null
        );
    }

    @PutMapping("/{id}/content")
    public ResponseEntity<?> updateContent(
            @PathVariable("id") long itineraryId,
            @RequestBody UpdateItineraryContentRequest request
    ) {
        long newVersionId = itineraryVersionService.updateItineraryContent(itineraryId, request);
        return ResponseEntity.ok(new UpdateItineraryContentResponse(true, newVersionId, null));
    }
}


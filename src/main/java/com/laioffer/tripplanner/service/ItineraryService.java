package com.laioffer.tripplanner.service;

import com.laioffer.tripplanner.entity.ItineraryEntity;
import com.laioffer.tripplanner.repository.ItineraryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ItineraryService {

    private final ItineraryRepository itineraryRepository;

    public ItineraryService(ItineraryRepository itineraryRepository) {
        this.itineraryRepository = itineraryRepository;
    }

    public ItineraryEntity createItinerary(Long userId, Long cityId, String title, Integer durationDays) {
        ItineraryEntity itinerary = new ItineraryEntity(
                null,
                userId,
                cityId,
                title,
                durationDays,
                null,
                null,
                null
        );
        return itineraryRepository.save(itinerary);
    }

    public List<ItineraryEntity> getMyItineraries(Long userId) {
        return itineraryRepository.findByUserId(userId);
    }

    public boolean updateDuration(Long itineraryId, Long userId, Integer durationDays) {
        Optional<ItineraryEntity> itinerary = itineraryRepository.findByIdAndUserId(itineraryId, userId);
        if (itinerary.isEmpty()) {
            return false;
        }

        int updated = itineraryRepository.updateDurationDays(itineraryId, userId, durationDays.shortValue());
        return updated > 0;
    }
}
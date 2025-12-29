package com.laioffer.tripplanner.repository;

import com.laioffer.tripplanner.entity.ItineraryVersionEntity;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.ListCrudRepository;

import java.util.List;
import java.util.Optional;

public interface ItineraryVersionRepository extends ListCrudRepository<ItineraryVersionEntity, Long> {
    List<ItineraryVersionEntity> findByItineraryId(Long itineraryId);
    
    Optional<ItineraryVersionEntity> findByItineraryIdAndVersionNo(Long itineraryId, Integer versionNo);
    
    Optional<ItineraryVersionEntity> findByIdAndItineraryId(Long id, Long itineraryId);
    
    @Query("SELECT * FROM itinerary_versions WHERE itinerary_id = :itineraryId ORDER BY version_no DESC LIMIT 1")
    Optional<ItineraryVersionEntity> findLatestByItineraryId(Long itineraryId);
    
    @Query("SELECT MAX(version_no) FROM itinerary_versions WHERE itinerary_id = :itineraryId")
    Integer findMaxVersionNoByItineraryId(Long itineraryId);
}


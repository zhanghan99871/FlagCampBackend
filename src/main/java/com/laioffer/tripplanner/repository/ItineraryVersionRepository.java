package com.laioffer.tripplanner.repository;

import com.laioffer.tripplanner.entity.ItineraryVersionEntity;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.query.Param;

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

    @Modifying
    @Query("""
    INSERT INTO itinerary_versions
      (created_at, data, is_published, itinerary_id, name, updated_at, version_no)
    VALUES
      (:createdAt, CAST(:data AS jsonb), :isPublished, :itineraryId, :name, :updatedAt, :versionNo)
    """)
    void insertVersion(
            @Param("createdAt") java.time.Instant createdAt,
            @Param("data") String data,
            @Param("isPublished") Boolean isPublished,
            @Param("itineraryId") Long itineraryId,
            @Param("name") String name,
            @Param("updatedAt") java.time.Instant updatedAt,
            @Param("versionNo") Integer versionNo
    );
}



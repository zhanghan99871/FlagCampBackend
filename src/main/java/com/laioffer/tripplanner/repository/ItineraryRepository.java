package com.laioffer.tripplanner.repository;

import com.laioffer.tripplanner.entity.ItineraryEntity;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.ListCrudRepository;

import java.util.List;
import java.util.Optional;

public interface ItineraryRepository extends ListCrudRepository<ItineraryEntity, Long> {
    List<ItineraryEntity> findByUserId(Long userId);
    
    Optional<ItineraryEntity> findByIdAndUserId(Long id, Long userId);
    
    List<ItineraryEntity> findByCityId(Long cityId);
    
    @Modifying
    @Query("UPDATE itineraries SET duration_days = :durationDays, updated_at = NOW() WHERE id = :id AND user_id = :userId")
    int updateDurationDays(Long id, Long userId, Short durationDays);
    
    @Modifying
    @Query("UPDATE itineraries SET current_version_id = :currentVersionId, updated_at = NOW() WHERE id = :id")
    int updateCurrentVersionId(Long id, Long currentVersionId);
}


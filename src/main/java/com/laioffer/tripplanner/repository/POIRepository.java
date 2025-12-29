package com.laioffer.tripplanner.repository;

import com.laioffer.tripplanner.entity.POIEntity;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.ListCrudRepository;

import java.util.List;
import java.util.Optional;

public interface POIRepository extends ListCrudRepository<POIEntity, Long> {
    List<POIEntity> findByCityId(Long cityId);
    
    List<POIEntity> findByType(String type);
    
    List<POIEntity> findByCityIdAndType(Long cityId, String type);
    
    Optional<POIEntity> findByProviderAndProviderPoiId(String provider, String providerPoiId);
    
    @Query("SELECT * FROM pois WHERE city_id = :cityId AND type = :type AND rating >= :minRating ORDER BY rating DESC LIMIT :limit")
    List<POIEntity> findByCityIdAndTypeAndMinRating(Long cityId, String type, java.math.BigDecimal minRating, Integer limit);
    
    @Query("SELECT * FROM pois WHERE city_id = :cityId AND name ILIKE '%' || :keyword || '%' ORDER BY rating DESC LIMIT :limit")
    List<POIEntity> findByCityIdAndKeyword(Long cityId, String keyword, Integer limit);
    
    @Query("SELECT * FROM pois WHERE city_id = :cityId AND rating >= :minRating ORDER BY rating DESC LIMIT :limit")
    List<POIEntity> findByCityIdAndMinRating(Long cityId, java.math.BigDecimal minRating, Integer limit);
}


package com.laioffer.tripplanner.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.laioffer.tripplanner.entity.POIEntity;
import com.laioffer.tripplanner.model.POIDetailDto;
import com.laioffer.tripplanner.model.POISearchDto;
import com.laioffer.tripplanner.repository.POIRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class POIService {
    
    private final POIRepository poiRepository;
    private final ObjectMapper objectMapper;
    
    public POIService(POIRepository poiRepository, ObjectMapper objectMapper) {
        this.poiRepository = poiRepository;
        this.objectMapper = objectMapper;
    }
    
    public List<POISearchDto> searchPOIs(Long cityId, String keyword, String type, BigDecimal minRating, Integer limit) {
        List<POIEntity> pois;
        
        // Apply default limit if not provided
        int queryLimit = (limit != null && limit > 0) ? limit : 20;
        
        // Build query based on provided parameters
        if (cityId != null && keyword != null && !keyword.trim().isEmpty() && type != null && minRating != null) {
            // All parameters provided - use single SQL query with all conditions
            pois = poiRepository.findByCityIdAndTypeAndMinRatingAndKeyword(cityId, type, minRating, keyword, queryLimit);
        } else if (cityId != null && keyword != null && !keyword.trim().isEmpty() && minRating != null) {
            // cityId + keyword + minRating - use single SQL query
            pois = poiRepository.findByCityIdAndMinRatingAndKeyword(cityId, minRating, keyword, queryLimit);
        } else if (cityId != null && keyword != null && !keyword.trim().isEmpty()) {
            // cityId + keyword
            pois = poiRepository.findByCityIdAndKeyword(cityId, keyword, queryLimit);
        } else if (cityId != null && type != null && minRating != null) {
            // cityId + type + minRating
            pois = poiRepository.findByCityIdAndTypeAndMinRating(cityId, type, minRating, queryLimit);
        } else if (cityId != null && type != null) {
            // cityId + type
            pois = poiRepository.findByCityIdAndType(cityId, type);
            if (queryLimit > 0) {
                pois = pois.stream().limit(queryLimit).collect(Collectors.toList());
            }
        } else if (cityId != null && minRating != null) {
            // cityId + minRating
            pois = poiRepository.findByCityIdAndMinRating(cityId, minRating, queryLimit);
        } else if (cityId != null) {
            // cityId only
            pois = poiRepository.findByCityId(cityId);
            if (queryLimit > 0) {
                pois = pois.stream().limit(queryLimit).collect(Collectors.toList());
            }
        } else {
            // No filters - return empty list (search should require at least cityId)
            pois = List.of();
        }
        
        // Convert to DTOs
        return pois.stream()
                .map(this::toPOISearchDto)
                .collect(Collectors.toList());
    }
    
    public POIDetailDto getPOIById(Long poiId) {
        POIEntity poi = poiRepository.findById(poiId)
                .orElseThrow(() -> new RuntimeException("POI not found"));
        
        return toPOIDetailDto(poi);
    }
    
    private POISearchDto toPOISearchDto(POIEntity poi) {
        return new POISearchDto(
                poi.id(),
                poi.name(),
                poi.type(),
                poi.rating(),
                poi.lat(),
                poi.lng(),
                poi.address()
        );
    }
    
    private POIDetailDto toPOIDetailDto(POIEntity poi) {
        Map<String, Object> extraMap = parseExtraJson(poi.extra());
        
        return new POIDetailDto(
                poi.id(),
                poi.name(),
                poi.type(),
                poi.rating(),
                poi.lat(),
                poi.lng(),
                extraMap
        );
    }
    
    private Map<String, Object> parseExtraJson(String extraJson) {
        if (extraJson == null || extraJson.trim().isEmpty() || extraJson.trim().equals("{}")) {
            return new HashMap<>();
        }
        
        try {
            return objectMapper.readValue(extraJson, new TypeReference<Map<String, Object>>() {});
        } catch (Exception e) {
            // If parsing fails, return empty map
            return new HashMap<>();
        }
    }
}


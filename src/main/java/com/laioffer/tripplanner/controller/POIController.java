package com.laioffer.tripplanner.controller;

import com.laioffer.tripplanner.model.POIDetailDto;
import com.laioffer.tripplanner.model.POIDetailResponse;
import com.laioffer.tripplanner.model.POISearchDto;
import com.laioffer.tripplanner.model.POISearchResponse;
import com.laioffer.tripplanner.service.POIService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/pois")
public class POIController {
    
    private final POIService poiService;
    
    public POIController(POIService poiService) {
        this.poiService = poiService;
    }
    
    @GetMapping("/search")
    public ResponseEntity<POISearchResponse> searchPOIs(
            @RequestParam(required = false) Long cityId,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) BigDecimal minRating,
            @RequestParam(required = false) Integer limit
    ) {
        try {
            List<POISearchDto> results = poiService.searchPOIs(cityId, keyword, type, minRating, limit);
            POISearchResponse response = new POISearchResponse(true, results, null);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            POISearchResponse response = new POISearchResponse(false, List.of(), e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }
    
    @GetMapping("/{poiId}")
    public ResponseEntity<POIDetailResponse> getPOIDetail(@PathVariable Long poiId) {
        try {
            POIDetailDto poiDetail = poiService.getPOIById(poiId);
            POIDetailResponse response = new POIDetailResponse(true, poiDetail, null);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            POIDetailResponse response = new POIDetailResponse(false, null, e.getMessage());
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            POIDetailResponse response = new POIDetailResponse(false, null, e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }
}


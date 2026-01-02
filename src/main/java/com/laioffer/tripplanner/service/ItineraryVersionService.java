package com.laioffer.tripplanner.service;

import com.laioffer.tripplanner.entity.ItineraryVersionEntity;
import com.laioffer.tripplanner.model.ItineraryContentDTO;
import com.laioffer.tripplanner.model.UpdateItineraryContentRequest;
import com.laioffer.tripplanner.repository.ItineraryVersionRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ItineraryVersionService {

    private final ItineraryVersionRepository versionRepo;
    private final ObjectMapper objectMapper;

    public ItineraryVersionService(ItineraryVersionRepository versionRepo,
                                   ObjectMapper objectMapper) {
        this.versionRepo = versionRepo;
        this.objectMapper = objectMapper;
    }

    public ItineraryContentDTO getCurrentContent(Long itineraryId) {
        ItineraryVersionEntity v = versionRepo.findLatestByItineraryId(itineraryId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Version not found"));

        Object parsed;
        try {
            parsed = objectMapper.readValue(v.data(), Object.class);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Invalid JSON data");
        }

        return new ItineraryContentDTO(v.id(), v.versionNo(), parsed);
    }

    @Transactional
    public Long updateItineraryContent(
            Long itineraryId,
            UpdateItineraryContentRequest request
    ) {

        // 0. check
        if (request == null || request.getPois() == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "pois is required");
        }

        // 1. read the latest version
        ItineraryVersionEntity current = versionRepo
                .findLatestByItineraryId(itineraryId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Version not found"));

        // 2. read jason
        Map<String, Object> oldRoot;
        try {
            oldRoot = objectMapper.readValue(
                    current.data(),
                    new TypeReference<Map<String, Object>>() {}
            );
        } catch (Exception e) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, "Invalid JSON data");
        }

        // 3. keep original routes
        Object routesObj = oldRoot.get("routes");
        Map<String, Object> routes =
                (routesObj instanceof Map<?, ?> m)
                        ? (Map<String, Object>) m
                        : new LinkedHashMap<>();

        // 4. group by day and rank by order
        Map<Integer, List<UpdateItineraryContentRequest.PoiPlacement>> byDay =
                request.getPois().stream()
                        .filter(p ->
                                p.getPoiId() != null &&
                                        p.getDay() != null &&
                                        p.getOrder() != null
                        )
                        .collect(Collectors.groupingBy(
                                UpdateItineraryContentRequest.PoiPlacement::getDay,
                                TreeMap::new,
                                Collectors.toList()
                        ));

        // 5. days list
        List<Map<String, Object>> days = new ArrayList<>();

        for (Map.Entry<Integer,
                List<UpdateItineraryContentRequest.PoiPlacement>> entry
                : byDay.entrySet()) {

            Integer day = entry.getKey();
            List<UpdateItineraryContentRequest.PoiPlacement> pois = entry.getValue();

            pois.sort(Comparator.comparing(
                    UpdateItineraryContentRequest.PoiPlacement::getOrder));

            List<Map<String, Object>> poiList = new ArrayList<>();
            for (UpdateItineraryContentRequest.PoiPlacement p : pois) {
                Map<String, Object> poi = new LinkedHashMap<>();
                poi.put("poiId", p.getPoiId());
                poi.put("order", p.getOrder());
                poiList.add(poi);
            }

            Map<String, Object> dayObj = new LinkedHashMap<>();
            dayObj.put("day", day);
            dayObj.put("pois", poiList);

            days.add(dayObj);
        }

        // 6. new data Json
        Map<String, Object> newData = new LinkedHashMap<>();
        newData.put("days", days);
        newData.put("routes", routes);

        String newJson;
        try {
            newJson = objectMapper.writeValueAsString(newData);
        } catch (Exception e) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, "Failed to serialize JSON");
        }

        // 7. new version_no
        Integer maxNo = versionRepo.findMaxVersionNoByItineraryId(itineraryId);
        int newVersionNo = (maxNo == null ? 1 : maxNo + 1);

        // 8. save new version
        Instant now = Instant.now();
        versionRepo.insertVersion(
                now,              // created_at
                newJson,          // data (String → SQL CAST into jsonb)
                false,            // is_published
                itineraryId,      // itinerary_id
                current.name(),   // name
                now,              // updated_at
                newVersionNo      // version_no
        );

        // 9. return newVersionId
        Long newVersionId = versionRepo
                .findByItineraryIdAndVersionNo(itineraryId, newVersionNo)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.INTERNAL_SERVER_ERROR, "Failed to fetch new version id"))
                .id();

        return newVersionId;
    }
}



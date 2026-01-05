package com.laioffer.tripplanner.model;

import java.util.List;

public class UpdateItineraryContentRequest {
    private List<PoiPlacement> pois;

    public List<PoiPlacement> getPois() { return pois; }
    public void setPois(List<PoiPlacement> pois) { this.pois = pois; }

    public static class PoiPlacement {
        private Long poiId;
        private Integer day;
        private Integer order;

        public Long getPoiId() { return poiId; }
        public void setPoiId(Long poiId) { this.poiId = poiId; }

        public Integer getDay() { return day; }
        public void setDay(Integer day) { this.day = day; }

        public Integer getOrder() { return order; }
        public void setOrder(Integer order) { this.order = order; }
    }
}
package com.laioffer.tripplanner.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UpdateItineraryContentResponse {

    private boolean success;
    private Data data;
    private String error;

    public UpdateItineraryContentResponse(boolean success, Long newVersionId, String error) {
        this.success = success;
        this.data = success ? new Data(newVersionId) : null;
        this.error = error;
    }

    public boolean isSuccess() { return success; }
    public Data getData() { return data; }
    public String getError() { return error; }

    public static class Data {
        @JsonProperty("newVersionId")
        private Long newVersionId;

        public Data(Long newVersionId) { this.newVersionId = newVersionId; }
        public Long getNewVersionId() { return newVersionId; }
    }
}

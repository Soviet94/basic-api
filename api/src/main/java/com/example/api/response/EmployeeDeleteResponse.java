package com.example.api.response;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class EmployeeDeleteResponse {
    private final String data;

    @JsonCreator
    public EmployeeDeleteResponse(@JsonProperty("data") String data) {
        this.data = data;
    }
}

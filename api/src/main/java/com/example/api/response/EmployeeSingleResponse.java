package com.example.api.response;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class EmployeeSingleResponse {
    private final EmployeeDto data;

    @JsonCreator
    public EmployeeSingleResponse(@JsonProperty("data") EmployeeDto data) {
        this.data = data;
    }
}

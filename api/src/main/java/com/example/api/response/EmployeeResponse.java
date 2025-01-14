package com.example.api.response;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class EmployeeResponse {
    private final List<EmployeeDto> data;

    @JsonCreator
    public EmployeeResponse(@JsonProperty("data") List<EmployeeDto> data) {
        this.data = data;
    }
}

package com.example.api.payload;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class DelEmployeeRequest {

    @NotBlank(message = "Name must not be blank")
    private String name;

    // Constructors
    public DelEmployeeRequest() {}

    public DelEmployeeRequest(String name) {
        this.name = name;
    }
}

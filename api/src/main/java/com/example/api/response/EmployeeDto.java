package com.example.api.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class EmployeeDto {
    private final String id;
    private final String employee_name;
    private final Integer employee_salary;
    private final Integer employee_age;
    private final String employee_title;
    private final String employee_email;
}

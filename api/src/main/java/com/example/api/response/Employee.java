package com.example.api.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Employee {
    private final String id;
    private final String employeeName;
    private final Integer employeeSalary;
    private final Integer employeeAge;
    private final String employeeTitle;
    private final String employeeEmail;
}

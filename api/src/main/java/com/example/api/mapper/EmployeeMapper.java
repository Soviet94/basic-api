package com.example.api.mapper;

import com.example.api.response.Employee;
import com.example.api.response.EmployeeDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface EmployeeMapper {
    EmployeeMapper INSTANCE = Mappers.getMapper(EmployeeMapper.class);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "employeeName", source = "employee_name")
    @Mapping(target = "employeeSalary", source = "employee_salary")
    @Mapping(target = "employeeAge", source = "employee_age")
    @Mapping(target = "employeeTitle", source = "employee_title")
    @Mapping(target = "employeeEmail", source = "employee_email")
    Employee toEmployee(EmployeeDto EmployeeDto);
}

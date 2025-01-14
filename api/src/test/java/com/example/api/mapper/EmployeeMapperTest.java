package com.example.api.mapper;

import static org.junit.jupiter.api.Assertions.assertNull;

import com.example.api.response.Employee;
import com.example.api.response.EmployeeDto;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Test;

class EmployeeMapperTest {

    private static final String ID = "123-456-abcd-efg";
    private static final String NAME = "Barry Allen";
    private static final Integer SALARY = 150;
    private static final Integer AGE = 20;
    private static final String TITLE = "Software Developer";
    private static final String EMAIL = "me@email.com";

    private final EmployeeMapper employeeMapper = EmployeeMapper.INSTANCE;

    @Test
    void eployeeMapperTest() {
        EmployeeDto employeeDto = new EmployeeDto(ID, NAME, SALARY, AGE, TITLE, EMAIL);

        Employee employee = new Employee(ID, NAME, SALARY, AGE, TITLE, EMAIL);

        Employee result = employeeMapper.toEmployee(employeeDto);

        AssertionsForClassTypes.assertThat(result).isEqualTo(employee);
    }

    @Test
    void shouldProvideNullResponseForEmptyObjectWhenMappingToEmployee() {
        EmployeeMapper employeeMapper = EmployeeMapper.INSTANCE;
        assertNull(employeeMapper.toEmployee(null));
    }
}

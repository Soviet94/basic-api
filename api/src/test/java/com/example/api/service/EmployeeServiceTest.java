package com.example.api.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import com.example.api.mapper.EmployeeMapper;
import com.example.api.response.Employee;
import com.example.api.response.EmployeeDto;
import com.example.api.response.EmployeeResponse;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

class EmployeeServiceTest {
    private static final String ID = "123-456-abcd-efg";
    private static final String ID2 = "126-456-abcd-efg";
    private static final String NAME = "Barry Allen";
    private static final String NAME2 = "Steve Allen";
    private static final Integer SALARY = 150;
    private static final Integer AGE = 20;
    private static final String TITLE = "Software Developer";
    private static final String EMAIL = "me@email.com";

    private final RestTemplate restTemplate = mock(RestTemplate.class);
    private final EmployeeMapper employeeMapper = mock(EmployeeMapper.class);
    private final EmployeeService employeeService = new EmployeeService(restTemplate, "apiUrl", employeeMapper);

    // Should return empty when exception
    @Test
    void shouldReturnEmptyListWhenExceptionThrown() {
        when(restTemplate.exchange(anyString(), any(HttpMethod.class), any(HttpEntity.class), any(Class.class)))
                .thenThrow(RestClientException.class);

        List<Employee> employee = employeeService.getAllEmployees();
        assertNotNull(employee);
        assertEquals(0, employee.size());
    }

    @Test
    void shouldReturnPartialListWhenExceptionThrownDuringMapping() {
        EmployeeDto employeeDto = new EmployeeDto(ID2, NAME2, SALARY, AGE, TITLE, EMAIL);

        List<EmployeeDto> employeeDtos = getEmployeeDtos();
        employeeDtos.add(employeeDto);
        EmployeeResponse employeeResponse = new EmployeeResponse(employeeDtos);
        ResponseEntity<EmployeeResponse> responseEntity = new ResponseEntity<>(employeeResponse, HttpStatus.OK);
        when(restTemplate.exchange(anyString(), any(HttpMethod.class), any(HttpEntity.class), any(Class.class)))
                .thenReturn(responseEntity);
        when(employeeMapper.toEmployee(employeeDto)).thenThrow(new RuntimeException());

        List<Employee> employees = employeeService.getAllEmployees();
        assertNotNull(employees);
        assertEquals(1, employees.size());
    }

    private List<EmployeeDto> getEmployeeDtos() {
        final List<EmployeeDto> employeeDto = new ArrayList<>();
        employeeDto.add(new EmployeeDto(ID, NAME, SALARY, AGE, TITLE, EMAIL));
        return employeeDto;
    }
}

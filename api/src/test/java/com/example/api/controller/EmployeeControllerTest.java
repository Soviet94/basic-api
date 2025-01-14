package com.example.api.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

import com.example.api.payload.AddEmployeeRequest;
import com.example.api.response.Employee;
import com.example.api.service.EmployeeService;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

class EmployeeControllerTest {
    private static final String ID = "123-456-abcd-efg";
    private static final String NAME = "Barry Allen";
    private static final Integer SALARY = 150;
    private static final Integer AGE = 20;
    private static final String TITLE = "Software Developer";
    private static final String EMAIL = "me@email.com";

    private final EmployeeService employeeService = mock(EmployeeService.class);
    private final EmployeeController employeeController = new EmployeeController(employeeService);

    @AfterEach
    void after() {
        verifyNoMoreInteractions(employeeService);
    }

    @Test
    void shouldReturnAllEmployeesInformation() {
        List<Employee> expectedEmployeeList = List.of(new Employee(ID, NAME, SALARY, AGE, TITLE, EMAIL));
        given(employeeService.getAllEmployees()).willReturn(expectedEmployeeList);
        ResponseEntity<List<Employee>> result = employeeController.getAllEmployees();

        verify(employeeService).getAllEmployees();
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody()).isEqualTo(expectedEmployeeList);
    }

    @Test
    void shouldReturnEmployeesByName() {
        List<Employee> expectedEmployeeList = List.of(new Employee(ID, NAME, SALARY, AGE, TITLE, EMAIL));
        given(employeeService.searchEmployeesByName(NAME)).willReturn(expectedEmployeeList);
        ResponseEntity<List<Employee>> result = employeeController.getEmployeesByNameSearch(NAME);

        verify(employeeService).searchEmployeesByName(NAME);
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody()).isEqualTo(expectedEmployeeList);
    }

    @Test
    void shouldReturnEmployeesById() {
        Employee expectedEmployee = new Employee(ID, NAME, SALARY, AGE, TITLE, EMAIL);
        given(employeeService.getEmployeeById(ID)).willReturn(expectedEmployee);
        ResponseEntity<Employee> result = employeeController.getEmployeeById(ID);

        verify(employeeService).getEmployeeById(ID);
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody()).isEqualTo(expectedEmployee);
    }

    @Test
    void shouldReturnHighestSalary() {
        given(employeeService.getHighestSalary()).willReturn(15);
        ResponseEntity<Integer> result = employeeController.getHighestSalaryOfEmployees();

        verify(employeeService).getHighestSalary();
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody()).isEqualTo(15);
    }

    @Test
    void shouldReturnListOfHighestSalaries() {
        List<String> expectedEmployeeList = List.of("Barry Allen, J'onn J'onzz, Clark Kent");
        given(employeeService.getTopTenHighestEarningEmployeeNames()).willReturn(expectedEmployeeList);
        ResponseEntity<List<String>> result = employeeController.getTopTenHighestEarningEmployeeNames();

        verify(employeeService).getTopTenHighestEarningEmployeeNames();
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody()).isEqualTo(expectedEmployeeList);
    }

    @Test
    void shouldCreateEmployee() {
        AddEmployeeRequest newEmployee = new AddEmployeeRequest(NAME, SALARY, AGE, TITLE);
        Employee expectedEmployee = new Employee(ID, NAME, SALARY, AGE, TITLE, EMAIL);
        given(employeeService.createEmployee(newEmployee)).willReturn(expectedEmployee);
        ResponseEntity<Employee> result = employeeController.createEmployee(newEmployee);

        verify(employeeService).createEmployee(newEmployee);
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(result.getBody()).isEqualTo(expectedEmployee);
    }

    @Test
    void shouldDeleteEmployee() {
        List<Employee> expectedEmployeeList = List.of(new Employee(ID, NAME, SALARY, AGE, TITLE, EMAIL));
        given(employeeService.getAllEmployees()).willReturn(expectedEmployeeList);
        ResponseEntity<List<Employee>> result = employeeController.getAllEmployees();

        verify(employeeService).getAllEmployees();
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody()).isEqualTo(expectedEmployeeList);
    }
}

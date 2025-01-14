package com.example.api.controller;

import com.example.api.payload.AddEmployeeRequest;
import com.example.api.response.Employee;
import com.example.api.service.EmployeeService;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/employees")
public class EmployeeController implements IEmployeeController<Employee, AddEmployeeRequest> {

    private static final Logger logger = LoggerFactory.getLogger(EmployeeController.class);
    private final EmployeeService employeeService;

    @Autowired
    EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @Override
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Employee>> getAllEmployees() {
        logger.info("Fetching all employees.");
        List<Employee> employees = this.employeeService.getAllEmployees();
        logger.info("Retrieved {} employees.", employees.size());
        return ResponseEntity.ok(employees);
    }

    @Override
    @GetMapping(value = "/search/{searchString}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Employee>> getEmployeesByNameSearch(@PathVariable String searchString) {
        logger.info("Searching employees by name: {}", searchString);
        List<Employee> employees = employeeService.searchEmployeesByName(searchString);
        logger.info("Found {} employees matching '{}'.", employees.size(), searchString);
        return ResponseEntity.ok(employees);
    }

    @Override
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Employee> getEmployeeById(@PathVariable String id) {
        logger.info("Fetching employee with ID: {}", id);
        Employee employee = employeeService.getEmployeeById(id);
        if (employee != null) {
            logger.info("Employee found: {}", employee);
            return ResponseEntity.ok(employee);
        } else {
            logger.warn("Employee with ID {} not found.", id);
            return ResponseEntity.notFound().build();
        }
    }

    @Override
    @GetMapping(value = "/highestSalary", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Integer> getHighestSalaryOfEmployees() {
        logger.info("Fetching highest salary among employees.");
        Integer highestSalary = employeeService.getHighestSalary();
        logger.info("Highest salary is: {}", highestSalary);
        return ResponseEntity.ok(highestSalary);
    }

    @Override
    @GetMapping("/topTenHighestEarningEmployeeNames")
    public ResponseEntity<List<String>> getTopTenHighestEarningEmployeeNames() {
        logger.info("Fetching top 10 highest earning employee names.");
        List<String> topEarningEmployees = employeeService.getTopTenHighestEarningEmployeeNames();
        logger.info("Retrieved top 10 highest earning employees: {}", topEarningEmployees);
        return ResponseEntity.ok(topEarningEmployees);
    }

    @Override
    @PostMapping()
    public ResponseEntity<Employee> createEmployee(@RequestBody AddEmployeeRequest employeeInput) {
        logger.info("Creating new employee with input: {}", employeeInput);
        Employee createdEmployee = employeeService.createEmployee(employeeInput);
        logger.info("Employee created successfully: {}", createdEmployee);
        return ResponseEntity.status(201).body(createdEmployee);
    }

    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteEmployeeById(@PathVariable String id) {
        logger.info("Deleting employee with ID: {}", id);
        boolean isDeleted = employeeService.deleteEmployeeById(id);
        if (isDeleted) {
            logger.info("Employee with ID {} deleted successfully.", id);
            return ResponseEntity.ok("Employee deleted successfully");
        } else {
            logger.warn("Failed to delete employee. Employee with ID {} not found.", id);
            return ResponseEntity.status(404).body("Employee not found");
        }
    }
}

package com.example.api.service;

import com.example.api.response.*;
import com.example.api.mapper.EmployeeMapper;
import com.example.api.payload.AddEmployeeRequest;
import com.example.api.payload.DelEmployeeRequest;
import java.util.*;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Service
public class EmployeeService {

    private static final Logger logger = LoggerFactory.getLogger(EmployeeService.class);

    private final String apiUrl;
    private final RestTemplate restTemplate;
    private final EmployeeMapper employeeMapper;

    @Autowired
    EmployeeService(RestTemplate restTemplate, @Value("${api.url}") String apiUrl, EmployeeMapper employeeMapper) {
        this.restTemplate = restTemplate;
        this.apiUrl = apiUrl;
        this.employeeMapper = employeeMapper;
    }

    private EmployeeResponse getEmployeeData() {
        logger.info("Fetching employee data from API: {}", apiUrl);
        EmployeeResponse employeeResponse = null;
        try {
            HttpHeaders headers = createHeaders();
            HttpEntity<String> entity = new HttpEntity<>(headers);
            employeeResponse = restTemplate
                    .exchange(apiUrl, HttpMethod.GET, entity, EmployeeResponse.class)
                    .getBody();
            logger.info("Successfully fetched employee data.");
        } catch (RestClientException e) {
            logger.error("Error fetching employee data: {}", e.getMessage(), e);
        }
        return employeeResponse;
    }

    private EmployeeSingleResponse getSpecificEmployeeData(String id) {
        logger.info("Fetching employee data for ID: {}", id);
        EmployeeSingleResponse employeeSingleResponse = null;
        try {
            HttpHeaders headers = createHeaders();
            HttpEntity<String> entity = new HttpEntity<>(headers);
            employeeSingleResponse = restTemplate
                    .exchange(apiUrl + "/" + id, HttpMethod.GET, entity, EmployeeSingleResponse.class)
                    .getBody();
            logger.info("Successfully fetched employee data for ID: {}", id);
        } catch (RestClientException e) {
            logger.error("Error fetching specific employee data for ID {}: {}", id, e.getMessage(), e);
        }
        return employeeSingleResponse;
    }

    private EmployeeSingleResponse addEmployeeData(AddEmployeeRequest employeeRequest) {
        logger.info("Adding employee data: {}", employeeRequest);
        EmployeeSingleResponse employeeSingleResponse = null;
        try {
            HttpHeaders headers = createHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<AddEmployeeRequest> entity = new HttpEntity<>(employeeRequest, headers);
            employeeSingleResponse = restTemplate
                    .exchange(apiUrl, HttpMethod.POST, entity, EmployeeSingleResponse.class)
                    .getBody();
            logger.info("Successfully added employee data.");
        } catch (RestClientException e) {
            logger.error("Error adding employee data: {}", e.getMessage(), e);
        }
        return employeeSingleResponse;
    }

    private EmployeeDeleteResponse deleteEmployeeData(String id) {
        EmployeeDeleteResponse employeeDeleteResponse = null;
        DelEmployeeRequest name = new DelEmployeeRequest(this.getEmployeeById(id).getEmployeeName());

        logger.info("the name :"+name);
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);
            headers.set("Content-Type", MediaType.APPLICATION_JSON_VALUE);
            headers.set("User-Agent", "Back-end tech test");

            HttpEntity<DelEmployeeRequest> entity = new HttpEntity<>(name, headers);

            employeeDeleteResponse = restTemplate
                    .exchange(apiUrl, HttpMethod.DELETE, entity, EmployeeDeleteResponse.class)
                    .getBody();
        } catch (RestClientException e) {
            System.out.println("Error deleting employee data: " + e.getMessage());
        }

        return employeeDeleteResponse;
    }

    public List<Employee> getAllEmployees() {
        logger.info("Retrieving all employees.");
        EmployeeResponse employeeResponse = this.getEmployeeData();
        if (employeeResponse != null && employeeResponse.getData() != null) {
            List<Employee> employees = employeeResponse.getData().stream()
                    .map(this::errorHandledEmployeeMapping)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
            logger.info("Retrieved {} employees.", employees.size());
            return employees;
        }
        logger.warn("No employee data found.");
        return Collections.emptyList();
    }

    public List<Employee> searchEmployeesByName(String searchString) {
        EmployeeResponse employeeResponse = this.getEmployeeData();

        final List<Employee> employees = new ArrayList<>();
        if (employeeResponse != null && employeeResponse.getData() != null) {
            final List<Employee> mappedEmployees = employeeResponse.getData().stream()
                    .map(this::errorHandledEmployeeMapping)
                    .filter(Objects::nonNull)
                    .filter(employee -> searchString.equalsIgnoreCase(employee.getEmployeeName()))
                    .collect(Collectors.toList());
            employees.addAll(mappedEmployees);
        }
        return employees;
    }

    public Employee getEmployeeById(String id) {
        logger.info("Retrieving employee with ID: {}", id);
        EmployeeSingleResponse employeeResponse = this.getSpecificEmployeeData(id);
        if (employeeResponse != null && employeeResponse.getData() != null) {
            Employee employee = errorHandledEmployeeMapping(employeeResponse.getData());
            logger.info("Employee retrieved: {}", employee);
            return employee;
        }
        logger.warn("Employee not found for ID: {}", id);
        throw new RuntimeException("Employee not found for ID: " + id);
    }

    public Integer getHighestSalary() {
        EmployeeResponse employeeResponse = this.getEmployeeData();

        if (employeeResponse != null && employeeResponse.getData() != null) {
            return employeeResponse.getData().stream()
                    .map(this::errorHandledEmployeeMapping)
                    .filter(Objects::nonNull)
                    .map(Employee::getEmployeeSalary)
                    .max(Integer::compareTo)
                    .orElse(0);
        }

        return 0;
    }

    public List<String> getTopTenHighestEarningEmployeeNames() {
        logger.info("Fetching top 10 highest earning employee names.");
        EmployeeResponse employeeResponse = this.getEmployeeData();
        if (employeeResponse != null && employeeResponse.getData() != null) {
            List<String> topEmployees = employeeResponse.getData().stream()
                    .map(this::errorHandledEmployeeMapping)
                    .filter(Objects::nonNull)
                    .sorted(Comparator.comparingInt(Employee::getEmployeeSalary).reversed())
                    .limit(10)
                    .map(Employee::getEmployeeName)
                    .collect(Collectors.toList());
            logger.info("Top 10 highest earning employee names: {}", topEmployees);
            return topEmployees;
        }
        logger.warn("No employee data found for calculating top earners.");
        return Collections.emptyList();
    }

    public Employee createEmployee(AddEmployeeRequest employeeInput) {
        EmployeeSingleResponse employeeResponse = addEmployeeData(employeeInput);

        if (employeeResponse != null && employeeResponse.getData() != null) {
            return errorHandledEmployeeMapping(employeeResponse.getData());
        }

        throw new RuntimeException("Issue");
    }

    public boolean deleteEmployeeById(String id) {
        EmployeeDeleteResponse employeeDeleteResponse = deleteEmployeeData(id);

        if (employeeDeleteResponse != null && employeeDeleteResponse.getData() != null) {
            return true;
        }

        return false;
    }

    private HttpHeaders createHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.set("User-Agent", "Back-end tech test");
        return headers;
    }

    private Employee errorHandledEmployeeMapping(final EmployeeDto employeeDto) {
        try {
            return employeeMapper.toEmployee(employeeDto);
        } catch (final Exception e) {
            logger.error("Error mapping EmployeeDto to Employee: {}", e.getMessage(), e);
            return null;
        }
    }
}

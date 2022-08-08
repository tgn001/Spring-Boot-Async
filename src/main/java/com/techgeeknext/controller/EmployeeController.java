package com.techgeeknext.controller;

import com.techgeeknext.model.Employee;
import com.techgeeknext.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@Slf4j
public class EmployeeController {

    @Autowired
    EmployeeService employeeService;

    /**
     * Test the async example
     *
     * @return ResponseEntity
     */
    @GetMapping("/async/test")
    public ResponseEntity<String> getEmployees() {
        try {

            CompletableFuture<List<Employee>> employees = employeeService.getAllEmployees();
            CompletableFuture<Employee> employee = employeeService.getEmployeeById();
            CompletableFuture<String> employeeRole = employeeService.getEmployeeRoleById();

            // Hold off until every above async job is finished.
            CompletableFuture.allOf(employees, employee, employeeRole).join();

            // instead of using join, we can also use isDone for that method to see if it's completed or not
            //if (employees.isDone()) { employees.get()}
            //if (employee.isDone()) { employee.get()}
            //if (employeeRole.isDone()) { employeeRole.get()}


            //Print all of the collected data.
            log.info("All Employees--> " + employees.get());
            log.info("Employee By Id--> " + employee.get());
            log.info("Employee Role By Id--> " + employeeRole.get());

            return new ResponseEntity<>("The async example worked perfectly!", HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

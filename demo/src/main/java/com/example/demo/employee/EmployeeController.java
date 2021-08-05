package com.example.demo.employee;

import lombok.AllArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping(path = "api/employee")
public class EmployeeController {

    private final EmployeeService employeeService;

    @GetMapping
    public CollectionModel<EntityModel<Employee>> getEmployees() {
        return employeeService.getEmployees();
    }

    @GetMapping("/{id}")
    public EntityModel<Employee> getEmployee(@PathVariable("id") Long id) {
        return employeeService.getEmployee(id);
    }

    @PostMapping("/registration")
    public ResponseEntity<?> addEmployee(@RequestBody Employee employee) {
        return employeeService.addEmployee(employee);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateEmployee(@RequestBody Employee newEmployee, @PathVariable("id") Long id) {
        return employeeService.updateEmployee(newEmployee, id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteEmployee(@PathVariable("id") Long id) {
        return employeeService.deleteEmployee(id);
    }
}

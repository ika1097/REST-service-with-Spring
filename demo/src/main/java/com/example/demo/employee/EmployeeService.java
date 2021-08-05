package com.example.demo.employee;

import com.example.demo.employee.exception.EmployeeNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
@AllArgsConstructor
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    private final EmployeeModelAssembler employeeModelAssembler;

    public CollectionModel<EntityModel<Employee>> getEmployees() {

        List<EntityModel<Employee>> employees = employeeRepository.findAll()
                .stream()
                .map(employeeModelAssembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(employees,
                linkTo(methodOn(EmployeeController.class).getEmployees()).withSelfRel());
    }

    public EntityModel<Employee> getEmployee(Long id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new EmployeeNotFoundException(id));

        return employeeModelAssembler.toModel(employee);
    }

    public ResponseEntity<?> addEmployee(Employee employee) {
        EntityModel<Employee> entityModel =
                employeeModelAssembler.toModel(employeeRepository.save(employee));

        return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }

    public ResponseEntity<?> updateEmployee(Employee newEmployee, Long id) {
        Employee updatedEmployee = employeeRepository.findById(id)
                .map(employee -> {
                    employee.setFullName(newEmployee.getFullName());
                    employee.setEmail(newEmployee.getEmail());
                    employee.setPosition(newEmployee.getPosition());
                    employee.setOffice(newEmployee.getOffice());
                    employee.setSalary(newEmployee.getSalary());
                    return employeeRepository.save(employee);
                })
                .orElseGet(() -> {
                    newEmployee.setId(id);
                    return employeeRepository.save(newEmployee);
                });

        EntityModel<Employee> entityModel = employeeModelAssembler.toModel(updatedEmployee);

        return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }

    public ResponseEntity<?> deleteEmployee(Long id) {
        employeeRepository.deleteById(id);

        return ResponseEntity.noContent().build();
    }
}

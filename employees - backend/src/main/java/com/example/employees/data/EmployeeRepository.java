package com.example.employees.data;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Set;

public interface EmployeeRepository extends MongoRepository<Employee, Long> {

    public List<Employee> findEmployeeByProjectId(Long projectId);
    public boolean existsEmployeeByEmpId(Long empId);
}

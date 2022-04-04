package com.example.employees.services;

import com.example.employees.data.Employee;
import com.example.employees.data.EmployeeRepository;
import com.example.employees.web.models.EmployeeMapper;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@AllArgsConstructor
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final ModelMapper modelMapper;

    //getting the most experienced personnel from project, grouped by pairs
    public List<Employee> getMostExperiencedPersonnel() {
        List<Employee> pairEmployees = new ArrayList<>();
        List<Employee> mostExperiencedDuo = new ArrayList<>();
        Set<Long> projects = this.getProjects();
        for (Long projectId : projects) {

            //check whether there is more than 1 employee in a project
            if (this.employeeRepository.findEmployeeByProjectId(projectId).size() < 2) {
                break;
            } else {
                pairEmployees = this.employeeRepository.findEmployeeByProjectId(projectId);
                Employee seniorEmployee = pairEmployees.get(0);
                Employee secondSeniorEmployee = new Employee();
                //find the first senior
                for (int i = 1; i < pairEmployees.size(); i++) {
                        //this means we haven't found the senior yet
                        if (seniorEmployee.getTimeInProject() < pairEmployees.get(i).getTimeInProject()) {
                            seniorEmployee = pairEmployees.get(i);
                        }
                }
                //find the second senior
                for (int i = 0; i < pairEmployees.size(); i++) {
                        //this means we haven't found the senior yet
                        if (!pairEmployees.get(i).getTimeInProject().equals(seniorEmployee.getTimeInProject())) {
                            if (secondSeniorEmployee.getDaysInProject() == 0L) {
                                secondSeniorEmployee = pairEmployees.get(i);
                            }
                             if(secondSeniorEmployee.getTimeInProject() < pairEmployees.get(i).getTimeInProject()) {
                                secondSeniorEmployee = pairEmployees.get(i);
                            }
                    }
                }
                mostExperiencedDuo.add(seniorEmployee);
                mostExperiencedDuo.add(secondSeniorEmployee);
            }
        }

        return mostExperiencedDuo;
    }

    //save an employee to the db
    public Employee saveEmployee(EmployeeMapper employee) {
        Employee mappedEmployee;
        mappedEmployee = this.modelMapper.map(employee, Employee.class);
        //check whether an employee with the same id already exists
        if (this.employeeRepository.existsEmployeeByEmpId(mappedEmployee.getEmpId())) {
            System.out.printf("Employee with ID -> " + mappedEmployee.getEmpId() +
                    " already exists in the Database!\n");
            return null;
        }
        //mapping the date manually and checking whether any of the dates is "NULL"
        if (employee.getDateTo().equals("NULL") && employee.getDateFrom().equals("NULL")) {
            mappedEmployee.setDateFrom(LocalDate.parse(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))));
            mappedEmployee.setDateTo(LocalDate.parse(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))));
        } else if (employee.getDateFrom().equals("NULL") && !employee.getDateTo().equals("NULL")) {
            mappedEmployee.setDateFrom(LocalDate.parse(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))));
            mappedEmployee.setDateTo(LocalDate.parse(employee.getDateTo(), DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        } else if (employee.getDateTo().equals("NULL") && !employee.getDateFrom().equals("NULL")) {
            mappedEmployee.setDateTo(LocalDate.parse(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))));
            mappedEmployee.setDateFrom(LocalDate.parse(employee.getDateFrom(), DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        } else {
            mappedEmployee.setDateTo(LocalDate.parse(employee.getDateTo(), DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            mappedEmployee.setDateFrom(LocalDate.parse(employee.getDateFrom(), DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        }
        System.out.println("Saving employee -> " + mappedEmployee);
        return this.employeeRepository.insert(mappedEmployee);
    }

    //gets the unique projects from the data
    public Set<Long> getProjects() {
        List<Employee> employees = this.employeeRepository.findAll();
        Set<Long> uniqueProjects = new HashSet<>();
        employees.forEach(e -> uniqueProjects.add(e.getProjectId()));
        return uniqueProjects;
    }
}

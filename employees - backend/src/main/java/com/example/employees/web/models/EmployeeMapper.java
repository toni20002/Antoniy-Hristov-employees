package com.example.employees.web.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeMapper {

    private String empId;
    private String projectId;
    private String dateFrom;
    private String dateTo;
}

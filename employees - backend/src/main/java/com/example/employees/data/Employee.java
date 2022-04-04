package com.example.employees.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Data
@Document
@NoArgsConstructor
@AllArgsConstructor
public class Employee {
    private Long empId;
    private Long projectId;
    private LocalDate dateFrom;
    private LocalDate dateTo;
    private Long daysInProject = 0L;

    public Long getTimeInProject() {
       return this.daysInProject = ChronoUnit.DAYS.between(this.getDateFrom(), this.getDateTo());
    }
}

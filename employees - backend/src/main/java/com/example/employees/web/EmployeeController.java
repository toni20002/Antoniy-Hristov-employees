package com.example.employees.web;

import com.example.employees.data.Employee;
import com.example.employees.services.EmployeeService;
import com.example.employees.web.models.EmployeeMapper;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

@RestController
@RequestMapping("api/v1/employees")
@CrossOrigin
@AllArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;

    //fetching the data from the db and returning the most experienced pairs
    @GetMapping("/fetch_experienced_pairs")
    public List<Employee> getMostExperiencedPairs() {
        return this.employeeService.getMostExperiencedPersonnel();
    }

    //uploading the file to the db
    @PostMapping("/upload")
    public String uploadCsv(@RequestParam("file") MultipartFile file) {
        // parse CSV file to create a list of `User` objects
        BufferedReader reader = null;
        String line = "";
        int counter = 0;
        try {
            reader =  new BufferedReader(new InputStreamReader(file.getInputStream()));
            while((line = reader.readLine()) != null) {
                //Skipping the first row of the file - header row
                if (counter == 0) {
                    counter++;
                    continue;
                }
                String[] arr = line.split( ",");
                EmployeeMapper mapper = new EmployeeMapper(arr[0], arr[1], arr[2], arr[3]);
                this.employeeService.saveEmployee(mapper);
            }
        } catch (IOException e) {
            System.out.println(e);
            return "There was an error uploading your file\n" +
                    "Check whether your file headers are present and try again!";
        } catch (NullPointerException nullPointerException) {
            System.out.println(nullPointerException.getMessage());
        }
        return "File successfully uploaded!";
    }

}

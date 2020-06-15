package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping(value= "/")
public class Controller {
	
	@Autowired
	public EmployeeService searchService;

    @GetMapping("/all")
    public Iterable<Employee> get_all_data() throws IOException {
    	return searchService.findAllEmployees();
    }

    @GetMapping("/search")
    public List<Employee> get_specific_docs(
    		@RequestParam(value = "firstname", defaultValue = "*") String fname,
    		@RequestParam(value = "lastname", defaultValue = "*") String lname,
    		@RequestParam(value = "salary", defaultValue = "*") String salary) 
    				throws IOException {
    	return searchService.Search(fname, lname, salary);
    }

}

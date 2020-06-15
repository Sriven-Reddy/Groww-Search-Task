package com.example.demo;

import java.util.List;

public interface EmployeeService {
	
	public Iterable<Employee> findAllEmployees();
	
	public List<Employee> Search(String fname, String lname, String salary);

}

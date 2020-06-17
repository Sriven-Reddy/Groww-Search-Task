package com.example.demo;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;

public interface EmployeeSearchHelper {
	
	public Iterable<Employee> FindAllEmployeesHelper(Pageable pageable);

	public void UpdatePopularity(List<Employee> updationList);
	
	public NativeSearchQuery SearchQueryBuilder(String fname, String lname, String salary, Pageable pageable);

}

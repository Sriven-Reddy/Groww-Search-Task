package com.example.demo;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.stereotype.Service;

@Service
public class EmployeeServiceImpl implements EmployeeService {
	
	@Autowired
    public EmployeeSearchHelper employeeSearchHelper;
	
	@Autowired
	public ElasticsearchOperations employeeSearch;
	
	public Iterable<Employee> findAllEmployees() {
		
		Sort sort = Sort.by("popularity").descending();
    	Pageable pageable = PageRequest.of(0, 1000, sort);
		
		return employeeSearchHelper.FindAllEmployeesHelper(pageable);		
	}
	
	public List<Employee> Search(String fname, String lname, String salary) { 
		
		Sort sort = Sort.by("popularity").descending();
    	Pageable pageable = PageRequest.of(0, 15, sort);
    	List<Employee> list = new ArrayList<Employee>();
    	List<SearchHit<Employee>> slist = new ArrayList<>();
    	
    	if (fname.equals("*") && lname.equals("*") && salary.equals("*")) {
    		employeeSearchHelper.FindAllEmployeesHelper(pageable).forEach(list::add);
    	}
    	else {
    		
    		NativeSearchQuery query = employeeSearchHelper.SearchQueryBuilder(fname, lname, salary, pageable);
	    	
	    	slist = employeeSearch.search(query, Employee.class).getSearchHits();
	    	
	    	for (SearchHit<Employee> emp : slist) {
	    		Employee temp = emp.getContent(); 
	    		list.add(temp);
	    	}
	    	
    	}
    	
    	employeeSearchHelper.UpdatePopularity(list);
    	
        return list;
    	
//    	else if (lname.equals("*") && salary.equals("*")) {
//    		
//    		System.out.println("FIRSTNAME ONLY");
//    		list = search_service.findByFirstname(fname, pageable).getContent();
//    	} 
//    	else if (fname.equals("*") && salary.equals("*")) {
//    		
//    		System.out.println("LASTNAME ONLY");
//    		list = search_service.findByLastname(lname, pageable).getContent();
//    	} 
//    	else if (lname.equals("*") && fname.equals("*")) {
//    		
//    		System.out.println("SALARY ONLY");
//    		list = search_service.findBySalary(salary, pageable).getContent();
//    	} 
//    	else if (salary.equals("*")) {
//    		
//    		System.out.println("FIRST AND LAST NAME");
//    		list = search_service.findByFirstnameAndLastname(fname, lname, pageable).getContent();
//    	} 
//    	else if (lname.equals("*")) {
//    		
//    		System.out.println("FIRSTNAME AND SALARY");
//    		list = search_service.findByFirstnameAndSalary(fname, salary, pageable).getContent();
//    	} 
//    	else if (fname.equals("*")) {
//    		
//    		System.out.println("LASTNAME AND SALARY");
//    		list = search_service.findByLastnameAndSalary(lname, salary, pageable).getContent();
//    	} 
//    	else if (!fname.equals("*") && !lname.equals("*") && !salary.equals("*")) {
//    		
//    		System.out.println("ALL THREE");
//    		list = search_service.findByFirstnameAndLastnameAndSalary(fname, lname, salary, pageable).getContent();
//    	} 
//    	else {
//    		throw new IllegalArgumentException("Search Failed!");
//    	}
//    	
    	
	}

}

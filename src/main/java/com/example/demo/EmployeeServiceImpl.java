package com.example.demo;

import java.util.ArrayList;
import java.util.List;

import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

@Service
public class EmployeeServiceImpl implements EmployeeService {
	
    public EmployeeRepository search_service;
	
	@Autowired
    public void setEmployeeRepository(EmployeeRepository employeeRepository) {
        this.search_service = employeeRepository;
    }
	
	public Iterable<Employee> findAllEmployees() {
		
		Sort sort = Sort.by("popularity").descending();
    	Pageable pageable = PageRequest.of(0, 3, sort);
		
		return search_service.findAll(pageable);		
	}
	
	public List<Employee> Search(String fname, String lname, String salary) {
		
		BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery(); 
		
		Sort sort = Sort.by("popularity").descending();
    	Pageable pageable = PageRequest.of(0, 3, sort);
    	List<Employee> list = new ArrayList<Employee>();
    	
    	System.out.println(fname);
    	System.out.println(lname);
    	System.out.println(salary);
    	
    	if (!fname.equals("*"))
    		boolQueryBuilder.must(QueryBuilders.matchQuery("firstname", fname));
    	
    	if (!lname.equals("*"))
    		boolQueryBuilder.must(QueryBuilders.matchQuery("lastname", lname));
    	
    	if (!salary.equals("*"))
    		boolQueryBuilder.must(QueryBuilders.matchQuery("salary", salary));
    	
    	if (fname.equals("*") && lname.equals("*") && salary.equals("*")) {
    		System.out.println("WRONG QUERY");
    		search_service.findAll(pageable).forEach(list::add);
    	}
    	else {
    	
	    	NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder();
	    	nativeSearchQueryBuilder.withQuery(boolQueryBuilder);
	    	nativeSearchQueryBuilder.withPageable(pageable);
	    	NativeSearchQuery query = nativeSearchQueryBuilder.build();
	    	
	    	list = search_service.search(query).getContent();
    	}
    	
    	for (Employee employee : list) {
    		employee.increasePopularity();
    		search_service.save(employee);; 
    	}
    	
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

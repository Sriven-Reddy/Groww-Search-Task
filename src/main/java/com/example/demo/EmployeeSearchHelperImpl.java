package com.example.demo;

import java.util.Arrays;
import java.util.List;

import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmployeeSearchHelperImpl implements EmployeeSearchHelper {
	
	@Autowired
	public EmployeeRepository employeeRepository;
	
	public Iterable<Employee> FindAllEmployeesHelper(Pageable pageable) {
		return employeeRepository.findAll(pageable);
	}
	
	@Async
	public void UpdatePopularity (List<Employee> updationList) {
		
		Integer x = 0;
		List<Integer> scoreList = Arrays.asList(10, 7, 4, 3, 3, 2, 2, 1, 1, 1);
		
		for (Employee employee : updationList) {
			
			if (x > 9)
				break;
			
    		employee.increasePopularity(scoreList.get(x));
    		employeeRepository.save(employee);
    		x++;
    	}
		
	}
	
	public NativeSearchQuery SearchQueryBuilder(String fname, String lname, String salary, Pageable pageable) {
		
		BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
		
		if (!fname.equals("*"))
    		boolQueryBuilder.must(QueryBuilders.matchQuery("firstname", fname));
    	
    	if (!lname.equals("*"))
    		boolQueryBuilder.must(QueryBuilders.matchQuery("lastname", lname));
    	
    	if (!salary.equals("*"))
    		boolQueryBuilder.must(QueryBuilders.matchQuery("salary", salary));
    	
    	NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder();
    	nativeSearchQueryBuilder.withQuery(boolQueryBuilder);
    	nativeSearchQueryBuilder.withPageable(pageable);
    	NativeSearchQuery query = nativeSearchQueryBuilder.build();
    	
    	return query;
	}

}

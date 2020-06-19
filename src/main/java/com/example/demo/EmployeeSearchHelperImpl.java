package com.example.demo;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
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
	private List<Integer> scoreList;
	
	public EmployeeSearchHelperImpl() throws IOException {
		
		this.scoreList = Arrays.asList(10, 7, 4, 3, 3, 2, 2, 1, 1, 1);
    }
	
	private static List<Integer> GetScoringRubric(String fileName) throws IOException {
		
		BufferedReader br = null;
		List<Integer> list = new ArrayList<>();
		
		try {

			br = new BufferedReader(new FileReader(fileName));
			String lines;
			
			while ((lines = br.readLine()) != null) {
				list.add(Integer.parseInt(lines));
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				br.close();
			}
		}
		
		return list;
	}
	
	public Iterable<Employee> FindAllEmployeesHelper(Pageable pageable) {
		return employeeRepository.findAll(pageable);
	}
	
	@Async
	public void UpdatePopularity (List<Employee> updationList) {
		
		Integer x = 0;
		String fileName = "/home/sriven/Documents/Spring Suite/demo/src/main/java/com/example/demo/ScoringRubric";
		
		try {
			this.scoreList = EmployeeSearchHelperImpl.GetScoringRubric(fileName);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			this.scoreList = Arrays.asList(10, 7, 4, 3, 3, 2, 2, 1, 1, 1);
		}
		
		
		for (Employee employee : updationList) {
			
			if (x > 9)
				break;
			
    		employee.increasePopularity(this.scoreList.get(x));
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

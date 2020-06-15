package com.example.demo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.Employee;

@Repository
public interface EmployeeRepository extends ElasticsearchRepository<Employee, String> {

   Page<Employee> findByFirstname(String name, Pageable pageable);
   Page<Employee> findByLastname(String name, Pageable pageable);
   Page<Employee> findBySalary(String salary, Pageable pageable);
   Page<Employee> findByFirstnameAndLastname(String fname, String lname, Pageable pageable);
   Page<Employee> findByFirstnameAndSalary(String fname, String salary, Pageable pageable);
   Page<Employee> findByLastnameAndSalary(String lname, String salary, Pageable pageable);
   Page<Employee> findByFirstnameAndLastnameAndSalary(String fname, String lname, String salary, Pageable pageable);
   

//   @Query("{\"bool\": {\"must\": [{\"match\": {\"authors.name\": \"?0\"}}]}}")
//   Page<Employee> findByFirstNameUsingCustomQuery(String name, Pageable pageable);

}

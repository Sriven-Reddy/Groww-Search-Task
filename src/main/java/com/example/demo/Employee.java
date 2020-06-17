package com.example.demo;

import lombok.Data;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.annotation.Id;

@Data
@Document(indexName= "test")
public class Employee {
	
    private String firstname;

    private String lastname;

    private String designation;

    private float salary;

    private String dateofjoining;

    private String address;

    private String gender;

    private Integer age;

    private String maritalstatus;

    private String interests;
    
    private Integer popularity;
    
    @Id
    private String id;
    
    public void increasePopularity(Integer x) {
    	setPopularity(getPopularity() + x);
    }

}

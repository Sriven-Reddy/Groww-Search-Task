package com.example.demo;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Search {

    @Autowired
    private RestHighLevelClient client;

    @Autowired
    private ObjectMapper objectMapper;

    public List<Employee> get_all_data() throws IOException {
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        boolQueryBuilder.must(QueryBuilders.matchAllQuery());
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.indices("employee_index");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(boolQueryBuilder);
        searchSourceBuilder.from(0);
        searchSourceBuilder.size(25);
        searchRequest.source(searchSourceBuilder);
        SearchResponse searchResponse =
                client.search(searchRequest, RequestOptions.DEFAULT);
        SearchHit[] searchHit = searchResponse.getHits().getHits();
        List<Employee> employeeList = new ArrayList<>();
        for (SearchHit hit : searchHit) {
            employeeList.add(objectMapper.convertValue(hit.getSourceAsMap(), Employee.class));
        }
        return employeeList;
    }

    public List<Employee> get_specific_docs(String search_string) throws IOException {
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        boolQueryBuilder.should(QueryBuilders.termQuery("Designation", search_string));
        boolQueryBuilder.should(QueryBuilders.termQuery("MaritalStatus", search_string));
        boolQueryBuilder.should(QueryBuilders.matchQuery("FirstName", search_string).boost(0.4f)
                .fuzziness(Fuzziness.AUTO));
        boolQueryBuilder.should(QueryBuilders.matchQuery("LastName", search_string).boost(0.3f)
                .fuzziness(Fuzziness.AUTO));
        boolQueryBuilder.should(QueryBuilders.matchQuery("Interests", search_string).boost(0.4f)
                .fuzziness(Fuzziness.AUTO));
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.indices("employee_index");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(boolQueryBuilder);
        searchRequest.source(searchSourceBuilder);
        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
        SearchHit[] searchHit = searchResponse.getHits().getHits();
        List<Employee> employeeList = new ArrayList<>();
        for (SearchHit hit : searchHit) {
            employeeList.add(objectMapper.convertValue(hit.getSourceAsMap(), Employee.class));
        }
        return employeeList;
    }

}

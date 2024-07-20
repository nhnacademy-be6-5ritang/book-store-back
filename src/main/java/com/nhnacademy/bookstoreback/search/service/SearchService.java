package com.nhnacademy.bookstoreback.search.service;

import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.logging.Logger;

@Service
public class SearchService {

	private static final Logger logger = Logger.getLogger(SearchService.class.getName());

	@Autowired
	private RestHighLevelClient client;

	public SearchResponse searchBooks(String query) throws IOException {
		logger.info("Searching for books with query: " + query);

		SearchRequest searchRequest = new SearchRequest("books", "index-author", "index-category", "index-publisher", "index-tag");
		SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
		sourceBuilder.query(QueryBuilders.multiMatchQuery(query)
			.type("best_fields"));
		searchRequest.source(sourceBuilder);

		SearchResponse response = client.search(searchRequest, RequestOptions.DEFAULT);
		logger.info("Search response: " + response.toString());

		return response;
	}
}
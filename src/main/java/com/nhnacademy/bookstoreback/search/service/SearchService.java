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

@Service
public class SearchService {

	@Autowired
	private RestHighLevelClient client;

	public SearchResponse searchBooks(String query) throws IOException {
		SearchRequest searchRequest = new SearchRequest("books");
		SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
		sourceBuilder.query(QueryBuilders.matchQuery("bookTitle", query));
		searchRequest.source(sourceBuilder);

		return client.search(searchRequest, RequestOptions.DEFAULT);
	}

	public SearchResponse searchAuthors(String query) throws IOException {
		SearchRequest searchRequest = new SearchRequest("index-author");
		SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
		sourceBuilder.query(QueryBuilders.matchQuery("authorName", query));
		searchRequest.source(sourceBuilder);

		return client.search(searchRequest, RequestOptions.DEFAULT);
	}

	// 추가로 다른 엔티티에 대한 검색 메서드를 구현할 수 있습니다.
}

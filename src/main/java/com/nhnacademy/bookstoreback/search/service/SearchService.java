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
import java.util.Collections;
import java.util.logging.Logger;

@Service
public class SearchService {

	private static final Logger logger = Logger.getLogger(SearchService.class.getName());

	@Autowired
	private RestHighLevelClient client;

	public SearchResponse searchBooks(String query) throws IOException {
		logger.info("Searching for books with query: " + query);

		SearchRequest searchRequest = new SearchRequest("books");
		SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
		sourceBuilder.query(QueryBuilders.multiMatchQuery(query, "book_title", "book_description", "book_isbn")
			.type("best_fields"));
		searchRequest.source(sourceBuilder);

		SearchResponse response = client.search(searchRequest, RequestOptions.DEFAULT);
		logger.info("Search response: " + response.toString());

		return response;
	}

	public SearchResponse searchAuthors(String query) throws IOException {
		logger.info("Searching for authors with query: " + query);

		SearchRequest searchRequest = new SearchRequest("index-author");
		SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
		sourceBuilder.query(QueryBuilders.matchQuery("author_name", query));
		searchRequest.source(sourceBuilder);

		SearchResponse response = client.search(searchRequest, RequestOptions.DEFAULT);
		String authorId = null;

		// Author ID 추출
		if (response.getHits().getTotalHits().value > 0) {
			authorId = response.getHits().getHits()[0].getId(); // 첫 번째 저자 ID 사용
			logger.info("authorId: " + authorId);
		}

		if (authorId == null) {
			logger.info("Author Search response: " + response.toString());
			return response; // 저자가 없을 경우 빈 리스트 반환
		}

		// Step 2: Book 검색
		SearchRequest bookSearchRequest = new SearchRequest("books");
		SearchSourceBuilder bookSourceBuilder = new SearchSourceBuilder();
		bookSourceBuilder.query(QueryBuilders.termQuery("author_id", authorId));
		bookSearchRequest.source(bookSourceBuilder);

		SearchResponse bookResponse = client.search(bookSearchRequest, RequestOptions.DEFAULT);

		logger.info("Book Search response: " + bookResponse.toString());

		return bookResponse;
	}


	// 추가로 다른 엔티티에 대한 검색 메서드를 구현할 수 있습니다.
}
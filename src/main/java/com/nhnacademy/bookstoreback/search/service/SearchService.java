package com.nhnacademy.bookstoreback.search.service;

import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
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
		logger.info("책 검색 쿼리: " + query);

		SearchRequest searchRequest = new SearchRequest("books_v2");
		SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
		sourceBuilder.query(QueryBuilders.multiMatchQuery(query, "book_title", "book_description", "book_isbn")
			.type("best_fields"));
		searchRequest.source(sourceBuilder);

		SearchResponse response = client.search(searchRequest, RequestOptions.DEFAULT);
		logger.info("책 검색 응답: " + response.toString());

		return response;
	}

	public SearchResponse searchAuthors(String query) throws IOException {
		logger.info("저자 검색 쿼리: " + query);

		SearchRequest searchRequest = new SearchRequest("index-author");
		SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
		sourceBuilder.query(QueryBuilders.matchQuery("author_name", query));
		searchRequest.source(sourceBuilder);

		SearchResponse response = client.search(searchRequest, RequestOptions.DEFAULT);
		String authorId = null;

		// 저자 ID 추출
		if (response.getHits().getTotalHits().value > 0) {
			authorId = response.getHits().getHits()[0].getId(); // 첫 번째 저자 ID 사용
			logger.info("저자 ID: " + authorId);
		}

		if (authorId == null) {
			logger.info("저자 검색 응답: " + response.toString());
			return response; // 저자가 없을 경우 빈 리스트 반환
		}

		// Step 2: 책 검색
		SearchRequest bookSearchRequest = new SearchRequest("books_v2");
		SearchSourceBuilder bookSourceBuilder = new SearchSourceBuilder();
		bookSourceBuilder.query(QueryBuilders.termQuery("author_id", authorId)); // author_id로 검색
		bookSearchRequest.source(bookSourceBuilder);

		SearchResponse bookResponse = client.search(bookSearchRequest, RequestOptions.DEFAULT);

		logger.info("책 검색 응답: " + bookResponse.toString());

		return bookResponse;
	}

	public SearchResponse searchBooksByCategory(String query) throws IOException {
		logger.info("카테고리 검색 쿼리: " + query);

		// Step 1: 카테고리 검색
		SearchRequest categorySearchRequest = new SearchRequest("index-category");
		SearchSourceBuilder categorySourceBuilder = new SearchSourceBuilder();
		categorySourceBuilder.query(QueryBuilders.matchQuery("category_name", query));
		categorySearchRequest.source(categorySourceBuilder);

		SearchResponse categoryResponse = client.search(categorySearchRequest, RequestOptions.DEFAULT);
		if (categoryResponse.getHits().getTotalHits().value == 0) {
			logger.info("카테고리 검색 결과가 없습니다.");
			return categoryResponse;
		}

		String categoryId = categoryResponse.getHits().getHits()[0].getId();
		logger.info("카테고리 ID: " + categoryId);

		// Step 2: 책 카테고리 매핑 검색
		SearchRequest bookCategorySearchRequest = new SearchRequest("books_and_categories");
		SearchSourceBuilder bookCategorySourceBuilder = new SearchSourceBuilder();
		bookCategorySourceBuilder.query(QueryBuilders.termQuery("category_id", categoryId));
		bookCategorySearchRequest.source(bookCategorySourceBuilder);

		SearchResponse bookCategoryResponse = client.search(bookCategorySearchRequest, RequestOptions.DEFAULT);
		if (bookCategoryResponse.getHits().getTotalHits().value == 0) {
			logger.info("해당 카테고리에 책이 없습니다.");
			return bookCategoryResponse;
		}

		// Step 3: 책 검색
		BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
		for (var hit : bookCategoryResponse.getHits().getHits()) {
			boolQueryBuilder.should(QueryBuilders.termQuery("book_id", hit.getSourceAsMap().get("book_id")));
		}

		SearchRequest bookSearchRequest = new SearchRequest("books_v2");
		SearchSourceBuilder bookSourceBuilder = new SearchSourceBuilder();
		bookSourceBuilder.query(boolQueryBuilder);
		bookSearchRequest.source(bookSourceBuilder);

		SearchResponse bookResponse = client.search(bookSearchRequest, RequestOptions.DEFAULT);
		logger.info("책 검색 응답: " + bookResponse.toString());

		return bookResponse;
	}

	public SearchResponse searchPublishers(String query) throws IOException {
		logger.info("출판사 검색 쿼리: " + query);

		SearchRequest searchRequest = new SearchRequest("index-publisher");
		SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
		sourceBuilder.query(QueryBuilders.matchQuery("publisher_name", query));
		searchRequest.source(sourceBuilder);

		SearchResponse response = client.search(searchRequest, RequestOptions.DEFAULT);
		String publisherID = null;

		// 출판사 ID 추출
		if (response.getHits().getTotalHits().value > 0) {
			publisherID = response.getHits().getHits()[0].getId(); // 첫 번째 출판사 ID 사용
			logger.info("출판사 ID: " + publisherID);
		}

		if (publisherID == null) {
			logger.info("출판사 검색 응답: " + response.toString());
			return response; // 저자가 없을 경우 빈 리스트 반환
		}

		// Step 2: 책 검색
		SearchRequest bookSearchRequest = new SearchRequest("books_v2");
		SearchSourceBuilder bookSourceBuilder = new SearchSourceBuilder();
		bookSourceBuilder.query(QueryBuilders.termQuery("publisher_id", publisherID)); // publisher_id로 검색
		bookSearchRequest.source(bookSourceBuilder);

		SearchResponse bookResponse = client.search(bookSearchRequest, RequestOptions.DEFAULT);

		logger.info("책 검색 응답: " + bookResponse.toString());

		return bookResponse;
	}

	public SearchResponse searchBooksByTag(String query) throws IOException {
		logger.info("태그 검색 쿼리: " + query);

		// Step 1: 태그 검색
		SearchRequest tagSearchRequest = new SearchRequest("index-tag");
		SearchSourceBuilder tagSourceBuilder = new SearchSourceBuilder();
		tagSourceBuilder.query(QueryBuilders.matchQuery("tag_name", query));
		tagSearchRequest.source(tagSourceBuilder);

		SearchResponse tagResponse = client.search(tagSearchRequest, RequestOptions.DEFAULT);
		if (tagResponse.getHits().getTotalHits().value == 0) {
			logger.info("태그 검색 결과가 없습니다.");
			return tagResponse;
		}

		String tagId = tagResponse.getHits().getHits()[0].getId();
		logger.info("태그 ID: " + tagId);

		// Step 2: 책 태그 매핑 검색
		SearchRequest bookTagSearchRequest = new SearchRequest("books_and_tags");
		SearchSourceBuilder bookTagSourceBuilder = new SearchSourceBuilder();
		bookTagSourceBuilder.query(QueryBuilders.termQuery("tag_id", tagId));
		bookTagSearchRequest.source(bookTagSourceBuilder);

		SearchResponse bookTagResponse = client.search(bookTagSearchRequest, RequestOptions.DEFAULT);
		if (bookTagResponse.getHits().getTotalHits().value == 0) {
			logger.info("해당 태그에 책이 없습니다.");
			return bookTagResponse;
		}

		// Step 3: 책 검색
		BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
		for (var hit : bookTagResponse.getHits().getHits()) {
			boolQueryBuilder.should(QueryBuilders.termQuery("book_id", hit.getSourceAsMap().get("book_id")));
		}

		SearchRequest bookSearchRequest = new SearchRequest("books_v2");
		SearchSourceBuilder bookSourceBuilder = new SearchSourceBuilder();
		bookSourceBuilder.query(boolQueryBuilder);
		bookSearchRequest.source(bookSourceBuilder);

		SearchResponse bookResponse = client.search(bookSearchRequest, RequestOptions.DEFAULT);
		logger.info("책 검색 응답: " + bookResponse.toString());

		return bookResponse;
	}

	// 추가로 다른 엔티티에 대한 검색 메서드를 구현할 수 있습니다.
}

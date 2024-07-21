package com.nhnacademy.bookstoreback.search.service;

import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;
import java.util.logging.Logger;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.bookstoreback.search.dto.reponse.BookSearchResponse;

@Service
public class SearchService {

	private static final Logger logger = Logger.getLogger(SearchService.class.getName());

	@Autowired
	private RestHighLevelClient client;

	public Page<BookSearchResponse> searchBooks(String query, Pageable pageable) throws IOException {
		logger.info("책 검색 쿼리: " + query);

		SearchRequest searchRequest = new SearchRequest("books");
		SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
		sourceBuilder.query(QueryBuilders.multiMatchQuery(query, "book_title", "book_description", "book_isbn").type("best_fields"));
		searchRequest.source(sourceBuilder);

		SearchResponse response = client.search(searchRequest, RequestOptions.DEFAULT);
		logger.info("책 검색 응답: " + response.toString());

		List<BookSearchResponse> bookDetails = parseJsonResponse(response.toString());
		return new PageImpl<>(bookDetails, pageable, response.getHits().getTotalHits().value);
	}

	public Page<BookSearchResponse> searchAuthors(String query, Pageable pageable) throws IOException {
		logger.info("저자 검색 쿼리: " + query);

		// Step 1: 저자 검색
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
			return new PageImpl<>(new ArrayList<>(), pageable, 0); // 저자가 없을 경우 빈 리스트 반환
		}

		// Step 2: 책 검색
		SearchRequest bookSearchRequest = new SearchRequest("books");
		SearchSourceBuilder bookSourceBuilder = new SearchSourceBuilder();
		bookSourceBuilder.query(QueryBuilders.termQuery("author_id", authorId)); // author_id로 검색
		bookSearchRequest.source(bookSourceBuilder);

		SearchResponse bookResponse = client.search(bookSearchRequest, RequestOptions.DEFAULT);
		logger.info("책 검색 응답: " + bookResponse.toString());

		List<BookSearchResponse> bookDetails = parseJsonResponse(bookResponse.toString());
		return new PageImpl<>(bookDetails, pageable, bookResponse.getHits().getTotalHits().value);
	}

	public Page<BookSearchResponse> searchPublishers(String query, Pageable pageable) throws IOException {
		logger.info("출판사 검색 쿼리: " + query);

		// Step 1: 출판사 검색
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
			return new PageImpl<>(new ArrayList<>(), pageable, 0); // 출판사가 없을 경우 빈 리스트 반환
		}

		// Step 2: 책 검색
		SearchRequest bookSearchRequest = new SearchRequest("books");
		SearchSourceBuilder bookSourceBuilder = new SearchSourceBuilder();
		bookSourceBuilder.query(QueryBuilders.termQuery("publisher_id", publisherID)); // publisher_id로 검색
		bookSearchRequest.source(bookSourceBuilder);

		SearchResponse bookResponse = client.search(bookSearchRequest, RequestOptions.DEFAULT);
		logger.info("책 검색 응답: " + bookResponse.toString());

		List<BookSearchResponse> bookDetails = parseJsonResponse(bookResponse.toString());
		return new PageImpl<>(bookDetails, pageable, bookResponse.getHits().getTotalHits().value);
	}

	public Page<BookSearchResponse> searchBooksByTag(String query, Pageable pageable) throws IOException {
		logger.info("태그 검색 쿼리: " + query);

		// Step 1: 태그 검색
		SearchRequest tagSearchRequest = new SearchRequest("index-tag");
		SearchSourceBuilder tagSourceBuilder = new SearchSourceBuilder();
		tagSourceBuilder.query(QueryBuilders.matchQuery("tag_name", query));
		tagSearchRequest.source(tagSourceBuilder);

		SearchResponse tagResponse = client.search(tagSearchRequest, RequestOptions.DEFAULT);
		if (tagResponse.getHits().getTotalHits().value == 0) {
			logger.info("태그 검색 결과가 없습니다.");
			return new PageImpl<>(new ArrayList<>(), pageable, 0); // 태그가 없을 경우 빈 리스트 반환
		}

		String tagId = tagResponse.getHits().getHits()[0].getId();
		logger.info("태그 ID: " + tagId);

		// Step 2: 책 태그 매핑 검색
		SearchRequest bookTagSearchRequest = new SearchRequest("index-books-and-tags");
		SearchSourceBuilder bookTagSourceBuilder = new SearchSourceBuilder();
		bookTagSourceBuilder.query(QueryBuilders.termQuery("tag_id", tagId));
		bookTagSearchRequest.source(bookTagSourceBuilder);

		SearchResponse bookTagResponse = client.search(bookTagSearchRequest, RequestOptions.DEFAULT);
		if (bookTagResponse.getHits().getTotalHits().value == 0) {
			logger.info("해당 태그에 책이 없습니다.");
			return new PageImpl<>(new ArrayList<>(), pageable, 0); // 해당 태그의 책이 없을 경우 빈 리스트 반환
		}

		// Step 3: 책 검색
		BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
		for (var hit : bookTagResponse.getHits().getHits()) {
			boolQueryBuilder.should(QueryBuilders.termQuery("book_id", hit.getSourceAsMap().get("book_id")));
		}

		SearchRequest bookSearchRequest = new SearchRequest("books");
		SearchSourceBuilder bookSourceBuilder = new SearchSourceBuilder();
		bookSourceBuilder.query(boolQueryBuilder);
		bookSearchRequest.source(bookSourceBuilder);

		SearchResponse bookResponse = client.search(bookSearchRequest, RequestOptions.DEFAULT);
		logger.info("책 검색 응답: " + bookResponse.toString());

		List<BookSearchResponse> bookDetails = parseJsonResponse(bookResponse.toString());
		return new PageImpl<>(bookDetails, pageable, bookResponse.getHits().getTotalHits().value);
	}

	public List<BookSearchResponse> parseJsonResponse(String jsonResponse) throws IOException {
		ObjectMapper objectMapper = new ObjectMapper();
		JsonNode rootNode = objectMapper.readTree(jsonResponse);
		JsonNode hitsNode = rootNode.path("hits").path("hits");

		List<BookSearchResponse> bookList = new ArrayList<>();
		for (JsonNode hit : hitsNode) {
			JsonNode sourceNode = hit.path("_source");

			// Manually map JSON fields to BookSearchResponse fields
			BookSearchResponse bookDetail = BookSearchResponse.builder()
				.bookId(sourceNode.path("book_id").asLong())
				.authorName(sourceNode.path("author_id").asText())
				.publisherName(sourceNode.path("publisher_id").asText())
				.bookStatusName(sourceNode.path("book_status_id").asText())
				.bookTitle(sourceNode.path("book_title").asText())
				.bookDescription(sourceNode.path("book_description").asText())
				.bookQuantity(sourceNode.path("book_quantity").asInt())
				.bookPublishDate(new Date(sourceNode.path("book_publish_date").asLong()))
				.bookIsbn(sourceNode.path("book_isbn").asText())
				.bookPrice(new BigDecimal(sourceNode.path("book_price").asText()))
				.bookSalePrice(new BigDecimal(sourceNode.path("book_sale_price").asText()))
				.bookSalePercent(new BigDecimal(sourceNode.path("book_sale_percent").asText()))
				.bookImageUrl(sourceNode.path("book_image_url").asText(null))
				.build();

			bookList.add(bookDetail);
		}
		return bookList;
	}
}

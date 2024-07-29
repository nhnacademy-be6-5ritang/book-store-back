package com.nhnacademy.bookstoreback.search.service;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Logger;

import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.bookstoreback.book.domain.entity.Book;
import com.nhnacademy.bookstoreback.book.repository.BookRepository;
import com.nhnacademy.bookstoreback.search.dto.reponse.BookSearchResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SearchService {
	private final RestHighLevelClient client;
	private final BookRepository bookRepository;
	private static final Logger logger = Logger.getLogger(SearchService.class.getName());

	public Page<BookSearchResponse> searchBooks(String query, Pageable pageable) {
		int page = Math.max(pageable.getPageNumber() - 1, 0);
		int pageSize = pageable.getPageSize();
		Sort sort = pageable.getSort();
		logger.info("책 검색 쿼리: " + query);

		SearchRequest searchRequest = new SearchRequest("books");
		SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();

		// Add sorting
		sort.forEach(order -> {
			SortOrder sortOrder = order.isAscending() ? SortOrder.ASC : SortOrder.DESC;
			sourceBuilder.sort(order.getProperty(), sortOrder);
		});

		// Add pagination
		sourceBuilder.from(page * pageSize);
		sourceBuilder.size(pageSize);

		// Add query
		sourceBuilder.query(
			QueryBuilders.multiMatchQuery(query, "book_title", "book_description", "book_isbn").type("best_fields"));
		searchRequest.source(sourceBuilder);

		try {
			SearchResponse response = client.search(searchRequest, RequestOptions.DEFAULT);
			logger.info("책 검색 응답: " + response.toString());

			List<BookSearchResponse> bookDetails = parseJsonResponse(response);
			return new PageImpl<>(bookDetails, PageRequest.of(page, pageSize, sort),
				response.getHits().getTotalHits().value);
		} catch (IOException e) {
			logger.severe("Elasticsearch 검색 요청 중 오류 발생: " + e.getMessage());
			throw new RuntimeException("검색 중 오류가 발생했습니다.", e);
		}
	}

	public Page<BookSearchResponse> searchAuthors(String query, Pageable pageable) throws IOException {
		int page = Math.max(pageable.getPageNumber() - 1, 0);
		int pageSize = pageable.getPageSize();
		Sort sort = pageable.getSort();
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

		List<BookSearchResponse> bookDetails = parseJsonResponse(bookResponse);
		return new PageImpl<>(bookDetails, PageRequest.of(page, pageSize, sort),
			bookResponse.getHits().getTotalHits().value);
	}

	public Page<BookSearchResponse> searchPublishers(String query, Pageable pageable) throws IOException {
		int page = Math.max(pageable.getPageNumber() - 1, 0);
		int pageSize = pageable.getPageSize();
		Sort sort = pageable.getSort();
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
			return new PageImpl<>(new ArrayList<>(),
				PageRequest.of(page, pageSize, sort), 0); // 출판사가 없을 경우 빈 리스트 반환
		}

		// Step 2: 책 검색
		SearchRequest bookSearchRequest = new SearchRequest("books");
		SearchSourceBuilder bookSourceBuilder = new SearchSourceBuilder();
		bookSourceBuilder.query(QueryBuilders.termQuery("publisher_id", publisherID)); // publisher_id로 검색
		bookSearchRequest.source(bookSourceBuilder);

		SearchResponse bookResponse = client.search(bookSearchRequest, RequestOptions.DEFAULT);
		logger.info("책 검색 응답: " + bookResponse.toString());

		List<BookSearchResponse> bookDetails = parseJsonResponse(bookResponse);
		return new PageImpl<>(bookDetails, PageRequest.of(page, pageSize, sort),
			bookResponse.getHits().getTotalHits().value);
	}

	public Page<BookSearchResponse> searchBooksByTag(String query, Pageable pageable) throws IOException {
		int page = Math.max(pageable.getPageNumber() - 1, 0);
		int pageSize = pageable.getPageSize();
		Sort sort = pageable.getSort();
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
			return new PageImpl<>(new ArrayList<>(),
				PageRequest.of(page, pageSize, sort), 0); // 해당 태그의 책이 없을 경우 빈 리스트 반환
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

		List<BookSearchResponse> bookDetails = parseJsonResponse(bookResponse);
		return new PageImpl<>(bookDetails, PageRequest.of(page, pageSize, sort),
			bookResponse.getHits().getTotalHits().value);
	}

	private List<BookSearchResponse> parseJsonResponse(SearchResponse response) {
		List<BookSearchResponse> bookDetails = new ArrayList<>();
		for (SearchHit hit : response.getHits().getHits()) {
			Map<String, Object> sourceAsMap = hit.getSourceAsMap();

			BookSearchResponse book = BookSearchResponse.builder()
				.bookId(Optional.ofNullable(sourceAsMap.get("bookId")).map(Object::toString).map(Long::parseLong).orElse(null))
				.authorName(Optional.ofNullable(sourceAsMap.get("authorName")).map(Object::toString).orElse(null))
				.publisherName(Optional.ofNullable(sourceAsMap.get("publisherName")).map(Object::toString).orElse(null))
				.bookStatusName(Optional.ofNullable(sourceAsMap.get("bookStatusName")).map(Object::toString).orElse(null))
				.bookTitle(Optional.ofNullable(sourceAsMap.get("bookTitle")).map(Object::toString).orElse(null))
				.bookDescription(Optional.ofNullable(sourceAsMap.get("bookDescription")).map(Object::toString).orElse(null))
				.bookQuantity(Optional.ofNullable(sourceAsMap.get("bookQuantity")).map(Object::toString).map(Integer::parseInt).orElse(null))
				.bookPublishDate(Optional.ofNullable(sourceAsMap.get("bookPublishDate")).map(Object::toString).map(Long::parseLong).map(Date::new).orElse(null))
				.bookIsbn(Optional.ofNullable(sourceAsMap.get("bookIsbn")).map(Object::toString).orElse(null))
				.bookPrice(Optional.ofNullable(sourceAsMap.get("bookPrice")).map(Object::toString).map(BigDecimal::new).orElse(null))
				.bookSalePrice(Optional.ofNullable(sourceAsMap.get("bookSalePrice")).map(Object::toString).map(BigDecimal::new).orElse(null))
				.bookSalePercent(Optional.ofNullable(sourceAsMap.get("bookSalePercent")).map(Object::toString).map(BigDecimal::new).orElse(null))
				.bookImageUrl(Optional.ofNullable(sourceAsMap.get("bookImageUrl")).map(Object::toString).orElse(null))
				.build();
			bookDetails.add(book);
		}
		return bookDetails;
	}
}
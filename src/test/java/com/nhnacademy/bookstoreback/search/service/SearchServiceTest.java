package com.nhnacademy.bookstoreback.search.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.bookstoreback.book.domain.entity.Book;
import com.nhnacademy.bookstoreback.book.repository.BookRepository;
import com.nhnacademy.bookstoreback.search.dto.reponse.BookSearchResponse;
import org.apache.lucene.search.TotalHits;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.io.IOException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SearchServiceTest {

	@Mock
	private RestHighLevelClient client;

	@Mock
	private BookRepository bookRepository;

	@InjectMocks
	private SearchService searchService;

	private ObjectMapper objectMapper;

	@BeforeEach
	void setUp() {
		objectMapper = new ObjectMapper();
	}

	@Test
	void searchBooks() throws IOException {
		String query = "허송세월";
		Pageable pageable = PageRequest.of(0, 20);

		String jsonResponse = "{"
			+ "\"hits\": {"
			+ "    \"total\": {"
			+ "        \"value\": 1,"
			+ "        \"relation\": \"eq\""
			+ "    },"
			+ "    \"hits\": ["
			+ "        {"
			+ "            \"_id\": \"1\","
			+ "            \"_source\": {"
			+ "                \"bookId\": 1307,"
			+ "                \"authorName\": \"김훈 (지은이)\","
			+ "                \"publisherName\": \"나남출판\","
			+ "                \"bookStatusName\": \"판매중\","
			+ "                \"bookTitle\": \"허송세월 - 한정부록 김훈 문장 엽서\","
			+ "                \"bookDescription\": \"삶의 어쩔 수 없는 비애와 아름다움을 누구보다 잘 이해하는 우리 시대의 문장가, 김훈. 그가 《연필로 쓰기》 이후 5년 만에 독자들을 다시 한번 사로잡을 산문으로 돌아왔다. 그 어느 때보다 치열하고 치밀했던 그의 ‘허송세월’을 담은 40여 편의 글이 실렸다.\","
			+ "                \"bookQuantity\": 87,"
			+ "                \"bookPublishDate\": \"2024-06-20\","
			+ "                \"bookIsbn\": \"9788930041683\","
			+ "                \"bookPrice\": 18000.00,"
			+ "                \"bookSalePrice\": 16200.00,"
			+ "                \"bookSalePercent\": 10.00,"
			+ "                \"bookImageUrl\": \"http://image.toast.com/aaaacuf/5ritang/books/허송세월 - 한정부록 김훈 문장 엽서.jpg\""
			+ "            }"
			+ "        }"
			+ "    ]"
			+ "},"
			+ "\"_shards\": {"
			+ "    \"total\": 1,"
			+ "    \"successful\": 1,"
			+ "    \"skipped\": 0,"
			+ "    \"failed\": 0"
			+ "},"
			+ "\"timed_out\": false,"
			+ "\"took\": 5"
			+ "}";

		SearchResponse searchResponse = mockElasticsearchResponse(jsonResponse);
		when(client.search(any(SearchRequest.class), any(RequestOptions.class))).thenReturn(searchResponse);

		Book book = new Book(); // Create and set properties for the Book object
		when(bookRepository.findById(any(Long.class))).thenReturn(Optional.of(book));

		Page<BookSearchResponse> result = searchService.searchBooks(query, pageable);

		assertEquals(1, result.getTotalElements());
		assertEquals("허송세월 - 한정부록 김훈 문장 엽서", result.getContent().get(0).getBookTitle());
	}

	@Test
	void searchAuthors() throws IOException {
		String query = "유시민";
		Pageable pageable = PageRequest.of(0, 20);

		// Mock Elasticsearch 응답 JSON (업데이트된 구조)
		String authorResponseJson = "{"
			+ "\"totalPages\": 1,"
			+ "\"totalElements\": 1,"
			+ "\"first\": true,"
			+ "\"last\": true,"
			+ "\"size\": 20,"
			+ "\"content\": ["
			+ "    {"
			+ "        \"bookId\": 1306,"
			+ "        \"authorName\": \"유시민 (지은이)\","
			+ "        \"publisherName\": \"생각의길\","
			+ "        \"bookStatusName\": \"판매중\","
			+ "        \"bookTitle\": \"그의 운명에 대한 아주 개인적인 생각\","
			+ "        \"bookDescription\": \"우리가 묻고 싶었던 것, 그리고 유시민의 답. 우리가 겪어낸 지난 2년을 정리하고 다시 해체해 냉철하게 원인과 결과를 분석하며 개인과 사회가 겪어야 했던 변화들을 일목요연하게 보여준다.\","
			+ "        \"bookQuantity\": 98,"
			+ "        \"bookPublishDate\": \"2024-06-19\","
			+ "        \"bookIsbn\": \"9788965138068\","
			+ "        \"bookPrice\": 16800.00,"
			+ "        \"bookSalePrice\": 15120.00,"
			+ "        \"bookSalePercent\": 10.00,"
			+ "        \"bookImageUrl\": \"http://image.toast.com/aaaacuf/5ritang/books/그의 운명에 대한 아주 개인적인 생각.jpg\""
			+ "    }"
			+ "],"
			+ "\"number\": 0,"
			+ "\"sort\": [],"
			+ "\"numberOfElements\": 1,"
			+ "\"pageable\": {"
			+ "    \"pageNumber\": 0,"
			+ "    \"pageSize\": 20,"
			+ "    \"sort\": [],"
			+ "    \"offset\": 0,"
			+ "    \"paged\": true,"
			+ "    \"unpaged\": false"
			+ "},"
			+ "\"empty\": false"
			+ "}";

		String booksResponseJson = "{"
			+ "\"hits\": {"
			+ "    \"total\": {"
			+ "        \"value\": 1,"
			+ "        \"relation\": \"eq\""
			+ "    },"
			+ "    \"hits\": ["
			+ "        {"
			+ "            \"_source\": {"
			+ "                \"bookId\": 1306,"
			+ "                \"bookTitle\": \"그의 운명에 대한 아주 개인적인 생각\""
			+ "            }"
			+ "        }"
			+ "    ]"
			+ "},"
			+ "\"_shards\": {"
			+ "    \"total\": 1,"
			+ "    \"successful\": 1,"
			+ "    \"skipped\": 0,"
			+ "    \"failed\": 0"
			+ "},"
			+ "\"timed_out\": false,"
			+ "\"took\": 5"
			+ "}";

		// Mock SearchResponse 객체 생성
		SearchResponse authorResponse = mockElasticsearchResponse(authorResponseJson);
		SearchResponse booksResponse = mockElasticsearchResponse(booksResponseJson);

		when(client.search(any(SearchRequest.class), any(RequestOptions.class)))
			.thenAnswer(invocation -> {
				SearchRequest request = invocation.getArgument(0);
				if (request.indices().equals("index-author")) {
					return authorResponse;
				} else if (request.indices().equals("books")) {
					return booksResponse;
				}
				return null;
			});

		Book book = new Book(); // Create and set properties for the Book object
		when(bookRepository.findById(any(Long.class))).thenReturn(Optional.of(book));

		Page<BookSearchResponse> result = searchService.searchAuthors(query, pageable);

		assertEquals(1, result.getTotalElements());
		assertEquals("그의 운명에 대한 아주 개인적인 생각", result.getContent().get(0).getBookTitle());
	}

	@Test
	void searchPublisher() throws IOException {
		String query = "생각의길";
		Pageable pageable = PageRequest.of(0, 20);

		// Mock Elasticsearch 응답 JSON
		String publisherResponseJson = "{"
			+ "\"totalPages\": 1,"
			+ "\"totalElements\": 1,"
			+ "\"first\": true,"
			+ "\"last\": true,"
			+ "\"size\": 20,"
			+ "\"content\": ["
			+ "    {"
			+ "        \"bookId\": 1306,"
			+ "        \"authorName\": \"유시민 (지은이)\","
			+ "        \"publisherName\": \"생각의길\","
			+ "        \"bookStatusName\": \"판매중\","
			+ "        \"bookTitle\": \"그의 운명에 대한 아주 개인적인 생각\","
			+ "        \"bookDescription\": \"우리가 묻고 싶었던 것, 그리고 유시민의 답. 우리가 겪어낸 지난 2년을 정리하고 다시 해체해 냉철하게 원인과 결과를 분석하며 개인과 사회가 겪어야 했던 변화들을 일목요연하게 보여준다.\","
			+ "        \"bookQuantity\": 98,"
			+ "        \"bookPublishDate\": \"2024-06-19\","
			+ "        \"bookIsbn\": \"9788965138068\","
			+ "        \"bookPrice\": 16800.00,"
			+ "        \"bookSalePrice\": 15120.00,"
			+ "        \"bookSalePercent\": 10.00,"
			+ "        \"bookImageUrl\": \"http://image.toast.com/aaaacuf/5ritang/books/그의 운명에 대한 아주 개인적인 생각.jpg\""
			+ "    }"
			+ "],"
			+ "\"number\": 0,"
			+ "\"sort\": [],"
			+ "\"numberOfElements\": 1,"
			+ "\"pageable\": {"
			+ "    \"pageNumber\": 0,"
			+ "    \"pageSize\": 20,"
			+ "    \"sort\": [],"
			+ "    \"offset\": 0,"
			+ "    \"paged\": true,"
			+ "    \"unpaged\": false"
			+ "},"
			+ "\"empty\": false"
			+ "}";

		// Mock SearchResponse 객체 생성
		SearchResponse publisherResponse = mockElasticsearchResponse(publisherResponseJson);

		// client.search() 메서드가 호출되면 위에서 설정한 publisherResponse를 반환하도록 설정
		when(client.search(any(SearchRequest.class), any(RequestOptions.class))).thenReturn(publisherResponse);

		// SearchService의 searchPublisher 메서드 호출
		Page<BookSearchResponse> result = searchService.searchPublishers(query, pageable);

		// 검증: 결과가 예상과 일치하는지 확인
		assertEquals(0, result.getTotalElements());
		assertEquals("그의 운명에 대한 아주 개인적인 생각", result.getContent().get(0).getBookTitle());
		assertEquals("생각의길", result.getContent().get(0).getPublisherName());
	}

	@Test
	void searchTag() throws IOException {
		String query = "밤에";
		Pageable pageable = PageRequest.of(0, 20);

		// Mock Elasticsearch 응답 JSON
		String tagResponseJson = "{"
			+ "\"totalPages\": 1,"
			+ "\"totalElements\": 1,"
			+ "\"first\": true,"
			+ "\"last\": true,"
			+ "\"size\": 20,"
			+ "\"content\": ["
			+ "    {"
			+ "        \"bookId\": 1309,"
			+ "        \"authorName\": \"이꽃님 (지은이)\","
			+ "        \"publisherName\": \"우리학교\","
			+ "        \"bookStatusName\": \"판매중\","
			+ "        \"bookTitle\": \"죽이고 싶은 아이 2\","
			+ "        \"bookDescription\": \"한국 청소년 문학의 역사를 새로 쓴 이꽃님의 『죽이고 싶은 아이』, 그 두 번째 이야기가 지금 우리에게 도착했다. 압도적인 몰입감과 휘몰아치는 전개로 수십만 독자를 단숨에 사로잡은 『죽이고 싶은 아이』의 명성을 고스란히 잇는, 기대 그 이상의 완벽한 속편의 모습으로.\","
			+ "        \"bookQuantity\": 77,"
			+ "        \"bookPublishDate\": \"2024-07-01\","
			+ "        \"bookIsbn\": \"9791167552723\","
			+ "        \"bookPrice\": 14000.00,"
			+ "        \"bookSalePrice\": 12600.00,"
			+ "        \"bookSalePercent\": 10.00,"
			+ "        \"bookImageUrl\": \"http://image.toast.com/aaaacuf/5ritang/books/죽이고 싶은 아이 2.jpg\""
			+ "    }"
			+ "],"
			+ "\"number\": 0,"
			+ "\"sort\": [],"
			+ "\"numberOfElements\": 1,"
			+ "\"pageable\": {"
			+ "    \"pageNumber\": 0,"
			+ "    \"pageSize\": 20,"
			+ "    \"sort\": [],"
			+ "    \"offset\": 0,"
			+ "    \"paged\": true,"
			+ "    \"unpaged\": false"
			+ "},"
			+ "\"empty\": false"
			+ "}";

		// Mock SearchResponse 객체 생성
		SearchResponse tagResponse = mockElasticsearchResponse(tagResponseJson);

		// client.search() 메서드가 호출되면 위에서 설정한 tagResponse를 반환하도록 설정
		when(client.search(any(SearchRequest.class), any(RequestOptions.class))).thenReturn(tagResponse);

		// SearchService의 searchTag 메서드 호출
		Page<BookSearchResponse> result = searchService.searchBooksByTag(query, pageable);

		// 검증: 결과가 예상과 일치하는지 확인
		assertEquals(0, result.getTotalElements());
		assertEquals("죽이고 싶은 아이 2", result.getContent().get(0).getBookTitle());
		assertEquals("이꽃님 (지은이)", result.getContent().get(0).getAuthorName());
		assertEquals("우리학교", result.getContent().get(0).getPublisherName());
	}

	private SearchResponse mockElasticsearchResponse(String jsonResponse) throws IOException {
		SearchResponse response = mock(SearchResponse.class);
		SearchHits hits = mock(SearchHits.class);

		// Parse the JSON response and create SearchHit objects
		List<SearchHit> searchHitList = new ArrayList<>();
		for (Map<String, Object> hit : parseHitsFromJson(jsonResponse)) {
			SearchHit searchHit = mock(SearchHit.class);
			when(searchHit.getSourceAsMap()).thenReturn(hit);
			when(searchHit.getSourceAsString()).thenReturn(objectMapper.writeValueAsString(hit));
			searchHitList.add(searchHit);
		}

		SearchHit[] searchHitsArray = searchHitList.toArray(new SearchHit[0]);

		when(hits.getHits()).thenReturn(searchHitsArray);
		when(hits.getTotalHits()).thenReturn(new TotalHits(searchHitList.size(), TotalHits.Relation.EQUAL_TO));
		when(response.getHits()).thenReturn(hits);

		return response;
	}


	private List<Map<String, Object>> parseHitsFromJson(String jsonResponse) throws IOException {
		JsonNode rootNode = objectMapper.readTree(jsonResponse);
		JsonNode hitsNode = rootNode.path("hits").path("hits");

		List<Map<String, Object>> hitsList = new ArrayList<>();
		for (JsonNode hitNode : hitsNode) {
			Map<String, Object> hitMap = objectMapper.convertValue(hitNode.path("_source"), new TypeReference<Map<String, Object>>() {});
			hitsList.add(hitMap);
		}
		return hitsList;
	}

}

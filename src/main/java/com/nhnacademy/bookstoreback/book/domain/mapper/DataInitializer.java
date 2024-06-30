package com.nhnacademy.bookstoreback.book.domain.mapper;

/**
 * 초기 데이터를 로드하는 클래스 (베스트 셀러 도서 정보를 로드)
 *
 * @author 김기욱
 * @version 1.0
 */
// @Component
// public class DataInitializer implements CommandLineRunner {
//
// 	private final BookService bookService;
//
// 	@Autowired
// 	public DataInitializer(BookService bookService) {
// 		this.bookService = bookService;
// 	}
//
// 	@Override
// 	public void run(String... args) throws Exception {
// 		String apiUrl = "http://www.aladin.co.kr/ttb/api/ItemList.aspx?ttbkey=ttb2897robo0933001&QueryType=Bestseller&MaxResults=50&start=1&SearchTarget=Book&output=js&Version=20131101";
// 		bookService.fetchAndSaveBooks(apiUrl);
// 	}
// }

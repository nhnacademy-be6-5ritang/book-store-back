package com.nhnacademy.bookstoreback.book.domain.mapper;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.nhnacademy.bookstoreback.book.domain.entity.Author;
import com.nhnacademy.bookstoreback.book.domain.entity.Book;
import com.nhnacademy.bookstoreback.book.domain.entity.BookStatus;
import com.nhnacademy.bookstoreback.book.domain.entity.Publisher;

/**
 * @author 김기욱
 * @version 1.0
 */
@Component
public class BookMapper {
	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

	public Book toEntity(Map<String, Object> item, Author author, Publisher publisher, BookStatus bookStatus) throws
		ParseException {
		Book book = new Book();

		// Set author, publisher, and book status
		book.setAuthor(author);
		book.setPublisher(publisher);
		book.setBookStatus(bookStatus);

		// Set book fields from API response
		book.setBookTitle((String)item.get("title"));
		book.setBookDescription((String)item.get("description"));
		book.setBookPublishDate(dateFormat.parse((String)item.get("pubDate")));
		book.setBookIsbn((String)item.get("isbn13"));

		// Set prices
		BigDecimal priceStandard = new BigDecimal((Integer)item.get("priceStandard"));
		BigDecimal priceSales = new BigDecimal((Integer)item.get("priceSales"));
		book.setBookPrice(priceStandard);
		book.setBookSalePrice(priceSales);

		// Calculate sale percent
		BigDecimal bookSalePercent = BigDecimal.ONE.subtract(
			priceSales.divide(priceStandard, 2, BigDecimal.ROUND_HALF_UP));
		book.setBookSalePercent(bookSalePercent);

		// Default values for packaging and index (set as per business logic)
		book.setBookPackaging(false);
		book.setBookIndex(""); // Assuming index is generated or set separately

		return book;
	}
}

package com.nhnacademy.bookstoreback.book.repository.impl;

import com.nhnacademy.bookstoreback.book.domain.dto.response.BookSearchResult;
import com.nhnacademy.bookstoreback.book.domain.dto.response.GetBookSimpleResponse;
import com.nhnacademy.bookstoreback.book.repository.CustomBookRepository;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static com.nhnacademy.bookstoreback.book.domain.entity.QBook.book;
import static com.nhnacademy.bookstoreback.book.domain.entity.QBookImage.bookImage;
import static com.nhnacademy.bookstoreback.category.domain.entity.QBookCategory.bookCategory;
import static com.nhnacademy.bookstoreback.category.domain.entity.QCategory.category;
import static com.nhnacademy.bookstoreback.image.domain.entity.QImage.image;

@RequiredArgsConstructor
public class CustomBookRepositoryImpl implements CustomBookRepository {
    private final JPAQueryFactory queryFactory;

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<GetBookSimpleResponse> findAllBooksByCategoryName(Pageable pageable, String categoryName) {
        List<GetBookSimpleResponse> result = queryFactory
                .select(Projections.constructor(
                        GetBookSimpleResponse.class,
                        book.bookId,
                        book.author.authorName,
                        book.bookTitle,
                        book.bookPrice,
                        book.bookSalePrice,
                        book.bookSalePercent,
                        image.imageUrl))
                .from(book)
                .leftJoin(bookImage).on(book.eq(bookImage.book))  // book 을 기준으로 bookImage 와 조인
                .join(bookCategory).on(book.eq(bookCategory.book))  // book 을 기준으로 bookCategory 와 조인
                .join(category).on(bookCategory.category.eq(category))  // bookCategory 의 category 와 category 조인
                .where(category.categoryName.eq(categoryName))
                .offset(pageable.getOffset()) // 페이지 시작점
                .limit(pageable.getPageSize()) // 페이지 크기
                .fetch();

        Long total = Optional.ofNullable(queryFactory
                .select(book.count())
                .from(book)
                .join(bookCategory).on(bookCategory.book.eq(book))
                .join(bookCategory.category, category)
                .where(category.categoryName.eq(categoryName))
                .fetchOne()).orElse(0L);

        return new PageImpl<>(result, pageable, total);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<BookSearchResult> findByBookTitleContainingIgnoreCaseCustom(String title) {
        return queryFactory
                .select(book.bookId, book.bookTitle)
                .from(book)
                .where(book.bookTitle.toLowerCase().contains(title.toLowerCase()))
                .fetch()
                .stream()
                .map(b -> new BookSearchResult(b.get(book.bookId), b.get(book.bookTitle)))
                .toList();
    }
}




package com.nhnacademy.bookstoreback.review.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.bookstoreback.auth.jwt.dto.CurrentUserDetails;
import com.nhnacademy.bookstoreback.image.domain.entity.Image;
import com.nhnacademy.bookstoreback.order.domain.entity.BookOrder;
import com.nhnacademy.bookstoreback.review.domain.dto.request.CreateReviewRequest;
import com.nhnacademy.bookstoreback.review.domain.dto.request.UpdateReviewRequest;
import com.nhnacademy.bookstoreback.review.domain.dto.response.GetBookOrderWithoutReviewResponse;
import com.nhnacademy.bookstoreback.review.domain.dto.response.GetReviewResponse;
import com.nhnacademy.bookstoreback.review.domain.entity.Review;
import com.nhnacademy.bookstoreback.review.domain.entity.ReviewImage;
import com.nhnacademy.bookstoreback.review.service.ReviewService;
import com.nhnacademy.bookstoreback.user.domain.entity.User;

@WebMvcTest(ReviewController.class)
class ReviewControllerTest {
	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private WebApplicationContext webApplicationContext;

	@MockBean
	private ReviewService reviewService;

	@Autowired
	private ObjectMapper objectMapper;

	private GetReviewResponse reviewResponse;
	private PageImpl<GetReviewResponse> reviewPage;
	private Review review;
	private ReviewImage reviewImage;

	@BeforeEach
	void setUp() {
		mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
		review = new Review(3, "comment", mock(BookOrder.class), mock(User.class));
		reviewImage = new ReviewImage(new Image("fileName", "imageUrl"), review);

		reviewResponse = GetReviewResponse.fromEntity(review, reviewImage);
		reviewPage = new PageImpl<>(Collections.singletonList(reviewResponse));
	}

	@Test
	void testGetReviews() throws Exception {
		when(reviewService.getReviews(any())).thenReturn(new PageImpl<>(Collections.singletonList(reviewResponse)));

		mockMvc.perform(get("/api/reviews/all/page"))
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.length()").value(11));
	}

	@Test
	void testGetReviewsByBookId() throws Exception {
		when(reviewService.getReviewsByBookId(anyLong(), any(Pageable.class))).thenReturn(reviewPage);

		mockMvc.perform(get("/api/books/1/reviews/all/page"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.content[0].reviewComment").value("comment"))
			.andExpect(jsonPath("$.content[0].reviewScore").value(3));
	}

	@Test
	@WithMockUser(roles = "MEMBER")
	void testCreateReview() throws Exception {
		CreateReviewRequest createReviewRequest = new CreateReviewRequest(1L, 1, "comment", "fileName.png");
		mockMvc.perform(post("/api/reviews")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(createReviewRequest)))
			.andExpect(status().isCreated());
	}

	@Test
	void testGetReview() throws Exception {
		when(reviewService.findReviewById(anyLong())).thenReturn(reviewResponse);

		mockMvc.perform(get("/api/reviews/1"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.reviewComment").value("comment"));
	}

	@Test
	@WithMockUser(roles = "MEMBER")
	void testUpdateReview() throws Exception {
		UpdateReviewRequest updateReviewRequest = new UpdateReviewRequest(1, "update comment");
		mockMvc.perform(put("/api/reviews/1")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(updateReviewRequest)))
			.andExpect(status().isOk());
	}

	@Test
	@WithMockUser(roles = "MEMBER")
	void testDeleteReview() throws Exception {
		mockMvc.perform(delete("/api/reviews/1"))
			.andExpect(status().isNoContent());
	}

	@Test
	void testGetReviewsAverageScoreByBookId() throws Exception {
		when(reviewService.getReviewsAverageScoreByBookId(anyLong())).thenReturn(4.5);

		mockMvc.perform(get("/api/books/1/reviews/average"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$").value(4.5));
	}

	@Test
	@WithMockUser(roles = "MEMBER")
	void testGetBooksByOrderStatusCompletionAndUserId() throws Exception {
		List<GetBookOrderWithoutReviewResponse> books = Collections.singletonList(
			new GetBookOrderWithoutReviewResponse(1L, "Book Title"));
		when(reviewService.getBooksWithoutReviews(any(CurrentUserDetails.class))).thenReturn(books);

		mockMvc.perform(get("/api/reviews/create/possible"))
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON));
	}

	@Test
	@WithMockUser(roles = "MEMBER")
	void testGetPhotoReviewsByBookId() throws Exception {
		when(reviewService.getPhotoReviewsByBookId(anyLong(), any(PageRequest.class))).thenReturn(reviewPage);

		mockMvc.perform(get("/api/books/1/reviews/photo/page"))
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.content.length()").value(1));
	}

	@Test
	@WithMockUser(roles = "MEMBER")
	void testGetGeneralReviewsByBookId() throws Exception {
		when(reviewService.getGeneralReviewsByBookId(anyLong(), any(PageRequest.class))).thenReturn(reviewPage);

		mockMvc.perform(get("/api/books/1/reviews/general/page"))
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.content.length()").value(1));
	}

	@Test
	@WithMockUser(roles = "MEMBER")
	void testGetReviewsByUserId() throws Exception {
		when(reviewService.getReviewsByUserId(any(PageRequest.class), any())).thenReturn(reviewPage);

		mockMvc.perform(get("/api/users/me/reviews/all/page"))
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.content.length()").value(1));
	}

	@Test
	@WithMockUser(roles = "MEMBER")
	void testGetGeneralReviewsByUserId() throws Exception {
		when(reviewService.getGeneralReviewsByUserId(any(PageRequest.class), any())).thenReturn(reviewPage);

		mockMvc.perform(get("/api/users/me/reviews/general/page"))
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.content.length()").value(1));
	}

	@Test
	@WithMockUser(roles = "MEMBER")
	void testGetPhotoReviewsByUserId() throws Exception {
		when(reviewService.getPhotoReviewsByUserId(any(PageRequest.class), any())).thenReturn(reviewPage);

		mockMvc.perform(get("/api/users/me/reviews/photo/page"))
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.content.length()").value(1));
	}
}

package com.nhnacademy.bookstoreback.order.domain.entity;

import java.math.BigDecimal;

import com.nhnacademy.bookstoreback.order.domain.dto.request.CreateWrappingRequest;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "wrappingPapers")
public class WrappingPaper {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "wrapping_paper_id")
	private Long wrappingPaperId;
	@Column(name = "wrapping_paper_name")
	private String wrappingPaperName;
	@Column(name = "wrapping_paper_content")
	private String wrappingPaperContent;
	@Column(name = "wrapping_paper_price")
	private BigDecimal wrappingPaperPrice;

	@ManyToOne
	@JoinColumn(name = "order_id")
	private Order order;

	@Builder
	public WrappingPaper(
		String wrappingPaperName,
		String wrappingPaperContent,
		BigDecimal wrappingPaperPrice,
		Order order) {
		this.wrappingPaperName = wrappingPaperName;
		this.wrappingPaperContent = wrappingPaperContent;
		this.wrappingPaperPrice = wrappingPaperPrice;
		this.order = order;
	}

	public static WrappingPaper toEntity(CreateWrappingRequest createWrappingRequest, Order order) {
		return WrappingPaper.builder()
			.wrappingPaperName(createWrappingRequest.wrappingPaperName())
			.wrappingPaperContent(createWrappingRequest.wrappingPaperContent())
			.wrappingPaperPrice(createWrappingRequest.wrappingPaperPrice())
			.order(order)
			.build();
	}

	public void update(String wrappingPaperName, String wrappingPaperContent, BigDecimal wrappingPaperPrice) {
		this.wrappingPaperName = wrappingPaperName;
		this.wrappingPaperContent = wrappingPaperContent;
		this.wrappingPaperPrice = wrappingPaperPrice;
	}
}

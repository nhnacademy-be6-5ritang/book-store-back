package com.nhnacademy.bookstoreback.order.domain.entity;

import java.math.BigDecimal;

import com.nhnacademy.bookstoreback.order.domain.dto.request.CreateWrappingRequest;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "WrappingPapers")
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

	@Builder
	public WrappingPaper(
		String wrappingPaperName,
		String wrappingPaperContent,
		BigDecimal wrappingPaperPrice) {
		this.wrappingPaperName = wrappingPaperName;
		this.wrappingPaperContent = wrappingPaperContent;
		this.wrappingPaperPrice = wrappingPaperPrice;
	}

	public static WrappingPaper toEntity(CreateWrappingRequest createWrappingRequest) {
		return WrappingPaper.builder()
			.wrappingPaperName(createWrappingRequest.wrappingPaperName())
			.wrappingPaperContent(createWrappingRequest.wrappingPaperContent())
			.wrappingPaperPrice(createWrappingRequest.wrappingPaperPrice())
			.build();
	}
}

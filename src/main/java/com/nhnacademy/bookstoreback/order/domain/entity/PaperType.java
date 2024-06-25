package com.nhnacademy.bookstoreback.order.domain.entity;

import java.math.BigDecimal;

import com.nhnacademy.bookstoreback.order.domain.dto.request.CreateWrappingTypeRequest;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "paper_type")
public class PaperType {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "paper_type_id")
	private Long paperTypeId;
	@Column(name = "paper_name")
	private String paperName;
	@Column(name = "paper_content")
	private String paperContent;
	@Column(name = "paper_price")
	private BigDecimal paperPrice;

	@Builder
	public PaperType(
		String paperName,
		String paperContent,
		BigDecimal paperPrice
	) {
		this.paperName = paperName;
		this.paperContent = paperContent;
		this.paperPrice = paperPrice;
	}

	public static PaperType toEntity(CreateWrappingTypeRequest createWrappingTypeRequest) {
		return PaperType.builder()
			.paperName(createWrappingTypeRequest.paperName())
			.paperContent(createWrappingTypeRequest.paperContent())
			.paperPrice(createWrappingTypeRequest.paperPrice())
			.build();
	}

	public void update(String paperName, String paperContent, BigDecimal paperPrice) {
		this.paperName = paperName;
		this.paperContent = paperContent;
		this.paperPrice = paperPrice;
	}
}

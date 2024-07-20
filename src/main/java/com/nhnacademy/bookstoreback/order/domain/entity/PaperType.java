package com.nhnacademy.bookstoreback.order.domain.entity;

import java.math.BigDecimal;

import com.nhnacademy.bookstoreback.order.domain.dto.request.CreateWrappingTypeRequest;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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

	@NotBlank
	@Size(max = 20)
	@Column(name = "paper_name", nullable = false)
	private String paperName;

	@NotBlank
	@Size(max = 200)
	@Column(name = "paper_content", nullable = false)
	private String paperContent;

	@NotNull
	@Column(name = "paper_price", nullable = false)
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

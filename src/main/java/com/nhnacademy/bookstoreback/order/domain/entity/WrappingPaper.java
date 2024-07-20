package com.nhnacademy.bookstoreback.order.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "wrapping_papers")
public class WrappingPaper {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "wrapping_paper_id")
	private Long wrappingPaperId;

	@NotNull
	@ManyToOne
	@JoinColumn(name = "order_list_id", nullable = false)
	private BookOrder bookOrder;

	@NotNull
	@ManyToOne
	@JoinColumn(name = "paper_type_id", nullable = false)
	private PaperType paperType;

	@NotNull
	@Column(name = "paper_quantity", nullable = false)
	private Integer paperQuantity;

	@Builder
	public WrappingPaper(
		BookOrder bookOrder,
		PaperType paperType,
		Integer paperQuantity) {
		this.bookOrder = bookOrder;
		this.paperType = paperType;
		this.paperQuantity = paperQuantity;
	}

	public static WrappingPaper toEntity(BookOrder bookOrder, PaperType paperType, Integer paperQuantity) {
		return WrappingPaper.builder()
			.bookOrder(bookOrder)
			.paperType(paperType)
			.paperQuantity(paperQuantity)
			.build();
	}

}

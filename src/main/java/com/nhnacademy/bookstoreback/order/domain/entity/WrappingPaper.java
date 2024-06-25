package com.nhnacademy.bookstoreback.order.domain.entity;

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
@Table(name = "wrapping_paper")
public class WrappingPaper {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "wrapping_paper_id")
	private Long wrappingPaperId;

	@ManyToOne
	@JoinColumn(name = "order_id")
	private Order order;

	@ManyToOne
	@JoinColumn(name = "paper_type_id")
	private PaperType paperType;

	@Builder
	public WrappingPaper(
		Order order,
		PaperType paperType) {
		this.order = order;
		this.paperType = paperType;
	}

	public static WrappingPaper toEntity(Order order, PaperType paperType) {
		return WrappingPaper.builder()
			.order(order)
			.paperType(paperType)
			.build();
	}

}

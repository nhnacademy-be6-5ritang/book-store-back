package com.nhnacademy.bookstoreback.category.domain.entity;

import com.nhnacademy.bookstoreback.category.domain.dto.request.CreateCategoryRequest;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * 카테고리 Entity
 *
 * @author 김기욱
 * @version 1.0
 */
@Entity
@Getter
@NoArgsConstructor
@Table(name = "categories")
public class Category {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "category_id")
	private Long categoryId;

	@ManyToOne
	@JoinColumn(name = "category_parent_id")
	private Category parentCategory;

	@NotBlank
	@Size(max = 20)
	@Field(type = FieldType.Text, analyzer = "nori_analyzer") // Nori 분석기 적용
	@Column(name = "category_name", length = 20)
	private String categoryName;

	@Builder
	public Category(String categoryName, Category parentCategory) {
		this.categoryName = categoryName;
		this.parentCategory = parentCategory;
	}

	public static Category toEntity(CreateCategoryRequest request, Category parentCategory) {
		return Category.builder()
			.categoryName(request.categoryName())
			.parentCategory(parentCategory)
			.build();
	}

	public void updateCategoryName(String categoryName, Category parentCategory) {
		this.categoryName = categoryName;
		this.parentCategory = parentCategory;
	}
}

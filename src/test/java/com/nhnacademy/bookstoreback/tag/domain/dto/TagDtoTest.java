package com.nhnacademy.bookstoreback.tag.domain.dto;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.nhnacademy.bookstoreback.tag.domain.dto.respnse.TagDto;
import com.nhnacademy.bookstoreback.tag.domain.entity.Tag;

class TagDtoTest {

	@Test
	void testFromEntity() {
		Tag tag = new Tag("Fiction");

		TagDto tagDto = TagDto.fromEntity(tag);

		assertNotNull(tagDto);
		assertEquals("Fiction", tagDto.tagName());
	}
}

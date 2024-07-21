package com.nhnacademy.bookstoreback.tag.domain.entity;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Constructor;

import org.junit.jupiter.api.Test;

import com.nhnacademy.bookstoreback.tag.domain.dto.respnse.TagDto;

class TagTest {

	@Test
	void testNoArgsConstructorIsProtected() {
		try {
			Constructor<Tag> constructor = Tag.class.getDeclaredConstructor();
			assertTrue(java.lang.reflect.Modifier.isProtected(constructor.getModifiers()),
				"Default constructor is not protected");
		} catch (NoSuchMethodException e) {
			fail("No default constructor found");
		}
		new Tag();
	}

	@Test
	void testTagBuilder() {
		String tagName = "exampleTag";

		Tag tag = Tag.builder()
			.tagName(tagName)
			.build();

		assertNull(tag.getTagId());
		assertEquals(tagName, tag.getTagName());
	}

	@Test
	void testTagToEntity() {
		String tagName = "exampleTag";
		TagDto tagDto = new TagDto(1L, tagName);

		Tag tag = Tag.toEntity(tagDto);

		assertEquals(tagName, tag.getTagName());
	}

	@Test
	void testUpdateTagName() {
		String initialTagName = "initialTag";
		String updatedTagName = "updatedTag";

		Tag tag = Tag.builder()
			.tagName(initialTagName)
			.build();

		tag.updateTagName(updatedTagName);

		assertEquals(updatedTagName, tag.getTagName());
	}
}
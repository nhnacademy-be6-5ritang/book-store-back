package com.nhnacademy.bookstoreback.upload.controller;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.multipart.MultipartFile;

import com.nhnacademy.bookstoreback.upload.service.impl.UploadServiceImpl;

@WebMvcTest(UploadController.class)
class UploadControllerTest {

	private MockMvc mockMvc;

	@MockBean
	private UploadServiceImpl uploadService;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		mockMvc = MockMvcBuilders.standaloneSetup(new UploadController(uploadService)).build();
	}

	@Test
	void testUploadImage() throws Exception {
		MockMultipartFile mockFile = new MockMultipartFile(
			"file",
			"test.jpg",
			MediaType.IMAGE_JPEG_VALUE,
			"test image content".getBytes()
		);

		when(uploadService.upload(any(MultipartFile.class), anyString())).thenReturn("Image uploaded successfully");

		mockMvc.perform(multipart("/api/uploads")
				.file(mockFile))
			.andExpect(status().isCreated());
	}
}

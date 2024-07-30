package com.nhnacademy.bookstoreback.order;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;

import com.nhnacademy.bookstoreback.global.exception.PaperFailException;
import com.nhnacademy.bookstoreback.global.exception.payload.ErrorStatus;
import com.nhnacademy.bookstoreback.order.domain.dto.request.CreateWrappingTypeRequest;
import com.nhnacademy.bookstoreback.order.domain.dto.request.UpdateWrappingTypeRequest;
import com.nhnacademy.bookstoreback.order.domain.dto.response.CreatePaperResponse;
import com.nhnacademy.bookstoreback.order.domain.dto.response.GetAdminAllPaperResponse;
import com.nhnacademy.bookstoreback.order.domain.dto.response.GetAllPaperResponse;
import com.nhnacademy.bookstoreback.order.domain.dto.response.GetPaperResponse;
import com.nhnacademy.bookstoreback.order.domain.entity.PaperType;
import com.nhnacademy.bookstoreback.order.repository.PaperTypeRepository;
import com.nhnacademy.bookstoreback.order.service.impl.PaperTypeServiceImpl;

class PaperTypeServiceImplTest {

	@InjectMocks
	private PaperTypeServiceImpl paperTypeService;

	@Mock
	private PaperTypeRepository paperTypeRepository;

	@BeforeEach
	void setup() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void testCreatePaper() {
		CreateWrappingTypeRequest request = new CreateWrappingTypeRequest("Paper Name", "Paper Content",
			BigDecimal.TEN);
		PaperType paperType = PaperType.toEntity(request);

		when(paperTypeRepository.save(any(PaperType.class))).thenReturn(paperType);

		CreatePaperResponse expectedResponse = CreatePaperResponse.builder()
			.paperName(paperType.getPaperName())
			.paperContent(paperType.getPaperContent())
			.paperPrice(paperType.getPaperPrice())
			.build();

		CreatePaperResponse result = paperTypeService.createPaper(request);

		assertThat(result).isEqualTo(expectedResponse);
		verify(paperTypeRepository).save(any(PaperType.class));
	}

	@Test
	void testGetAllPaperTypes() {
		PaperType paperType = new PaperType("Paper Name", "Paper Content", BigDecimal.TEN);
		when(paperTypeRepository.findAll()).thenReturn(Collections.singletonList(paperType));

		GetAllPaperResponse expectedResponse = GetAllPaperResponse.builder()
			.papers(Collections.singletonList(
				GetPaperResponse.builder()
					.paperName(paperType.getPaperName())
					.paperContent(paperType.getPaperContent())
					.paperPrice(paperType.getPaperPrice())
					.build()
			))
			.build();

		GetAllPaperResponse result = paperTypeService.getAllPaperTypes();

		assertThat(result).isEqualTo(expectedResponse);
		verify(paperTypeRepository).findAll();
	}

	@Test
	void testGetAdminAllPaperTypes() {
		PaperType paperType = new PaperType("Paper Name", "Paper Content", BigDecimal.TEN);
		when(paperTypeRepository.findAll()).thenReturn(Collections.singletonList(paperType));

		GetAdminAllPaperResponse expectedResponse = GetAdminAllPaperResponse.builder()
			.papers(Collections.singletonList(
				GetPaperResponse.builder()
					.paperName(paperType.getPaperName())
					.paperContent(paperType.getPaperContent())
					.paperPrice(paperType.getPaperPrice())
					.build()
			))
			.build();

		GetAdminAllPaperResponse result = paperTypeService.getAdminAllPaperTypes();

		assertThat(result).isEqualTo(expectedResponse);
		verify(paperTypeRepository).findAll();
	}

	@Test
	void testGetPaperTypeById_Success() {
		PaperType paperType = new PaperType("Paper Name", "Paper Content", BigDecimal.TEN);
		when(paperTypeRepository.findById(1L)).thenReturn(Optional.of(paperType));

		GetPaperResponse expectedResponse = GetPaperResponse.builder()
			.paperName(paperType.getPaperName())
			.paperContent(paperType.getPaperContent())
			.paperPrice(paperType.getPaperPrice())
			.build();

		GetPaperResponse result = paperTypeService.getPaperTypeById(1L);

		assertThat(result).isEqualTo(expectedResponse);
		verify(paperTypeRepository).findById(1L);
	}

	@Test
	void testGetPaperTypeById_NotFound() {
		when(paperTypeRepository.findById(1L)).thenReturn(Optional.empty());

		ErrorStatus expectedErrorStatus = ErrorStatus.from(
			"포장지를 가져올 수 없습니다",
			HttpStatus.NOT_FOUND,
			LocalDateTime.now()
		);

		PaperFailException thrown = assertThrows(PaperFailException.class, () -> paperTypeService.getPaperTypeById(1L));
		ErrorStatus actualErrorStatus = thrown.getErrorStatus();

		assertThat(actualErrorStatus.getMessage()).isEqualTo(expectedErrorStatus.getMessage());
		assertThat(actualErrorStatus.getStatus()).isEqualTo(expectedErrorStatus.getStatus());
		verify(paperTypeRepository).findById(1L);
	}

	@Test
	void testUpdatePaperTypeById_Success() {
		Long id = 1L;
		UpdateWrappingTypeRequest request = new UpdateWrappingTypeRequest("Updated Name", "Updated Content",
			BigDecimal.ONE);
		PaperType existingPaperType = new PaperType("Old Name", "Old Content", BigDecimal.ZERO);
		PaperType updatedPaperType = new PaperType("Updated Name", "Updated Content", BigDecimal.ONE);

		when(paperTypeRepository.findById(id)).thenReturn(Optional.of(existingPaperType));
		when(paperTypeRepository.save(any(PaperType.class))).thenReturn(updatedPaperType);

		GetPaperResponse expectedResponse = GetPaperResponse.builder()
			.paperName(updatedPaperType.getPaperName())
			.paperContent(updatedPaperType.getPaperContent())
			.paperPrice(updatedPaperType.getPaperPrice())
			.build();

		GetPaperResponse result = paperTypeService.updatePaperTypeById(id, request);

		assertThat(result).isEqualTo(expectedResponse);
		verify(paperTypeRepository).findById(id);
		verify(paperTypeRepository).save(any(PaperType.class));
	}

	@Test
	void testUpdatePaperTypeById_NotFound() {
		Long id = 1L;
		UpdateWrappingTypeRequest request = new UpdateWrappingTypeRequest("Updated Name", "Updated Content",
			BigDecimal.ONE);

		when(paperTypeRepository.findById(id)).thenReturn(Optional.empty());

		ErrorStatus expectedErrorStatus = ErrorStatus.from(
			"포장지를 가져올 수 없습니다",
			HttpStatus.NOT_FOUND,
			LocalDateTime.now()
		);

		PaperFailException thrown = assertThrows(PaperFailException.class,
			() -> paperTypeService.updatePaperTypeById(id, request));
		ErrorStatus actualErrorStatus = thrown.getErrorStatus();

		assertThat(actualErrorStatus.getMessage()).isEqualTo(expectedErrorStatus.getMessage());
		assertThat(actualErrorStatus.getStatus()).isEqualTo(expectedErrorStatus.getStatus());
		verify(paperTypeRepository).findById(id);
		verify(paperTypeRepository, never()).save(any(PaperType.class));
	}

	@Test
	void testDeletePaperTypeById() {
		doNothing().when(paperTypeRepository).deleteById(1L);

		paperTypeService.deletePaperTypeById(1L);

		verify(paperTypeRepository).deleteById(1L);
	}
}

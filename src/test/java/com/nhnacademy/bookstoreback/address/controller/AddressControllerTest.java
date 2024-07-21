package com.nhnacademy.bookstoreback.address.controller;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.nhnacademy.bookstoreback.address.domain.dto.request.RegisterAddressRequest;
import com.nhnacademy.bookstoreback.address.domain.dto.request.UpdateAddressRequest;
import com.nhnacademy.bookstoreback.address.domain.dto.response.GetAddressResponse;
import com.nhnacademy.bookstoreback.address.domain.dto.response.RegisterAddressResponse;
import com.nhnacademy.bookstoreback.address.domain.dto.response.UpdateAddressResponse;
import com.nhnacademy.bookstoreback.address.service.AddressService;
import com.nhnacademy.bookstoreback.auth.jwt.dto.CurrentUserDetails;
import com.nhnacademy.bookstoreback.user.domain.dto.response.UserTokenInfo;

@WebMvcTest(AddressController.class)
class AddressControllerTest {

	private MockMvc mockMvc;

	@MockBean
	private AddressService addressService;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.initMocks(this);
		mockMvc = MockMvcBuilders.standaloneSetup(new AddressController(addressService)).build();
	}

	@Test
	@WithMockUser(roles = "MEMBER")
	void getAddresses() throws Exception {
		UserTokenInfo userTokenInfo = UserTokenInfo.builder()
			.id(1L)
			.password("password")
			.roles(List.of("MEMBER"))
			.status("ACTIVE")
			.build();
		CurrentUserDetails currentUserDetails = new CurrentUserDetails(userTokenInfo);

		List<GetAddressResponse> mockAddresses = Arrays.asList(
			GetAddressResponse.builder()
				.id(1L)
				.postCode("10001")
				.baseAddress("123 Main St")
				.detailAddress("Apt 1")
				.alias("Home")
				.isDefault(true)
				.build(),
			GetAddressResponse.builder()
				.id(2L)
				.postCode("90001")
				.baseAddress("456 Elm St")
				.detailAddress("Apt 2")
				.alias("Office")
				.isDefault(false)
				.build()
		);

		when(addressService.getAddresses(any(CurrentUserDetails.class))).thenReturn(mockAddresses);

		mockMvc.perform(get("/api/addresses")
				.principal(() -> "testUser")
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(content().json(
				"[{'id':1,'postCode':'10001','baseAddress':'123 Main St','detailAddress':'Apt 1','alias':'Home','isDefault':true},{'id':2,'postCode':'90001','baseAddress':'456 Elm St','detailAddress':'Apt 2','alias':'Office','isDefault':false}]"));
	}

	@Test
	@WithMockUser(roles = "MEMBER")
	void registerAddress() throws Exception {
		RegisterAddressRequest request = RegisterAddressRequest.builder()
			.alias("Home")
			.postCode("10001")
			.baseAddress("123 Main St")
			.detailAddress("Apt 1")
			.build();

		RegisterAddressResponse response = RegisterAddressResponse.builder()
			.id(1L)
			.userId(1L)
			.postCode("10001")
			.baseAddress("123 Main St")
			.detailAddress("Apt 1")
			.alias("Home")
			.build();

		when(addressService.registerAddress(any(CurrentUserDetails.class),
			any(RegisterAddressRequest.class))).thenReturn(response);

		mockMvc.perform(post("/api/addresses")
				.principal(() -> "testUser")
				.contentType(MediaType.APPLICATION_JSON)
				.content(
					"{\"alias\":\"Home\",\"postCode\":\"10001\",\"baseAddress\":\"123 Main St\",\"detailAddress\":\"Apt 1\"}"))
			.andExpect(status().isCreated())
			.andExpect(content().json(
				"{'id':1,'userId':1,'postCode':'10001','baseAddress':'123 Main St','detailAddress':'Apt 1','alias':'Home'}"));
	}

	@Test
	@WithMockUser(roles = "MEMBER")
	void getDefaultAddress() throws Exception {
		GetAddressResponse response = GetAddressResponse.builder()
			.id(1L)
			.postCode("10001")
			.baseAddress("123 Main St")
			.detailAddress("Apt 1")
			.alias("Home")
			.isDefault(true)
			.build();

		when(addressService.getDefaultAddress(any(CurrentUserDetails.class))).thenReturn(Optional.of(response));

		mockMvc.perform(get("/api/addresses/default")
				.principal(() -> "testUser")
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(content().json(
				"{'id':1,'postCode':'10001','baseAddress':'123 Main St','detailAddress':'Apt 1','alias':'Home','isDefault':true}"));
	}

	@Test
	@WithMockUser(roles = "MEMBER")
	void updateAddress() throws Exception {
		UpdateAddressRequest request = new UpdateAddressRequest("Home", "10001", "123 Main St", "Apt 1");
		UpdateAddressResponse response = UpdateAddressResponse.builder()
			.id(1L)
			.userId(1L)
			.postCode("10001")
			.baseAddress("123 Main St")
			.detailAddress("Apt 1")
			.alias("Home")
			.build();

		when(addressService.updateAddress(any(CurrentUserDetails.class), any(Long.class),
			any(UpdateAddressRequest.class))).thenReturn(response);

		mockMvc.perform(put("/api/addresses/1")
				.principal(() -> "testUser")
				.contentType(MediaType.APPLICATION_JSON)
				.content(
					"{\"alias\":\"Home\",\"postCode\":\"10001\",\"baseAddress\":\"123 Main St\",\"detailAddress\":\"Apt 1\"}"))
			.andExpect(status().isOk())
			.andExpect(content().json(
				"{'id':1,'userId':1,'postCode':'10001','baseAddress':'123 Main St','detailAddress':'Apt 1','alias':'Home'}"));
	}

	@Test
	@WithMockUser(roles = "MEMBER")
	void deleteAddress() throws Exception {
		mockMvc.perform(delete("/api/addresses/1")
				.principal(() -> "testUser")
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isNoContent());
	}

	@Test
	@WithMockUser(roles = "MEMBER")
	void setDefaultAddress() throws Exception {
		mockMvc.perform(put("/api/addresses/1/default")
				.principal(() -> "testUser")
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk());
	}
}

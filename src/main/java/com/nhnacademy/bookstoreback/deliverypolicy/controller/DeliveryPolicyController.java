package com.nhnacademy.bookstoreback.deliverypolicy.controller;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nhnacademy.bookstoreback.auth.annotation.AuthorizeRole;
import com.nhnacademy.bookstoreback.deliverypolicy.domain.dto.request.CreateDeliveryPolicyRequest;
import com.nhnacademy.bookstoreback.deliverypolicy.domain.dto.request.UpdateDeliveryPolicyRequest;
import com.nhnacademy.bookstoreback.deliverypolicy.domain.dto.response.GetDeliveryPoliciesResponse;
import com.nhnacademy.bookstoreback.deliverypolicy.domain.dto.response.GetDeliveryPolicyResponse;
import com.nhnacademy.bookstoreback.deliverypolicy.service.DeliveryPolicyService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/deliveryPolicies")
public class DeliveryPolicyController {
	private final DeliveryPolicyService deliveryPolicyService;

	@GetMapping
	public ResponseEntity<List<GetDeliveryPoliciesResponse>> getDeliveryPolicies() {
		return ResponseEntity.status(HttpStatus.OK).body(deliveryPolicyService.getDeliveryPolicies());
	}

	@GetMapping("/{deliveryPolicyId}")
	public ResponseEntity<GetDeliveryPolicyResponse> getDeliveryPolicy(@PathVariable Long deliveryPolicyId) {
		return ResponseEntity.status(HttpStatus.OK).body(deliveryPolicyService.getDeliveryPolicy(deliveryPolicyId));
	}

	@AuthorizeRole({"DELIVERY_ADMIN", "HEAD_ADMIN"})
	@PostMapping
	public ResponseEntity<Void> createDeliveryPolicy(
		@Valid @RequestBody CreateDeliveryPolicyRequest request) {
		deliveryPolicyService.createDeliveryPolicy(request);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

	@AuthorizeRole({"DELIVERY_ADMIN", "HEAD_ADMIN"})
	@PutMapping("/{deliveryPolicyId}")
	public ResponseEntity<Void> updateDeliveryPolicy(@PathVariable Long deliveryPolicyId,
		@Valid @RequestBody UpdateDeliveryPolicyRequest request) {
		deliveryPolicyService.updateDeliveryPolicy(deliveryPolicyId, request);
		return ResponseEntity.status(HttpStatus.OK).build();
	}

	@AuthorizeRole({"DELIVERY_ADMIN", "HEAD_ADMIN"})
	@DeleteMapping("/{deliveryPolicyId}")
	public ResponseEntity<Void> deleteDeliveryPolicy(@PathVariable Long deliveryPolicyId) {
		deliveryPolicyService.deleteDeliveryPolicy(deliveryPolicyId);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}

	@PutMapping("/{deliveryId}/{price}/addPolicies")
	public ResponseEntity<GetDeliveryPolicyResponse> addPolicy(@PathVariable Long deliveryId,
		@PathVariable BigDecimal price) {
		return ResponseEntity.status(HttpStatus.OK)
			.body(
				deliveryPolicyService
					.findByDeliveryPolicyStandardPriceLessThanEqualOrderByDeliveryPolicyStandardPriceDesc(deliveryId,
						price));
	}
}

package com.edu.aniket.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.edu.aniket.config.ResponseStructure;
import com.edu.aniket.dto.PageResponse;
import com.edu.aniket.entity.FoodProduct;
import com.edu.aniket.service.FoodProductService;

@RestController
@RequestMapping("/foodproduct")
public class FoodProductController {

	private final FoodProductService foodProductService;

	@Autowired
	public FoodProductController(FoodProductService foodProductService) {
		this.foodProductService = foodProductService;
	}

	@PostMapping("/save")
	@PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
	public ResponseEntity<ResponseStructure<FoodProduct>> saveFoodProduct(
			@RequestBody FoodProduct foodProduct,
			@RequestParam(required = false, defaultValue = "0") long userId
	) {
		return foodProductService.saveFoodProduct(foodProduct, userId);
	}

	@GetMapping("/findById")
	public ResponseEntity<ResponseStructure<FoodProduct>> findFoodProductById(@RequestParam long id) {
		return foodProductService.findFoodProductById(id);
	}

	@GetMapping("/{id}")
	public ResponseEntity<ResponseStructure<FoodProduct>> getFoodProductById(@PathVariable long id) {
		return foodProductService.findFoodProductById(id);
	}

	@GetMapping("/findAll")
	public ResponseEntity<ResponseStructure<PageResponse<FoodProduct>>> findAllFoodProducts(
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size,
			@RequestParam(defaultValue = "id,asc") String sort
	) {
		return foodProductService.findAllFoodProductsPaginated(page, size, sort);
	}

	@DeleteMapping("/delete")
	@PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
	public ResponseEntity<ResponseStructure<String>> deleteFoodProductById(@RequestParam long id) {
		return foodProductService.deleteFoodProductById(id);
	}

	@DeleteMapping("/{id}")
	@PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
	public ResponseEntity<ResponseStructure<String>> removeFoodProductById(@PathVariable long id) {
		return foodProductService.deleteFoodProductById(id);
	}

	@PutMapping("/update")
	@PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
	public ResponseEntity<ResponseStructure<FoodProduct>> updateFoodProduct(@RequestBody FoodProduct foodProduct) {
		return foodProductService.updateFoodProduct(foodProduct);
	}
}

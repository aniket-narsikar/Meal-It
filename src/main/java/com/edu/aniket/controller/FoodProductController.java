package com.edu.aniket.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.edu.aniket.config.ResponseStructure;
import com.edu.aniket.entity.FoodProduct;
import com.edu.aniket.service.FoodProductService;

@RestController
@RequestMapping("/foodproduct")
public class FoodProductController {

	@Autowired
	private FoodProductService foodProductService;

	@PostMapping("/save")
	public ResponseEntity<ResponseStructure<FoodProduct>> saveFoodProduct(@RequestBody FoodProduct foodProduct, @RequestParam long userId) {
		return foodProductService.saveFoodProduct(foodProduct, userId);
	}

	@GetMapping("/findById")
	public ResponseEntity<ResponseStructure<FoodProduct>> findFoodProductById(@RequestParam long id) {
		return foodProductService.findFoodProductById(id);
	}

	@GetMapping("/findAll")
	public ResponseEntity<ResponseStructure<List<FoodProduct>>> findAllFoodProducts() {
		return foodProductService.findAllFoodProducts();
	}

	@DeleteMapping("/delete")
	public ResponseEntity<ResponseStructure<String>> deleteFoodProductById(@RequestParam long id) {
		return foodProductService.deleteFoodProductById(id);
	}

	@PutMapping("/update")
	public ResponseEntity<ResponseStructure<FoodProduct>> updateFoodProduct(@RequestBody FoodProduct foodProduct) {
		return foodProductService.updateFoodProduct(foodProduct);
	}
}

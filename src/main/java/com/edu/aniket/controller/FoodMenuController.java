package com.edu.aniket.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.edu.aniket.config.ResponseStructure;
import com.edu.aniket.dto.PageResponse;
import com.edu.aniket.entity.FoodMenu;
import com.edu.aniket.service.FoodMenuService;

@RestController
@RequestMapping("/foodmenu")
public class FoodMenuController {

	private final FoodMenuService foodMenuService;

	@Autowired
	public FoodMenuController(FoodMenuService foodMenuService) {
		this.foodMenuService = foodMenuService;
	}

	@PostMapping("/save")
	public ResponseEntity<ResponseStructure<FoodMenu>> saveFoodMenu(@RequestBody FoodMenu foodMenu) {
		return foodMenuService.saveFoodMenu(foodMenu);
	}

	@GetMapping("/findById")
	public ResponseEntity<ResponseStructure<FoodMenu>> findFoodMenuById(@RequestParam long id) {
		return foodMenuService.findFoodMenuById(id);
	}

	@GetMapping("/{id}")
	public ResponseEntity<ResponseStructure<FoodMenu>> getFoodMenuById(@PathVariable long id) {
		return foodMenuService.findFoodMenuById(id);
	}

	@GetMapping("/findAll")
	public ResponseEntity<ResponseStructure<PageResponse<FoodMenu>>> findAllFoodMenus(
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size,
			@RequestParam(defaultValue = "id,asc") String sort
	) {
		return foodMenuService.findAllFoodMenusPaginated(page, size, sort);
	}

	@DeleteMapping("/delete")
	public ResponseEntity<ResponseStructure<String>> deleteFoodMenuById(@RequestParam long id) {
		return foodMenuService.deleteFoodMenuById(id);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<ResponseStructure<String>> removeFoodMenuById(@PathVariable long id) {
		return foodMenuService.deleteFoodMenuById(id);
	}
}

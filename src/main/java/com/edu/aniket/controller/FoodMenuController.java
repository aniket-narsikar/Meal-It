package com.edu.aniket.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.edu.aniket.config.ResponseStructure;
import com.edu.aniket.entity.FoodMenu;
import com.edu.aniket.service.FoodMenuService;

@RestController
@RequestMapping("/foodmenu")
public class FoodMenuController {

	@Autowired
	private FoodMenuService foodMenuService;

	@PostMapping("/save")
	public ResponseEntity<ResponseStructure<FoodMenu>> saveFoodMenu(@RequestBody FoodMenu foodMenu) {
		return foodMenuService.saveFoodMenu(foodMenu);
	}

	@GetMapping("/findById")
	public ResponseEntity<ResponseStructure<FoodMenu>> findFoodMenuById(@RequestParam long id) {
		return foodMenuService.findFoodMenuById(id);
	}

	@GetMapping("/findAll")
	public ResponseEntity<ResponseStructure<List<FoodMenu>>> findAllFoodMenus() {
		return foodMenuService.findAllFoodMenus();
	}

	@DeleteMapping("/delete")
	public ResponseEntity<ResponseStructure<String>> deleteFoodMenuById(@RequestParam long id) {
		return foodMenuService.deleteFoodMenuById(id);
	}
}

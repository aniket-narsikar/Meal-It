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
import com.edu.aniket.entity.FoodOrder;
import com.edu.aniket.entity.Status;
import com.edu.aniket.service.FoodOrderService;

@RestController
@RequestMapping("/foodorder")
public class FoodOrderController {

	@Autowired
	private FoodOrderService foodOrderService;

	@PostMapping("/save")
	public ResponseEntity<ResponseStructure<FoodOrder>> saveFoodOrder(@RequestBody FoodOrder foodOrder, @RequestParam long userId) {
		return foodOrderService.saveFoodOrder(foodOrder, userId);
	}

	@GetMapping("/findById")
	public ResponseEntity<ResponseStructure<FoodOrder>> findFoodOrderById(@RequestParam long id) {
		return foodOrderService.findFoodOrderById(id);
	}

	@GetMapping("/findAll")
	public ResponseEntity<ResponseStructure<List<FoodOrder>>> findAllFoodOrders() {
		return foodOrderService.findAllFoodOrders();
	}

	@DeleteMapping("/delete")
	public ResponseEntity<ResponseStructure<String>> deleteFoodOrderById(@RequestParam long id) {
		return foodOrderService.deleteFoodOrderById(id);
	}

	@PutMapping("/updateStatus")
	public ResponseEntity<ResponseStructure<FoodOrder>> updateOrderStatus(@RequestParam long id, @RequestParam Status status) {
		return foodOrderService.updateOrderStatus(id, status);
	}
}

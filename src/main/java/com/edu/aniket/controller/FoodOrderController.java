package com.edu.aniket.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
import com.edu.aniket.entity.FoodOrder;
import com.edu.aniket.entity.Status;
import com.edu.aniket.service.FoodOrderService;

@RestController
@RequestMapping("/foodorder")
public class FoodOrderController {

	private final FoodOrderService foodOrderService;

	@Autowired
	public FoodOrderController(FoodOrderService foodOrderService) {
		this.foodOrderService = foodOrderService;
	}

	@PostMapping("/save")
	public ResponseEntity<ResponseStructure<FoodOrder>> saveFoodOrder(@RequestBody FoodOrder foodOrder, @RequestParam long userId) {
		return foodOrderService.saveFoodOrder(foodOrder, userId);
	}

	@GetMapping("/findById")
	public ResponseEntity<ResponseStructure<FoodOrder>> findFoodOrderById(@RequestParam long id) {
		return foodOrderService.findFoodOrderById(id);
	}

	@GetMapping("/{id}")
	public ResponseEntity<ResponseStructure<FoodOrder>> getFoodOrderById(@PathVariable long id) {
		return foodOrderService.findFoodOrderById(id);
	}

	@GetMapping("/findAll")
	public ResponseEntity<ResponseStructure<PageResponse<FoodOrder>>> findAllFoodOrders(
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size,
			@RequestParam(defaultValue = "id,asc") String sort
	) {
		return foodOrderService.findAllFoodOrdersPaginated(page, size, sort);
	}

	@DeleteMapping("/delete")
	public ResponseEntity<ResponseStructure<String>> deleteFoodOrderById(@RequestParam long id) {
		return foodOrderService.deleteFoodOrderById(id);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<ResponseStructure<String>> removeFoodOrderById(@PathVariable long id) {
		return foodOrderService.deleteFoodOrderById(id);
	}

	@PutMapping("/updateStatus")
	public ResponseEntity<ResponseStructure<FoodOrder>> updateOrderStatus(@RequestParam long id, @RequestParam Status status) {
		return foodOrderService.updateOrderStatus(id, status);
	}
}

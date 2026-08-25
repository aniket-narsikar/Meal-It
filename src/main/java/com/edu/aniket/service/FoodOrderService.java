package com.edu.aniket.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.edu.aniket.config.ResponseStructure;
import com.edu.aniket.dao.FoodOrderDao;
import com.edu.aniket.dao.UserDao;
import com.edu.aniket.entity.FoodOrder;
import com.edu.aniket.entity.FoodProduct;
import com.edu.aniket.entity.Status;
import com.edu.aniket.entity.User;

@Service
public class FoodOrderService {

	@Autowired
	private FoodOrderDao foodOrderDao;

	@Autowired
	private UserDao userDao;

	public ResponseEntity<ResponseStructure<FoodOrder>> saveFoodOrder(FoodOrder foodOrder, long userId) {
		User user = userDao.findUserById(userId);
		if (foodOrder.getFoodStatus() == null) {
			foodOrder.setFoodStatus(Status.PLACED);
		}
		if (foodOrder.getProducts() != null && !foodOrder.getProducts().isEmpty()) {
			double total = 0;
			for (FoodProduct product : foodOrder.getProducts()) {
				total += product.getTotalPrice();
			}
			foodOrder.setTotalPrice(total);
		}
		FoodOrder savedOrder = foodOrderDao.saveFoodOrder(foodOrder);

		if (user.getFoodOrders() == null) {
			List<FoodOrder> orders = new ArrayList<>();
			orders.add(savedOrder);
			user.setFoodOrders(orders);
		} else {
			user.getFoodOrders().add(savedOrder);
		}
		userDao.updateUser(user);

		ResponseStructure<FoodOrder> responseStructure = new ResponseStructure<>();
		responseStructure.setData(savedOrder);
		responseStructure.setMessage("Food Order Placed Successfully");
		responseStructure.setStatus(HttpStatus.CREATED.value());
		return new ResponseEntity<>(responseStructure, HttpStatus.CREATED);
	}

	public ResponseEntity<ResponseStructure<FoodOrder>> findFoodOrderById(long foodOrderId) {
		FoodOrder foodOrder = foodOrderDao.findFoodOrderById(foodOrderId);
		ResponseStructure<FoodOrder> responseStructure = new ResponseStructure<>();
		responseStructure.setData(foodOrder);
		responseStructure.setMessage("Food Order Found Successfully");
		responseStructure.setStatus(HttpStatus.OK.value());
		return new ResponseEntity<>(responseStructure, HttpStatus.OK);
	}

	public ResponseEntity<ResponseStructure<List<FoodOrder>>> findAllFoodOrders() {
		List<FoodOrder> foodOrders = foodOrderDao.findAllFoodOrder();
		ResponseStructure<List<FoodOrder>> responseStructure = new ResponseStructure<>();
		responseStructure.setData(foodOrders);
		responseStructure.setMessage("All Food Orders Retrieved Successfully");
		responseStructure.setStatus(HttpStatus.OK.value());
		return new ResponseEntity<>(responseStructure, HttpStatus.OK);
	}

	public ResponseEntity<ResponseStructure<String>> deleteFoodOrderById(long foodOrderId) {
		foodOrderDao.removeFoodOrderById(foodOrderId);
		ResponseStructure<String> responseStructure = new ResponseStructure<>();
		responseStructure.setData("Food Order with ID " + foodOrderId + " deleted successfully");
		responseStructure.setMessage("Food Order Deleted");
		responseStructure.setStatus(HttpStatus.OK.value());
		return new ResponseEntity<>(responseStructure, HttpStatus.OK);
	}

	public ResponseEntity<ResponseStructure<FoodOrder>> updateOrderStatus(long foodOrderId, Status status) {
		FoodOrder foodOrder = foodOrderDao.findFoodOrderById(foodOrderId);
		foodOrder.setFoodStatus(status);
		FoodOrder updatedOrder = foodOrderDao.updateFoodOrder(foodOrder);
		ResponseStructure<FoodOrder> responseStructure = new ResponseStructure<>();
		responseStructure.setData(updatedOrder);
		responseStructure.setMessage("Food Order Status Updated Successfully");
		responseStructure.setStatus(HttpStatus.OK.value());
		return new ResponseEntity<>(responseStructure, HttpStatus.OK);
	}
}

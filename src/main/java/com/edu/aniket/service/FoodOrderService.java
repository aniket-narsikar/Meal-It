package com.edu.aniket.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.edu.aniket.config.ResponseStructure;
import com.edu.aniket.dao.FoodOrderDao;
import com.edu.aniket.dao.UserDao;
import com.edu.aniket.dto.PageResponse;
import com.edu.aniket.entity.FoodOrder;
import com.edu.aniket.entity.FoodProduct;
import com.edu.aniket.entity.OrderType;
import com.edu.aniket.entity.Status;
import com.edu.aniket.entity.User;

@Service
public class FoodOrderService {

	private final FoodOrderDao foodOrderDao;
	private final UserDao userDao;

	@Autowired
	public FoodOrderService(FoodOrderDao foodOrderDao, UserDao userDao) {
		this.foodOrderDao = foodOrderDao;
		this.userDao = userDao;
	}

	public ResponseEntity<ResponseStructure<FoodOrder>> saveFoodOrder(FoodOrder foodOrder, long userId) {
		User user = userDao.findUserById(userId);
		if (foodOrder.getFoodStatus() == null) {
			foodOrder.setFoodStatus(Status.PLACED);
		}
		if (foodOrder.getOrderType() == null) {
			foodOrder.setOrderType(OrderType.HOME_DELIVERY);
		}
		if (foodOrder.getOrderType() == OrderType.DINE_IN) {
			if (foodOrder.getTableNumber() == null || foodOrder.getTableNumber().trim().isEmpty()) {
				throw new IllegalArgumentException("Table number is required for DINE_IN orders");
			}
		} else if (foodOrder.getOrderType() == OrderType.HOME_DELIVERY) {
			if (foodOrder.getDeliveryAddress() == null || foodOrder.getDeliveryAddress().trim().isEmpty()) {
				throw new IllegalArgumentException("Delivery address is required for HOME_DELIVERY orders");
			}
		}
		if (foodOrder.getProducts() != null && !foodOrder.getProducts().isEmpty()) {
			double total = 0;
			for (FoodProduct product : foodOrder.getProducts()) {
				double price = product.getTotalPrice() - product.getDiscount();
				total += Math.max(0, price);
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

	public ResponseEntity<ResponseStructure<PageResponse<FoodOrder>>> findAllFoodOrdersPaginated(int page, int size, String sort) {
		if (page < 0) page = 0;
		if (size <= 0) size = 10;
		if (size > 100) size = 100;

		String[] sortParams = sort.split(",");
		String sortField = sortParams[0];
		Sort.Direction direction = (sortParams.length > 1 && sortParams[1].equalsIgnoreCase("desc"))
				? Sort.Direction.DESC : Sort.Direction.ASC;

		Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortField));
		Page<FoodOrder> orderPage = foodOrderDao.findAllFoodOrder(pageable);
		PageResponse<FoodOrder> pageResponse = PageResponse.fromPage(orderPage);

		ResponseStructure<PageResponse<FoodOrder>> responseStructure = new ResponseStructure<>();
		responseStructure.setData(pageResponse);
		responseStructure.setMessage("Food Orders fetched successfully");
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

	public ResponseEntity<ResponseStructure<List<FoodOrder>>> findFoodOrdersByUserId(long userId) {
		User user = userDao.findUserById(userId);
		List<FoodOrder> orders = user.getFoodOrders() != null ? new ArrayList<>(user.getFoodOrders()) : new ArrayList<>();
		orders.sort((a, b) -> Long.compare(b.getId(), a.getId()));

		ResponseStructure<List<FoodOrder>> responseStructure = new ResponseStructure<>();
		responseStructure.setData(orders);
		responseStructure.setMessage("User Food Orders Retrieved Successfully");
		responseStructure.setStatus(HttpStatus.OK.value());
		return new ResponseEntity<>(responseStructure, HttpStatus.OK);
	}
}

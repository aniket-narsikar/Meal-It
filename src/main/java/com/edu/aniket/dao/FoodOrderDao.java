package com.edu.aniket.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.edu.aniket.entity.FoodOrder;
import com.edu.aniket.exception.FoodOrderNotFoundException;
import com.edu.aniket.repository.FoodOrderRepository;

@Repository
public class FoodOrderDao {

	private final FoodOrderRepository foodOrderRepository;

	@Autowired
	public FoodOrderDao(FoodOrderRepository foodOrderRepository) {
		this.foodOrderRepository = foodOrderRepository;
	}

	public FoodOrder saveFoodOrder(FoodOrder foodOrder) {
		return foodOrderRepository.save(foodOrder);
	}

	public FoodOrder findFoodOrderById(long foodOrderId) {
		Optional<FoodOrder> optional = foodOrderRepository.findById(foodOrderId);
		if (optional.isPresent()) {
			return optional.get();
		}
		throw new FoodOrderNotFoundException("Food Order not found with id: " + foodOrderId);
	}

	public List<FoodOrder> findAllFoodOrder() {
		return foodOrderRepository.findAll();
	}

	public Page<FoodOrder> findAllFoodOrder(Pageable pageable) {
		return foodOrderRepository.findAll(pageable);
	}

	public void removeFoodOrderById(long foodOrderId) {
		FoodOrder foodOrder = findFoodOrderById(foodOrderId);
		foodOrderRepository.delete(foodOrder);
	}

	public FoodOrder updateFoodOrder(FoodOrder foodOrder) {
		findFoodOrderById(foodOrder.getId());
		return foodOrderRepository.save(foodOrder);
	}
}

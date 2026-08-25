package com.edu.aniket.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.edu.aniket.entity.FoodProduct;
import com.edu.aniket.exception.FoodProductWithTheGivenIdNotFound;
import com.edu.aniket.repository.FoodProductRepository;

@Repository
public class FoodProductDao {

	@Autowired
	private FoodProductRepository foodProductRepository;

	public FoodProduct saveFoodProduct(FoodProduct foodProduct) {
		return foodProductRepository.save(foodProduct);
	}

	public FoodProduct findFoodProductById(long foodProductId) {
		Optional<FoodProduct> optionalFoodProduct = foodProductRepository.findById(foodProductId);
		if (optionalFoodProduct.isPresent()) {
			return optionalFoodProduct.get();
		}
		throw new FoodProductWithTheGivenIdNotFound("Food Product not found with id: " + foodProductId);
	}

	public List<FoodProduct> findAllFoodProduct() {
		return foodProductRepository.findAll();
	}

	public void removeFoodProductById(long foodProductId) {
		FoodProduct foodProduct = findFoodProductById(foodProductId);
		foodProductRepository.delete(foodProduct);
	}

	public FoodProduct updateFoodProduct(FoodProduct foodProduct) {
		findFoodProductById(foodProduct.getId());
		return foodProductRepository.save(foodProduct);
	}
}

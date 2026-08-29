package com.edu.aniket.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.edu.aniket.entity.FoodMenu;
import com.edu.aniket.exception.FoodMenuNotFoundException;
import com.edu.aniket.repository.FoodMenuRepository;

@Repository
public class FoodMenuDao {

	private final FoodMenuRepository foodMenuRepository;

	@Autowired
	public FoodMenuDao(FoodMenuRepository foodMenuRepository) {
		this.foodMenuRepository = foodMenuRepository;
	}

	public FoodMenu saveFoodMenu(FoodMenu foodMenu) {
		return foodMenuRepository.save(foodMenu);
	}

	public FoodMenu findFoodMenuById(long foodMenuId) {
		Optional<FoodMenu> optional = foodMenuRepository.findById(foodMenuId);
		if (optional.isPresent()) {
			return optional.get();
		}
		throw new FoodMenuNotFoundException("Food Menu not found with id: " + foodMenuId);
	}

	public List<FoodMenu> findAllFoodMenu() {
		return foodMenuRepository.findAll();
	}

	public Page<FoodMenu> findAllFoodMenu(Pageable pageable) {
		return foodMenuRepository.findAll(pageable);
	}

	public void removeFoodMenuById(long foodMenuId) {
		FoodMenu foodMenu = findFoodMenuById(foodMenuId);
		foodMenuRepository.delete(foodMenu);
	}
}

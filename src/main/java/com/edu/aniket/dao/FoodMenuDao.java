package com.edu.aniket.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.edu.aniket.entity.FoodMenu;
import com.edu.aniket.exception.FoodMenuNotFoundException;
import com.edu.aniket.repository.FoodMenuRepository;

@Repository
public class FoodMenuDao {

	@Autowired
	private FoodMenuRepository foodMenuRepository;

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

	public void removeFoodMenuById(long foodMenuId) {
		FoodMenu foodMenu = findFoodMenuById(foodMenuId);
		foodMenuRepository.delete(foodMenu);
	}
}

package com.edu.aniket.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.edu.aniket.config.ResponseStructure;
import com.edu.aniket.dao.FoodMenuDao;
import com.edu.aniket.entity.FoodMenu;

@Service
public class FoodMenuService {

	@Autowired
	private FoodMenuDao foodMenuDao;

	public ResponseEntity<ResponseStructure<FoodMenu>> saveFoodMenu(FoodMenu foodMenu) {
		FoodMenu savedMenu = foodMenuDao.saveFoodMenu(foodMenu);
		ResponseStructure<FoodMenu> responseStructure = new ResponseStructure<>();
		responseStructure.setData(savedMenu);
		responseStructure.setMessage("Food Menu Saved Successfully");
		responseStructure.setStatus(HttpStatus.CREATED.value());
		return new ResponseEntity<>(responseStructure, HttpStatus.CREATED);
	}

	public ResponseEntity<ResponseStructure<FoodMenu>> findFoodMenuById(long foodMenuId) {
		FoodMenu foodMenu = foodMenuDao.findFoodMenuById(foodMenuId);
		ResponseStructure<FoodMenu> responseStructure = new ResponseStructure<>();
		responseStructure.setData(foodMenu);
		responseStructure.setMessage("Food Menu Found Successfully");
		responseStructure.setStatus(HttpStatus.OK.value());
		return new ResponseEntity<>(responseStructure, HttpStatus.OK);
	}

	public ResponseEntity<ResponseStructure<List<FoodMenu>>> findAllFoodMenus() {
		List<FoodMenu> foodMenus = foodMenuDao.findAllFoodMenu();
		ResponseStructure<List<FoodMenu>> responseStructure = new ResponseStructure<>();
		responseStructure.setData(foodMenus);
		responseStructure.setMessage("All Food Menus Retrieved Successfully");
		responseStructure.setStatus(HttpStatus.OK.value());
		return new ResponseEntity<>(responseStructure, HttpStatus.OK);
	}

	public ResponseEntity<ResponseStructure<String>> deleteFoodMenuById(long foodMenuId) {
		foodMenuDao.removeFoodMenuById(foodMenuId);
		ResponseStructure<String> responseStructure = new ResponseStructure<>();
		responseStructure.setData("Food Menu with ID " + foodMenuId + " deleted successfully");
		responseStructure.setMessage("Food Menu Deleted");
		responseStructure.setStatus(HttpStatus.OK.value());
		return new ResponseEntity<>(responseStructure, HttpStatus.OK);
	}
}

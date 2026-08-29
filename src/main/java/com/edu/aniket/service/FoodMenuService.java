package com.edu.aniket.service;

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
import com.edu.aniket.dao.FoodMenuDao;
import com.edu.aniket.dto.PageResponse;
import com.edu.aniket.entity.FoodMenu;

@Service
public class FoodMenuService {

	private final FoodMenuDao foodMenuDao;

	@Autowired
	public FoodMenuService(FoodMenuDao foodMenuDao) {
		this.foodMenuDao = foodMenuDao;
	}

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

	public ResponseEntity<ResponseStructure<PageResponse<FoodMenu>>> findAllFoodMenusPaginated(int page, int size, String sort) {
		if (page < 0) page = 0;
		if (size <= 0) size = 10;
		if (size > 100) size = 100;

		String[] sortParams = sort.split(",");
		String sortField = sortParams[0];
		Sort.Direction direction = (sortParams.length > 1 && sortParams[1].equalsIgnoreCase("desc"))
				? Sort.Direction.DESC : Sort.Direction.ASC;

		Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortField));
		Page<FoodMenu> menuPage = foodMenuDao.findAllFoodMenu(pageable);
		PageResponse<FoodMenu> pageResponse = PageResponse.fromPage(menuPage);

		ResponseStructure<PageResponse<FoodMenu>> responseStructure = new ResponseStructure<>();
		responseStructure.setData(pageResponse);
		responseStructure.setMessage("Food Menus fetched successfully");
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

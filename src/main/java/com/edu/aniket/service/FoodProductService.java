package com.edu.aniket.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.edu.aniket.config.ResponseStructure;
import com.edu.aniket.dao.FoodProductDao;
import com.edu.aniket.dao.UserDao;
import com.edu.aniket.entity.FoodProduct;
import com.edu.aniket.entity.Role;
import com.edu.aniket.entity.User;
import com.edu.aniket.exception.UserIsNotValidToAddItem;

@Service
public class FoodProductService {

	@Autowired
	private FoodProductDao foodProductDao;

	@Autowired
	private UserDao userDao;

	public ResponseEntity<ResponseStructure<FoodProduct>> saveFoodProduct(FoodProduct foodProduct, long userId) {
		User user = userDao.findUserById(userId);
		if (user != null && (user.getRole() == Role.ADMIN || user.getRole() == Role.MANAGER)) {
			FoodProduct savedProduct = foodProductDao.saveFoodProduct(foodProduct);
			ResponseStructure<FoodProduct> responseStructure = new ResponseStructure<>();
			responseStructure.setData(savedProduct);
			responseStructure.setMessage("Food Product Saved Successfully");
			responseStructure.setStatus(HttpStatus.CREATED.value());
			return new ResponseEntity<>(responseStructure, HttpStatus.CREATED);
		}
		throw new UserIsNotValidToAddItem("User does not have access to add food products");
	}

	public ResponseEntity<ResponseStructure<FoodProduct>> findFoodProductById(long foodProductId) {
		FoodProduct foodProduct = foodProductDao.findFoodProductById(foodProductId);
		ResponseStructure<FoodProduct> responseStructure = new ResponseStructure<>();
		responseStructure.setData(foodProduct);
		responseStructure.setMessage("Food Product Found Successfully");
		responseStructure.setStatus(HttpStatus.OK.value());
		return new ResponseEntity<>(responseStructure, HttpStatus.OK);
	}

	public ResponseEntity<ResponseStructure<List<FoodProduct>>> findAllFoodProducts() {
		List<FoodProduct> foodProducts = foodProductDao.findAllFoodProduct();
		ResponseStructure<List<FoodProduct>> responseStructure = new ResponseStructure<>();
		responseStructure.setData(foodProducts);
		responseStructure.setMessage("All Food Products Retrieved Successfully");
		responseStructure.setStatus(HttpStatus.OK.value());
		return new ResponseEntity<>(responseStructure, HttpStatus.OK);
	}

	public ResponseEntity<ResponseStructure<String>> deleteFoodProductById(long foodProductId) {
		foodProductDao.removeFoodProductById(foodProductId);
		ResponseStructure<String> responseStructure = new ResponseStructure<>();
		responseStructure.setData("Food Product with ID " + foodProductId + " deleted successfully");
		responseStructure.setMessage("Food Product Deleted");
		responseStructure.setStatus(HttpStatus.OK.value());
		return new ResponseEntity<>(responseStructure, HttpStatus.OK);
	}

	public ResponseEntity<ResponseStructure<FoodProduct>> updateFoodProduct(FoodProduct foodProduct) {
		FoodProduct updatedProduct = foodProductDao.updateFoodProduct(foodProduct);
		ResponseStructure<FoodProduct> responseStructure = new ResponseStructure<>();
		responseStructure.setData(updatedProduct);
		responseStructure.setMessage("Food Product Updated Successfully");
		responseStructure.setStatus(HttpStatus.OK.value());
		return new ResponseEntity<>(responseStructure, HttpStatus.OK);
	}
}

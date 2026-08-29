package com.edu.aniket.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.edu.aniket.config.ResponseStructure;
import com.edu.aniket.dao.FoodProductDao;
import com.edu.aniket.dao.UserDao;
import com.edu.aniket.dto.PageResponse;
import com.edu.aniket.entity.FoodProduct;
import com.edu.aniket.entity.Role;
import com.edu.aniket.entity.User;
import com.edu.aniket.exception.UserIsNotValidToAddItemException;

@Service
public class FoodProductService {

	private final FoodProductDao foodProductDao;
	private final UserDao userDao;

	@Autowired
	public FoodProductService(FoodProductDao foodProductDao, UserDao userDao) {
		this.foodProductDao = foodProductDao;
		this.userDao = userDao;
	}

	public ResponseEntity<ResponseStructure<FoodProduct>> saveFoodProduct(FoodProduct foodProduct, long userId) {
		boolean authorized = false;

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication != null && authentication.isAuthenticated()) {
			boolean hasRole = authentication.getAuthorities().stream()
					.anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN") || a.getAuthority().equals("ROLE_MANAGER"));
			if (hasRole) {
				authorized = true;
			}
		}

		if (!authorized && userId > 0) {
			try {
				User user = userDao.findUserById(userId);
				if (user != null && (user.getRole() == Role.ADMIN || user.getRole() == Role.MANAGER)) {
					authorized = true;
				}
			} catch (Exception e) {
				// User not found or not authorized
			}
		}

		if (authorized) {
			FoodProduct savedProduct = foodProductDao.saveFoodProduct(foodProduct);
			ResponseStructure<FoodProduct> responseStructure = new ResponseStructure<>();
			responseStructure.setData(savedProduct);
			responseStructure.setMessage("Food Product Saved Successfully");
			responseStructure.setStatus(HttpStatus.CREATED.value());
			return new ResponseEntity<>(responseStructure, HttpStatus.CREATED);
		}

		throw new UserIsNotValidToAddItemException("User does not have access to add food products");
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

	public ResponseEntity<ResponseStructure<PageResponse<FoodProduct>>> findAllFoodProductsPaginated(int page, int size, String sort) {
		if (page < 0) page = 0;
		if (size <= 0) size = 10;
		if (size > 100) size = 100;

		String[] sortParams = sort.split(",");
		String sortField = sortParams[0];
		Sort.Direction direction = (sortParams.length > 1 && sortParams[1].equalsIgnoreCase("desc"))
				? Sort.Direction.DESC : Sort.Direction.ASC;

		Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortField));
		Page<FoodProduct> productPage = foodProductDao.findAllFoodProduct(pageable);
		PageResponse<FoodProduct> pageResponse = PageResponse.fromPage(productPage);

		ResponseStructure<PageResponse<FoodProduct>> responseStructure = new ResponseStructure<>();
		responseStructure.setData(pageResponse);
		responseStructure.setMessage("Food Products fetched successfully");
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

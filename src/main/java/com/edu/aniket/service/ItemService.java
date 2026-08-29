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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.edu.aniket.config.ResponseStructure;
import com.edu.aniket.dao.ItemDao;
import com.edu.aniket.dao.UserDao;
import com.edu.aniket.dto.PageResponse;
import com.edu.aniket.entity.Item;
import com.edu.aniket.entity.Role;
import com.edu.aniket.entity.User;
import com.edu.aniket.exception.UserIsNotValidToAddItemException;

@Service
public class ItemService {

	private final UserDao userDao;
	private final ItemDao itemDao;

	@Autowired
	public ItemService(UserDao userDao, ItemDao itemDao) {
		this.userDao = userDao;
		this.itemDao = itemDao;
	}

	public ResponseEntity<ResponseStructure<List<Item>>> saveItems(List<Item> items, long userId) {
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
			List<Item> listOfItems = new ArrayList<>();
			for (Item item : items) {
				listOfItems.add(itemDao.saveItem(item));
			}
			ResponseStructure<List<Item>> responseStructure = new ResponseStructure<>();
			responseStructure.setData(listOfItems);
			responseStructure.setMessage("Items Saved Successfully");
			responseStructure.setStatus(HttpStatus.CREATED.value());
			return new ResponseEntity<>(responseStructure, HttpStatus.CREATED);
		}

		throw new UserIsNotValidToAddItemException("User does not have access to add items");
	}

	public ResponseEntity<ResponseStructure<Item>> findItemById(long itemId) {
		Item item = itemDao.findItemById(itemId);
		ResponseStructure<Item> responseStructure = new ResponseStructure<>();
		responseStructure.setData(item);
		responseStructure.setMessage("Item Found Successfully");
		responseStructure.setStatus(HttpStatus.OK.value());
		return new ResponseEntity<>(responseStructure, HttpStatus.OK);
	}

	public ResponseEntity<ResponseStructure<List<Item>>> findAllItems() {
		List<Item> items = itemDao.findAllItem();
		ResponseStructure<List<Item>> responseStructure = new ResponseStructure<>();
		responseStructure.setData(items);
		responseStructure.setMessage("All Items Retrieved Successfully");
		responseStructure.setStatus(HttpStatus.OK.value());
		return new ResponseEntity<>(responseStructure, HttpStatus.OK);
	}

	public ResponseEntity<ResponseStructure<PageResponse<Item>>> findAllItemsPaginated(int page, int size, String sort) {
		if (page < 0) page = 0;
		if (size <= 0) size = 10;
		if (size > 100) size = 100;

		String[] sortParams = sort.split(",");
		String sortField = sortParams[0];
		Sort.Direction direction = (sortParams.length > 1 && sortParams[1].equalsIgnoreCase("desc"))
				? Sort.Direction.DESC : Sort.Direction.ASC;

		Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortField));
		Page<Item> itemPage = itemDao.findAllItem(pageable);
		PageResponse<Item> pageResponse = PageResponse.fromPage(itemPage);

		ResponseStructure<PageResponse<Item>> responseStructure = new ResponseStructure<>();
		responseStructure.setData(pageResponse);
		responseStructure.setMessage("Items fetched successfully");
		responseStructure.setStatus(HttpStatus.OK.value());

		return new ResponseEntity<>(responseStructure, HttpStatus.OK);
	}

	public ResponseEntity<ResponseStructure<String>> deleteItemById(long itemId) {
		itemDao.removeItemById(itemId);
		ResponseStructure<String> responseStructure = new ResponseStructure<>();
		responseStructure.setData("Item with ID " + itemId + " deleted successfully");
		responseStructure.setMessage("Item Deleted");
		responseStructure.setStatus(HttpStatus.OK.value());
		return new ResponseEntity<>(responseStructure, HttpStatus.OK);
	}

	public ResponseEntity<ResponseStructure<Item>> updateItem(Item item) {
		Item updatedItem = itemDao.updateItem(item);
		ResponseStructure<Item> responseStructure = new ResponseStructure<>();
		responseStructure.setData(updatedItem);
		responseStructure.setMessage("Item Updated Successfully");
		responseStructure.setStatus(HttpStatus.OK.value());
		return new ResponseEntity<>(responseStructure, HttpStatus.OK);
	}
}

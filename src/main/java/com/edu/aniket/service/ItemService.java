package com.edu.aniket.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.edu.aniket.config.ResponseStructure;
import com.edu.aniket.dao.ItemDao;
import com.edu.aniket.dao.UserDao;
import com.edu.aniket.entity.Item;
import com.edu.aniket.entity.Role;
import com.edu.aniket.entity.User;
import com.edu.aniket.exception.UserIsNotValidToAddItem;

@Service
public class ItemService {

	@Autowired
	private UserDao userDao;

	@Autowired
	private ItemDao itemDao;

	public ResponseEntity<ResponseStructure<List<Item>>> saveItems(List<Item> items, long userId) {
		User user = userDao.findUserById(userId);
		if (user != null && (user.getRole() == Role.ADMIN || user.getRole() == Role.MANAGER)) {
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
		throw new UserIsNotValidToAddItem("User does not have access to add items");
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

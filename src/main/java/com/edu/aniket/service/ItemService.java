package com.edu.aniket.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.edu.aniket.config.ResponcseStructure;
import com.edu.aniket.dao.ItemDao;
import com.edu.aniket.dao.UserDao;
import com.edu.aniket.entity.Item;
import com.edu.aniket.entity.Role;
import com.edu.aniket.entity.User;
import com.edu.aniket.exception.UserIdNotFound;
import com.edu.aniket.exception.UserIsNotValiedToAddItem;

@Service
public class ItemService {
	@Autowired
	private UserDao userDao;
	@Autowired
	private ItemDao itemDao;
	
	public ResponseEntity<ResponcseStructure<List<Item>>> saveItems(List<Item> items, long userId) {
		User user = userDao.findUserById(userId);
		if (user.getRole().equals(Role.ADMIN) || user.getRole().equals(Role.MANAGER)) {
			List<Item> listOfItems = new ArrayList<>();
			for (Item item : items) {
				listOfItems.add(itemDao.saveItem(item));
			}
			ResponcseStructure<List<Item>> responseStructure = new ResponcseStructure<>();
			responseStructure.setData(listOfItems);
			responseStructure.setMessage("Saved!!");
			responseStructure.setStatus(HttpStatus.CREATED.value());
			return new ResponseEntity<>(responseStructure, HttpStatus.CREATED);
		}

		throw new UserIsNotValiedToAddItem("User Is Not Have Access to Add the Item");
	}
}

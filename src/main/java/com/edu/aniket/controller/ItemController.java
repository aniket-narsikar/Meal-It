package com.edu.aniket.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.edu.aniket.config.ResponseStructure;
import com.edu.aniket.entity.Item;
import com.edu.aniket.service.ItemService;

@RestController
@RequestMapping("/item")
public class ItemController {

	@Autowired
	private ItemService itemService;

	@PostMapping("/save")
	public ResponseEntity<ResponseStructure<List<Item>>> saveItems(@RequestBody List<Item> items, @RequestParam long userId) {
		return itemService.saveItems(items, userId);
	}

	@GetMapping("/findById")
	public ResponseEntity<ResponseStructure<Item>> findItemById(@RequestParam long id) {
		return itemService.findItemById(id);
	}

	@GetMapping("/findAll")
	public ResponseEntity<ResponseStructure<List<Item>>> findAllItems() {
		return itemService.findAllItems();
	}

	@DeleteMapping("/delete")
	public ResponseEntity<ResponseStructure<String>> deleteItemById(@RequestParam long id) {
		return itemService.deleteItemById(id);
	}

	@PutMapping("/update")
	public ResponseEntity<ResponseStructure<Item>> updateItem(@RequestBody Item item) {
		return itemService.updateItem(item);
	}
}

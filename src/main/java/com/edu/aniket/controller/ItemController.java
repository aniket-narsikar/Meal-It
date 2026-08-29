package com.edu.aniket.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.edu.aniket.config.ResponseStructure;
import com.edu.aniket.dto.PageResponse;
import com.edu.aniket.entity.Item;
import com.edu.aniket.service.ItemService;

@RestController
@RequestMapping("/item")
public class ItemController {

	private final ItemService itemService;

	@Autowired
	public ItemController(ItemService itemService) {
		this.itemService = itemService;
	}

	@PostMapping("/save")
	@PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
	public ResponseEntity<ResponseStructure<List<Item>>> saveItems(
			@RequestBody List<Item> items,
			@RequestParam(required = false, defaultValue = "0") long userId
	) {
		return itemService.saveItems(items, userId);
	}

	@GetMapping("/findById")
	public ResponseEntity<ResponseStructure<Item>> findItemById(@RequestParam long id) {
		return itemService.findItemById(id);
	}

	@GetMapping("/{id}")
	public ResponseEntity<ResponseStructure<Item>> getItemById(@PathVariable long id) {
		return itemService.findItemById(id);
	}

	@GetMapping("/findAll")
	public ResponseEntity<ResponseStructure<PageResponse<Item>>> findAllItems(
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size,
			@RequestParam(defaultValue = "id,asc") String sort
	) {
		return itemService.findAllItemsPaginated(page, size, sort);
	}

	@DeleteMapping("/delete")
	@PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
	public ResponseEntity<ResponseStructure<String>> deleteItemById(@RequestParam long id) {
		return itemService.deleteItemById(id);
	}

	@DeleteMapping("/{id}")
	@PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
	public ResponseEntity<ResponseStructure<String>> removeItemById(@PathVariable long id) {
		return itemService.deleteItemById(id);
	}

	@PutMapping("/update")
	@PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
	public ResponseEntity<ResponseStructure<Item>> updateItem(@RequestBody Item item) {
		return itemService.updateItem(item);
	}
}

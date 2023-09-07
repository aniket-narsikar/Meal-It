package com.edu.aniket.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.edu.aniket.entity.Item;
import com.edu.aniket.config.ResponcseStructure;
import com.edu.aniket.service.ItemService;

@RestController
@RequestMapping("/item")
public class ItemController {
	@Autowired
	private ItemService itemservice;
	
	@PostMapping("/save")
	public ResponseEntity<ResponcseStructure<List<Item>>> saveItems(@RequestParam List<Item> items,@RequestParam long userId) 
	{
		return itemservice.saveItems(items, userId);
	}
}

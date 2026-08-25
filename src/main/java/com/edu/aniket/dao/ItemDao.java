package com.edu.aniket.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.edu.aniket.entity.Item;
import com.edu.aniket.exception.ItemWithIdNotFound;
import com.edu.aniket.repository.ItemRepository;

@Repository
public class ItemDao {

	@Autowired
	private ItemRepository itemRepository;

	public Item saveItem(Item item) {
		return itemRepository.save(item);
	}

	public Item findItemById(long itemId) {
		Optional<Item> optionalItem = itemRepository.findById(itemId);
		if (optionalItem.isPresent()) {
			return optionalItem.get();
		}
		throw new ItemWithIdNotFound("Item not found with id: " + itemId);
	}

	public List<Item> findAllItem() {
		return itemRepository.findAll();
	}

	public void removeItemById(long itemId) {
		Item item = findItemById(itemId);
		itemRepository.delete(item);
	}

	public Item updateItem(Item item) {
		findItemById(item.getId());
		return itemRepository.save(item);
	}
}

package com.edu.aniket.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.edu.aniket.entity.Item;
import com.edu.aniket.exception.ItemWithIdNotFound;
import com.edu.aniket.repository.ItemRepository;

@Repository
public class ItemDao {

	private final ItemRepository itemRepository;

	@Autowired
	public ItemDao(ItemRepository itemRepository) {
		this.itemRepository = itemRepository;
	}

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

	public Page<Item> findAllItem(Pageable pageable) {
		return itemRepository.findAll(pageable);
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

package com.edu.aniket.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import com.edu.aniket.entity.Item;
import com.edu.aniket.exception.ItemWithIdNotFound;
import com.edu.aniket.repostory.ItemRespostory;
public class ItemDao {


	@Autowired
	private ItemRespostory itemRespostory;

	public Item saveItem(Item item) {
		return itemRespostory.save(item);
	}

	public Item findItemById(long itemId) {
		Optional<Item> optionalItem = itemRespostory.findById(itemId);
		if (optionalItem.isPresent()) {
			return optionalItem.get();
		}
		throw new ItemWithIdNotFound("Item with the given Id Not Found");
	}

	public List<Item> findAllItem() {
		return itemRespostory.findAll();
	}

	public void removeItemById(long itemId) {
		itemRespostory.delete(findItemById(itemId));
	}

//public void saveItem(Item item){
//}
//public void findItemById(long itemId) {
//}
//public void findAllItem(){
//}
//public void removeItemById(long itemId){
//}

}

package com.edu.aniket.dao;



import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.edu.aniket.entity.FoodOrder;
import com.edu.aniket.entity.FoodProduct;
import com.edu.aniket.exception.FoodProductWithTheGivenIdNotFound;
import com.edu.aniket.repostory.FoodProductRepository;

@Repository
public class FoodProductDao 
{

	private FoodProductRepository foodProductRepository;
	
public FoodProduct saveFoodProduct(FoodProduct foodProduct){
	return foodProductRepository.save(foodProduct);
}


public FoodProduct findFoodProductById(long foodProductId){
	Optional<FoodProduct> optionalFoodProduct = foodProductRepository.findById(foodProductId);
	if (optionalFoodProduct.isPresent())
	{
		return optionalFoodProduct.get();
	}
	throw new FoodProductWithTheGivenIdNotFound("FoodProduct With the Given Id Not Found");
}
public void findAllFoodProduct()
{
}
public void removeFoodProductById(){	
}
}

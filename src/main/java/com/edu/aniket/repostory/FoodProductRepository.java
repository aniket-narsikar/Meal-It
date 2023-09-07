package com.edu.aniket.repostory;

import org.springframework.data.jpa.repository.JpaRepository;

import com.edu.aniket.entity.FoodProduct;

public interface FoodProductRepository  extends JpaRepository<FoodProduct, Long>{

}

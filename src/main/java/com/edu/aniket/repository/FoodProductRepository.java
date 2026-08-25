package com.edu.aniket.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.edu.aniket.entity.FoodProduct;

@Repository
public interface FoodProductRepository extends JpaRepository<FoodProduct, Long> {

}

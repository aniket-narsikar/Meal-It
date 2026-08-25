package com.edu.aniket.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.edu.aniket.entity.Item;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {

}

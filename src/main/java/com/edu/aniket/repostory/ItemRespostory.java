package com.edu.aniket.repostory;

import org.springframework.data.jpa.repository.JpaRepository;

import com.edu.aniket.entity.Item;

public interface ItemRespostory extends JpaRepository<Item, Long>
{

}

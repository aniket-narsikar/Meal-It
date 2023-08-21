package com.edu.aniket.entity;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Entity
public class FoodProduct {

@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private long id;
private String name;
private double totalPrice;
private double discount;
private String availability;
private Type type;

@OneToMany
private List<Item> items;
}

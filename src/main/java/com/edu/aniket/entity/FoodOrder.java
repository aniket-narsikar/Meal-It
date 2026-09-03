package com.edu.aniket.entity;

import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Entity
public class FoodOrder {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@Enumerated(EnumType.STRING)
	private Status foodStatus;

	@Enumerated(EnumType.STRING)
	private OrderType orderType;

	private String tableNumber;

	private String deliveryAddress;

	private String specialInstructions;

	@CreationTimestamp
	private LocalDateTime orderCreatedTime;

	private LocalDateTime orderDeliveryTime;

	private double totalPrice;

	@OneToMany
	private List<FoodProduct> products;

	public FoodOrder() {
	}

	public FoodOrder(long id, Status foodStatus, LocalDateTime orderCreatedTime, LocalDateTime orderDeliveryTime, double totalPrice, List<FoodProduct> products) {
		this.id = id;
		this.foodStatus = foodStatus;
		this.orderCreatedTime = orderCreatedTime;
		this.orderDeliveryTime = orderDeliveryTime;
		this.totalPrice = totalPrice;
		this.products = products;
	}

	public FoodOrder(long id, Status foodStatus, OrderType orderType, String tableNumber, String deliveryAddress, String specialInstructions, LocalDateTime orderCreatedTime, LocalDateTime orderDeliveryTime, double totalPrice, List<FoodProduct> products) {
		this.id = id;
		this.foodStatus = foodStatus;
		this.orderType = orderType;
		this.tableNumber = tableNumber;
		this.deliveryAddress = deliveryAddress;
		this.specialInstructions = specialInstructions;
		this.orderCreatedTime = orderCreatedTime;
		this.orderDeliveryTime = orderDeliveryTime;
		this.totalPrice = totalPrice;
		this.products = products;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Status getFoodStatus() {
		return foodStatus;
	}

	public void setFoodStatus(Status foodStatus) {
		this.foodStatus = foodStatus;
	}

	public OrderType getOrderType() {
		return orderType;
	}

	public void setOrderType(OrderType orderType) {
		this.orderType = orderType;
	}

	public String getTableNumber() {
		return tableNumber;
	}

	public void setTableNumber(String tableNumber) {
		this.tableNumber = tableNumber;
	}

	public String getDeliveryAddress() {
		return deliveryAddress;
	}

	public void setDeliveryAddress(String deliveryAddress) {
		this.deliveryAddress = deliveryAddress;
	}

	public String getSpecialInstructions() {
		return specialInstructions;
	}

	public void setSpecialInstructions(String specialInstructions) {
		this.specialInstructions = specialInstructions;
	}

	public LocalDateTime getOrderCreatedTime() {
		return orderCreatedTime;
	}

	public void setOrderCreatedTime(LocalDateTime orderCreatedTime) {
		this.orderCreatedTime = orderCreatedTime;
	}

	public LocalDateTime getOrderDeliveryTime() {
		return orderDeliveryTime;
	}

	public void setOrderDeliveryTime(LocalDateTime orderDeliveryTime) {
		this.orderDeliveryTime = orderDeliveryTime;
	}

	public double getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(double totalPrice) {
		this.totalPrice = totalPrice;
	}

	public List<FoodProduct> getProducts() {
		return products;
	}

	public void setProducts(List<FoodProduct> products) {
		this.products = products;
	}
}

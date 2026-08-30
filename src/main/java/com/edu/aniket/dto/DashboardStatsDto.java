package com.edu.aniket.dto;

import java.util.List;
import com.edu.aniket.entity.FoodOrder;

public class DashboardStatsDto {

	private double totalRevenue;
	private long totalOrders;
	private long activeOrders;
	private long deliveredOrders;
	private long cancelledOrders;
	private long totalUsers;
	private long totalProducts;
	private List<FoodOrder> recentOrders;

	public DashboardStatsDto() {
	}

	public DashboardStatsDto(double totalRevenue, long totalOrders, long activeOrders, long deliveredOrders,
			long cancelledOrders, long totalUsers, long totalProducts, List<FoodOrder> recentOrders) {
		this.totalRevenue = totalRevenue;
		this.totalOrders = totalOrders;
		this.activeOrders = activeOrders;
		this.deliveredOrders = deliveredOrders;
		this.cancelledOrders = cancelledOrders;
		this.totalUsers = totalUsers;
		this.totalProducts = totalProducts;
		this.recentOrders = recentOrders;
	}

	public double getTotalRevenue() {
		return totalRevenue;
	}

	public void setTotalRevenue(double totalRevenue) {
		this.totalRevenue = totalRevenue;
	}

	public long getTotalOrders() {
		return totalOrders;
	}

	public void setTotalOrders(long totalOrders) {
		this.totalOrders = totalOrders;
	}

	public long getActiveOrders() {
		return activeOrders;
	}

	public void setActiveOrders(long activeOrders) {
		this.activeOrders = activeOrders;
	}

	public long getDeliveredOrders() {
		return deliveredOrders;
	}

	public void setDeliveredOrders(long deliveredOrders) {
		this.deliveredOrders = deliveredOrders;
	}

	public long getCancelledOrders() {
		return cancelledOrders;
	}

	public void setCancelledOrders(long cancelledOrders) {
		this.cancelledOrders = cancelledOrders;
	}

	public long getTotalUsers() {
		return totalUsers;
	}

	public void setTotalUsers(long totalUsers) {
		this.totalUsers = totalUsers;
	}

	public long getTotalProducts() {
		return totalProducts;
	}

	public void setTotalProducts(long totalProducts) {
		this.totalProducts = totalProducts;
	}

	public List<FoodOrder> getRecentOrders() {
		return recentOrders;
	}

	public void setRecentOrders(List<FoodOrder> recentOrders) {
		this.recentOrders = recentOrders;
	}
}

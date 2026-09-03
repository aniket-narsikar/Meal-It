package com.edu.aniket.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.edu.aniket.config.ResponseStructure;
import com.edu.aniket.dto.DashboardStatsDto;
import com.edu.aniket.entity.FoodOrder;
import com.edu.aniket.entity.Status;
import com.edu.aniket.repository.FoodOrderRepository;
import com.edu.aniket.repository.FoodProductRepository;
import com.edu.aniket.repository.UserRepository;

@Service
public class DashboardService {

	private final FoodOrderRepository foodOrderRepository;
	private final UserRepository userRepository;
	private final FoodProductRepository foodProductRepository;

	@Autowired
	public DashboardService(
			FoodOrderRepository foodOrderRepository,
			UserRepository userRepository,
			FoodProductRepository foodProductRepository
	) {
		this.foodOrderRepository = foodOrderRepository;
		this.userRepository = userRepository;
		this.foodProductRepository = foodProductRepository;
	}

	public ResponseEntity<ResponseStructure<DashboardStatsDto>> getDashboardStats() {
		List<FoodOrder> allOrders = foodOrderRepository.findAll();

		long totalOrders = allOrders.size();
		long activeOrders = 0;
		long deliveredOrders = 0;
		long cancelledOrders = 0;
		double totalRevenue = 0.0;

		for (FoodOrder order : allOrders) {
			if (order.getFoodStatus() != null) {
				if (order.getFoodStatus() == Status.DELIVERED) {
					deliveredOrders++;
					totalRevenue += order.getTotalPrice();
				} else if (order.getFoodStatus() == Status.CANCELLED) {
					cancelledOrders++;
				} else {
					activeOrders++;
					totalRevenue += order.getTotalPrice();
				}
			}
		}

		long totalUsers = userRepository.count();
		long totalProducts = foodProductRepository.count();

		List<FoodOrder> recentOrders = foodOrderRepository.findAll(
				PageRequest.of(0, 5, Sort.by(Sort.Direction.DESC, "id"))
		).getContent();

		DashboardStatsDto stats = new DashboardStatsDto(
				totalRevenue,
				totalOrders,
				activeOrders,
				deliveredOrders,
				cancelledOrders,
				totalUsers,
				totalProducts,
				recentOrders
		);

		ResponseStructure<DashboardStatsDto> responseStructure = new ResponseStructure<>();
		responseStructure.setData(stats);
		responseStructure.setMessage("Dashboard analytics retrieved successfully");
		responseStructure.setStatus(HttpStatus.OK.value());

		return new ResponseEntity<>(responseStructure, HttpStatus.OK);
	}
}

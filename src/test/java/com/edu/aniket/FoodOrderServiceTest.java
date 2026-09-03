package com.edu.aniket;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.edu.aniket.config.ResponseStructure;
import com.edu.aniket.dao.FoodOrderDao;
import com.edu.aniket.dao.UserDao;
import com.edu.aniket.entity.FoodOrder;
import com.edu.aniket.entity.FoodProduct;
import com.edu.aniket.entity.OrderType;
import com.edu.aniket.entity.Status;
import com.edu.aniket.entity.User;
import com.edu.aniket.service.FoodOrderService;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FoodOrderServiceTest {

	@Mock
	private FoodOrderDao foodOrderDao;

	@Mock
	private UserDao userDao;

	@InjectMocks
	private FoodOrderService foodOrderService;

	private User mockUser;

	@BeforeEach
	public void setUp() {
		mockUser = new User();
		mockUser.setId(1L);
		mockUser.setName("John Doe");
		mockUser.setEmail("john@example.com");
		mockUser.setFoodOrders(new ArrayList<>());
	}

	@Test
	public void testSaveFoodOrderWithNullOrderTypeDefaultsToHomeDelivery() {
		when(userDao.findUserById(1L)).thenReturn(mockUser);
		when(foodOrderDao.saveFoodOrder(any(FoodOrder.class))).thenAnswer(invocation -> invocation.getArgument(0));

		FoodOrder order = new FoodOrder();
		order.setDeliveryAddress("123 Maple Street");
		order.setSpecialInstructions("Leave at door");

		ResponseEntity<ResponseStructure<FoodOrder>> response = foodOrderService.saveFoodOrder(order, 1L);

		assertEquals(HttpStatus.CREATED, response.getStatusCode());
		assertNotNull(response.getBody());
		FoodOrder saved = response.getBody().getData();
		assertEquals(OrderType.HOME_DELIVERY, saved.getOrderType());
		assertEquals(Status.PLACED, saved.getFoodStatus());
		assertEquals("123 Maple Street", saved.getDeliveryAddress());
		assertEquals("Leave at door", saved.getSpecialInstructions());
		verify(userDao, times(1)).updateUser(mockUser);
	}

	@Test
	public void testSaveFoodOrderDineInSuccess() {
		when(userDao.findUserById(1L)).thenReturn(mockUser);
		when(foodOrderDao.saveFoodOrder(any(FoodOrder.class))).thenAnswer(invocation -> invocation.getArgument(0));

		FoodOrder order = new FoodOrder();
		order.setOrderType(OrderType.DINE_IN);
		order.setTableNumber("Table 4B");
		order.setSpecialInstructions("Extra napkins");

		ResponseEntity<ResponseStructure<FoodOrder>> response = foodOrderService.saveFoodOrder(order, 1L);

		assertEquals(HttpStatus.CREATED, response.getStatusCode());
		FoodOrder saved = response.getBody().getData();
		assertEquals(OrderType.DINE_IN, saved.getOrderType());
		assertEquals("Table 4B", saved.getTableNumber());
		assertEquals("Extra napkins", saved.getSpecialInstructions());
		assertNull(saved.getDeliveryAddress());
	}

	@Test
	public void testSaveFoodOrderHomeDeliverySuccess() {
		when(userDao.findUserById(1L)).thenReturn(mockUser);
		when(foodOrderDao.saveFoodOrder(any(FoodOrder.class))).thenAnswer(invocation -> invocation.getArgument(0));

		FoodOrder order = new FoodOrder();
		order.setOrderType(OrderType.HOME_DELIVERY);
		order.setDeliveryAddress("456 Elm St, Apt 2");
		order.setSpecialInstructions("Ring buzzer 202");

		ResponseEntity<ResponseStructure<FoodOrder>> response = foodOrderService.saveFoodOrder(order, 1L);

		assertEquals(HttpStatus.CREATED, response.getStatusCode());
		FoodOrder saved = response.getBody().getData();
		assertEquals(OrderType.HOME_DELIVERY, saved.getOrderType());
		assertEquals("456 Elm St, Apt 2", saved.getDeliveryAddress());
		assertEquals("Ring buzzer 202", saved.getSpecialInstructions());
	}

	@Test
	public void testSaveFoodOrderDineInMissingTableNumberThrowsException() {
		when(userDao.findUserById(1L)).thenReturn(mockUser);

		FoodOrder orderWithNullTable = new FoodOrder();
		orderWithNullTable.setOrderType(OrderType.DINE_IN);
		orderWithNullTable.setTableNumber(null);

		IllegalArgumentException ex1 = assertThrows(IllegalArgumentException.class, () -> {
			foodOrderService.saveFoodOrder(orderWithNullTable, 1L);
		});
		assertEquals("Table number is required for DINE_IN orders", ex1.getMessage());

		FoodOrder orderWithBlankTable = new FoodOrder();
		orderWithBlankTable.setOrderType(OrderType.DINE_IN);
		orderWithBlankTable.setTableNumber("   ");

		IllegalArgumentException ex2 = assertThrows(IllegalArgumentException.class, () -> {
			foodOrderService.saveFoodOrder(orderWithBlankTable, 1L);
		});
		assertEquals("Table number is required for DINE_IN orders", ex2.getMessage());

		verify(foodOrderDao, never()).saveFoodOrder(any());
	}

	@Test
	public void testSaveFoodOrderHomeDeliveryMissingAddressThrowsException() {
		when(userDao.findUserById(1L)).thenReturn(mockUser);

		FoodOrder orderWithNullAddress = new FoodOrder();
		orderWithNullAddress.setOrderType(OrderType.HOME_DELIVERY);
		orderWithNullAddress.setDeliveryAddress(null);

		IllegalArgumentException ex1 = assertThrows(IllegalArgumentException.class, () -> {
			foodOrderService.saveFoodOrder(orderWithNullAddress, 1L);
		});
		assertEquals("Delivery address is required for HOME_DELIVERY orders", ex1.getMessage());

		FoodOrder orderWithBlankAddress = new FoodOrder();
		orderWithBlankAddress.setOrderType(OrderType.HOME_DELIVERY);
		orderWithBlankAddress.setDeliveryAddress("   ");

		IllegalArgumentException ex2 = assertThrows(IllegalArgumentException.class, () -> {
			foodOrderService.saveFoodOrder(orderWithBlankAddress, 1L);
		});
		assertEquals("Delivery address is required for HOME_DELIVERY orders", ex2.getMessage());

		verify(foodOrderDao, never()).saveFoodOrder(any());
	}

	@Test
	public void testSaveFoodOrderTotalPriceCalculationAndPreservedStatus() {
		when(userDao.findUserById(1L)).thenReturn(mockUser);
		when(foodOrderDao.saveFoodOrder(any(FoodOrder.class))).thenAnswer(invocation -> invocation.getArgument(0));

		FoodProduct p1 = new FoodProduct();
		p1.setTotalPrice(200.0);
		p1.setDiscount(20.0); // 180.0

		FoodProduct p2 = new FoodProduct();
		p2.setTotalPrice(150.0);
		p2.setDiscount(0.0); // 150.0

		FoodOrder order = new FoodOrder();
		order.setFoodStatus(Status.PREPARING);
		order.setOrderType(OrderType.DINE_IN);
		order.setTableNumber("Table 7");
		order.setProducts(List.of(p1, p2));

		ResponseEntity<ResponseStructure<FoodOrder>> response = foodOrderService.saveFoodOrder(order, 1L);

		assertEquals(HttpStatus.CREATED, response.getStatusCode());
		FoodOrder saved = response.getBody().getData();
		assertEquals(Status.PREPARING, saved.getFoodStatus());
		assertEquals(330.0, saved.getTotalPrice());
		assertEquals(1, mockUser.getFoodOrders().size());
	}
}

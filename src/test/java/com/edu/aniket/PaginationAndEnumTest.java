package com.edu.aniket;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import com.edu.aniket.dto.PageResponse;
import com.edu.aniket.entity.OrderType;
import com.edu.aniket.entity.Status;

import static org.junit.jupiter.api.Assertions.*;

public class PaginationAndEnumTest {

	@Test
	public void testStatusEnumValuesCorrectness() {
		Status[] statuses = Status.values();
		List<String> names = Arrays.stream(statuses).map(Enum::name).toList();

		assertTrue(names.contains("PLACED"));
		assertTrue(names.contains("RECEIVED"));
		assertTrue(names.contains("PREPARING"));
		assertTrue(names.contains("READYTOSERVE"));
		assertTrue(names.contains("DELIVERED"));
		assertTrue(names.contains("CANCELLED"));

		assertFalse(names.contains("RECIVED"), "RECIVED typo should be removed");
		assertFalse(names.contains("PREPRING"), "PREPRING typo should be removed");
	}

	@Test
	public void testOrderTypeEnumValues() {
		OrderType[] orderTypes = OrderType.values();
		List<String> names = Arrays.stream(orderTypes).map(Enum::name).toList();

		assertEquals(2, names.size());
		assertTrue(names.contains("DINE_IN"));
		assertTrue(names.contains("HOME_DELIVERY"));
	}

	@Test
	public void testPageResponseMapping() {
		List<String> items = Arrays.asList("Item1", "Item2", "Item3");
		Page<String> page = new PageImpl<>(items, PageRequest.of(0, 10), 25);

		PageResponse<String> response = PageResponse.fromPage(page);

		assertEquals(3, response.getContent().size());
		assertEquals(0, response.getPage());
		assertEquals(10, response.getSize());
		assertEquals(25, response.getTotalElements());
		assertEquals(3, response.getTotalPages());
		assertTrue(response.isFirst());
		assertFalse(response.isLast());
	}

	@Test
	public void testRoleEnumValues() {
		com.edu.aniket.entity.Role[] roles = com.edu.aniket.entity.Role.values();
		List<String> names = Arrays.stream(roles).map(Enum::name).toList();

		assertEquals(6, names.size());
		assertTrue(names.contains("ADMIN"));
		assertTrue(names.contains("MANAGER"));
		assertTrue(names.contains("STAFF"));
		assertTrue(names.contains("CHEF"));
		assertTrue(names.contains("WAITER"));
		assertTrue(names.contains("CUSTOMER"));
	}
}


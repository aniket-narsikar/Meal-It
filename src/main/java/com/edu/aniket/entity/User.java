package com.edu.aniket.entity;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="MealItUserTable")
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	@NotEmpty(message = "User Can't be Empty")
	private String name;
	@Column(unique = true)
	@Email(message = "Invalid Email Id")
	private String email;
	@Column(unique = true)
	@Min(value = 6000000000l)
	@Max(value = 9999999999l)
	private long phoneNumber;
	private String password;
	private Role role;
	
	@OneToMany
	private List<FoodOrder> foodOrders;
}

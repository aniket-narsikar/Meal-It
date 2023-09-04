package com.edu.aniket.dto;
import com.edu.aniket.entity.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
	private long id;
	private String name;
	private String email;
	// The 'Role' import has been removed, so you can remove 'private Role role;' as well
	private Role role;
}

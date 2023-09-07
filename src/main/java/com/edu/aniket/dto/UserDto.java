package com.edu.aniket.dto;
import com.edu.aniket.entity.Role;
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
    private long phoneNumber;
	// The 'Role' import has been removed, so you can remove 'private Role role;' as well
	private Role role;
}

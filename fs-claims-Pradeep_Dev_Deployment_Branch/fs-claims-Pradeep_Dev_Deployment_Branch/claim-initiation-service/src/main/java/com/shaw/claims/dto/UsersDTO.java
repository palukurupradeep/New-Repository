package com.shaw.claims.dto;

import lombok.Data;

@Data
public class UsersDTO {
	private String firstName;
	private Character middleInitial;
	private String lastName;
  	private String emailAddress;
    private String title;
}

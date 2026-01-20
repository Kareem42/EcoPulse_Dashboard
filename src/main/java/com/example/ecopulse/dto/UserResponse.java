package com.example.ecopulse.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserResponse {
    private Long id = 123L;
    private String email = "jon.doe@gmail.com";
    private String name = "Jon Doe";
    private String homeLocation = "Texas";
}

package com.pragma.powerup.application.dto.request;

import com.pragma.powerup.application.dto.response.RoleDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRequestDto {

    private Long id;
    private String name;
    private String lastName;
    private String documentNumber;
    private String cellPhone;
    private LocalDate birthDate;
    private String email;
    private String password;
    private RoleDto role;

}

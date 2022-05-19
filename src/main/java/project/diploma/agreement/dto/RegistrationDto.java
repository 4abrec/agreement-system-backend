package project.diploma.agreement.dto;

import lombok.Data;

@Data
public class RegistrationDto {

    private String username;
    private String password;
    private String fio;
    private String university;
    private String groupNumber;
    private String phoneNumber;
    private String role;
}

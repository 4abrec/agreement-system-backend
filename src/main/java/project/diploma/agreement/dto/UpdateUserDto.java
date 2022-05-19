package project.diploma.agreement.dto;

import lombok.Data;

@Data
public class UpdateUserDto {

    private Integer id;
    private String username;
    private String fio;
    private String university;
    private String groupNumber;
    private String address;
    private String position;
    private String phoneNumber;
}

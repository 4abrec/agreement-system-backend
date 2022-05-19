package project.diploma.agreement.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import project.diploma.agreement.domain.Role;

import java.util.Set;

@Data
public class UserDto {

    private Integer id;
    private String username;
    @JsonIgnore
    private String password;
    private String fio;
    private String university;
    private String groupNumber;
    private String address;
    private String position;
    private String phoneNumber;
    private Set<Role> roles;
}

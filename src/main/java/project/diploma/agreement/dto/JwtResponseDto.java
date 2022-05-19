package project.diploma.agreement.dto;


import lombok.Data;

import java.util.List;

@Data
public class JwtResponseDto {

    private String token;
    private final String type = "Bearer";
    private Integer id;
    private String username;
    private String fio;
    private List<String> role;

    public JwtResponseDto(String token, Integer id, String username, String fio, List<String> role) {
        this.token = token;
        this.id = id;
        this.username = username;
        this.fio = fio;
        this.role = role;
    }
}

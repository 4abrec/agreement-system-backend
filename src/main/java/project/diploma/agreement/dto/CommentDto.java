package project.diploma.agreement.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentDto {

    private Integer id;
    private String text;
    private LocalDateTime dateTime;
    private UserDto user;

}

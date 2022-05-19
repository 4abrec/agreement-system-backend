package project.diploma.agreement.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ModuleDto {
    private Integer id;
    private String title;
    private List<TaskDto> tasks;
    private List<UserDto> performers;
    private List<UserDto> reviewers;
}

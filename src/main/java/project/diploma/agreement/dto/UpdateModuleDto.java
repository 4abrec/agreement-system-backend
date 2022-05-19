package project.diploma.agreement.dto;

import lombok.Data;

import java.util.List;

@Data
public class UpdateModuleDto {

    private Integer id;
    private String title;
    private List<TaskDto> tasks;
    private List<String> users;

    @Override
    public String toString() {
        return "UpdateModuleDto{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", tasks=" + tasks +
                ", users=" + users +
                '}';
    }
}

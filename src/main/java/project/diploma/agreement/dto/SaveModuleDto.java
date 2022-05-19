package project.diploma.agreement.dto;

import lombok.Data;

import java.util.List;

@Data
public class SaveModuleDto {

    private String title;
    private List<SaveTaskDto> tasks;
    private List<String> users;

    @Override
    public String toString() {
        return "SaveModuleDto{" +
                "title='" + title + '\'' +
                ", tasks=" + tasks +
                ", users=" + users +
                '}';
    }
}

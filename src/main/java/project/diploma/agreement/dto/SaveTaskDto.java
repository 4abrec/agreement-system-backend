package project.diploma.agreement.dto;

import lombok.Data;

@Data
public class SaveTaskDto {

    private String title;
    private String description;
    private String deadLine;
    private Integer typeAssessment;

    @Override
    public String toString() {
        return "SaveTaskDto{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", deadLine='" + deadLine + '\'' +
                ", typeAssessment=" + typeAssessment +
                '}';
    }
}

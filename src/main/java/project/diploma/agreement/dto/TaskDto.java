package project.diploma.agreement.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode
public class TaskDto {

    private Integer id;
    private String title;
    private String description;
    private String deadLine;
    private Integer typeAssessment;
}

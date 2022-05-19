package project.diploma.agreement.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskWithStudentSolutionDto {

    private TaskDto taskDto;
    private SolutionDto solutionDto;

}
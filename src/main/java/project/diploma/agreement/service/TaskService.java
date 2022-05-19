package project.diploma.agreement.service;

import project.diploma.agreement.dto.TaskWithAllStudentsSolutionDto;
import project.diploma.agreement.dto.TaskWithStudentSolutionDto;

import java.util.List;

public interface TaskService {
    void deleteById(Integer id);

    List<TaskWithStudentSolutionDto> getTasksWithSolution(Integer moduleId, String username);

    TaskWithStudentSolutionDto getTaskWithSolutionById(Integer taskId, String username);

    List<TaskWithAllStudentsSolutionDto> getTaskWithAllSolutions(Integer taskId);

}

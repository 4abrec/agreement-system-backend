package project.diploma.agreement.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import project.diploma.agreement.dto.TaskWithAllStudentsSolutionDto;
import project.diploma.agreement.dto.TaskWithStudentSolutionDto;
import project.diploma.agreement.service.TaskService;

import java.util.List;

@RestController
@RequestMapping("api/task")
@RequiredArgsConstructor
@CrossOrigin
public class RestTaskController {

    private final TaskService taskService;

    @GetMapping("/{id}")
    public ResponseEntity<TaskWithStudentSolutionDto> getTaskWithSolution(@PathVariable Integer id) {
        String principalUsername = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        return new ResponseEntity<>(taskService.getTaskWithSolutionById(id, principalUsername), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<TaskWithStudentSolutionDto>> getTasksWithSolution(@RequestParam Integer moduleId) {
        String principalUsername = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        return new ResponseEntity<>(taskService.getTasksWithSolution(moduleId, principalUsername), HttpStatus.OK);
    }

    @GetMapping("/students/{id}")
    public ResponseEntity<List<TaskWithAllStudentsSolutionDto>> getTaskWithAllSolutions(@PathVariable Integer id) {
        return new ResponseEntity<>(taskService.getTaskWithAllSolutions(id), HttpStatus.OK);
    }
}

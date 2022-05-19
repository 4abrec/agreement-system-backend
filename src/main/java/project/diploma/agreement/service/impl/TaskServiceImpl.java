package project.diploma.agreement.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import project.diploma.agreement.domain.*;
import project.diploma.agreement.domain.Module;
import project.diploma.agreement.dto.ResponseFileDto;
import project.diploma.agreement.dto.TaskWithAllStudentsSolutionDto;
import project.diploma.agreement.dto.TaskWithStudentSolutionDto;
import project.diploma.agreement.exception.NotFoundException;
import project.diploma.agreement.mapper.solution.SolutionMapper;
import project.diploma.agreement.mapper.task.TaskMapper;
import project.diploma.agreement.mapper.user.UserMapper;
import project.diploma.agreement.repository.ModuleRepository;
import project.diploma.agreement.repository.RoleRepository;
import project.diploma.agreement.repository.TaskRepository;
import project.diploma.agreement.service.ModuleService;
import project.diploma.agreement.service.SolutionService;
import project.diploma.agreement.service.TaskService;
import project.diploma.agreement.service.UserService;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final UserService userService;
    private final ModuleRepository moduleRepository;
    private final TaskMapper taskMapper;
    private final SolutionMapper solutionMapper;
    private final SolutionService solutionService;
    private final UserMapper userMapper;
    private final RoleRepository roleRepository;

    @Override
    @Transactional
    public void deleteById(Integer id) {
        taskRepository.deleteById(id);
    }

    @Override
    public TaskWithStudentSolutionDto getTaskWithSolutionById(Integer taskId, String username) {
        List<ResponseFileDto> files;
        User user = userService.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("Пользователь " + username + " не найден", HttpStatus.BAD_REQUEST));
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new NotFoundException("Задание с " + taskId + " не найдено", HttpStatus.BAD_REQUEST));
        Solution solution = null;
        Optional<Solution> s = task.getSolutions()
                .stream().filter(sol -> sol.getAuthor().equals(user))
                .findFirst();
        if (s.isPresent()) {
            solution = s.get();
            files = solutionService.getFilesBySolutionId(solution.getId());
            return new TaskWithStudentSolutionDto(taskMapper.toDto(task), solutionMapper.toDto(solution, files));
        } else
            return new TaskWithStudentSolutionDto(taskMapper.toDto(task), null);
    }

    @Override
    public List<TaskWithStudentSolutionDto> getTasksWithSolution(Integer moduleId, String username) {
        List<ResponseFileDto> files;
        List<TaskWithStudentSolutionDto> taskWithStudentSolutionDtoList = new ArrayList<>();
        Module module = moduleRepository.findById(moduleId)
                .orElseThrow(() -> new NotFoundException("Модуль не найден", HttpStatus.BAD_REQUEST));
        User user = userService.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("Пользователь " + username + " не найден", HttpStatus.BAD_REQUEST));
        List<Task> allTasks = taskRepository.findAll();
        List<Task> filteredTasks = allTasks.stream().filter(task -> task.getModule().equals(module))
                .collect(Collectors.toList());
        for (Task task : filteredTasks) {
            Solution solution = null;
            Optional<Solution> s = task.getSolutions()
                    .stream().filter(sol -> sol.getAuthor().equals(user))
                    .findFirst();
            if (s.isPresent()) {
                solution = s.get();
                files = solutionService.getFilesBySolutionId(solution.getId());
                taskWithStudentSolutionDtoList.add(new TaskWithStudentSolutionDto(taskMapper.toDto(task), solutionMapper.toDto(solution, files)));
            } else {
                taskWithStudentSolutionDtoList.add(new TaskWithStudentSolutionDto(taskMapper.toDto(task), null));
            }
        }
        return taskWithStudentSolutionDtoList;
    }

    @Override
    public List<TaskWithAllStudentsSolutionDto> getTaskWithAllSolutions(Integer taskId) {
        List<ResponseFileDto> files;
        Role role = roleRepository.findByName(ERole.ROLE_STUDENT).get();
        List<TaskWithAllStudentsSolutionDto> taskWithStudentSolutionDtoList = new ArrayList<>();
        Task task = taskRepository.getById(taskId);
        List<User> users = task.getModule().getUsers().stream()
                .filter(user -> user.getRoles().contains(role))
                .collect(Collectors.toList());
        for (User user: users) {
            Solution solution = null;
            Optional<Solution> s = task.getSolutions()
                    .stream().filter(sol -> sol.getAuthor().equals(user))
                    .findFirst();
            if (s.isPresent()) {
                solution = s.get();
                files = solutionService.getFilesBySolutionId(solution.getId());
                taskWithStudentSolutionDtoList.add(new TaskWithAllStudentsSolutionDto(userMapper.toDto(user), taskMapper.toDto(task), solutionMapper.toDto(solution, files)));
            } else {
                taskWithStudentSolutionDtoList.add(new TaskWithAllStudentsSolutionDto(userMapper.toDto(user), taskMapper.toDto(task), null));
            }
        }
        return taskWithStudentSolutionDtoList;
    }

}

package project.diploma.agreement.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import project.diploma.agreement.domain.Module;
import project.diploma.agreement.domain.*;
import project.diploma.agreement.dto.*;
import project.diploma.agreement.exception.NotFoundException;
import project.diploma.agreement.mapper.task.TaskListMapper;
import project.diploma.agreement.repository.FileDBRepository;
import project.diploma.agreement.repository.ModuleRepository;
import project.diploma.agreement.repository.SolutionRepository;
import project.diploma.agreement.repository.TaskRepository;
import project.diploma.agreement.service.ModuleService;
import project.diploma.agreement.service.UserService;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ModuleServiceImpl implements ModuleService {

    private final ModuleRepository moduleRepository;
    private final TaskRepository taskRepository;
    private final UserService userService;
    private final TaskListMapper taskListMapper;
    private final SolutionRepository solutionRepository;
    private final FileDBRepository fileDBRepository;

    @Override
    public void save(Module module) {
        moduleRepository.save(module);
    }

    @Override
    public MessageResponseDto toMoodleEntity(SaveModuleDto saveModuleDto) {
        Module moodle = new Module(saveModuleDto.getTitle());
        save(moodle);
        Integer id = moodle.getId();
        Module newModule = findById(id);
        for (SaveTaskDto taskDto : saveModuleDto.getTasks()) {
            Task task = new Task(taskDto.getTitle(), taskDto.getDescription(), taskDto.getTypeAssessment(), LocalDate.parse(taskDto.getDeadLine()).atStartOfDay());
            task.setModule(newModule);
            taskRepository.save(task);
        }
        for (String username : saveModuleDto.getUsers()) {
            User user = userService.findByUsername(username).orElseThrow(() ->
                    new NotFoundException("Пользователя с логином " + username + " не существует!", HttpStatus.BAD_REQUEST));
            List<Module> modules = user.getModules();
            modules.add(newModule);
            user.setModules(modules);
            userService.save(user);
        }
        moduleRepository.save(newModule);
        return new MessageResponseDto("Модуль успешно сохранен");
    }

    @Override
    @Transactional
    public MessageResponseDto updateModule(UpdateModuleDto updateModuleDto) {
        Module module = findById(updateModuleDto.getId());
        module.setTitle(updateModuleDto.getTitle());
        List<TaskDto> currentModuleTasks = taskListMapper.toDtoList(module.getTasks());
        List<TaskDto> updateModuleTasks = updateModuleDto.getTasks();
        if (currentModuleTasks.size() <= updateModuleTasks.size()) {
            for (TaskDto taskDto : updateModuleTasks) {
                Task task;
                if (taskDto.getId() == null) {
                    task = new Task(taskDto.getTitle(), taskDto.getDescription(), taskDto.getTypeAssessment(), LocalDate.parse(taskDto.getDeadLine()).atStartOfDay(), module);
                } else {
                    task = taskRepository.getById(taskDto.getId());
                    task.setTitle(taskDto.getTitle());
                    task.setDescription(taskDto.getDescription());
                    task.setTypeAssessment(taskDto.getTypeAssessment());
                    task.setDeadLine(LocalDate.parse(taskDto.getDeadLine()).atStartOfDay());
                }
                taskRepository.save(task);
            }
        } else {
            for (TaskDto taskDto : updateModuleTasks) {
                Task task;
                task = taskRepository.getById(taskDto.getId());
                task.setTitle(taskDto.getTitle());
                task.setDescription(taskDto.getDescription());
                task.setTypeAssessment(taskDto.getTypeAssessment());
                task.setDeadLine(LocalDate.parse(taskDto.getDeadLine()).atStartOfDay());
                taskRepository.save(task);
            }
            List<Integer> updateIds = updateModuleTasks.stream().map(TaskDto::getId).collect(Collectors.toList());
            List<Integer> currentIds = currentModuleTasks.stream().map(TaskDto::getId).collect(Collectors.toList());
            currentIds.removeAll(updateIds);
            for (Integer id : currentIds) {
                Task task = taskRepository.getById(id);
                System.out.println(task.getSolutions());
                for (Solution solution : task.getSolutions()) {
                    for (FileDB fileDB : solution.getFiles()) {
                        fileDBRepository.deleteById(fileDB.getId());
                    }
                    solutionRepository.deleteById(solution.getId());
                }
                taskRepository.deleteById(id);
                System.out.println("delete");
            }
        }

        for (User user : userService.findAll()) {
            List<Module> mod = user.getModules();
            mod.remove(module);
            user.setModules(mod);
            userService.save(user);
        }

        for (String username : updateModuleDto.getUsers()) {
            User user = userService.findByUsername(username).orElseThrow(() ->
                    new NotFoundException("Пользователя с логином " + username + " не существует!", HttpStatus.BAD_REQUEST));
            List<Module> modules = user.getModules();
            modules.add(module);
            user.setModules(modules);
            userService.save(user);
        }
        moduleRepository.save(module);
        return new MessageResponseDto("Модуль успешно изменен");
    }

    @Override
    public Module findById(Integer id) {
        return moduleRepository.getById(id);
    }
}

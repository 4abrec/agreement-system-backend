package project.diploma.agreement.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import project.diploma.agreement.domain.ERole;
import project.diploma.agreement.domain.Module;
import project.diploma.agreement.domain.Task;
import project.diploma.agreement.domain.User;
import project.diploma.agreement.dto.MessageResponseDto;
import project.diploma.agreement.dto.ModuleDto;
import project.diploma.agreement.dto.SaveModuleDto;
import project.diploma.agreement.dto.UpdateModuleDto;
import project.diploma.agreement.exception.NotFoundException;
import project.diploma.agreement.mapper.task.TaskListMapper;
import project.diploma.agreement.mapper.user.UserListMapper;
import project.diploma.agreement.repository.ModuleRepository;
import project.diploma.agreement.repository.RoleRepository;
import project.diploma.agreement.service.ModuleService;
import project.diploma.agreement.service.UserService;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/module")
@RequiredArgsConstructor
@CrossOrigin(maxAge = 3600)
public class RestModuleController {

    private final ModuleService moduleService;
    private final ModuleRepository moduleRepository;
    private final RoleRepository roleRepository;
    private final UserListMapper userListMapper;
    private final TaskListMapper taskListMapper;
    private final UserService userService;

    @PostMapping("/save")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public ResponseEntity<MessageResponseDto> save(@RequestBody SaveModuleDto module) throws InterruptedException {
        return new ResponseEntity<>(moduleService.toMoodleEntity(module), HttpStatus.OK);
    }

    @PostMapping("/update")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public ResponseEntity<MessageResponseDto> update(@RequestBody UpdateModuleDto module) throws InterruptedException {
        System.out.println("Схавал");
        return new ResponseEntity<>(moduleService.updateModule(module), HttpStatus.OK);
    }


    @GetMapping
    public ResponseEntity<List<ModuleDto>> getAllModules() {
        String principalUsername = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        User principal = userService.findByUsername(principalUsername).orElseThrow(
                () -> new NotFoundException("Пользователь " + principalUsername + " не найден", HttpStatus.BAD_REQUEST));
        List<User> performers = new ArrayList<>();
        List<User> reviewers = new ArrayList<>();
        List<Module> allModules = moduleRepository.findAll().stream().filter(module -> module.getUsers().contains(principal))
                .collect(Collectors.toList());
        List<ModuleDto> modules = new ArrayList<>();
        for (Module module : allModules) {
            performers = module.getUsers().stream().filter(user -> user.getRoles().contains(roleRepository.findByName(
                            ERole.ROLE_STUDENT).orElseThrow(() -> new NotFoundException("Роль STUDENT не найдена", HttpStatus.BAD_REQUEST))))
                    .collect(Collectors.toList());
            reviewers = module.getUsers().stream().filter(user -> user.getRoles().contains(roleRepository.findByName(
                            ERole.ROLE_ADMIN).orElseThrow(() -> new NotFoundException("Роль ADMIN не найдена", HttpStatus.BAD_REQUEST))))
                    .collect(Collectors.toList());
            List<Task> tasks = module.getTasks();
            tasks.sort(Comparator.comparing(Task::getDeadLine));
            modules.add(new ModuleDto(module.getId(), module.getTitle(), taskListMapper.toDtoList(tasks), userListMapper.toDtoList(performers)
                    , userListMapper.toDtoList(reviewers)));
        }
        return new ResponseEntity<>(modules, HttpStatus.OK);
    }

}

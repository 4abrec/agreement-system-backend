package project.diploma.agreement.controller;

import lombok.RequiredArgsConstructor;
import org.mapstruct.control.MappingControl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import project.diploma.agreement.domain.ERole;
import project.diploma.agreement.domain.Module;
import project.diploma.agreement.domain.Role;
import project.diploma.agreement.domain.User;
import project.diploma.agreement.dto.MessageResponseDto;
import project.diploma.agreement.dto.UpdateAdminDto;
import project.diploma.agreement.dto.UpdateUserDto;
import project.diploma.agreement.dto.UserDto;
import project.diploma.agreement.mapper.user.UserListMapper;
import project.diploma.agreement.mapper.user.UserMapper;
import project.diploma.agreement.repository.RoleRepository;
import project.diploma.agreement.service.ModuleService;
import project.diploma.agreement.service.UserService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/user")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
public class RestUserController {

    private final UserService userService;
    private final UserListMapper userListMapper;
    private final UserMapper userMapper;
    private final ModuleService moduleService;
    private final RoleRepository roleRepository;

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @GetMapping("/student")
    public ResponseEntity<List<UserDto>> getAllStudents() {
        return new ResponseEntity<>(userListMapper.toDtoList(userService.getAllStudents()), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<UserDto> getUserByUsername(@RequestParam String username) {
        return new ResponseEntity<>(userMapper.toDto(userService.findByUsername(username).get()), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @GetMapping("/admin")
    public ResponseEntity<List<UserDto>> getAllAdmins() {
        return new ResponseEntity<>(userListMapper.toDtoList(userService.getAllAdmins()), HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<List<UserDto>> getAllUsers() {
        String principalUsername = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        User principal = userService.findByUsername(principalUsername).get();
        List<User> allUsers = userService.findAll();
        allUsers.remove(principal);
        return new ResponseEntity<>(userListMapper.toDtoList(allUsers), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @DeleteMapping("/delete/{username}")
    public ResponseEntity<MessageResponseDto> deleteUserByUsername(@PathVariable String username) {
        return new ResponseEntity<>(userService.deleteByUsername(username), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @PostMapping("/student/update")
    public ResponseEntity<MessageResponseDto> updateStudent(@RequestBody UpdateUserDto userDto) {
        return new ResponseEntity<>(userService.updateStudent(userDto), HttpStatus.OK);
    }

    @PostMapping("/admin/update")
    public ResponseEntity<MessageResponseDto> updateAdmin(@RequestBody UpdateAdminDto adminDto) {
        return new ResponseEntity<>(userService.updateAdmin(adminDto), HttpStatus.OK);
    }

    @GetMapping("/module")
    public ResponseEntity<List<UserDto>> getUsersByModule(@RequestParam Integer moduleId) {
        Module module = moduleService.findById(moduleId);
        Role role = roleRepository.findByName(ERole.ROLE_STUDENT).get();
        List<UserDto> users = userListMapper.toDtoList(userService.findAll().stream().filter(user -> user.getModules().contains(module)
                && user.getRoles().contains(role)).collect(Collectors.toList()));
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping("/chat")
    public ResponseEntity<List<UserDto>> getAllChat(@RequestParam String username) {
        return new ResponseEntity<>(userListMapper.toDtoList(userService.findAllUsersChat(username)), HttpStatus.OK);
    }

    @GetMapping("/add/chat")
    public ResponseEntity<MessageResponseDto> addChat(@RequestParam String senderUsername, @RequestParam String recipientUsername) {
        return new ResponseEntity<>(userService.addChat(senderUsername, recipientUsername), HttpStatus.OK);
    }
}

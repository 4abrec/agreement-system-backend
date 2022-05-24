package project.diploma.agreement.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import project.diploma.agreement.config.jwt.JwtUtils;
import project.diploma.agreement.domain.ERole;
import project.diploma.agreement.domain.Role;
import project.diploma.agreement.domain.User;
import project.diploma.agreement.dto.*;
import project.diploma.agreement.exception.NotFoundException;
import project.diploma.agreement.repository.RoleRepository;
import project.diploma.agreement.repository.UserRepository;
import project.diploma.agreement.service.UserService;

import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;


    @Override
    public User save(User user) {
        return userRepository.save(user);
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public void deleteById(Integer id) {
        userRepository.deleteById(id);
    }

    @Override
    public JwtResponseDto login(LoginDto loginDto) {
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(
                        loginDto.getUsername(),
                        loginDto.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        return new JwtResponseDto(jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getFio(),
                roles);
    }

    @Override
    public MessageResponseDto registration(RegistrationDto registrationDto) {
        if (userRepository.existsByUsername(registrationDto.getUsername()))
            throw new NotFoundException("Ошибка: Данный пользователь уже зарегистрирован!",
                    HttpStatus.BAD_REQUEST);

        User user = new User(registrationDto.getUsername(),
                passwordEncoder.encode(registrationDto.getPassword()),
                registrationDto.getFio());

        String requestRole = registrationDto.getRole();
        List<Role> roles = new ArrayList<>();

        switch (requestRole) {
            case "ROLE_STUDENT":
                Role roleStudent = roleRepository.findByName(ERole.ROLE_STUDENT)
                        .orElseThrow(() -> new NotFoundException("Роль 'USER' не найдена", HttpStatus.BAD_REQUEST));
                roles.add(roleStudent);
                break;
            case "ROLE_ADMIN":
                Role roleTeacher = roleRepository.findByName(ERole.ROLE_ADMIN)
                        .orElseThrow(() -> new NotFoundException("Роль 'ADMIN' не найдена", HttpStatus.BAD_REQUEST));
                roles.add(roleTeacher);
                break;
        }
        if (registrationDto.getGroupNumber() != null) {
            user.setGroupNumber(registrationDto.getGroupNumber());
        } else {
            user.setGroupNumber("Не указано");
        }
        if (registrationDto.getPhoneNumber() != null) {
            user.setPhoneNumber(registrationDto.getPhoneNumber());
        } else {
            user.setPhoneNumber("Не указано");
        }
        if (registrationDto.getUniversity() != null) {
            user.setUniversity(registrationDto.getUniversity());
        } else {
            user.setUniversity("Не указано");
        }
        if (registrationDto.getAddress() == null) {
            user.setAddress("Не указано");
        }
        if (registrationDto.getPosition() == null) {
            user.setPosition("Не указано");
        }
        user.setRoles(roles);
        userRepository.save(user);
        return new MessageResponseDto("Пользователь " + user.getUsername() + " успешно зарегистрирован!");
    }

    @Override
    public List<User> getAllStudents() {
        List<User> allUsersList = userRepository.findAll();
        Role studentRole = roleRepository.findByName(ERole.ROLE_STUDENT)
                .orElseThrow(() -> new NotFoundException("Роль 'STUDENT' не найдена", HttpStatus.BAD_REQUEST));
        return allUsersList.stream().filter(user -> user.getRoles().contains(studentRole))
                .collect(Collectors.toList());
    }

    @Override
    public List<User> getAllAdmins() {
        List<User> allUsersList = userRepository.findAll();
        Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                .orElseThrow(() -> new NotFoundException("Роль 'ADMIN' не найдена", HttpStatus.BAD_REQUEST));
        return allUsersList.stream().filter(user -> user.getRoles().contains(adminRole))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public MessageResponseDto deleteByUsername(String username) {
        userRepository.deleteByUsername(username);
        return new MessageResponseDto("Пользователь " + username + " удален");
    }

    @Override
    public MessageResponseDto updateStudent(UpdateUserDto userDto) {
        User user = userRepository.findById(userDto.getId())
                .orElseThrow(() -> new NotFoundException("Пользователь с id " + userDto.getId() + " не найден", HttpStatus.BAD_REQUEST));
        user.setUsername(userDto.getUsername());
        user.setFio(userDto.getFio());
        user.setUniversity(user.getUniversity());
        user.setGroupNumber(userDto.getGroupNumber());
        user.setPhoneNumber(userDto.getPhoneNumber());
        userRepository.save(user);
        return new MessageResponseDto("Данные успешно изменены");
    }

    @Override
    public MessageResponseDto updateAdmin(UpdateAdminDto adminDto) {
        User user = userRepository.findByUsername(adminDto.getUsername()).get();
        user.setUsername(adminDto.getUsername());
        user.setFio(adminDto.getFio());
        user.setPosition(adminDto.getPosition());
        user.setAddress(adminDto.getAddress());
        user.setUniversity(adminDto.getUniversity());
        user.setPhoneNumber(adminDto.getPhoneNumber());
        userRepository.save(user);
        return new MessageResponseDto("Данные сохранены");
    }

    @Override
    public List<User> findAllUsersChat(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден", HttpStatus.BAD_REQUEST));
        Set<User> allUsers = new HashSet<>(user.getSubscribers());
        allUsers.addAll(user.getSubscriptions());
        allUsers.remove(user);
        return new ArrayList<>(allUsers);
    }

    @Override
    public MessageResponseDto addChat(String senderUsername, String recipientUsername) {
        User sender = userRepository.findByUsername(senderUsername).get();
        User recipient = userRepository.findByUsername(recipientUsername).get();
        Set<User> subscribers = sender.getSubscribers();
        subscribers.add(recipient);
        sender.setSubscribers(subscribers);
        userRepository.save(sender);
        return new MessageResponseDto("Чат добавлен");
    }
}



package project.diploma.agreement.service;

import project.diploma.agreement.domain.User;
import project.diploma.agreement.dto.*;

import java.util.List;
import java.util.Optional;

public interface UserService {

    User save(User user);

    List<User> findAll();

    Optional<User> findByUsername(String username);

    void deleteById(Integer id);

    JwtResponseDto login(LoginDto loginDto);

    MessageResponseDto registration(RegistrationDto registrationDto);

    List<User> getAllStudents();

    List<User> getAllAdmins();

    MessageResponseDto deleteByUsername(String username);

    MessageResponseDto updateStudent(UpdateUserDto userDto);

}

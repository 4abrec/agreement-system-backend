package project.diploma.agreement.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import project.diploma.agreement.dto.JwtResponseDto;
import project.diploma.agreement.dto.LoginDto;
import project.diploma.agreement.dto.MessageResponseDto;
import project.diploma.agreement.dto.RegistrationDto;
import project.diploma.agreement.service.UserService;

@RestController
@RequestMapping("api/auth")
@RequiredArgsConstructor
@CrossOrigin(maxAge = 3600)
public class RestAuthController {

    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<MessageResponseDto> registerUser(@RequestBody RegistrationDto registrationDto) {
        return new ResponseEntity<>(userService.registration(registrationDto), HttpStatus.OK);

    }
    
    @PostMapping("/login")
    public ResponseEntity<JwtResponseDto> authUser(@RequestBody LoginDto loginDto) {
        return new ResponseEntity<>(userService.login(loginDto), HttpStatus.OK);
    }
}

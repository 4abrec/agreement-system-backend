package project.diploma.agreement.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import project.diploma.agreement.domain.User;
import project.diploma.agreement.dto.MessageResponseDto;
import project.diploma.agreement.exception.NotFoundException;
import project.diploma.agreement.repository.UserRepository;
import project.diploma.agreement.service.FileStorageService;
import project.diploma.agreement.service.SolutionService;

@RestController
@RequestMapping("api/solution")
@RequiredArgsConstructor
@CrossOrigin
public class RestSolutionController {

    private final FileStorageService storageService;
    private final UserRepository userRepository;
    private final SolutionService solutionService;

    @PostMapping("/upload")
    public ResponseEntity<MessageResponseDto> uploadFile(@RequestParam("file") MultipartFile file,
                                                         @RequestParam Integer taskId,
                                                         @RequestParam String username) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден", HttpStatus.OK));
        String message = "";
        try {
            storageService.store(file, user.getId(), taskId);
            message = "Uploaded the file successfully: " + file.getOriginalFilename();
            return ResponseEntity.status(HttpStatus.OK).body(new MessageResponseDto(message));
        } catch (Exception e) {
            message = "Could not upload the file: " + file.getOriginalFilename() + "!";
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new MessageResponseDto(message));
        }
    }

    @GetMapping("/save")
    public ResponseEntity<MessageResponseDto> saveSolution(@RequestParam Integer taskId,
                                                           @RequestParam String text,
                                                           @RequestParam String username) {

        return new ResponseEntity<>(solutionService.save(taskId, username, text), HttpStatus.OK);

    }

    @GetMapping("/update")
    public ResponseEntity<MessageResponseDto> updateSolution(@RequestParam Integer taskId,
                                                             @RequestParam Integer solutionId,
                                                             @RequestParam String text,
                                                             @RequestParam String username) {
        return new ResponseEntity<>(solutionService.update(taskId, solutionId, text, username), HttpStatus.OK);
    }

    @GetMapping("/agreement")
    public ResponseEntity<MessageResponseDto> agreementSolution(@RequestParam Integer solId,
                                                                @RequestParam Integer mark,
                                                                @RequestParam String comment,
                                                                @RequestParam Boolean isAgree) {
        String principalUsername = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        return new ResponseEntity<>(solutionService.agreementSolution(solId, mark, comment, isAgree, principalUsername), HttpStatus.OK);

    }

}
